package de.bigmachines.blocks.blocks.reactor;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DirectionalBlock;

public class ReactorCoreBlock extends DirectionalBlock {
	public ReactorCoreBlock() {
		super(Block.Properties.from(Blocks.OBSERVER));
	}
}