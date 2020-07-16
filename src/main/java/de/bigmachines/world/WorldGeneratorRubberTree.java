package de.bigmachines.world;

import java.util.Random;

import de.bigmachines.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGeneratorRubberTree {
	
	public static class Generator extends WorldGenAbstractTree {

		private IBlockState blockStateLog = ModBlocks.blockRubberLog.getDefaultState();
		private IBlockState blockStateLeaves = ModBlocks.blockRubberLeaves.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
		
		public Generator(boolean notify) {
			super(notify);
		}

		@Override
		public boolean generate(World worldIn, Random rand, BlockPos position) {
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
