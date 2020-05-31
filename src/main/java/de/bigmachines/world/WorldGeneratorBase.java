package de.bigmachines.world;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

import java.util.HashMap;
import java.util.Random;

public abstract class WorldGeneratorBase {
	
	public static HashMap<String, Class<? extends WorldGeneratorBase>> worldGeneratorTypes = new HashMap<>();
	
	public static void addWorldGeneratorType(String name, Class<? extends WorldGeneratorBase> worldGeneratorType) {
		worldGeneratorTypes.put(name, worldGeneratorType);
	}
	
	public static void registerGenerators() {
		addWorldGeneratorType("mineable", WorldGeneratorMineable.class);
	}

	public void generateChunk(Random random, int chunkX, int chunkY, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		
	}
	
}
