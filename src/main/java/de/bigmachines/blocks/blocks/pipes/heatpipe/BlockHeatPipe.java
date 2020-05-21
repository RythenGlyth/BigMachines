package de.bigmachines.blocks.blocks.pipes.heatpipe;

import de.bigmachines.blocks.blocks.pipes.BlockPipeBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHeatPipe extends BlockPipeBase {

	public BlockHeatPipe() {
		super("pipe_heat");
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityHeatPipe();
	}
	
}
