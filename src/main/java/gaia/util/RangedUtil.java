package gaia.util;

import gaia.config.GaiaConfig;
import gaia.entity.projectile.BombProjectile;
import gaia.entity.projectile.BubbleProjectile;
import gaia.entity.projectile.GaiaSmallFireball;
import gaia.entity.projectile.MagicProjectile;
import gaia.entity.projectile.PoisonProjectile;
import gaia.entity.projectile.RandomMagicProjectile;
import gaia.entity.projectile.WebProjectile;
import gaia.registry.GaiaSounds;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.phys.Vec3;

public class RangedUtil {

	/**
	 * Shortcut Method for entities using ranged attacks. Use this to replace RangedAttackMob#performRangedAttack.
	 *
	 * @param target         the entity to fire at
	 * @param shooter        the entity that is shooting
	 * @param distanceFactor bonus damage
	 * @see net.minecraft.world.entity.monster.AbstractSkeleton#performRangedAttack(LivingEntity, float)
	 */
	public static void rangedAttack(LivingEntity target, LivingEntity shooter, float distanceFactor) {
		ItemStack arrowStack = shooter.getProjectile(shooter.getItemInHand(ProjectileUtil.getWeaponHoldingHand(shooter, item -> item instanceof net.minecraft.world.item.BowItem)));
		rangedAttack(target, shooter, distanceFactor, arrowStack);
	}

