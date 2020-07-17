package de.bigmachines.world;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import de.bigmachines.config.WorldGenerationConfig;
import de.bigmachines.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGeneratorRubberTree extends WorldGeneratorBase {
	
	WorldGeneratorRubberTree.Generator generator;
	int chance;
	
	public WorldGeneratorRubberTree(WorldGeneratorRubberTree.Generator generator, List<Integer> blacklistedDimensions, int chance) {
		super(blacklistedDimensions);
		this.generator = generator;
		this.chance = chance;
	}

	@Override
	public void generateChunk(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		int x = chunkX * 16;
		int z = chunkZ * 16;
		for(int i = random.nextInt(4); i < 16; i += random.nextInt(6) + 4) {
			for(int j = random.nextInt(4); j < 16; j += random.nextInt(6) + 4) {
				if(random.nextInt(chance) == 0) {
					BlockPos pos = ModWorldGenerator.getGrassFromAbove(world, x + i, z + j).add(0, 1, 0);
					if(pos.getY() > 50) this.generator.generate(world, random, pos);
				}
			}
		}
	}

	public static class WorldGeneratorRubberTreeDeserializer implements JsonDeserializer<WorldGeneratorRubberTree> {

		@Override
		public WorldGeneratorRubberTree deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObj = json.getAsJsonObject();
			
			List<Integer> blacklistedDimensions;
			if(jsonObj.has("blacklisted-dimensions") && jsonObj.get("blacklisted-dimensions").isJsonArray()) {
				blacklistedDimensions = WorldGenerationConfig.gson.fromJson(jsonObj.get("blacklisted-dimensions").getAsJsonArray(), new TypeToken<List<Integer>>(){}.getType());
			} else {
				blacklistedDimensions = new ArrayList<>();
			}
			
			return new WorldGeneratorRubberTree(
					new WorldGeneratorRubberTree.Generator(),
					blacklistedDimensions,
					jsonObj.has("chance") ? jsonObj.get("chance").getAsInt() : 100
			);
		}
		
	}

	public static class Generator extends WorldGenAbstractTree {

		private IBlockState blockStateLog = ModBlocks.blockRubberLog.getDefaultState();
		private IBlockState blockStateLeaves = ModBlocks.blockRubberLeaves.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
		
		public Generator() {
			super(true);
		}

		@Override
		public boolean generate(World worldIn, Random rand, BlockPos position) {
			System.out.println(position);
			if(checkForBlockFreedom(worldIn, position.add(0, 1, 0), position.add(0, 5, 0))) {
				int height = 6;
				if(isReplaceable(worldIn, position.add(0, 6, 0)) && rand.nextInt(2) == 0) height = 7;
				for(int i = 0; i < height; i++) {
					worldIn.setBlockState(position.add(0, i, 0), blockStateLog);
				}
				
				if(isReplaceable(worldIn, position.add(0, height, 0))) worldIn.setBlockState(position.add(0, height, 0), blockStateLeaves);

				
				if(isReplaceable(worldIn, position.add( 1, height - 1,  0))) worldIn.setBlockState(position.add( 1, height - 1,  0), blockStateLeaves);
				if(isReplaceable(worldIn, position.add(-1, height - 1,  0))) worldIn.setBlockState(position.add(-1, height - 1,  0), blockStateLeaves);
				if(isReplaceable(worldIn, position.add( 0, height - 1,  1))) worldIn.setBlockState(position.add( 0, height - 1,  1), blockStateLeaves);
				if(isReplaceable(worldIn, position.add( 0, height - 1, -1))) worldIn.setBlockState(position.add( 0, height - 1, -1), blockStateLeaves);
				

				if(isReplaceable(worldIn, position.add( 1, height - 2,  0))) worldIn.setBlockState(position.add( 1, height - 2,  0), blockStateLeaves);
				if(isReplaceable(worldIn, position.add(-1, height - 2,  0))) worldIn.setBlockState(position.add(-1, height - 2,  0), blockStateLeaves);
				if(isReplaceable(worldIn, position.add( 0, height - 2,  1))) worldIn.setBlockState(position.add( 0, height - 2,  1), blockStateLeaves);
				if(isReplaceable(worldIn, position.add( 0, height - 2, -1))) worldIn.setBlockState(position.add( 0, height - 2, -1), blockStateLeaves);
				

				if(isReplaceable(worldIn, position.add( 1, height - 3,  0))) worldIn.setBlockState(position.add( 1, height - 3,  0), blockStateLeaves);
				if(isReplaceable(worldIn, position.add(-1, height - 3,  0))) worldIn.setBlockState(position.add(-1, height - 3,  0), blockStateLeaves);
				if(isReplaceable(worldIn, position.add( 0, height - 3,  1))) worldIn.setBlockState(position.add( 0, height - 3,  1), blockStateLeaves);
				if(isReplaceable(worldIn, position.add( 0, height - 3, -1))) worldIn.setBlockState(position.add( 0, height - 3, -1), blockStateLeaves);
				
				if(isReplaceable(worldIn, position.add( 1, height - 3,  1))) worldIn.setBlockState(position.add( 1, height - 3,  1), blockStateLeaves);
				if(isReplaceable(worldIn, position.add(-1, height - 3,  1))) worldIn.setBlockState(position.add(-1, height - 3,  1), blockStateLeaves);
				if(isReplaceable(worldIn, position.add(-1, height - 3, -1))) worldIn.setBlockState(position.add(-1, height - 3, -1), blockStateLeaves);
				if(isReplaceable(worldIn, position.add( 1, height - 3, -1))) worldIn.setBlockState(position.add( 1, height - 3, -1), blockStateLeaves);
				
				if(isReplaceable(worldIn, position.add( 2, height - 3,  0))) worldIn.setBlockState(position.add( 2, height - 3,  0), blockStateLeaves);
				if(isReplaceable(worldIn, position.add(-2, height - 3,  0))) worldIn.setBlockState(position.add(-2, height - 3,  0), blockStateLeaves);
				if(isReplaceable(worldIn, position.add( 0, height - 3,  2))) worldIn.setBlockState(position.add( 0, height - 3,  2), blockStateLeaves);
				if(isReplaceable(worldIn, position.add( 0, height - 3, -2))) worldIn.setBlockState(position.add( 0, height - 3, -2), blockStateLeaves);
				

				if(isReplaceable(worldIn, position.add( 1, height - 4,  0))) worldIn.setBlockState(position.add( 1, height - 4,  0), blockStateLeaves);
				if(isReplaceable(worldIn, position.add(-1, height - 4,  0))) worldIn.setBlockState(position.add(-1, height - 4,  0), blockStateLeaves);
				if(isReplaceable(worldIn, position.add( 0, height - 4,  1))) worldIn.setBlockState(position.add( 0, height - 4,  1), blockStateLeaves);
				if(isReplaceable(worldIn, position.add( 0, height - 4, -1))) worldIn.setBlockState(position.add( 0, height - 4, -1), blockStateLeaves);
				
				if(isReplaceable(worldIn, position.add( 1, height - 4,  1))) worldIn.setBlockState(position.add( 1, height - 4,  1), blockStateLeaves);
				if(isReplaceable(worldIn, position.add(-1, height - 4,  1))) worldIn.setBlockState(position.add(-1, height - 4,  1), blockStateLeaves);
				if(isReplaceable(worldIn, position.add(-1, height - 4, -1))) worldIn.setBlockState(position.add(-1, height - 4, -1), blockStateLeaves);
				if(isReplaceable(worldIn, position.add( 1, height - 4, -1))) worldIn.setBlockState(position.add( 1, height - 4, -1), blockStateLeaves);
				
				if(isReplaceable(worldIn, position.add( 2, height - 4,  0))) worldIn.setBlockState(position.add( 2, height - 4,  0), blockStateLeaves);
				if(isReplaceable(worldIn, position.add(-2, height - 4,  0))) worldIn.setBlockState(position.add(-2, height - 4,  0), blockStateLeaves);
				if(isReplaceable(worldIn, position.add( 0, height - 4,  2))) worldIn.setBlockState(position.add( 0, height - 4,  2), blockStateLeaves);
				if(isReplaceable(worldIn, position.add( 0, height - 4, -2))) worldIn.setBlockState(position.add( 0, height - 4, -2), blockStateLeaves);
				
				return true;
			} else {
				return false;
			}
		}
		
		public boolean isReplaceable(World world, BlockPos pos) {
			Block b = world.getBlockState(pos).getBlock();
			return b instanceof BlockLeaves || b.isReplaceable(world, pos);
		}
		
		public boolean checkForBlockFreedom(World world, BlockPos pos1, BlockPos pos2) {
	        for (BlockPos.MutableBlockPos mutableBlockPos : BlockPos.getAllInBoxMutable(pos1, pos2)) {
	            if (!isReplaceable(world, mutableBlockPos)) {
	            	return false;
	            }
	        }
	        return true;
		}
		
	}

}
