package de.bigmachines.blocks.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class RubberLogBlock extends RotatedPillarBlock {
	
	public static final IntegerProperty RUBBER_LEVEL = IntegerProperty.create("rubber_level", 0, 4);
	
	public RubberLogBlock() {
		super(Block.Properties.from(Blocks.OAK_LOG).tickRandomly());
		this.setDefaultState(this.getDefaultState().with(RUBBER_LEVEL, 0));
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if(random.nextInt(8) == 0) {
			int rubber_level = state.get(RUBBER_LEVEL).intValue();
			if (rubber_level < 4) {
				worldIn.setBlockState(pos, state.with(RUBBER_LEVEL, rubber_level + 1), 2);
			}
		}
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(RUBBER_LEVEL);
	}
	

}