	/**
	 * Shortcut Method for entities using ranged attacks. Use this to replace RangedAttackMob#performRangedAttack.
	 *
	 * @param target         the entity to fire at
	 * @param shooter        the entity that is shooting
	 * @param distanceFactor bonus damage
	 * @param arrowStack     the arrow stack
	 * @see net.minecraft.world.entity.monster.AbstractSkeleton#performRangedAttack(LivingEntity, float)
	 */
	public static void rangedAttack(LivingEntity target, LivingEntity shooter, float distanceFactor, ItemStack arrowStack) {
		AbstractArrow abstractarrow = getArrow(shooter, arrowStack, distanceFactor);
		ItemStack heldItem = shooter.getMainHandItem();
		if (heldItem.getItem() instanceof BowItem bowItem)
			abstractarrow = bowItem.customArrow(abstractarrow, arrowStack, heldItem);
		double d0 = target.getX() - shooter.getX();
		double d1 = target.getY(0.3333333333333333D) - abstractarrow.getY();
		double d2 = target.getZ() - shooter.getZ();
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		abstractarrow.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - shooter.level().getDifficulty().getId() * 4));
		abstractarrow.setBaseDamage(distanceFactor * 2.0D + shooter.getRandom().nextGaussian() * 0.25D + shooter.level().getDifficulty().getId() * 0.11D);
		abstractarrow.setOwner(shooter);

		if (shooter.level().getDifficulty() == Difficulty.HARD && GaiaConfig.COMMON.baseDamageArchers.get() && abstractarrow instanceof Arrow arrow) {
			arrow.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 0));
		}

		shooter.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));
		shooter.level().addFreshEntity(abstractarrow);
	}

	public static AbstractArrow getArrow(LivingEntity shooter, ItemStack arrowStack, float distanceFactor) {
		return ProjectileUtil.getMobArrow(shooter, arrowStack, distanceFactor, null);
	}

	/**
	 * Shortcut Method for entities using blaze attacks. Use this to replace entity RangedAttackMob#performRangedAttack.
	 *
	 * @param target         the entity to fire at
	 * @param shooter        the entity that is shooting
	 * @param distanceFactor bonus damage
	 * @see net.minecraft.world.entity.monster.Blaze
	 */
	public static void fireball(LivingEntity target, LivingEntity shooter, float distanceFactor) {
		shooter.playSound(GaiaSounds.GAIA_SHOOT.get(), 1.0F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));

		double d0 = target.getX() - shooter.getX();
		double d1 = target.getY(0.5D) - shooter.getY(0.5D);
		double d2 = target.getZ() - shooter.getZ();
		double f1 = Mth.sqrt(distanceFactor) * 0.5D;

		Vec3 vec3 = new Vec3(d0 + shooter.getRandom().nextGaussian() * f1, d1, d2 + shooter.getRandom().nextGaussian() * f1);
		GaiaSmallFireball smallFireball = new GaiaSmallFireball(shooter.level(), shooter.getX(), shooter.getY(), shooter.getZ(), vec3.normalize());
		smallFireball.setPos(smallFireball.getX(), shooter.getY(0.5D) + 0.5D, smallFireball.getZ());
		smallFireball.setOwner(shooter);
		shooter.level().addFreshEntity(smallFireball);
	}

	/**
	 * Shortcut Method for entities using potion attacks. Use this to replace entity RangedAttackMob#performRangedAttack.
	 *
	 * @param target         the entity to fire at
	 * @param shooter        the entity that is shooting
	 * @param distanceFactor bonus damage (Unused)
	 * @param potionHolder   the potion to apply
	 * @see net.minecraft.world.entity.monster.Witch
	 */
	public static void potion(LivingEntity target, LivingEntity shooter, float distanceFactor, Holder<Potion> potionHolder) {
		Vec3 vec3 = shooter.getDeltaMovement();
		double d0 = target.getY() + (double) target.getEyeHeight() - 1.100000023841858D;
		double d1 = target.getX() + vec3.x - shooter.getX();
		double d2 = d0 - shooter.getY();
		double d3 = target.getZ() + vec3.z - shooter.getZ();
		float f = Mth.sqrt((float) (d1 * d1 + d3 * d3));

		ThrownPotion thrownpotion = new ThrownPotion(shooter.level(), shooter);
		ItemStack potionStack = Items.SPLASH_POTION.getDefaultInstance();
		potionStack.set(DataComponents.POTION_CONTENTS, new PotionContents(potionHolder));
		thrownpotion.setItem(potionStack);
		thrownpotion.setXRot(thrownpotion.getXRot() + 20.0F);
		thrownpotion.shoot(d1, d2 + (double) (f * 0.2F), d3, 0.75F, 8.0F);
		thrownpotion.setOwner(shooter);

		shooter.level().playSound((Player) null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.WITCH_THROW, shooter.getSoundSource(), 1.0F, 0.8F + shooter.getRandom().nextFloat() * 0.4F);
		shooter.level().addFreshEntity(thrownpotion);
	}

	/**
	 * Shortcut Method for entities using magic attacks. Use this to replace entity RangedAttackMob#performRangedAttack.
	 *
	 * @param target         the entity to fire at
	 * @param shooter        the entity that is shooting
	 * @param distanceFactor bonus damage (Unused)
	 * @see net.minecraft.world.entity.monster.Blaze
	 */
	public static void magic(LivingEntity target, LivingEntity shooter, float distanceFactor) {
		shooter.playSound(GaiaSounds.GAIA_SHOOT.get(), 1.0F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));

		double d0 = target.getX() - shooter.getX();
		double d1 = target.getY(0.5D) - shooter.getY(0.5D);
		double d2 = target.getZ() - shooter.getZ();
		double f1 = Mth.sqrt(distanceFactor) * 0.5D;

		Vec3 vec3 = new Vec3(d0 + shooter.getRandom().nextGaussian() * f1, d1, d2 + shooter.getRandom().nextGaussian() * f1);
		MagicProjectile magic = new MagicProjectile(shooter.level(), shooter.getX(), shooter.getY(), shooter.getZ(), vec3.normalize());
		magic.setPos(magic.getX(), shooter.getY(0.5D) + 0.5D, magic.getZ());
		magic.setOwner(shooter);
		shooter.level().addFreshEntity(magic);
	}

	/**
	 * Shortcut Method for entities using magic attacks. Use this to replace entity RangedAttackMob#performRangedAttack.
	 *
	 * @param target         the entity to fire at
	 * @param shooter        the entity that is shooting
	 * @param distanceFactor bonus damage (Unused)
	 * @see net.minecraft.world.entity.monster.Blaze
	 */
	public static void magicRandom(LivingEntity target, LivingEntity shooter, float distanceFactor, double yOffset, Holder<MobEffect> mobEffect) {
		shooter.playSound(GaiaSounds.GAIA_SHOOT.get(), 1.0F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));

		double d0 = target.getX() - shooter.getX();
		double d1 = target.getY(0.5D) - shooter.getY(0.5D);
		double d2 = target.getZ() - shooter.getZ();
		double f1 = Mth.sqrt(distanceFactor) * 0.5D;

		Vec3 vec3 = new Vec3(d0 + shooter.getRandom().nextGaussian() * f1, d1, d2 + shooter.getRandom().nextGaussian() * f1);
		RandomMagicProjectile magic = new RandomMagicProjectile(shooter.level(), shooter.getX(), shooter.getY(), shooter.getZ(), vec3.normalize());
		magic.setPos(magic.getX(), shooter.getY(0.5D) + 0.5D + yOffset, magic.getZ());
		magic.setEffect(mobEffect);
		magic.setOwner(shooter);
		shooter.level().addFreshEntity(magic);
	}

	/**
	 * Shortcut Method for entities using web attacks. Use this to replace entity RangedAttackMob#performRangedAttack.
	 *
	 * @param target         the entity to fire at
	 * @param shooter        the entity that is shooting
	 * @param distanceFactor bonus damage (Unused)
	 * @see net.minecraft.world.entity.monster.Blaze
	 */
	public static void web(LivingEntity target, LivingEntity shooter, float distanceFactor) {
		shooter.playSound(GaiaSounds.GAIA_SHOOT.get(), 1.0F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));

		double d0 = target.getX() - shooter.getX();
		double d1 = target.getY(0.5D) - shooter.getY(0.5D);
		double d2 = target.getZ() - shooter.getZ();
		double f1 = Mth.sqrt(distanceFactor) * 0.5D;

		Vec3 vec3 = new Vec3(d0 + shooter.getRandom().nextGaussian() * f1, d1, d2 + shooter.getRandom().nextGaussian() * f1);
		WebProjectile web = new WebProjectile(shooter.level(), shooter.getX(), shooter.getY(), shooter.getZ(), vec3.normalize());
		web.setPos(web.getX(), shooter.getY(0.5D) + 0.5D, web.getZ());
		web.setOwner(shooter);
		shooter.level().addFreshEntity(web);
	}

	/**
	 * Shortcut Method for entities using bubble attacks. Use this to replace entity RangedAttackMob#performRangedAttack.
	 *
	 * @param target         the entity to fire at
	 * @param shooter        the entity that is shooting
	 * @param distanceFactor bonus damage (Unused)
	 * @see net.minecraft.world.entity.monster.Blaze
	 */
	public static void bubble(LivingEntity target, LivingEntity shooter, float distanceFactor) {
		shooter.playSound(GaiaSounds.GAIA_SHOOT.get(), 1.0F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));

		double d0 = target.getX() - shooter.getX();
		double d1 = target.getY(0.5D) - shooter.getY(0.5D);
		double d2 = target.getZ() - shooter.getZ();
		double f1 = Mth.sqrt(distanceFactor) * 0.5D;

		Vec3 vec3 = new Vec3(d0 + shooter.getRandom().nextGaussian() * f1, d1, d2 + shooter.getRandom().nextGaussian() * f1);
		BubbleProjectile bubble = new BubbleProjectile(shooter.level(), shooter.getX(), shooter.getY(), shooter.getZ(), vec3.normalize());
		bubble.setPos(bubble.getX(), shooter.getY(0.5D) + 0.5D, bubble.getZ());
		bubble.setOwner(shooter);
		shooter.level().addFreshEntity(bubble);
	}

	/**
	 * Shortcut Method for entities using poison attacks. Use this to replace entity RangedAttackMob#performRangedAttack.
	 *
	 * @param target         the entity to fire at
	 * @param shooter        the entity that is shooting
	 * @param distanceFactor bonus damage (Unused)
	 * @see net.minecraft.world.entity.monster.Blaze
	 */
	public static void poison(LivingEntity target, LivingEntity shooter, float distanceFactor) {
		shooter.playSound(GaiaSounds.GAIA_SHOOT.get(), 1.0F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));

		double d0 = target.getX() - shooter.getX();
		double d1 = target.getY(0.5D) - shooter.getY(0.5D);
		double d2 = target.getZ() - shooter.getZ();
		double f1 = Mth.sqrt(distanceFactor) * 0.5D;

		Vec3 vec3 = new Vec3(d0 + shooter.getRandom().nextGaussian() * f1, d1, d2 + shooter.getRandom().nextGaussian() * f1);
		PoisonProjectile poison = new PoisonProjectile(shooter.level(), shooter.getX(), shooter.getY(), shooter.getZ(), vec3.normalize());
		poison.setPos(poison.getX(), shooter.getY(0.5D) + 0.5D, poison.getZ());
		poison.setOwner(shooter);
		shooter.level().addFreshEntity(poison);
	}

	/**
	 * Shortcut Method for entities using bomb attacks. Use this to replace entity RangedAttackMob#performRangedAttack.
	 *
	 * @param target         the entity to fire at
	 * @param shooter        the entity that is shooting
	 * @param distanceFactor bonus damage (Unused)
	 * @see net.minecraft.world.entity.animal.SnowGolem#performRangedAttack
	 */
	public static void bomb(LivingEntity target, LivingEntity shooter, float distanceFactor) {
		shooter.playSound(GaiaSounds.GAIA_SHOOT.get(), 1.0F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));

		BombProjectile bomb = new BombProjectile(shooter.level(), shooter);
		double d0 = target.getEyeY() - (double) 1.1F;
		double d1 = target.getX() - shooter.getX();
		double d2 = d0 - bomb.getY();
		double d3 = target.getZ() - shooter.getZ();
		double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double) 0.2F;
		bomb.shoot(d1, d2 + d4, d3, 0.75F, 8.0F);
		bomb.setOwner(shooter);
		shooter.level().addFreshEntity(bomb);
	}
}
