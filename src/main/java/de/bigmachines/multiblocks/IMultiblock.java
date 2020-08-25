package de.bigmachines.multiblocks;

import de.bigmachines.multiblocks.validators.MultiblockValidator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public interface IMultiblock {
	
	/**
	 * Resource Location of the multiblock
	 * @return
	 */
	ResourceLocation getID();
	
	/**
	 * Size of the Multiblock Structure
	 * @return
	 */
	Vec3i getSize();
	
	/**
	 * create the Structure
	 * @param world
	 * @param pos
	 * @param side on which the front is
	 * @param player who created
	 * @return if it is created correctly
	 */
	boolean createStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player);
	
	/**
	 * Break the Structure
	 * @param world
	 * @param pos where it 
	 * @param player who created
	 * @return if it is broken correctly
	 */
	boolean breakStructure(World world, BlockPos pos, EntityPlayer player);
	
	
	MultiblockValidator getValidator();
	
}
