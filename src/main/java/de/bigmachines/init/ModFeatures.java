package de.bigmachines.init;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.TwoLayerFeature;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

public class ModFeatures {
	
	/*public static final ConfiguredFeature<?, ?> ORE_COPPER = register("ore_copper",
			new ConfiguredFeature<OreFeatureConfig, Feature<OreFeatureConfig>>(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
							ModMaterials.copper_ore.get().getDefaultState(), 8))
			.range(75).square().func_242731_b(12));
	
	public static final ConfiguredFeature<?, ?> ORE_LEAD = register("ore_lead",
			new ConfiguredFeature<OreFeatureConfig, Feature<OreFeatureConfig>>(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
							ModMaterials.lead_ore.get().getDefaultState(), 8))
			.range(40).square().func_242731_b(8));
	
	public static final ConfiguredFeature<?, ?> ORE_TIN = register("ore_tin",
			new ConfiguredFeature<OreFeatureConfig, Feature<OreFeatureConfig>>(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
							ModMaterials.tin_ore.get().getDefaultState(), 8))
			.range(75).square().func_242731_b(10));
	
	public static final ConfiguredFeature<?, ?> ORE_SILVER = register("ore_silver",
			new ConfiguredFeature<OreFeatureConfig, Feature<OreFeatureConfig>>(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
							ModMaterials.silver_ore.get().getDefaultState(), 8))
			.range(40).square().func_242731_b(6));
	
	public static final ConfiguredFeature<?, ?> ORE_ALUMINUM = register("ore_aluminum",
			new ConfiguredFeature<OreFeatureConfig, Feature<OreFeatureConfig>>(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
							ModMaterials.aluminum_ore.get().getDefaultState(), 8))
			.range(75).square().func_242731_b(10));*/
	
	public static Map<ConfiguredFeature<?, ?>, GenerationStage.Decoration> features = new HashMap<ConfiguredFeature<?,?>, GenerationStage.Decoration>();
	
	public static ConfiguredFeature<BaseTreeFeatureConfig, ?> tree_rubber = Feature.TREE.withConfiguration(
		new BaseTreeFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.rubber_log.get().defaultBlockState()),
			new SimpleBlockStateProvider(ModBlocks.rubber_leaves.get().defaultBlockState()),
			new BlobFoliagePlacer(
				/* radius: */ FeatureSpread.of(
					2, //base
					0  //spread
				),
				/* offset: */ FeatureSpread.of(
					0, //base
					0  //spread
				),
				/* height: */ 3
			),
			new StraightTrunkPlacer(
				4, //baseHeight
				2, //heightRandA
				0  //heightRandB
			),
			new TwoLayerFeature(
				1, //limit
				0, //lowerSize
				1  //upperSize
			)
		).ignoreVines().build()
	);
	
	public static ConfiguredFeature<?, ?> tree_rubber_placement = tree_rubber.withPlacement(Features.Placements.HEIGHTMAP).withPlacement(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(2, 0.1f, 2)).withPlacement(Placement.CHANCE.configure(new ChanceConfig(100))));
	
	static {
		registerDefaults();
	}
	
	public static void registerDefaults() {
		register("tree_rubber_placement", tree_rubber_placement, GenerationStage.Decoration.VEGETAL_DECORATION);
		//register("tree_rubber", tree_rubber, GenerationStage.Decoration.VEGETAL_DECORATION);
	}
	
	public static void init() {
		//NBTUtil.readBlockState(JsonToNBT.getTagFromJson(jsonString))
	}
	
	public static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> configuredFeature, GenerationStage.Decoration decoration) {
		ConfiguredFeature<FC, ?> feature = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key, configuredFeature);
		features.put(feature, decoration);
		return feature;
	}

    public static TopSolidRangeConfig topRangeConfig(int minHeight, int maxHeight) {
        return new TopSolidRangeConfig(minHeight, minHeight, maxHeight);
    }
	
}
