package gaia.item.edible;

import gaia.config.GaiaConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Function;

public class XPEdibleItem extends EdibleEffectItem {
	private final Function<RandomSource, Integer> experienceFunction;

	public XPEdibleItem(Properties properties, Function<RandomSource, Integer> experienceFunction) {
		super(properties);
		this.experienceFunction = experienceFunction;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, context, list, flag);
		if(!GaiaConfig.CLIENT.hideFoodXpTooltips.getAsBoolean()){
			list.add(Component.translatable("text.grimoireofgaia.gain_experience"));
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		if (entity instanceof Player player) {
			rewardEXP(player, experienceFunction.apply(level.random));
		}
		return super.finishUsingItem(stack, level, entity);
	}
}
