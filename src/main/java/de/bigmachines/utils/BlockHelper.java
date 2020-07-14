package de.bigmachines.utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockHelper {

	@Nullable
	public static TileEntity getAdjacentTileEntity(final World world, BlockPos pos, final EnumFacing dir) {
		pos = pos.offset(dir);
		return world == null || !world.isBlockLoaded(pos) ? null : world.getTileEntity(pos);
	}

	@Nullable
	public static TileEntity getAdjacentTileEntity(final TileEntity refTile, final EnumFacing dir) {
		return refTile == null ? null : getAdjacentTileEntity(refTile.getWorld(), refTile.getPos(), dir);
	}
	
	public static void callBlockUpdate(@Nonnull final World world, final BlockPos pos) {
		final IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, Constants.BlockFlags.DEFAULT);
	}
	
	public static void callNeighborStateChange(final World world, final BlockPos pos) {
		world.notifyNeighborsOfStateChange(pos, world.getBlockState(pos).getBlock(), false);
	}

	public static void callNeighborTileChange(final World world, final BlockPos pos) {
		world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
	}

	@Nullable
	public static EnumFacing getConnectingFace(final BlockPos from, final BlockPos to) {
		final int distX = to.getX() - from.getX();
		final int distY = to.getY() - from.getY();
		final int distZ = to.getZ() - from.getZ();
		if (distX == +1 && distY == 00 && distZ == 00) return EnumFacing.WEST;
		if (distX == -1 && distY == 00 && distZ == 00) return EnumFacing.EAST;
		if (distX == 00 && distY == +1 && distZ == 00) return EnumFacing.DOWN;
		if (distX == 00 && distY == -1 && distZ == 00) return EnumFacing.UP;
		if (distX == 00 && distY == 00 && distZ == +1) return EnumFacing.NORTH;
		if (distX == 00 && distY == 00 && distZ == -1) return EnumFacing.SOUTH;
		return null;
	}
	
	@Nullable
	public static RayTraceResult rayTrace(final EntityPlayer player, final double blockReachDistance, final BlockPos pos, final AxisAlignedBB boundingBox) {
		final float partialTicks = 1F; //Minecraft.getMinecraft().getRenderPartialTicks();
        /*Vec3d vec3d = Minecraft.getMinecraft().player.getPositionEyes(partialTicks);
        Vec3d vec3d1 = Minecraft.getMinecraft().player.getPositionEyes(partialTicks).addVector(vec3d.x * blockReachDistance, vec3d.y * blockReachDistance, vec3d.z * blockReachDistance)
        		.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
        */
		Vec3d vec3d = player.getPositionEyes(partialTicks);
        final Vec3d vec3d1 = player.getLook(partialTicks);
        Vec3d vec3d2 = vec3d.addVector(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
        
        vec3d = vec3d.subtract(pos.getX(), pos.getY(), pos.getZ());
        vec3d2 = vec3d2.subtract(pos.getX(), pos.getY(), pos.getZ());
        
        final RayTraceResult raytraceresult = boundingBox.calculateIntercept(vec3d, vec3d2);
        return raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.addVector(pos.getX(), pos.getY(), pos.getZ()), raytraceresult.sideHit, pos);
	}
	
}
