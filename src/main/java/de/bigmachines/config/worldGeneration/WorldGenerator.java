package de.bigmachines.config.worldGeneration;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class WorldGenerator {
	
	public static HashMap<String, Class<? extends WorldGenerator>> worldGeneratorTypes;
	
	public static void addWorldGeneratorType(String name, Class<? extends WorldGenerator> worldGeneratorType) {
		worldGeneratorTypes.put(name, worldGeneratorType);
	}
	
	public static void registerGenerators() {
		addWorldGeneratorType("oreGen", WorldGeneratorMineable.class);
	}
	
}
