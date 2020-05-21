package de.bigmachines.blocks.blocks.pipes;

import java.util.List;

import de.bigmachines.blocks.BlockBase;
import de.bigmachines.init.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPipeBase extends BlockBase {

	public BlockPipeBase(String name) {
		super(Material.GLASS, name);
		setCreativeTab(ModCreativeTabs.modTab);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		TileEntity tile = worldIn.getTileEntity(pos);

		if (tile instanceof TileEntityPipeBase) {
			((TileEntityPipeBase) tile).onBlockPlaced(state, placer, stack);
		}
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		System.out.println("1:" + pos.getX() + ":" + pos.getY() + ":" + pos.getZ());
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TileEntityPipeBase) {
			((TileEntityPipeBase) tile).updateAttachments();
		}
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		System.out.println("2:" + pos.getX() + ":" + pos.getY() + ":" + pos.getZ());
		super.onNeighborChange(world, pos, neighbor);
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityPipeBase) {
			((TileEntityPipeBase) tile).updateAttachments();
		}
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
		return 50;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(4.9D / 16, 4.9D / 16, 4.9D / 16, 11.2D / 16, 11.2D / 16, 11.2D / 16);
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}
	
}
