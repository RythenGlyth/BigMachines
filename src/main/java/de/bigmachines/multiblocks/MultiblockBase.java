package de.bigmachines.multiblocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public abstract class MultiblockBase implements IMultiblock {
	
	public ResourceLocation id;
	
	public MultiblockBase(ResourceLocation id) {
		this.id = id;
	}
	
	@Override
	public ResourceLocation getID() {
		return id;
	}
	
	
	
}
