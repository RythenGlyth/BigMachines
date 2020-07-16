package de.bigmachines.world;

import java.util.Random;

import de.bigmachines.init.ModBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGeneratorRubberTree {
	
	public static class Generator extends WorldGenAbstractTree {

		public Generator(boolean notify) {
			super(notify);
		}

		@Override
		public boolean generate(World worldIn, Random rand, BlockPos position) {
			worldIn.setBlockState(position, ModBlocks.blockRubberLog.getDefaultState());
			
			return false;
		}
		
	}

}
