package de.bigmachines.world;

import java.lang.reflect.Type;
import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import de.bigmachines.config.WorldGenerationConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.GameData;

public class WorldGeneratorMineable extends WorldGeneratorBase {
	
	private WorldGenMinable generator;
	
	private IBlockState blockState;
	private int maxPerChunk;
	private int minHeight;
	private int maxHeight;

	public WorldGeneratorMineable(IBlockState blockState, int maxPerChunk, int maxVeinSize, int minHeight, int maxHeight) {
		this.blockState = blockState;
		this.maxPerChunk = maxPerChunk;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		
		this.generator = new WorldGenMinable(blockState, maxVeinSize);
	}
	
	@Override
	public void generateChunk(Random random, int chunkX, int chunkY, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		//chunk to world coordinates
		int x = chunkX * 16;
		int z = chunkY * 16;
		for (int i = 0; i < this.maxPerChunk; i++) {
			int randPosX = x + random.nextInt(16);
			int randPosY = random.nextInt(this.maxHeight - this.minHeight) + this.minHeight;
			int randPosZ = z + random.nextInt(16);
			generator.generate(world, random, new BlockPos(randPosX, randPosY, randPosZ));
		}
	}

	public static class WorldGeneratorMineableDeserializer implements JsonDeserializer<WorldGeneratorMineable> {
		
		@Override
		public WorldGeneratorMineable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObj = json.getAsJsonObject();
			return new WorldGeneratorMineable(
					Block.getBlockFromName(jsonObj.has("block-id") ? jsonObj.get("block-id").getAsString() : "minecraft:stone").getDefaultState(),
					jsonObj.has("max-per-chunk") ? jsonObj.get("max-per-chunk").getAsInt() : 4,
					jsonObj.has("max-vein-size") ? jsonObj.get("max-vein-size").getAsInt() : 8,
					jsonObj.has("min-height") ? jsonObj.get("min-height").getAsInt() : 0,
					jsonObj.has("max-height") ? jsonObj.get("max-height").getAsInt() : 255
			);
		}
		
	}
	
}
