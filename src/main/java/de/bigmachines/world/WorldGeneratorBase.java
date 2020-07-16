package de.bigmachines.world;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public abstract class WorldGeneratorBase {
	
	public final List<Integer> blacklistedDimensions;
	
	public WorldGeneratorBase(List<Integer> blacklistedDimensions) {
		this.blacklistedDimensions = blacklistedDimensions;
	}
	
	public static final HashMap<String, Class<? extends WorldGeneratorBase>> worldGeneratorTypes = new HashMap<>();
	
	public static void addWorldGeneratorType(String name, Class<? extends WorldGeneratorBase> worldGeneratorType) {
		worldGeneratorTypes.put(name, worldGeneratorType);
	}
	
	public static void registerGenerators() {
		addWorldGeneratorType("mineable", WorldGeneratorMineable.class);
		addWorldGeneratorType("structure", WorldGeneratorStructure.class);
	}

	public boolean isDimensionBlacklisted(int dimension) {
		for (Integer blacklistedDimension : blacklistedDimensions) if (blacklistedDimension == dimension) return true;
		return false;
	}

	public abstract void generateChunk(Random random, int chunkX, int chunkY, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider);

	public static class WorldGeneratorBaseDeserializer implements JsonDeserializer<ModWorldGenerator> {

		@Override
		public ModWorldGenerator deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			return null;
		}
		
	}
	
}
