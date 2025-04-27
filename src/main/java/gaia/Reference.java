package gaia;

import net.minecraft.resources.ResourceLocation;

public class Reference {
	public static final String SUMMONED_TAG = GrimoireOfGaia.MOD_ID + "_staff_summoned";
	public static final String SUMMONER_TAG = GrimoireOfGaia.MOD_ID + "_staff_summoner";

	public static ResourceLocation modLoc(String path) {
		return ResourceLocation.fromNamespaceAndPath(GrimoireOfGaia.MOD_ID, path);
	}
}
