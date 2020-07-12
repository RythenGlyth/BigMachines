package de.bigmachines.multiblocks.multiblocks;

import de.bigmachines.Reference;
import de.bigmachines.multiblocks.MultiblockBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class MultiblockTest extends MultiblockBase {

	public MultiblockTest() {
		super(new ResourceLocation(Reference.MOD_ID, "multiblocks/test"));
	}

	@Override
	public Vec3i getSize() {
		return null;
	}

	@Override
	public boolean createStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean breakStructure(World world, BlockPos pos, EntityPlayer player) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
