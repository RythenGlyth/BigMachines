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
		super(Block.Properties.copy(Blocks.OAK_LOG).randomTicks());
		registerDefaultState(defaultBlockState().setValue(RUBBER_LEVEL, 0));
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if(random.nextInt(8) == 0) {
			int rubber_level = state.getValue(RUBBER_LEVEL).intValue();
			if (rubber_level < 4) {
				worldIn.setBlock(pos, state.setValue(RUBBER_LEVEL, rubber_level + 1), 2);
			}
		}
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(RUBBER_LEVEL);
	}
	

}
