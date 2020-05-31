package de.bigmachines.utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

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
	
	public static void callNeighborStateChange(World world, BlockPos pos) {
		world.notifyNeighborsOfStateChange(pos, world.getBlockState(pos).getBlock(), false);
	}

	public static void callNeighborTileChange(World world, BlockPos pos) {
		world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
	}
	
	@Nullable
	public static RayTraceResult rayTrace(EntityPlayer player, double blockReachDistance, BlockPos pos, AxisAlignedBB boundingBox) {
		float partialTicks = 1F; //Minecraft.getMinecraft().getRenderPartialTicks();
        /*Vec3d vec3d = Minecraft.getMinecraft().player.getPositionEyes(partialTicks);
        Vec3d vec3d1 = Minecraft.getMinecraft().player.getPositionEyes(partialTicks).addVector(vec3d.x * blockReachDistance, vec3d.y * blockReachDistance, vec3d.z * blockReachDistance)
        		.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
        */
		Vec3d vec3d = player.getPositionEyes(partialTicks);
        Vec3d vec3d1 = player.getLook(partialTicks);
        Vec3d vec3d2 = vec3d.addVector(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
        
        vec3d = vec3d.subtract(pos.getX(), pos.getY(), pos.getZ());
        vec3d2 = vec3d2.subtract(pos.getX(), pos.getY(), pos.getZ());
        
        RayTraceResult raytraceresult = boundingBox.calculateIntercept(vec3d, vec3d2);
        return raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.addVector(pos.getX(), pos.getY(), pos.getZ()), raytraceresult.sideHit, pos);
	}
	
}
