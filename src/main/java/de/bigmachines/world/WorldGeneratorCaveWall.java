package de.bigmachines.world;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import de.bigmachines.config.WorldGenerationConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldGeneratorCaveWall extends WorldGeneratorBase {

	private final IBlockState placeState;
	private final int maxPerChunk;
	private final int minHeight;
	private final int maxHeight;
	private final boolean invertFacing;

	public WorldGeneratorCaveWall(IBlockState placeState, int maxPerChunk, int minHeight, int maxHeight, boolean invertFacing, List<Integer> blacklistedDimensions) {
		super(blacklistedDimensions);
		this.placeState = placeState;
		this.maxPerChunk = maxPerChunk;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		this.invertFacing = invertFacing;
	}

	@Override
	public void generateChunk(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		for(int i = 0; i < maxPerChunk; i++) {
			int x = chunkX * 16 + random.nextInt(15);
			int y = minHeight + random.nextInt(Math.max(0, maxHeight - minHeight) + 1);
			int z = chunkZ * 16 + random.nextInt(15);
			BlockPos pos = new BlockPos(x, y, z);
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock().isReplaceable(world, pos)) {
				ArrayList<EnumFacing> blocksNearby = new ArrayList<EnumFacing>();
				for(EnumFacing facing : EnumFacing.values()) {
					BlockPos offsetPos = pos.offset(facing);
					IBlockState offsetState = world.getBlockState(offsetPos);
					if(!offsetState.getBlock().isReplaceable(world, offsetPos)) blocksNearby.add(facing);
				}
				if(blocksNearby.size() > 0) {
					EnumFacing facing = blocksNearby.get(random.nextInt(blocksNearby.size()));
					world.setBlockState(pos, placeState.withProperty(BlockDirectional.FACING, facing));
				}
			}
		}
	}

	public static class WorldGeneratorCaveWallDeserializer implements JsonDeserializer<WorldGeneratorCaveWall> {
		
		@Override
		public WorldGeneratorCaveWall deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObj = json.getAsJsonObject();
			
			if(jsonObj.has("block")) {
			 
				List<Integer> blacklistedDimensions;
				if(jsonObj.has("blacklisted-dimensions") && jsonObj.get("blacklisted-dimensions").isJsonArray()) {
					blacklistedDimensions = WorldGenerationConfig.gson.fromJson(jsonObj.get("blacklisted-dimensions").getAsJsonArray(), new TypeToken<List<Integer>>(){}.getType());
				} else {
					blacklistedDimensions = new ArrayList<>();
				}
				
				try {
					return new WorldGeneratorCaveWall(
							NBTUtil.readBlockState(JsonToNBT.getTagFromJson(jsonObj.get("block").toString())),
							jsonObj.has("max-per-chunk") ? jsonObj.get("max-per-chunk").getAsInt() : 4,
							jsonObj.has("min-height") ? jsonObj.get("min-height").getAsInt() : 0,
							jsonObj.has("max-height") ? jsonObj.get("max-height").getAsInt() : 60,
							jsonObj.has("invertFacing") ? jsonObj.get("invertFacing").getAsBoolean() : false,
							blacklistedDimensions
					);
				} catch (NBTException e) {
					e.printStackTrace();
				}
				
			}
			return null;
		}
		
	}

}
