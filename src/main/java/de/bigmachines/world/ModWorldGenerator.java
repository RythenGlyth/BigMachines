package de.bigmachines.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class ModWorldGenerator implements IWorldGenerator {

	public static List<WorldGeneratorBase> generators = new ArrayList<WorldGeneratorBase>();
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		for(WorldGeneratorBase generator : generators) {
			generator.generateChunk(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
		}
	}

	public static void addGenerator(WorldGeneratorBase generator) {
		generators.add(generator);
	}
	
}
