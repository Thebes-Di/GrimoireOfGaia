package gaia.entity;

import gaia.entity.goal.MobAttackGoal;
import gaia.registry.GaiaRegistry;
import gaia.util.RangedUtil;
import gaia.util.SharedEntityData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
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
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class Shaman extends AbstractGaiaEntity implements RangedAttackMob {
	private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(Shaman.class, EntityDataSerializers.INT);
	private final RangedAttackGoal rangedAttackGoal = new RangedAttackGoal(this, SharedEntityData.ATTACK_SPEED_2, 60, 15.0F);
	private final MobAttackGoal mobAttackGoal = new MobAttackGoal(this, SharedEntityData.ATTACK_SPEED_2, true);

	private int switchHealth;
	private int spawn;
	private int spawnTimer;

	private boolean animationPlay;
	private int animationTimer;

	public Shaman(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
		this.xpReward = SharedEntityData.EXPERIENCE_VALUE_2;

		switchHealth = 0;
		spawn = 0;
		spawnTimer = 0;

		animationPlay = false;
		animationTimer = 0;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));

		this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
		this.targetSelector.addGoal(2, this.targetPlayerGoal = new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 80.0D)
				.add(Attributes.FOLLOW_RANGE, SharedEntityData.FOLLOW_RANGE_MIXED)
				.add(Attributes.MOVEMENT_SPEED, SharedEntityData.MOVE_SPEED_2)
				.add(Attributes.ATTACK_DAMAGE, 8.0D)
				.add(Attributes.ARMOR, SharedEntityData.RATE_ARMOR_2)
				.add(Attributes.KNOCKBACK_RESISTANCE, SharedEntityData.KNOCKBACK_2)
				.add(Attributes.STEP_HEIGHT, 1.0F);
	}

	@Override
	public int getGaiaLevel() {
		return 2;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(ANIMATION_STATE, 0);
	}

	public int getAnimationState() {
		return this.entityData.get(ANIMATION_STATE);
	}

	public void setAnimationState(int state) {
		this.entityData.set(ANIMATION_STATE, state);
	}

	@Override
	public float getBaseDefense() {
		return SharedEntityData.getBaseDefense2();
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
					livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, effectTime * 20, 1));
				}
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void aiStep() {
		this.beaconMonster(6, (entity) -> {
			if (entity instanceof Zombie || entity instanceof Skeleton) {
				entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 1, true, true));
			}
		});

		if ((getHealth() < getMaxHealth() * 0.75F) && (switchHealth == 0)) {
			setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(GaiaRegistry.METAL_DAGGER.get()));
			setGoals(1);
			switchHealth = 1;
		}

		if ((getHealth() > getMaxHealth() * 0.75F) && (switchHealth == 1)) {
			setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(GaiaRegistry.ZOMBIE_STAFF.get(), 1));
			setGoals(0);
			switchHealth = 0;
		}

		if (getHealth() < getMaxHealth() * 0.75F && getHealth() > 0.0F && spawn == 0) {
			setAnimationState(2);

			if (spawnTimer != 30) {
				spawnTimer += 1;
			}

			if (spawnTimer == 30) {
				this.level().broadcastEntityEvent(this, (byte) 9);
				setAnimationState(0);

				setSpawn(0);

				spawnTimer = 0;
				spawn = 1;
			}
		}

		if (getHealth() < getMaxHealth() * 0.25F && getHealth() > 0.0F && spawn == 1) {
			setAnimationState(2);

			if (spawnTimer != 30) {
				spawnTimer += 1;
			}

			if (spawnTimer == 30) {
				this.level().broadcastEntityEvent(this, (byte) 9);
				setAnimationState(0);

				setSpawn(0);

				spawnTimer = 0;
				spawn = 2;
			}
		}

		if (animationPlay) {
			if (animationTimer != 20) {
				animationTimer += 1;
			} else {
				setAnimationState(0);
				animationPlay = false;
			}
		}

		super.aiStep();
	}

	private void setGoals(int id) {
		if (id == 1) {
			this.goalSelector.removeGoal(rangedAttackGoal);
			this.goalSelector.addGoal(1, mobAttackGoal);
		} else {
			this.goalSelector.removeGoal(mobAttackGoal);
			this.goalSelector.addGoal(1, rangedAttackGoal);

			setAnimationState(0);
			animationPlay = false;
			animationTimer = 0;
		}
	}

	private void setCombatTask() {
		if (getMainHandItem().is(GaiaRegistry.ZOMBIE_STAFF.get())) {
			setGoals(0);
		} else {
			setGoals(1);
		}
	}

	private void setSpawn(int id) {
		if (!this.level().isClientSide) {
			BlockPos blockpos = blockPosition().offset(-1 + random.nextInt(3), 1, -1 + random.nextInt(3));

			if (id == 0) {
				Zombie summon = EntityType.ZOMBIE.create(this.level());
				summon.moveTo(blockpos, 0.0F, 0.0F);
				summon.finalizeSpawn((ServerLevel) this.level(), this.level().getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null);
				summon.setItemSlot(EquipmentSlot.HEAD, new ItemStack(GaiaRegistry.HEADGEAR_BOLT.get()));
				summon.setDropChance(EquipmentSlot.MAINHAND, 0);
				summon.setDropChance(EquipmentSlot.OFFHAND, 0);
				summon.setDropChance(EquipmentSlot.FEET, 0);
				summon.setDropChance(EquipmentSlot.LEGS, 0);
				summon.setDropChance(EquipmentSlot.CHEST, 0);
				summon.setDropChance(EquipmentSlot.HEAD, 0);
				this.level().addFreshEntity(summon);
			}
		}
	}

	@Override
	public boolean canAttackType(EntityType<?> type) {
		return super.canAttackType(type) && type != GaiaRegistry.SHAMAN.getEntityType();
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
		if (target.isAlive()) {
			RangedUtil.potion(target, this, distanceFactor, Potions.POISON);

			setAnimationState(1);
			animationPlay = true;
			animationTimer = 0;
		}
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance instance) {
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(GaiaRegistry.ZOMBIE_STAFF.get()));
		ItemStack splashPotion = new ItemStack(Items.SPLASH_POTION);
		splashPotion.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.POISON));
		setItemSlot(EquipmentSlot.OFFHAND, splashPotion);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance,
	                                    MobSpawnType spawnType, @Nullable SpawnGroupData data) {
		data = super.finalizeSpawn(levelAccessor, difficultyInstance, spawnType, data);

		this.populateDefaultEquipmentSlots(random, difficultyInstance);
		this.populateDefaultEquipmentSlots(random, difficultyInstance);

		setCombatTask();

		return data;
	}

	@Override
	public boolean canBeAffected(MobEffectInstance effectInstance) {
		return effectInstance.getEffect() != MobEffects.POISON && super.canBeAffected(effectInstance);
	}

	@Override
	protected float getDamageAfterMagicAbsorb(DamageSource source, float damage) {
		damage = super.getDamageAfterMagicAbsorb(source, damage);

		if (source.getEntity() == this) {
			damage = 0.0F;
		}

		if (source.is(DamageTypeTags.WITCH_RESISTANT_TO)) {
			damage *= 0.15F;
		}

		return damage;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("AnimationState", getAnimationState());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("AnimationState")) {
			int state = tag.getInt("AnimationState");
			setAnimationState(state);
		}

		setCombatTask();
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return GaiaRegistry.SHAMAN.getSay();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return GaiaRegistry.SHAMAN.getHurt();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return GaiaRegistry.SHAMAN.getDeath();
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return SharedEntityData.CHUNK_LIMIT_2;
	}

	public static boolean checkShamanSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
		return checkDaysPassed(levelAccessor) && checkAboveSeaLevel(levelAccessor, pos) && checkMonsterSpawnRules(entityType, levelAccessor, spawnType, pos, random);
	}
}