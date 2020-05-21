package de.bigmachines.utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class BlockHelper {
	
	public static TileEntity getAdjacentTileEntity(World world, BlockPos pos, EnumFacing dir) {
		pos = pos.offset(dir);
		return world == null || !world.isBlockLoaded(pos) ? null : world.getTileEntity(pos);
	}
	
	public static TileEntity getAdjacentTileEntity(TileEntity refTile, EnumFacing dir) {
		return refTile == null ? null : getAdjacentTileEntity(refTile.getWorld(), refTile.getPos(), dir);
	}
	
	public static void callBlockUpdate(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, Constants.BlockFlags.DEFAULT);
	}
	
	public void callNeighborStateChange(World world, BlockPos pos) {
		world.notifyNeighborsOfStateChange(pos, world.getBlockState(pos).getBlock(), false);
	}

	public void callNeighborTileChange(World world, BlockPos pos) {
		world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
	}
	
}
