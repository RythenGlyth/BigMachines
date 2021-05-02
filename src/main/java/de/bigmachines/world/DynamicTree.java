package de.bigmachines.world;

import net.minecraft.block.BlockState;
import net.minecraft.block.trees.Tree;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

// Thanks to @Random on discord
public class DynamicTree extends Tree {
	private final ResourceLocation configuredFeatureId;
	@Nullable private ConfiguredFeature<BaseTreeFeatureConfig,?> treeFeature;
	
	public DynamicTree(ResourceLocation configuredFeatureId) {
		this.configuredFeatureId = configuredFeatureId;
	}
	
	@Nullable
	@Override
	protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random randomIn, boolean largeHive) {
		return treeFeature;
	}
	
	@Override
	public boolean growTree(ServerWorld world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, Random rand) {
		MutableRegistry<ConfiguredFeature<?, ?>> registry = world.registryAccess().registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY);
		ConfiguredFeature<?, ?> feature = registry.get(configuredFeatureId);
		
		if (feature != null && feature.config instanceof BaseTreeFeatureConfig) {
			this.treeFeature = (ConfiguredFeature<BaseTreeFeatureConfig, ?>) feature;
		} else {
			this.treeFeature = null;
			return false;
		}
		
		return super.growTree(world, chunkGenerator, pos, state, rand);
	}
}
