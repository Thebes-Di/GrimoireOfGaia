package gaia.entity;

import gaia.config.GaiaConfig;
import gaia.registry.GaiaRegistry;
import gaia.util.EnchantUtil;
import gaia.util.SharedEntityData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

public class Mermaid extends AbstractAssistGaiaEntity {
	protected final WaterBoundPathNavigation waterNavigation;
	protected final GroundPathNavigation groundNavigation;
	private byte inWaterTimer;

	public Mermaid(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
		this.moveControl = new MermaidMoveControl(this);

		this.setPathfindingMalus(PathType.WATER, 8.0F);

		this.xpReward = SharedEntityData.EXPERIENCE_VALUE_2;
		this.inWaterTimer = 0;

		this.waterNavigation = new WaterBoundPathNavigation(this, level);
		this.groundNavigation = new GroundPathNavigation(this, level);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, SharedEntityData.ATTACK_SPEED_2, true));
		this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(new Class[0]));
		this.targetPlayerGoal = new NearestAttackableTargetGoal<>(this, Player.class, true);
		if (GaiaConfig.COMMON.allPassiveMobsHostile.get()) {
			this.targetSelector.addGoal(2, this.targetPlayerGoal);
		}
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 80.0D)
				.add(Attributes.FOLLOW_RANGE, SharedEntityData.FOLLOW_RANGE)
				.add(Attributes.MOVEMENT_SPEED, SharedEntityData.MOVE_SPEED_2)
				.add(Attributes.ATTACK_DAMAGE, 8.0D)
				.add(Attributes.ARMOR, SharedEntityData.RATE_ARMOR_2)
				.add(Attributes.KNOCKBACK_RESISTANCE, SharedEntityData.KNOCKBACK_2)

				.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D)
				.add(Attributes.STEP_HEIGHT, 1.0F);
	}

	@Override
	public int getGaiaLevel() {
		return 2;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
	}

	@Override
	public int maxVariants() {
		return 1;
	}


	@Override
	public float getBaseDefense() {
		return SharedEntityData.getBaseDefense2();
	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		float input = getBaseDamage(source, damage);
		if (hasShield()) {
			return !(source.getDirectEntity() instanceof AbstractArrow) && super.hurt(source, input);
		}
		return super.hurt(source, input);
	}

	@Override
	public boolean doHurtTarget(Entity entityIn) {
		if (super.doHurtTarget(entityIn)) {
			if (entityIn instanceof LivingEntity livingEntity) {
				int effectTime = 0;

				if (this.level().getDifficulty() == Difficulty.NORMAL) {
					effectTime = 10;
				} else if (this.level().getDifficulty() == Difficulty.HARD) {
					effectTime = 20;
				}

				if (effectTime > 0) {
					livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, effectTime * 20, 0));
					livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, effectTime * 20, 2));
				}
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void aiStep() {
		if (!this.level().isClientSide) {
			if (isInWater()) {
				if (inWaterTimer <= 100) {
					++inWaterTimer;
				} else {
					this.level().broadcastEntityEvent(this, (byte) 8);
					heal(getMaxHealth() * 0.10F);
					addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 5 * 20, 0));
					inWaterTimer = 0;
				}
			}
		}

		super.aiStep();
	}

	boolean wantsToSwim() {
		LivingEntity livingentity = this.getTarget();
		return livingentity != null && livingentity.isInWater();
	}

	public void updateSwimming() {
		if (!this.level().isClientSide) {
			if (this.isEffectiveAi() && this.isInWater() && this.wantsToSwim()) {
				this.navigation = this.waterNavigation;
				this.setSwimming(true);
			} else {
				this.navigation = this.groundNavigation;
				this.setSwimming(false);
			}
		}
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance instance) {
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance,
										MobSpawnType spawnType, @Nullable SpawnGroupData data) {
		data = super.finalizeSpawn(levelAccessor, difficultyInstance, spawnType, data);

		if (random.nextInt(4) == 0) {
			setVariant(1);
		}

		this.populateDefaultEquipmentSlots(random, difficultyInstance);
		this.populateDefaultEquipmentEnchantments(levelAccessor, random, difficultyInstance);

		ItemStack shield = new ItemStack(GaiaRegistry.GOLD_SHIELD.get());
		setItemSlot(EquipmentSlot.OFFHAND, shield);

		ItemStack swimmingBoots = new ItemStack(Items.LEATHER_BOOTS);
		setItemSlot(EquipmentSlot.FEET, swimmingBoots);
		swimmingBoots.enchant(EnchantUtil.getEnchantmentHolder(this, Enchantments.DEPTH_STRIDER), 3);

		return data;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return GaiaRegistry.MERMAID.getSay();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return GaiaRegistry.MERMAID.getHurt();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return GaiaRegistry.MERMAID.getDeath();
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
	}

	@Override
	public boolean isPushedByFluid(FluidType type) {
		return false;
	}

	@Override
	protected int getFireImmuneTicks() {
		return 10;
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return SharedEntityData.CHUNK_LIMIT_1;
	}

	@Override
	public boolean checkSpawnObstruction(LevelReader reader) {
		return reader.isUnobstructed(this);
	}

	public static boolean checkMermaidSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
		boolean daysPassed = checkDaysPassed(levelAccessor);
		boolean isNight = !checkDaytime(levelAccessor);
		boolean inWater = checkInWater(levelAccessor, pos,5);
		boolean notPeaceful = checkNotPeaceful(levelAccessor);
		boolean matchRandom = random.nextInt(15) == 0;

		return daysPassed && isNight && inWater && notPeaceful && matchRandom;
	}

	static class MermaidMoveControl extends MoveControl {
		private final Mermaid mermaid;

		public MermaidMoveControl(Mermaid mermaid) {
			super(mermaid);
			this.mermaid = mermaid;
		}

		public void tick() {
			LivingEntity livingentity = this.mermaid.getTarget();
			if (this.mermaid.wantsToSwim() && this.mermaid.isInWater()) {
				if (livingentity != null && livingentity.getY() > this.mermaid.getY()) {
					this.mermaid.setDeltaMovement(this.mermaid.getDeltaMovement().add(0.0D, 0.002D, 0.0D));
				}

				if (this.operation != MoveControl.Operation.MOVE_TO || this.mermaid.getNavigation().isDone()) {
					this.mermaid.setSpeed(0.0F);
					return;
				}

				double d0 = this.wantedX - this.mermaid.getX();
				double d1 = this.wantedY - this.mermaid.getY();
				double d2 = this.wantedZ - this.mermaid.getZ();
				double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
				d1 /= d3;
				float f = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
				this.mermaid.setYRot(this.rotlerp(this.mermaid.getYRot(), f, 90.0F));
				this.mermaid.yBodyRot = this.mermaid.getYRot();
				float f1 = (float) (this.speedModifier * this.mermaid.getAttributeValue(Attributes.MOVEMENT_SPEED));
				float f2 = Mth.lerp(0.125F, this.mermaid.getSpeed(), f1);
				this.mermaid.setSpeed(f2);
				this.mermaid.setDeltaMovement(this.mermaid.getDeltaMovement().add((double) f2 * d0 * 0.005D, (double) f2 * d1 * 0.1D, (double) f2 * d2 * 0.005D));
			} else {
				if (!this.mermaid.onGround()) {
					this.mermaid.setDeltaMovement(this.mermaid.getDeltaMovement().add(0.0D, -0.008D, 0.0D));
				}

				super.tick();
			}
		}
	}
}