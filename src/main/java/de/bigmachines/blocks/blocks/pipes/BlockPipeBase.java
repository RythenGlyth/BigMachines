package de.bigmachines.blocks.blocks.pipes;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import de.bigmachines.blocks.BlockBase;
import de.bigmachines.init.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPipeBase extends BlockBase {
	
	private static final double offsetBox = 0.3;
	
	private static final AxisAlignedBB box_base
	= new AxisAlignedBB((5D - offsetBox) / 16, (5D - offsetBox) / 16, (5D - offsetBox) / 16, (11D + offsetBox) / 16, (11D + offsetBox) / 16, (11D + offsetBox) / 16);
	private static final AxisAlignedBB box_down
	= new AxisAlignedBB((5D - offsetBox) / 16, 0D, (5D - offsetBox) / 16, (11D + offsetBox) / 16, (5D - offsetBox) / 16, (11D + offsetBox) / 16);
	private static final AxisAlignedBB box_east
	= new AxisAlignedBB((11D + offsetBox) / 16, (5D - offsetBox) / 16, (5D - offsetBox) / 16, 1D, (11D + offsetBox) / 16, (11D + offsetBox) / 16);
	private static final AxisAlignedBB box_north
	= new AxisAlignedBB((5D - offsetBox) / 16, (5D - offsetBox) / 16, 0D, (11D + offsetBox) / 16, (11D + offsetBox) / 16, (5D - offsetBox) / 16);
	private static final AxisAlignedBB box_south
	= new AxisAlignedBB((5D - offsetBox) / 16, (5D - offsetBox) / 16, (11D + offsetBox) / 16, (11D + offsetBox) / 16, (11D + offsetBox) / 16, 1D);
	private static final AxisAlignedBB box_up
	= new AxisAlignedBB((5D - offsetBox) / 16, (11D + offsetBox) / 16, (5D - offsetBox) / 16, (11D + offsetBox) / 16, 1D, (11D + offsetBox) / 16);
	private static final AxisAlignedBB box_west
	= new AxisAlignedBB(0D, (5D - offsetBox) / 16, (5D - offsetBox) / 16, (5D - offsetBox) / 16, (11D + offsetBox) / 16, (11D + offsetBox) / 16);
	
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
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) {
		/*addCollisionBoxToList(pos, entityBox, collidingBoxes, getBoundingBox(state, worldIn, pos));
		
		TileEntity tile = worldIn.getTileEntity(pos);

		if (tile instanceof TileEntityPipeBase) {
			TileEntityPipeBase tileEntityPipeBase = (TileEntityPipeBase) tile;
			for(EnumFacing side : EnumFacing.VALUES) {
				if(tileEntityPipeBase.hasAttachment(side)) addCollisionBoxToList(pos, entityBox, collidingBoxes, getBox(side));
			}
		}*/
		List<AxisAlignedBB> boxes = getCollisionBoxList(worldIn, pos);
		for(int i = 0; i < boxes.size(); i++) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, boxes.get(i));
		}
		//collidingBoxes.addAll(getCollisionBoxList(worldIn, pos));
	}
	
	public List<AxisAlignedBB> getCollisionBoxList(World worldIn, BlockPos pos) {
		List<AxisAlignedBB> collidingBoxes = new ArrayList<AxisAlignedBB>();
		
		collidingBoxes.add(box_base);
		
		TileEntity tile = worldIn.getTileEntity(pos);

		if (tile instanceof TileEntityPipeBase) {
			TileEntityPipeBase tileEntityPipeBase = (TileEntityPipeBase) tile;
			for(EnumFacing side : tileEntityPipeBase.attachments) {
				collidingBoxes.add(getBox(side));
			}
		}
		
		return collidingBoxes;
	}
	
	@Nonnull
	public static AxisAlignedBB getBox(@Nullable EnumFacing side) {
		if(side == null) 
			return box_base;
		switch(side) {
			case DOWN:
				return box_down;
			case EAST:
				return box_east;
			case NORTH:
				return box_north;
			case SOUTH:
				return box_south;
			case UP:
				return box_up;
			case WEST:
				return box_west;
			default:
				return box_base;
		}
	}
	
	@Override
    @Nullable
	public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
		List<RayTraceResult> list = Lists.<RayTraceResult>newArrayList();

        for (AxisAlignedBB axisalignedbb : getCollisionBoxList(worldIn, pos)) {
            list.add(this.rayTrace(pos, start, end, axisalignedbb));
        }
        
        RayTraceResult returnRayTraceResult = null;
        double lastDistance = 0.0D;
        
        for (RayTraceResult raytraceresult : list) {
            if (raytraceresult != null) {
                double distance = raytraceresult.hitVec.squareDistanceTo(end);
                if (distance > lastDistance) {
                	returnRayTraceResult = raytraceresult;
                	lastDistance = distance;
                }
            }
        }
        
		return returnRayTraceResult;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return super.hasTileEntity(state);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		//System.out.println("1:" + pos.getX() + ":" + pos.getY() + ":" + pos.getZ());
		if(!worldIn.isRemote) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if(tile instanceof TileEntityPipeBase) {
				((TileEntityPipeBase) tile).updateAttachments();
			}
		}
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		//System.out.println("2:" + pos.getX() + ":" + pos.getY() + ":" + pos.getZ());
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return getBox(null);
		//return new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
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
