package gaia.registry;

import gaia.GrimoireOfGaia;
import gaia.block.DecorationBlock;
import gaia.entity.AntSalvager;
import gaia.entity.AntWorker;
import gaia.entity.Anubis;
import gaia.entity.Arachne;
import gaia.entity.Banshee;
import gaia.entity.Bee;
import gaia.entity.Behender;
import gaia.entity.BoneKnight;
import gaia.entity.Cecaelia;
import gaia.entity.Centaur;
import gaia.entity.CobbleGolem;
import gaia.entity.CobblestoneGolem;
import gaia.entity.Creep;
import gaia.entity.Cyclops;
import gaia.entity.Deathword;
import gaia.entity.Dryad;
import gaia.entity.Dullahan;
import gaia.entity.Dwarf;
import gaia.entity.EnderDragonGirl;
import gaia.entity.EnderEye;
import gaia.entity.FleshLich;
import gaia.entity.GaiaHorse;
import gaia.entity.GelatinousSlime;
import gaia.entity.Goblin;
import gaia.entity.GoblinFeral;
import gaia.entity.GraveMite;
import gaia.entity.Gryphon;
import gaia.entity.Harpy;
import gaia.entity.Hunter;
import gaia.entity.Kobold;
import gaia.entity.Mandragora;
import gaia.entity.Matango;
import gaia.entity.Mermaid;
import gaia.entity.Mimic;
import gaia.entity.Minotaur;
import gaia.entity.Minotaurus;
import gaia.entity.Mummy;
import gaia.entity.Naga;
import gaia.entity.NineTails;
import gaia.entity.Oni;
import gaia.entity.Orc;
import gaia.entity.Satyress;
import gaia.entity.Shaman;
import gaia.entity.Sharko;
import gaia.entity.Siren;
import gaia.entity.SludgeGirl;
import gaia.entity.Sphinx;
import gaia.entity.Sporeling;
import gaia.entity.Spriggan;
import gaia.entity.Succubus;
import gaia.entity.Toad;
import gaia.entity.Valkyrie;
import gaia.entity.Werecat;
import gaia.entity.Witch;
import gaia.entity.WitherCow;
import gaia.entity.WizardHarpy;
import gaia.entity.YukiOnna;
import gaia.entity.projectile.BombProjectile;
import gaia.entity.projectile.BubbleProjectile;
import gaia.entity.projectile.GaiaSmallFireball;
import gaia.entity.projectile.MagicProjectile;
import gaia.entity.projectile.PoisonProjectile;
import gaia.entity.projectile.RandomMagicProjectile;
import gaia.entity.projectile.WebProjectile;
import gaia.entity.prop.AntHill;
import gaia.entity.prop.Chest;
import gaia.entity.prop.CyanFlower;
import gaia.entity.trader.CreeperGirl;
import gaia.entity.trader.EnderGirl;
import gaia.entity.trader.SlimeGirl;
import gaia.entity.trader.Trader;
import gaia.item.ExperienceItem;
import gaia.item.LootableItem;
import gaia.item.MemoryBookItem;
import gaia.item.accessory.HeavyBarbellItem;
import gaia.item.accessory.KnucklesItem;
import gaia.item.accessory.RingItem;
import gaia.item.accessory.SeashellHairpinItem;
import gaia.item.armor.HeadgearItem;
import gaia.item.edible.EdibleEffectItem;
import gaia.item.edible.HoneydewItem;
import gaia.item.edible.MandrakeItem;
import gaia.item.edible.MonsterFeedItem;
import gaia.item.edible.TaprootItem;
import gaia.item.edible.WartJamItem;
import gaia.item.edible.XPEdibleItem;
import gaia.item.fuel.FireshardItem;
import gaia.item.fuel.FuelItem;
import gaia.item.fuel.GigaGearItem;
import gaia.item.fuel.SoulfireItem;
import gaia.item.shield.TieredShieldItem;
import gaia.item.weapon.BombItem;
import gaia.item.weapon.FanItem;
import gaia.item.weapon.FireFanItem;
import gaia.item.weapon.IceFanItem;
import gaia.item.weapon.MagicStaffItem;
import gaia.item.weapon.SummonStaffItem;
import gaia.item.weapon.book.BattleBookItem;
import gaia.item.weapon.book.BuffBookItem;
import gaia.item.weapon.book.EnderBookItem;
import gaia.item.weapon.book.FreezingBookItem;
import gaia.item.weapon.book.HungerBookItem;
import gaia.item.weapon.book.MetalBookItem;
import gaia.item.weapon.book.NatureBookItem;
import gaia.item.weapon.book.NightmareBookItem;
import gaia.item.weapon.book.WeaponBookItem;
import gaia.item.weapon.book.WitherBookItem;
import gaia.registry.helper.GaiaMobType;
import gaia.registry.helper.MobReg;
import gaia.registry.helper.PropReg;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class GaiaRegistry {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(GrimoireOfGaia.MOD_ID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GrimoireOfGaia.MOD_ID);
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, GrimoireOfGaia.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GrimoireOfGaia.MOD_ID);

	public static final MobReg<AntSalvager> ANT_SALVAGER = new MobReg.Builder<>("ant_salvager", EntityType.Builder.of(AntSalvager::new, MobCategory.MONSTER).sized(0.5F, 0.5F).clientTrackingRange(8), 0x8a7264, 0x303030).withDefaultSounds().build();
	public static final MobReg<AntWorker> ANT_WORKER = new MobReg.Builder<>("ant", EntityType.Builder.of(AntWorker::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0x303030, 0x8a7264).withDefaultSounds().build();
	public static final MobReg<Anubis> ANUBIS = new MobReg.Builder<>("anubis", EntityType.Builder.of(Anubis::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0x353535, 0xb19534).withDefaultSounds().withGender().build();
	public static final MobReg<Arachne> ARACHNE = new MobReg.Builder<>("arachne", EntityType.Builder.of(Arachne::new, MobCategory.MONSTER).sized(1.4F, 1.6F).clientTrackingRange(8), 3815994, 11013646).withDefaultSounds().build();
	public static final MobReg<Banshee> BANSHEE = new MobReg.Builder<>("banshee", EntityType.Builder.of(Banshee::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0xeed2e8, 0xc6b0ed).withDefaultSounds().build();
	public static final MobReg<Bee> BEE = new MobReg.Builder<>("bee", GaiaMobType.ASSIST, EntityType.Builder.of(Bee::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0xc9b161, 0x353535).withDefaultSounds().build();
	public static final MobReg<Behender> BEHENDER = new MobReg.Builder<>("behender", EntityType.Builder.of(Behender::new, MobCategory.MONSTER).sized(1.5F, 1.6F).clientTrackingRange(8), 0x2f5754, 0x82ba74).withDefaultSounds().build();
	public static final MobReg<BoneKnight> BONE_KNIGHT = new MobReg.Builder<>("bone_knight", EntityType.Builder.of(BoneKnight::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 4602533, 13619151).withDefaultSounds().withStep().build();
	public static final MobReg<Cecaelia> CECAELIA = new MobReg.Builder<>("cecaelia", EntityType.Builder.of(Cecaelia::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0xdb5760, 0xd893a9).withDefaultSounds().build();
	public static final MobReg<Centaur> CENTAUR = new MobReg.Builder<>("centaur", GaiaMobType.ASSIST, EntityType.Builder.of(Centaur::new, MobCategory.MONSTER).sized(1.3964844F, 1.99F).clientTrackingRange(8), 0x8d4f41, 0x353535).withDefaultSounds().withGender().build();
	public static final MobReg<CobbleGolem> COBBLE_GOLEM = new MobReg.Builder<>("cobble_golem", GaiaMobType.ASSIST, EntityType.Builder.of(CobbleGolem::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 11513775, 11513775).withAttack().withStep().withDeath().build();
	public static final MobReg<CobblestoneGolem> COBBLESTONE_GOLEM = new MobReg.Builder<>("cobblestone_golem", EntityType.Builder.of(CobblestoneGolem::new, MobCategory.MONSTER).sized(1.4F, 2.2F).clientTrackingRange(8), 11513775, 11513775).withAttack().withStep().withDeath().build();
	public static final MobReg<Creep> CREEP = new MobReg.Builder<>("creep", EntityType.Builder.of(Creep::new, MobCategory.MONSTER).sized(0.75F, 0.75F).clientTrackingRange(8), 7917159, 2053400).withDefaultSounds().build();
	public static final MobReg<Cyclops> CYCLOPS = new MobReg.Builder<>("cyclops", GaiaMobType.ASSIST, EntityType.Builder.of(Cyclops::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 4936602, 3487029).withDefaultSounds().build();
	public static final MobReg<Deathword> DEATHWORD = new MobReg.Builder<>("deathword", GaiaMobType.AGGRESSIVE, EntityType.Builder.of(Deathword::new, MobCategory.MONSTER).sized(0.6F, 0.6F).clientTrackingRange(8), 0xb77a35, 0xffd800).build();
	public static final MobReg<Dryad> DRYAD = new MobReg.Builder<>("dryad", GaiaMobType.ASSIST, EntityType.Builder.of(Dryad::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 10255437, 5681460).withDefaultSounds().build();
	public static final MobReg<Dullahan> DULLAHAN = new MobReg.Builder<>("dullahan", EntityType.Builder.of(Dullahan::new, MobCategory.MONSTER).sized(0.6F, 1.6F).clientTrackingRange(8), 0x824fab, 0xa4452d).withDefaultSounds().build();
	public static final MobReg<Dwarf> DWARF = new MobReg.Builder<>("dwarf", GaiaMobType.ASSIST, EntityType.Builder.of(Dwarf::new, MobCategory.MONSTER).sized(0.5F, 1.5F).clientTrackingRange(8), 0x969696, 0xf09942).withDefaultSounds().build();
	public static final MobReg<EnderDragonGirl> ENDER_DRAGON_GIRL = new MobReg.Builder<>("ender_dragon_girl", GaiaMobType.ASSIST, EntityType.Builder.of(EnderDragonGirl::new, MobCategory.MONSTER).sized(0.6F, 2.2F).clientTrackingRange(8), 3158064, 14711290).withDefaultSounds().build();
	public static final MobReg<EnderEye> ENDER_EYE = new MobReg.Builder<>("ender_eye", EntityType.Builder.of(EnderEye::new, MobCategory.MONSTER).sized(1.0F, 1.0F).clientTrackingRange(8), 2039583, 0x3158064).withDefaultSounds().build();
	public static final MobReg<FleshLich> FLESH_LICH = new MobReg.Builder<>("flesh_lich", EntityType.Builder.of(FleshLich::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0x00cccc, 0x799c65).withDefaultSounds().withStep().build();
	public static final MobReg<GelatinousSlime> GELATINOUS_SLIME = new MobReg.Builder<>("gelatinous_slime", EntityType.Builder.of(GelatinousSlime::new, MobCategory.MONSTER).sized(1.75F, 1.75F).clientTrackingRange(8), 6595667, 13619151).withDefaultSounds().build();
	public static final MobReg<Goblin> GOBLIN = new MobReg.Builder<>("goblin", GaiaMobType.ASSIST, EntityType.Builder.of(Goblin::new, MobCategory.MONSTER).sized(0.6F, 1.6F).clientTrackingRange(8), 0x718a60, 0x8d4f41).withDefaultSounds().build();
	public static final MobReg<GoblinFeral> GOBLIN_FERAL = new MobReg.Builder<>("goblin_feral", EntityType.Builder.of(GoblinFeral::new, MobCategory.MONSTER).sized(0.6F, 1.6F).clientTrackingRange(8), 0x718a60, 0x8a1d3e).withDefaultSounds().build();
	public static final MobReg<Gryphon> GRYPHON = new MobReg.Builder<>("gryphon", GaiaMobType.ASSIST, EntityType.Builder.of(Gryphon::new, MobCategory.MONSTER).sized(1.2F, 1.8F).clientTrackingRange(8), 0xf09942, 0xe2e2e2).withDefaultSounds().build();
	public static final MobReg<Harpy> HARPY = new MobReg.Builder<>("harpy", EntityType.Builder.of(Harpy::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0xc9b161, 0xa5884e).withDefaultSounds().build();
	public static final MobReg<Hunter> HUNTER = new MobReg.Builder<>("hunter", GaiaMobType.ASSIST, EntityType.Builder.of(Hunter::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0xae6b3c, 0x353535).withDefaultSounds().build();
	public static final MobReg<Kobold> KOBOLD = new MobReg.Builder<>("kobold", EntityType.Builder.of(Kobold::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0x938dab, 0xafa7c1).withDefaultSounds().build();
	public static final MobReg<Mandragora> MANDRAGORA = new MobReg.Builder<>("mandragora", EntityType.Builder.of(Mandragora::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0xab1311, 0xd8d8d8).withDefaultSounds().build();
	public static final MobReg<Matango> MATANGO = new MobReg.Builder<>("matango", EntityType.Builder.of(Matango::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0xab1311, 0xd8d8d8).withDefaultSounds().build();
	public static final MobReg<Mermaid> MERMAID = new MobReg.Builder<>("mermaid", GaiaMobType.ASSIST, EntityType.Builder.of(Mermaid::new, MobCategory.MONSTER).eyeHeight(0.9F).clientTrackingRange(8), 0x5c70b1, 0xa4452d).withDefaultSounds().build();
	public static final MobReg<Mimic> MIMIC = new MobReg.Builder<>("mimic", EntityType.Builder.of(Mimic::new, MobCategory.MONSTER).sized(1.0F, 1.0F).eyeHeight(0.5F).clientTrackingRange(8), 11237677, 4274991).build();
	public static final MobReg<Minotaur> MINOTAUR = new MobReg.Builder<>("minotaur", EntityType.Builder.of(Minotaur::new, MobCategory.MONSTER).sized(1.4F, 3.0F).clientTrackingRange(8), 0x8d4f41, 0xd54242).withDefaultSounds().withStep().build();
	public static final MobReg<Minotaurus> MINOTAURUS = new MobReg.Builder<>("minotaurus", EntityType.Builder.of(Minotaurus::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0x8d4f41, 0xa9a9a9).withDefaultSounds().build();
	public static final MobReg<Mummy> MUMMY = new MobReg.Builder<>("mummy", EntityType.Builder.of(Mummy::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 3815994, 11013646).withDefaultSounds().build();
	public static final MobReg<Naga> NAGA = new MobReg.Builder<>("naga", EntityType.Builder.of(Naga::new, MobCategory.MONSTER).sized(1.0F, 2.2F).clientTrackingRange(8), 0x29bc55, 0xccb63f).withDefaultSounds().build();
	public static final MobReg<NineTails> NINE_TAILS = new MobReg.Builder<>("nine_tails", EntityType.Builder.of(NineTails::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 11809844, 13218145).withDefaultSounds().build();
	public static final MobReg<Oni> ONI = new MobReg.Builder<>("oni", EntityType.Builder.of(Oni::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0x8b302d, 0xc9b161).withDefaultSounds().build();
	public static final MobReg<Orc> ORC = new MobReg.Builder<>("orc", EntityType.Builder.of(Orc::new, MobCategory.MONSTER).sized(0.8F, 2.2F).clientTrackingRange(8), 0x718a60, 0xc0d696).withDefaultSounds().build();
	public static final MobReg<Satyress> SATYRESS = new MobReg.Builder<>("satyress", GaiaMobType.ASSIST, EntityType.Builder.of(Satyress::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0x707b4f, 0xa4452d).withDefaultSounds().build();
	public static final MobReg<Shaman> SHAMAN = new MobReg.Builder<>("shaman", EntityType.Builder.of(Shaman::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0xae6b3c, 0x56b134).withDefaultSounds().build();
	public static final MobReg<Sharko> SHARKO = new MobReg.Builder<>("sharko", EntityType.Builder.of(Sharko::new, MobCategory.MONSTER).sized(1.4F, 2.0F).clientTrackingRange(8), 0x84a498, 0x5c70b1).withDefaultSounds().build();
	public static final MobReg<Siren> SIREN = new MobReg.Builder<>("siren", EntityType.Builder.of(Siren::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0x29bc55, 0x48a0de).withDefaultSounds().build();
	public static final MobReg<SludgeGirl> SLUDGE_GIRL = new MobReg.Builder<>("sludge_girl", EntityType.Builder.of(SludgeGirl::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 6595667, 7715172).withDefaultSounds().build();
	public static final MobReg<Sphinx> SPHINX = new MobReg.Builder<>("sphinx", EntityType.Builder.of(Sphinx::new, MobCategory.MONSTER).sized(1.2F, 1.8F).eyeHeight(0.45F).clientTrackingRange(8), 0xf09942, 0x353535).withDefaultSounds().build();
	public static final MobReg<Sporeling> SPORELING = new MobReg.Builder<>("sporeling", EntityType.Builder.of(Sporeling::new, MobCategory.MONSTER).sized(0.25F, 0.50F).eyeHeight(0.45F).clientTrackingRange(8), 0xc32826, 0x977251).withDefaultSounds().build();
	public static final MobReg<Spriggan> SPRIGGAN = new MobReg.Builder<>("spriggan", EntityType.Builder.of(Spriggan::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0x7c623e, 0xc2dda5).withDefaultSounds().build();
	public static final MobReg<Succubus> SUCCUBUS = new MobReg.Builder<>("succubus", EntityType.Builder.of(Succubus::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 4079166, 13218145).withDefaultSounds().withGender().build();
	public static final MobReg<Toad> TOAD = new MobReg.Builder<>("toad", EntityType.Builder.of(Toad::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0x355d2b, 0x779f5a).withDefaultSounds().build();
	public static final MobReg<Valkyrie> VALKYRIE = new MobReg.Builder<>("valkyrie", GaiaMobType.ASSIST, EntityType.Builder.of(Valkyrie::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0xc9b161, 0xd54242).withDefaultSounds().build();
	public static final MobReg<Werecat> WERECAT = new MobReg.Builder<>("werecat", EntityType.Builder.of(Werecat::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0x7a7e8a, 0xdddadb).withDefaultSounds().build();
	public static final MobReg<Witch> WITCH = new MobReg.Builder<>("witch", EntityType.Builder.of(Witch::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 0x303030, 0x943dbb).withDefaultSounds().build();
	public static final MobReg<WitherCow> WITHER_COW = new MobReg.Builder<>("wither_cow", EntityType.Builder.of(WitherCow::new, MobCategory.MONSTER).sized(0.9F, 1.4F).eyeHeight(0.45F).clientTrackingRange(8), 5791069, 16777215).withDefaultSounds().withStep().build();
	public static final MobReg<WizardHarpy> WIZARD_HARPY = new MobReg.Builder<>("wizard_harpy", GaiaMobType.ASSIST, EntityType.Builder.of(WizardHarpy::new, MobCategory.MONSTER).sized(0.6F, 1.99F).eyeHeight(0.99F).clientTrackingRange(8), 0xdddadb, 0xba9b53).withDefaultSounds().build();
	public static final MobReg<YukiOnna> YUKI_ONNA = new MobReg.Builder<>("yuki_onna", GaiaMobType.ASSIST, EntityType.Builder.of(YukiOnna::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), 6781114, 13817330).withDefaultSounds().build();

	public static final MobReg<Trader> TRADER = new MobReg.Builder<>("trader", GaiaMobType.PASSIVE, EntityType.Builder.of(Trader::new, MobCategory.CREATURE).sized(0.6F, 1.99F).clientTrackingRange(8)).traderEgg().withDefaultSounds().build();
	public static final MobReg<CreeperGirl> CREEPER_GIRL = new MobReg.Builder<>("creeper_girl", GaiaMobType.PASSIVE, EntityType.Builder.of(CreeperGirl::new, MobCategory.CREATURE).sized(0.6F, 1.99F).clientTrackingRange(8)).traderEgg().withDefaultSounds().build();
	public static final MobReg<EnderGirl> ENDER_GIRL = new MobReg.Builder<>("ender_girl", GaiaMobType.PASSIVE, EntityType.Builder.of(EnderGirl::new, MobCategory.CREATURE).sized(0.6F, 1.99F).clientTrackingRange(8)).traderEgg().withDefaultSounds().build();
	//	public static final MobReg<Holstaurus> HOLSTAURUS = new MobReg.Builder<>("holstaurus", GaiaMobType.PASSIVE, EntityType.Builder.of(Holstaurus::new, MobCategory.CREATURE).sized(0.6F, 1.99F).clientTrackingRange(8)).traderEgg().withDefaultSounds().build();
	public static final MobReg<SlimeGirl> SLIME_GIRL = new MobReg.Builder<>("slime_girl", GaiaMobType.PASSIVE, EntityType.Builder.of(SlimeGirl::new, MobCategory.CREATURE).sized(0.6F, 1.99F).clientTrackingRange(8)).traderEgg().withDefaultSounds().build();
//	public static final MobReg<Weresheep> WERESHEEP = new MobReg.Builder<>("weresheep", GaiaMobType.PASSIVE, EntityType.Builder.of(Weresheep::new, MobCategory.CREATURE).sized(0.6F, 1.99F).clientTrackingRange(8)).traderEgg().withDefaultSounds().build();

	public static final MobReg<GaiaHorse> HORSE = new MobReg.Builder<>("horse", GaiaMobType.PASSIVE, EntityType.Builder.of(GaiaHorse::new, MobCategory.CREATURE).sized(1.3964844F, 1.6F).clientTrackingRange(10), 0x252525, 0x3a3a3a).withDefaultSounds().build();
	public static final MobReg<GraveMite> GRAVEMITE = new MobReg.Builder<>("gravemite", GaiaMobType.AGGRESSIVE, EntityType.Builder.of(GraveMite::new, MobCategory.CREATURE).sized(0.4F, 0.3F).clientTrackingRange(10), 0x252525, 0x3a3a3a).withDefaultSounds().withStep().build();

	//Props
	public static final PropReg<AntHill> ANT_HILL = new PropReg<>("ant_hill", EntityType.Builder.of(AntHill::new, MobCategory.CREATURE).sized(1.0F, 0.5F).clientTrackingRange(10), 0xe4dea7, 0x545454);
	public static final PropReg<Chest> CHEST = new PropReg<>("chest", EntityType.Builder.of(Chest::new, MobCategory.CREATURE).sized(0.8F, 0.8F).clientTrackingRange(10), 11237677, 4274991);
	public static final PropReg<CyanFlower> CYAN_FLOWER = new PropReg<>("cyan_flower", EntityType.Builder.of(CyanFlower::new, MobCategory.CREATURE).sized(0.8F, 0.8F).clientTrackingRange(10), 11237677, 4274991);

	//Projectiles
	public static final Supplier<EntityType<GaiaSmallFireball>> SMALL_FIREBALL = ENTITIES.register("small_fireball", () ->
			EntityType.Builder.<GaiaSmallFireball>of(GaiaSmallFireball::new, MobCategory.MISC)
					.sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10)
					.build("small_fireball"));
	public static final Supplier<EntityType<MagicProjectile>> MAGIC = ENTITIES.register("magic", () ->
			EntityType.Builder.<MagicProjectile>of(MagicProjectile::new, MobCategory.MISC)
					.sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10)
					.build("magic"));
	public static final Supplier<EntityType<RandomMagicProjectile>> RANDOM_MAGIC = ENTITIES.register("random_magic", () ->
			EntityType.Builder.<RandomMagicProjectile>of(RandomMagicProjectile::new, MobCategory.MISC)
					.sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10)
					.build("random_magic"));
	public static final Supplier<EntityType<WebProjectile>> WEB = ENTITIES.register("web", () ->
			EntityType.Builder.<WebProjectile>of(WebProjectile::new, MobCategory.MISC)
					.sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10)
					.build("web"));

	public static final Supplier<EntityType<PoisonProjectile>> POISON = ENTITIES.register("poison", () ->
			EntityType.Builder.<PoisonProjectile>of(PoisonProjectile::new, MobCategory.MISC)
					.sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10)
					.build("poison"));
	public static final Supplier<EntityType<BombProjectile>> BOMB = ENTITIES.register("bomb", () ->
			EntityType.Builder.<BombProjectile>of(BombProjectile::new, MobCategory.MISC)
					.sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10)
					.build("bomb"));
	public static final Supplier<EntityType<BubbleProjectile>> BUBBLE = ENTITIES.register("bubble", () ->
			EntityType.Builder.<BubbleProjectile>of(BubbleProjectile::new, MobCategory.MISC)
					.sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10)
					.build("bubble"));


	//Blocks
	public static final DeferredBlock<DecorationBlock> BUST_GORGON = BLOCKS.register("bust_gorgon", () -> new DecorationBlock(Block.Properties.of().mapColor(MapColor.STONE).strength(1.5F, 6F).sound(SoundType.STONE)));
	public static final DeferredBlock<DecorationBlock> BUST_MINOTAUR = BLOCKS.register("bust_minotaur", () -> new DecorationBlock(Block.Properties.of().mapColor(MapColor.STONE).strength(1.5F, 6F).sound(SoundType.STONE)));
	public static final DeferredBlock<DecorationBlock> BUST_SPHINX = BLOCKS.register("bust_sphinx", () -> new DecorationBlock(Block.Properties.of().mapColor(MapColor.STONE).strength(1.5F, 6F).sound(SoundType.STONE)));
	public static final DeferredBlock<DecorationBlock> BUST_VALKYRIE = BLOCKS.register("bust_valkyrie", () -> new DecorationBlock(Block.Properties.of().mapColor(MapColor.STONE).strength(1.5F, 6F).sound(SoundType.STONE)));
	public static final DeferredBlock<DecorationBlock> BUST_VAMPIRE = BLOCKS.register("bust_vampire", () -> new DecorationBlock(Block.Properties.of().mapColor(MapColor.STONE).strength(1.5F, 6F).sound(SoundType.STONE)));
	public static final DeferredBlock<DecorationBlock> DECO_GARDEN_GNOME = BLOCKS.register("deco_garden_gnome", () -> new DecorationBlock(Block.Properties.of().mapColor(MapColor.STONE).strength(0.8F, 6F).sound(SoundType.STONE)));
	public static final DeferredBlock<DecorationBlock> DECO_MANDRAGORA_POT = BLOCKS.register("deco_mandragora_pot", () -> new DecorationBlock(Block.Properties.of().mapColor(MapColor.STONE).strength(0.8F, 6F).sound(SoundType.STONE)));
	public static final DeferredBlock<DecorationBlock> DECO_NEST_HARPY = BLOCKS.register("deco_nest_harpy", () -> new DecorationBlock(Block.Properties.of().mapColor(MapColor.STONE).strength(0.8F, 6F).sound(SoundType.STONE)));
	public static final DeferredBlock<DecorationBlock> DOLL_CREEPER_GIRL = BLOCKS.register("doll_creeper_girl", () -> new DecorationBlock(Block.Properties.of().mapColor(MapColor.WOOL).strength(0.8F, 6F).sound(SoundType.WOOL)));
	public static final DeferredBlock<DecorationBlock> DOLL_DRYAD = BLOCKS.register("doll_dryad", () -> new DecorationBlock(Block.Properties.of().mapColor(MapColor.WOOL).strength(0.8F, 6F).sound(SoundType.WOOL)));
	public static final DeferredBlock<DecorationBlock> DOLL_DULLAHAN = BLOCKS.register("doll_dullahan", () -> new DecorationBlock(Block.Properties.of().mapColor(MapColor.WOOL).strength(0.8F, 6F).sound(SoundType.WOOL)));
	public static final DeferredBlock<DecorationBlock> DOLL_ENDER_GIRL = BLOCKS.register("doll_ender_girl", () -> new DecorationBlock(Block.Properties.of().mapColor(MapColor.WOOL).strength(0.8F, 6F).sound(SoundType.WOOL)));
	public static final DeferredBlock<DecorationBlock> DOLL_MAID = BLOCKS.register("doll_maid", () -> new DecorationBlock(Block.Properties.of().mapColor(MapColor.WOOL).strength(0.8F, 6F).sound(SoundType.WOOL)));
	public static final DeferredBlock<DecorationBlock> DOLL_MERMAID = BLOCKS.register("doll_mermaid", () -> new DecorationBlock(Block.Properties.of().mapColor(MapColor.WOOL).strength(0.8F, 6F).sound(SoundType.WOOL)));
	public static final DeferredBlock<DecorationBlock> DOLL_NINE_TAILS = BLOCKS.register("doll_nine_tails", () -> new DecorationBlock(Block.Properties.of().mapColor(MapColor.WOOL).strength(0.8F, 6F).sound(SoundType.WOOL)));
	public static final DeferredBlock<DecorationBlock> DOLL_SLIME_GIRL = BLOCKS.register("doll_slime_girl", () -> new DecorationBlock(Block.Properties.of().mapColor(MapColor.WOOL).strength(0.8F, 6F).sound(SoundType.WOOL)));
	public static final DeferredBlock<Block> PEARL_BLOCK = BLOCKS.register("pearl_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).requiresCorrectToolForDrops().strength(0.8F)));


	//BlockItems
	public static final DeferredItem<BlockItem> BUST_GORGON_ITEM = ITEMS.registerSimpleBlockItem(BUST_GORGON);
	public static final DeferredItem<BlockItem> BUST_MINOTAUR_ITEM = ITEMS.registerSimpleBlockItem(BUST_MINOTAUR);
	public static final DeferredItem<BlockItem> BUST_SPHINX_ITEM = ITEMS.registerSimpleBlockItem(BUST_SPHINX);
	public static final DeferredItem<BlockItem> BUST_VALKYRIE_ITEM = ITEMS.registerSimpleBlockItem(BUST_VALKYRIE);
	public static final DeferredItem<BlockItem> BUST_VAMPIRE_ITEM = ITEMS.registerSimpleBlockItem(BUST_VAMPIRE);
	public static final DeferredItem<BlockItem> DECO_GARDEN_GNOME_ITEM = ITEMS.registerSimpleBlockItem(DECO_GARDEN_GNOME);
	public static final DeferredItem<BlockItem> DECO_MANDRAGORA_POT_ITEM = ITEMS.registerSimpleBlockItem(DECO_MANDRAGORA_POT);
	public static final DeferredItem<BlockItem> DECO_NEST_HARPY_ITEM = ITEMS.registerSimpleBlockItem(DECO_NEST_HARPY);
	public static final DeferredItem<BlockItem> DOLL_CREEPER_GIRL_ITEM = ITEMS.registerSimpleBlockItem(DOLL_CREEPER_GIRL);
	public static final DeferredItem<BlockItem> DOLL_DRYAD_ITEM = ITEMS.registerSimpleBlockItem(DOLL_DRYAD);
	public static final DeferredItem<BlockItem> DOLL_DULLAHAN_ITEM = ITEMS.registerSimpleBlockItem(DOLL_DULLAHAN);
	public static final DeferredItem<BlockItem> DOLL_ENDER_GIRL_ITEM = ITEMS.registerSimpleBlockItem(DOLL_ENDER_GIRL);
	public static final DeferredItem<BlockItem> DOLL_MAID_ITEM = ITEMS.registerSimpleBlockItem(DOLL_MAID);
	public static final DeferredItem<BlockItem> DOLL_MERMAID_ITEM = ITEMS.registerSimpleBlockItem(DOLL_MERMAID);
	public static final DeferredItem<BlockItem> DOLL_NINE_TAILS_ITEM = ITEMS.registerSimpleBlockItem(DOLL_NINE_TAILS);
	public static final DeferredItem<BlockItem> DOLL_SLIME_GIRL_ITEM = ITEMS.registerSimpleBlockItem(DOLL_SLIME_GIRL);
	public static final DeferredItem<BlockItem> PEARL_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(PEARL_BLOCK);

	//Items
	public static final DeferredItem<MemoryBookItem> BOOK_OF_MEMORY = ITEMS.register("book_of_memory", () -> new MemoryBookItem(itemBuilder().rarity(Rarity.RARE)));
	public static final DeferredItem<Item> BROOM = ITEMS.register("broom", () -> new Item(itemBuilder().rarity(Rarity.RARE)));
	public static final DeferredItem<WeaponBookItem> WEAPON_BOOK = ITEMS.register("weapon_book", () -> new WeaponBookItem(GaiaTiers.BOOK, itemBuilder().attributes(WeaponBookItem.createAttributes(GaiaTiers.BOOK, 3, -2.4F)).rarity(Rarity.RARE)));
	public static final DeferredItem<FreezingBookItem> WEAPON_BOOK_FREEZING = ITEMS.register("weapon_book_freezing", () -> new FreezingBookItem(GaiaTiers.BOOK, itemBuilder().rarity(Rarity.RARE)));
	public static final DeferredItem<NightmareBookItem> WEAPON_BOOK_NIGHTMARE = ITEMS.register("weapon_book_nightmare", () -> new NightmareBookItem(GaiaTiers.BOOK, itemBuilder().rarity(Rarity.RARE)));
	public static final DeferredItem<MetalBookItem> WEAPON_BOOK_METAL = ITEMS.register("weapon_book_metal", () -> new MetalBookItem(GaiaTiers.BOOK, itemBuilder().rarity(Rarity.RARE)));
	public static final DeferredItem<EnderBookItem> WEAPON_BOOK_ENDER = ITEMS.register("weapon_book_ender", () -> new EnderBookItem(GaiaTiers.BOOK, itemBuilder().rarity(Rarity.RARE)));
	public static final DeferredItem<HungerBookItem> WEAPON_BOOK_HUNGER = ITEMS.register("weapon_book_hunger", () -> new HungerBookItem(GaiaTiers.BOOK, itemBuilder().rarity(Rarity.RARE)));
	public static final DeferredItem<BattleBookItem> WEAPON_BOOK_BATTLE = ITEMS.register("weapon_book_battle", () -> new BattleBookItem(GaiaTiers.BOOK, itemBuilder().rarity(Rarity.RARE)));
	public static final DeferredItem<NatureBookItem> WEAPON_BOOK_NATURE = ITEMS.register("weapon_book_nature", () -> new NatureBookItem(GaiaTiers.BOOK, itemBuilder().rarity(Rarity.RARE)));
	public static final DeferredItem<WitherBookItem> WEAPON_BOOK_WITHER = ITEMS.register("weapon_book_wither", () -> new WitherBookItem(GaiaTiers.BOOK, itemBuilder().rarity(Rarity.EPIC)));
	public static final DeferredItem<BuffBookItem> WEAPON_BOOK_BUFF = ITEMS.register("weapon_book_buff", () -> new BuffBookItem(itemBuilder().durability(64).rarity(Rarity.RARE)));
	public static final DeferredItem<SwordItem> CURSED_METAL_SWORD = ITEMS.register("cursed_metal_sword", () -> new SwordItem(GaiaTiers.CURSED_METAL, itemBuilder().attributes(SwordItem.createAttributes(GaiaTiers.CURSED_METAL, 3, -2.2F)).rarity(Rarity.RARE)));
	public static final DeferredItem<SwordItem> METAL_CLUB = ITEMS.register("metal_club", () -> new SwordItem(Tiers.IRON, itemBuilder().attributes(SwordItem.createAttributes(Tiers.IRON, 4, -2.8F)).rarity(Rarity.RARE)));
	public static final DeferredItem<ExperienceItem> EXPERIENCE_IRON = ITEMS.register("experience_iron", () -> new ExperienceItem(itemBuilder().rarity(Rarity.RARE), 1));
	public static final DeferredItem<ExperienceItem> EXPERIENCE_GOLD = ITEMS.register("experience_gold", () -> new ExperienceItem(itemBuilder().rarity(Rarity.RARE), 2));
	public static final DeferredItem<ExperienceItem> EXPERIENCE_DIAMOND = ITEMS.register("experience_diamond", () -> new ExperienceItem(itemBuilder().rarity(Rarity.RARE), 4));
	public static final DeferredItem<Item> ELYTRA_FRAGMENT = ITEMS.register("elytra_fragment", () -> new Item(itemBuilder()));
	public static final DeferredItem<Item> TOTEM_FRAGMENT = ITEMS.register("totem_of_undying_fragment", () -> new Item(itemBuilder()));
	public static final DeferredItem<Item> DIAMOND_SHARD = ITEMS.register("diamond_shard", () -> new Item(itemBuilder()));
	public static final DeferredItem<Item> EMERALD_SHARD = ITEMS.register("emerald_shard", () -> new Item(itemBuilder()));
	public static final DeferredItem<Item> SHINY_PEARL = ITEMS.register("shiny_pearl", () -> new Item(itemBuilder()));
	public static final DeferredItem<FanItem> FAN = ITEMS.register("fan", () -> new FanItem(itemBuilder()));
	public static final DeferredItem<FireFanItem> FAN_FIRE = ITEMS.register("fan_fire", () -> new FireFanItem(itemBuilder().attributes(IceFanItem.createAttributes(7))));
	public static final DeferredItem<IceFanItem> FAN_ICE = ITEMS.register("fan_ice", () -> new IceFanItem(itemBuilder().attributes(IceFanItem.createAttributes(0))));
	public static final DeferredItem<FireshardItem> FIRESHARD = ITEMS.register("fireshard", () -> new FireshardItem(itemBuilder()));
	public static final DeferredItem<Item> FUR = ITEMS.register("fur", () -> new Item(itemBuilder()));
	public static final DeferredItem<GigaGearItem> GIGA_GEAR = ITEMS.register("giga_gear", () -> new GigaGearItem(itemBuilder().stacksTo(1).rarity(Rarity.EPIC)));
	public static final DeferredItem<XPEdibleItem> GOLDEN_APPLE_PIE = ITEMS.register("golden_apple_pie", () -> new XPEdibleItem(itemBuilder().stacksTo(1).food(GaiaFoods.GOLDEN_APPLY_PIE).rarity(Rarity.RARE), (rand) -> rand.nextInt(32) + 16));
	public static final DeferredItem<EdibleEffectItem> GOLDEN_APPLE_PIE_SLICE = ITEMS.register("golden_apple_pie_slice", () -> new EdibleEffectItem(itemBuilder().stacksTo(64).food(GaiaFoods.GOLDEN_APPLY_PIE_SLICE).rarity(Rarity.UNCOMMON)));
	public static final DeferredItem<MandrakeItem> MANDRAKE = ITEMS.register("mandrake", () -> new MandrakeItem(itemBuilder().stacksTo(16).food(GaiaFoods.MANDRAKE).rarity(Rarity.UNCOMMON)));
	public static final DeferredItem<SwordItem> MINOTAUR_HAMMER = ITEMS.register("minotaur_hammer", () -> new SwordItem(Tiers.IRON, itemBuilder().attributes(SwordItem.createAttributes(Tiers.IRON, 8, -2.8F))));
	public static final DeferredItem<HoneydewItem> HONEYDEW = ITEMS.register("honeydew", () -> new HoneydewItem(itemBuilder().stacksTo(64).food(GaiaFoods.HONEYDEW).rarity(Rarity.UNCOMMON)));
	public static final DeferredItem<HeadgearItem> HEADGEAR_BOOK = ITEMS.register("headgear_book", () -> new HeadgearItem(itemBuilder().stacksTo(1)));
	public static final DeferredItem<HeadgearItem> HEADGEAR_MOB = ITEMS.register("headgear_mob", () -> new HeadgearItem(itemBuilder().stacksTo(1)));
	public static final DeferredItem<HeadgearItem> HEADGEAR_BOLT = ITEMS.register("headgear_bolt", () -> new HeadgearItem(itemBuilder().stacksTo(1)));
	public static final DeferredItem<HeadgearItem> HEADGEAR_ARROW = ITEMS.register("headgear_arrow", () -> new HeadgearItem(itemBuilder().stacksTo(1)));
	public static final DeferredItem<HeadgearItem> HEADGEAR_DOLL = ITEMS.register("headgear_doll", () -> new HeadgearItem(itemBuilder().stacksTo(1)));
	public static final DeferredItem<HeadgearItem> HEADGEAR_EARS_ELF = ITEMS.register("headgear_ears_elf", () -> new HeadgearItem(itemBuilder().stacksTo(1)));
	public static final DeferredItem<KnucklesItem> KNUCKLES = ITEMS.register("knuckles", () -> new KnucklesItem(itemBuilder().stacksTo(1)));
	public static final DeferredItem<SeashellHairpinItem> SEASHELL_HAIRPIN = ITEMS.register("seashell_hairpin", () -> new SeashellHairpinItem(itemBuilder().durability(1)));
	public static final DeferredItem<RingItem> RING_OF_SPEED = ITEMS.register("ring_of_speed", () -> new RingItem(itemBuilder(),
			List.of(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 5 * 20, 1, true, false))));
	public static final DeferredItem<RingItem> RING_OF_HASTE = ITEMS.register("ring_of_haste", () -> new RingItem(itemBuilder(),
			List.of(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 5 * 20, 1, true, false))));
	public static final DeferredItem<RingItem> RING_OF_JUMP = ITEMS.register("ring_of_jump", () -> new RingItem(itemBuilder(),
			List.of(() -> new MobEffectInstance(MobEffects.JUMP, 5 * 20, 1, true, false))));
	public static final DeferredItem<RingItem> RING_OF_NIGHT = ITEMS.register("ring_of_night", () -> new RingItem(itemBuilder(),
			List.of(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 15 * 20, 1, true, false))));
	public static final DeferredItem<HeavyBarbellItem> HEAVY_BARBELL = ITEMS.register("heavy_barbell", () -> new HeavyBarbellItem(itemBuilder()));
	public static final DeferredItem<Item> MEAT = ITEMS.register("meat", () -> new Item(itemBuilder().food(GaiaFoods.MEAT)));
	public static final DeferredItem<SwordItem> METAL_DAGGER = ITEMS.register("metal_dagger", () -> new SwordItem(Tiers.IRON, itemBuilder().attributes(SwordItem.createAttributes(Tiers.IRON, 0, -2.0F))));
	public static final DeferredItem<MonsterFeedItem> MONSTER_FEED = ITEMS.register("monster_feed", () -> new MonsterFeedItem(itemBuilder().stacksTo(4).food(GaiaFoods.MONSTER_FEED)));
	public static final DeferredItem<MonsterFeedItem> PREMIUM_MONSTER_FEED = ITEMS.register("premium_monster_feed", () -> new MonsterFeedItem(itemBuilder().stacksTo(1).food(GaiaFoods.PREMIUM_MONSTER_FEED)));
	public static final DeferredItem<Item> QUILL = ITEMS.register("quill", () -> new Item(itemBuilder()));
	public static final DeferredItem<EdibleEffectItem> ROTTEN_HEART = ITEMS.register("rotten_heart", () -> new EdibleEffectItem(itemBuilder().food(GaiaFoods.ROTTEN_HEART).stacksTo(1)));
	public static final DeferredItem<SoulfireItem> SOULFIRE = ITEMS.register("soulfire", () -> new SoulfireItem(itemBuilder()));
	public static final DeferredItem<FuelItem> STONE_COAL = ITEMS.register("stone_coal", () -> new FuelItem(itemBuilder(), 3600));
	public static final DeferredItem<TaprootItem> TAPROOT = ITEMS.register("taproot", () -> new TaprootItem(itemBuilder().food(GaiaFoods.TAPROOT)));
	public static final DeferredItem<SummonStaffItem> ZOMBIE_STAFF = ITEMS.register("zombie_staff", () -> new SummonStaffItem(itemBuilder().rarity(Rarity.RARE).durability(10), () -> EntityType.ZOMBIE, () -> Ingredient.of(Items.ROTTEN_FLESH)));
	public static final DeferredItem<SummonStaffItem> SKELETON_STAFF = ITEMS.register("skeleton_staff", () -> new SummonStaffItem(itemBuilder().rarity(Rarity.RARE).durability(10), () -> EntityType.SKELETON, () -> Ingredient.of(Items.BONE)));
	public static final DeferredItem<SummonStaffItem> CAVE_SPIDER_STAFF = ITEMS.register("cave_spider_staff", () -> new SummonStaffItem(itemBuilder().rarity(Rarity.RARE).durability(10), () -> EntityType.CAVE_SPIDER, () -> Ingredient.of(Items.SPIDER_EYE)));
	public static final DeferredItem<MagicStaffItem> MAGIC_STAFF = ITEMS.register("magic_staff", () -> new MagicStaffItem(itemBuilder().rarity(Rarity.RARE).durability(64), () -> EntityType.CAVE_SPIDER));
	public static final DeferredItem<Item> PROJECTILE_MAGIC = ITEMS.register("projectile_magic", () -> new Item(itemBuilder().stacksTo(1)));
	public static final DeferredItem<Item> PROJECTILE_RANDOM_MAGIC = ITEMS.register("projectile_random_magic", () -> new Item(itemBuilder().stacksTo(1)));
	public static final DeferredItem<Item> PROJECTILE_WEB = ITEMS.register("projectile_web", () -> new Item(itemBuilder().stacksTo(1)));
	public static final DeferredItem<BombItem> PROJECTILE_BOMB = ITEMS.register("projectile_bomb", () -> new BombItem(itemBuilder().stacksTo(1)));
	public static final DeferredItem<Item> PROJECTILE_POISON = ITEMS.register("projectile_poison", () -> new Item(itemBuilder().stacksTo(1)));
	public static final DeferredItem<Item> PROJECTILE_BUBBLE = ITEMS.register("projectile_bubble", () -> new Item(itemBuilder().stacksTo(1)));
	public static final DeferredItem<WartJamItem> NETHER_WART_JAM = ITEMS.register("nether_wart_jam", () -> new WartJamItem(itemBuilder().food(GaiaFoods.NETHER_WART_JAM)));
	public static final DeferredItem<EdibleEffectItem> WITHERED_BRAIN = ITEMS.register("withered_brain", () -> new EdibleEffectItem(itemBuilder().stacksTo(1).food(GaiaFoods.WITHERED_BRAIN)));

	public static final DeferredItem<TieredShieldItem> STONE_SHIELD = ITEMS.register("stone_shield", () -> new TieredShieldItem(itemBuilder().rarity(Rarity.UNCOMMON).durability(150), () -> Ingredient.of(Tags.Items.COBBLESTONES)));
	public static final DeferredItem<TieredShieldItem> IRON_SHIELD = ITEMS.register("iron_shield", () -> new TieredShieldItem(itemBuilder().rarity(Rarity.UNCOMMON).durability(336), () -> Ingredient.of(Tags.Items.INGOTS_IRON)));
	public static final DeferredItem<TieredShieldItem> GOLD_SHIELD = ITEMS.register("gold_shield", () -> new TieredShieldItem(itemBuilder().rarity(Rarity.UNCOMMON).durability(260), () -> Ingredient.of(Tags.Items.INGOTS_GOLD)));
	public static final DeferredItem<TieredShieldItem> BONE_SHIELD = ITEMS.register("bone_shield", () -> new TieredShieldItem(itemBuilder().rarity(Rarity.UNCOMMON).durability(200), () -> Ingredient.of(Tags.Items.BONES)));

	//Lootable Item
	public static final DeferredItem<LootableItem> BAG_ARROWS = ITEMS.register("bag_arrows", () -> new LootableItem(itemBuilder().rarity(Rarity.RARE), GaiaLootTables.BAG_ARROW, GaiaSounds.BAG_OPEN));
	public static final DeferredItem<LootableItem> BAG_BOOK = ITEMS.register("bag_book", () -> new LootableItem(itemBuilder().rarity(Rarity.RARE), GaiaLootTables.BAG_BOOK, GaiaSounds.BAG_OPEN));
	public static final DeferredItem<LootableItem> BAG_RECORD = ITEMS.register("bag_record", () -> new LootableItem(itemBuilder().rarity(Rarity.RARE), GaiaLootTables.BAG_RECORD, GaiaSounds.BAG_OPEN));
	public static final DeferredItem<LootableItem> BOX_DIAMOND = ITEMS.register("box_diamond", () -> new LootableItem(itemBuilder().rarity(Rarity.RARE), GaiaLootTables.BOXES_DIAMOND, GaiaSounds.BOX_OPEN));
	public static final DeferredItem<LootableItem> BOX_END = ITEMS.register("box_end", () -> new LootableItem(itemBuilder().rarity(Rarity.RARE), GaiaLootTables.BOXES_END, GaiaSounds.BOX_OPEN));
	public static final DeferredItem<LootableItem> BOX_GOLD = ITEMS.register("box_gold", () -> new LootableItem(itemBuilder().rarity(Rarity.RARE), GaiaLootTables.BOXES_GOLD, GaiaSounds.BOX_OPEN));
	public static final DeferredItem<LootableItem> BOX_HAT = ITEMS.register("box_hat", () -> new LootableItem(itemBuilder(), GaiaLootTables.BOXES_HAT, GaiaSounds.BOX_OPEN));
	public static final DeferredItem<LootableItem> BOX_IRON = ITEMS.register("box_iron", () -> new LootableItem(itemBuilder().rarity(Rarity.RARE), GaiaLootTables.BOXES_IRON, GaiaSounds.BOX_OPEN));
	public static final DeferredItem<LootableItem> BOX_NETHER = ITEMS.register("box_nether", () -> new LootableItem(itemBuilder().rarity(Rarity.RARE), GaiaLootTables.BOXES_NETHER, GaiaSounds.BOX_OPEN));
	public static final DeferredItem<LootableItem> BOX_OLD = ITEMS.register("box_old", () -> new LootableItem(itemBuilder().rarity(Rarity.RARE), GaiaLootTables.BOXES_OLD, GaiaSounds.BOX_OPEN));
	public static final DeferredItem<LootableItem> BOX_OVERWORLD = ITEMS.register("box_overworld", () -> new LootableItem(itemBuilder().rarity(Rarity.RARE).rarity(Rarity.RARE), GaiaLootTables.BOXES_OVERWORLD, GaiaSounds.BOX_OPEN));
	public static final DeferredItem<LootableItem> BOX_EGG = ITEMS.register("box_egg", () -> new LootableItem(itemBuilder().rarity(Rarity.RARE).rarity(Rarity.RARE), GaiaLootTables.BOXES_EGG, GaiaSounds.BOX_OPEN));
	public static final DeferredItem<LootableItem> CHEST_DESERT = ITEMS.register("chest_desert", () -> new LootableItem(itemBuilder().rarity(Rarity.RARE), BuiltInLootTables.DESERT_PYRAMID, GaiaSounds.BOX_OPEN));
	public static final DeferredItem<LootableItem> CHEST_DUNGEON = ITEMS.register("chest_dungeon", () -> new LootableItem(itemBuilder().rarity(Rarity.RARE), BuiltInLootTables.SIMPLE_DUNGEON, GaiaSounds.BOX_OPEN));
	public static final DeferredItem<LootableItem> CHEST_JUNGLE = ITEMS.register("chest_jungle", () -> new LootableItem(itemBuilder().rarity(Rarity.RARE), BuiltInLootTables.JUNGLE_TEMPLE, GaiaSounds.BOX_OPEN));

	//Merchant Item
	public static final DeferredItem<Item> TRADER_TOKEN = ITEMS.register("trader_token", () -> new Item(itemBuilder()));
	public static final DeferredItem<Item> HOLSTAURUS_TOKEN = ITEMS.register("holstaurus_token", () -> new Item(itemBuilder()));
	public static final DeferredItem<Item> WERESHEEP_TOKEN = ITEMS.register("weresheep_token", () -> new Item(itemBuilder()));

	private static Item.Properties itemBuilder() {
		return new Item.Properties();
	}

	public static final Supplier<CreativeModeTab> GAIA_TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
			.icon(() -> new ItemStack(GaiaRegistry.DOLL_DRYAD.get()))
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.grimoireofgaia"))
			.displayItems((features, output) -> {
				List<ItemStack> stacks = GaiaRegistry.ITEMS.getEntries().stream()
						.map(reg -> new ItemStack(reg.get())).toList();
				output.acceptAll(stacks);
			}).build());
}
