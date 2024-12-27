package gaia.entity;

import gaia.entity.goal.MobAttackGoal;
import gaia.entity.type.IDayMob;
import gaia.registry.GaiaRegistry;
import gaia.registry.GaiaTags;
import gaia.util.EnchantUtil;
import gaia.util.RangedUtil;
import gaia.util.SharedEntityData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

public class Siren extends AbstractGaiaEntity implements RangedAttackMob, IDayMob {
	private final RangedBowAttackGoal<Siren> bowAttackGoal = new RangedBowAttackGoal<>(this, SharedEntityData.ATTACK_SPEED_1, 20, 15.0F);
	private final MobAttackGoal mobAttackGoal = new MobAttackGoal(this, SharedEntityData.ATTACK_SPEED_1, true);

	private int timer;
	private int switchDetect;
	private int switchEquip;

	private byte inWaterTimer;

	public Siren(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);

		this.setPathfindingMalus(PathType.WATER, 8.0F);

		timer = 0;
		switchDetect = 0;
		switchEquip = 0;

		inWaterTimer = 0;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));

		this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(new Class[0]));
		this.targetSelector.addGoal(2, this.targetPlayerGoal = new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 40.0D)
				.add(Attributes.FOLLOW_RANGE, SharedEntityData.FOLLOW_RANGE_MIXED)
				.add(Attributes.MOVEMENT_SPEED, SharedEntityData.MOVE_SPEED_1)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.ARMOR, SharedEntityData.RATE_ARMOR_1)
				.add(Attributes.KNOCKBACK_RESISTANCE, SharedEntityData.KNOCKBACK_1)
				.add(Attributes.STEP_HEIGHT, 1.0F);
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
		return SharedEntityData.getBaseDefense1();
	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		float input = getBaseDamage(source, damage);
		return super.hurt(source, input);
	}

	@Override
	public boolean doHurtTarget(Entity entityIn) {
		if (super.doHurtTarget(entityIn)) {
			if (entityIn instanceof LivingEntity livingEntity) {
				int effectTime = 0;

				if (this.level().getDifficulty() == Difficulty.NORMAL) {
					effectTime = 5;
				} else if (this.level().getDifficulty() == Difficulty.HARD) {
					effectTime = 10;
				}

				if (effectTime > 0) {
					livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, effectTime * 20, 1));
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

		if (playerDetection(3, TargetingConditions.forCombat())) {
			if (switchDetect == 0) {
				switchDetect = 1;
			}
		} else {
			if (switchDetect == 1) {
				switchDetect = 0;
			}
		}

		if (switchDetect == 1 && switchEquip == 0) {
			if (timer <= 20) {
				++timer;
			} else {
				if (!hasEffect(MobEffects.MOVEMENT_SPEED)) {
					addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10 * 20, 0));
				}
				setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(GaiaRegistry.METAL_DAGGER.get()));
				setGoals(1);
				timer = 0;
				switchEquip = 1;
			}
		}

		if (switchDetect == 0 && switchEquip == 1) {
			if (timer <= 20) {
				++timer;
			} else {
				if (hasEffect(MobEffects.MOVEMENT_SPEED)) {
					removeEffect(MobEffects.MOVEMENT_SPEED);
				}
				setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
				setGoals(0);
				timer = 0;
				switchEquip = 0;
			}
		}

		super.aiStep();
	}


	private void setGoals(int id) {
		if (id == 1) {
			this.goalSelector.removeGoal(bowAttackGoal);
			this.goalSelector.addGoal(1, mobAttackGoal);
		} else {
			this.goalSelector.removeGoal(mobAttackGoal);
			this.goalSelector.addGoal(1, bowAttackGoal);
		}
	}

	private void setCombatTask(Difficulty difficulty) {
		this.goalSelector.removeGoal(mobAttackGoal);
		this.goalSelector.removeGoal(bowAttackGoal);

		if (getMainHandItem().getItem() == Items.BOW) {
			int i = difficulty != Difficulty.HARD ? 40 : 20;
			bowAttackGoal.setMinAttackInterval(i);
			setGoals(0);
		} else {
			setGoals(1);
		}
	}

	@Override
	public boolean canAttackType(EntityType<?> type) {
		return super.canAttackType(type) && type != GaiaRegistry.SIREN.getEntityType();
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
		if (target.isAlive()) {
			RangedUtil.rangedAttack(target, this, distanceFactor);
		}
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance instance) {
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));

		ItemStack bootsSwimming = new ItemStack(Items.LEATHER_BOOTS);
		bootsSwimming.enchant(EnchantUtil.getEnchantmentHolder(this, Enchantments.DEPTH_STRIDER), 2);
		setItemSlot(EquipmentSlot.FEET, bootsSwimming);

		if (random.nextBoolean()) {
			ItemStack tippedArrow = new ItemStack(Items.TIPPED_ARROW);
			if (random.nextInt(2) == 0) {
				tippedArrow.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.SLOWNESS));
				setItemSlot(EquipmentSlot.OFFHAND, tippedArrow);
			} else {
				tippedArrow.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WEAKNESS));
				setItemSlot(EquipmentSlot.OFFHAND, tippedArrow);
			}
		}
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance,
										MobSpawnType spawnType, @Nullable SpawnGroupData data) {
		data = super.finalizeSpawn(levelAccessor, difficultyInstance, spawnType, data);

		if (isHalloween() && random.nextFloat() < 0.25F) {
			setVariant(1);
		}

		this.populateDefaultEquipmentSlots(random, difficultyInstance);
		this.populateDefaultEquipmentSlots(random, difficultyInstance);

		setCombatTask(difficultyInstance.getDifficulty());

		return data;
	}

	@Override
	public boolean isPushedByFluid(FluidType type) {
		return false;
	}

	@Override
	public boolean canBeAffected(MobEffectInstance effectInstance) {
		return effectInstance.getEffect() != MobEffects.POISON && super.canBeAffected(effectInstance);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);

		setCombatTask(this.level().getDifficulty());
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return GaiaRegistry.SIREN.getSay();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return GaiaRegistry.SIREN.getHurt();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return GaiaRegistry.SIREN.getDeath();
	}

	@Override
	protected int getFireImmuneTicks() {
		return 10;
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return SharedEntityData.CHUNK_LIMIT_1;
	}

	public static boolean checkSirenSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
		return checkDaysPassed(levelAccessor) && checkDaytime(levelAccessor) &&
				checkTagBlocks(levelAccessor, pos, GaiaTags.GAIA_SPAWABLE_ON) &&
				checkAboveY(pos, (levelAccessor.getSeaLevel() - 8)) &&
				checkGaiaDaySpawnRules(entityType, levelAccessor, spawnType, pos, random);
	}
}