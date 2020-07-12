package de.bigmachines.multiblocks;

import javax.annotation.Nullable;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class MultiblockPartTileEntity extends TileEntity {
	
	public boolean formed;
	@Nullable
	public BlockPos offsetFromOrigin;
	
}
