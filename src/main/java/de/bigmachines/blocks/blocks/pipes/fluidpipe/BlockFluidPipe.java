package de.bigmachines.blocks.blocks.pipes.fluidpipe;

import de.bigmachines.blocks.blocks.pipes.BlockPipeBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFluidPipe extends BlockPipeBase implements ITileEntityProvider {

	public BlockFluidPipe() {
		super("pipe_fluid");
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityFluidPipe();
	}
	
	
}
