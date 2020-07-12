package de.bigmachines.blocks.blocks.pipes.heatpipe;

import de.bigmachines.blocks.blocks.pipes.BlockPipeBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockHeatPipe extends BlockPipeBase implements ITileEntityProvider {

	public BlockHeatPipe() {
		super("pipe_heat");
	}
	
	@Override
	public TileEntity createNewTileEntity(@Nonnull final World worldIn, final int meta) {
		return new TileEntityHeatPipe();
	}
	
}
