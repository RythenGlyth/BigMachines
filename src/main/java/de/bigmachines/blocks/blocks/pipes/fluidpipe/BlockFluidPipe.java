package de.bigmachines.blocks.blocks.pipes.fluidpipe;

import de.bigmachines.blocks.blocks.pipes.BlockPipeBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockFluidPipe extends BlockPipeBase implements ITileEntityProvider {

	public BlockFluidPipe() {
		super("pipe_fluid");
	}
	
	@Override
	@Nonnull
	public TileEntity createNewTileEntity(@Nullable final World worldIn, final int meta) {
		return new TileEntityFluidPipe();
	}
	
	@Override
	public int getLightValue(@Nonnull final IBlockState state, @Nonnull final IBlockAccess world, @Nonnull final BlockPos pos) {
		final TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityFluidPipe) {
			final TileEntityFluidPipe tileEntityPipeBase = (TileEntityFluidPipe) tile;
			if(tileEntityPipeBase.fluidStorage.getContents() != null) {
				return tileEntityPipeBase.fluidStorage.getContents().getFluid().getLuminosity();
			}
		}
		return super.getLightValue(state, world, pos);
	}
	
}
