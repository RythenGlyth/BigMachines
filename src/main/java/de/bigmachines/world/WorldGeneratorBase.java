package de.bigmachines.world;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class WorldGeneratorBase {
	
	public static HashMap<String, Class<? extends WorldGeneratorBase>> worldGeneratorTypes;
	
	public static void addWorldGeneratorType(String name, Class<? extends WorldGeneratorBase> worldGeneratorType) {
		worldGeneratorTypes.put(name, worldGeneratorType);
	}
	
	public static void registerGenerators() {
		addWorldGeneratorType("ore", WorldGeneratorMineable.class);
	}

	public void generateChunk(Random random, int chunkX, int chunkY, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		
	}
	
}
