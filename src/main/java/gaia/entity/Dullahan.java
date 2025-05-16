package gaia.entity;

import gaia.entity.goal.MobAttackGoal;
import gaia.registry.GaiaRegistry;
import gaia.registry.GaiaTags;
import gaia.util.SharedEntityData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

public class Dullahan extends AbstractGaiaEntity {

	public Dullahan(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new MobAttackGoal(this, SharedEntityData.ATTACK_SPEED_1, true));
		this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
		this.targetSelector.addGoal(2, this.targetPlayerGoal = new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	public double getMyRidingOffset() {
		return super.getMyRidingOffset();
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 40.0D)
				.add(Attributes.FOLLOW_RANGE, SharedEntityData.FOLLOW_RANGE)
				.add(Attributes.MOVEMENT_SPEED, SharedEntityData.MOVE_SPEED_1)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.ARMOR, SharedEntityData.RATE_ARMOR_1)
				.add(Attributes.ATTACK_KNOCKBACK, SharedEntityData.KNOCKBACK_1)
				.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0F);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
	}

	@Override
	public float getBaseDefense() {
		return SharedEntityData.getBaseDefense1();
	}

	@Override
	public boolean doHurtTarget(Entity entityIn) {
		if (super.doHurtTarget(entityIn)) {
			if (entityIn instanceof LivingEntity livingEntity) {
				int effectTime = 0;

				if (this.level.getDifficulty() == Difficulty.NORMAL) {
					effectTime = 5;
				} else if (this.level.getDifficulty() == Difficulty.HARD) {
					effectTime = 10;
				}

				if (effectTime > 0) {
					livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, effectTime * 20, 1));
				}
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		float input = getBaseDamage(source, damage);

		if (source.getEntity() instanceof Player player) {
			ItemStack itemstack = player.getItemInHand(player.getUsedItemHand());

			if (itemstack.is(GaiaTags.GOLDEN_TOOLS)) {
				input = Tiers.GOLD.getAttackDamageBonus() * 8;
			}
		}

		return super.hurt(source, input);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance instance) {
		setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(GaiaRegistry.CURSED_METAL_SWORD.get()));
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance,
										MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
		SpawnGroupData data = super.finalizeSpawn(levelAccessor, difficultyInstance, spawnType, groupData, tag);

		this.populateDefaultEquipmentSlots(random, difficultyInstance);
		this.populateDefaultEquipmentSlots(random, difficultyInstance);

		if (random.nextInt(8) == 0 && level.getDifficulty() != Difficulty.PEACEFUL) {
			GaiaHorse gaiaHorse = createHorse(difficultyInstance);
			if (gaiaHorse != null) {
				startRiding(gaiaHorse);
			}
		}

		return data;
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
	protected SoundEvent getAmbientSound() {
		return GaiaRegistry.DULLAHAN.getSay();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return GaiaRegistry.DULLAHAN.getHurt();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return GaiaRegistry.DULLAHAN.getDeath();
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return SharedEntityData.CHUNK_LIMIT_1;
	}

	public static boolean checkDullahanSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
		return checkDaysPassed(levelAccessor) && checkAboveSeaLevel(levelAccessor, pos) && checkMonsterSpawnRules(entityType, levelAccessor, spawnType, pos, random);
	}
}