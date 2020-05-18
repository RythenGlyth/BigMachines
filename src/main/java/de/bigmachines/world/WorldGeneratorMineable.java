package de.bigmachines.world;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import de.bigmachines.config.WorldGenerationConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import scala.actors.threadpool.Arrays;

public class WorldGeneratorMineable extends WorldGeneratorBase {
	
	private WorldGenMinable generator;
	
	private IBlockState blockState;
	private int maxPerChunk;
	private int maxVeinSize;
	private int minHeight;
	private int maxHeight;
	private List<Integer> blacklistedDimensions;

	public WorldGeneratorMineable(IBlockState blockState, int maxPerChunk, int maxVeinSize, int minHeight, int maxHeight, List<Integer> blacklistedDimensions, List<Block> replaceable) {
		this.blockState = blockState;
		this.maxPerChunk = maxPerChunk;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		this.blacklistedDimensions = blacklistedDimensions;
		this.maxVeinSize = maxVeinSize;
		
		this.generator = new WorldGenMinable(blockState, maxVeinSize, new Predicate<IBlockState>() {
			
			@Override
			public boolean apply(IBlockState input) {
				if(input == null) return false;
				for(int i = 0; i < replaceable.size(); i++) if(input.getBlock().equals(replaceable.get(i))) {
					return true;
				}
				return false;
			}
		});
	}
	
	@Override
	public void generateChunk(Random random, int chunkX, int chunkY, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(!isDimensionBlacklisted(world.provider.getDimension())) {
			//chunk to world coordinates
			int x = chunkX * 16;
			int z = chunkY * 16;
			for (int i = 0; i < this.maxPerChunk; i++) {
				int randPosX = x + random.nextInt(16);
				int randPosY = random.nextInt(this.maxHeight - this.minHeight) + this.minHeight;
				int randPosZ = z + random.nextInt(16);
				this.generator.generate(world, random, new BlockPos(randPosX, randPosY, randPosZ));
			}
			
		}
	}

	private boolean isDimensionBlacklisted(int dimension) {
		for(int i = 0; i < blacklistedDimensions.size(); i++) {
			if(blacklistedDimensions.get(i) == dimension) return true;
		}
		return false;
	}

	public static class WorldGeneratorMineableDeserializer implements JsonDeserializer<WorldGeneratorMineable> {
		
		@Override
		public WorldGeneratorMineable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObj = json.getAsJsonObject();
			
			
			if(!jsonObj.has("block-id")) return null;
			Block b = Block.getBlockFromName(jsonObj.get("block-id").getAsString());
			IBlockState blockState = b.getDefaultState();
			
			
			List<Integer> blacklistedDimensions;
			if(jsonObj.has("blacklisted-dimensions") && jsonObj.get("blacklisted-dimensions").isJsonArray()) {
				blacklistedDimensions = WorldGenerationConfig.gson.fromJson(jsonObj.get("blacklisted-dimensions").getAsJsonArray(), new TypeToken<List<Integer>>(){}.getType());
			} else {
				blacklistedDimensions = new ArrayList<Integer>();
			}
			
			
			List<Block> replaceables = new ArrayList<Block>();
			if(jsonObj.has("replaceable-blocks") && jsonObj.get("replaceable-blocks").isJsonArray()) {
				JsonArray replaceableBlocks = jsonObj.get("replaceable-blocks").getAsJsonArray();
				for(int i = 0; i < replaceableBlocks.size(); i++) {
					Block replaceableBlock = Block.getBlockFromName(replaceableBlocks.get(i).getAsString());
					if(replaceableBlock != null) replaceables.add(replaceableBlock);
				}
			}
			if(replaceables.isEmpty()) {
				replaceables.add(Blocks.STONE);
			}
			
			
			return new WorldGeneratorMineable(
					blockState,
					jsonObj.has("max-per-chunk") ? jsonObj.get("max-per-chunk").getAsInt() : 4,
					jsonObj.has("max-vein-size") ? jsonObj.get("max-vein-size").getAsInt() : 8,
					jsonObj.has("min-height") ? jsonObj.get("min-height").getAsInt() : 0,
					jsonObj.has("max-height") ? jsonObj.get("max-height").getAsInt() : 255,
					blacklistedDimensions,
					replaceables
			);
		}
		
	}
	
}
