package de.bigmachines.blocks.blocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;

public class PinkGravelBlock extends FallingBlock {

    public PinkGravelBlock(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    public int getDustColor(BlockState blockState, IBlockReader blockGetter, BlockPos blockPos) {
        return 0xb80ef4;
    }

}

