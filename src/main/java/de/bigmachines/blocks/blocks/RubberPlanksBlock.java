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

public class RubberPlanksBlock extends RotatedPillarBlock {
	
	public RubberPlanksBlock() {
		super(Block.Properties.copy(Blocks.OAK_PLANKS));
	}

}
