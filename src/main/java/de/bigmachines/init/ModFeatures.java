package de.bigmachines.init;

import java.util.ArrayList;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.DepthAverageConfig;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;

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
	
	public static ArrayList<ConfiguredFeature<?, ?>> features = new ArrayList<ConfiguredFeature<?,?>>();
	
	public static void init() {
		//NBTUtil.readBlockState(JsonToNBT.getTagFromJson(jsonString))
	}
	
	public static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> configuredFeature) {
		ConfiguredFeature<FC, ?> feature = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key, configuredFeature);
		features.add(feature);
		return feature;
	}

    public static TopSolidRangeConfig topRangeConfig(int minHeight, int maxHeight) {
        return new TopSolidRangeConfig(minHeight, minHeight, maxHeight);
    }
	
}
