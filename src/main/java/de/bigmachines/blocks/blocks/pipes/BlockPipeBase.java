package de.bigmachines.blocks.blocks.pipes;

import com.google.common.collect.Lists;
import de.bigmachines.BigMachines;
import de.bigmachines.blocks.BlockBase;
import de.bigmachines.init.ModCreativeTabs;
import de.bigmachines.utils.BlockHelper;
import de.bigmachines.utils.classes.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockPipeBase extends BlockBase {
	
	private static final double offsetBox = 0.1;
	
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
		setHardness(0.5F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
	}
	
	@Override
	public void onBlockPlacedBy(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		@Nullable TileEntity tile = worldIn.getTileEntity(pos);

		if (tile instanceof TileEntityPipeBase) {
			((TileEntityPipeBase) tile).onBlockPlaced(state, placer, stack);
		}
	}
	
	@Override
	public boolean canConnectRedstone(@Nullable IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos, @Nullable EnumFacing side) {
		return true;
	}
	
	@Override
	public void addCollisionBoxToList(@Nullable IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox,
									  @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		List<AxisAlignedBB> boxes = getCollisionBoxList(worldIn, pos);
		for (AxisAlignedBB box : boxes) addCollisionBoxToList(pos, entityBox, collidingBoxes, box);
	}
	
	public static List<AxisAlignedBB> getCollisionBoxList(@Nonnull World worldIn, @Nonnull BlockPos pos) {
		List<AxisAlignedBB> collidingBoxes = new ArrayList<>();
		
		collidingBoxes.add(box_base);
		
		TileEntity tile = worldIn.getTileEntity(pos);

		if (tile instanceof TileEntityPipeBase) {
			TileEntityPipeBase tileEntityPipeBase = (TileEntityPipeBase) tile;
			for(EnumFacing side : tileEntityPipeBase.getAttachments().keySet()) {
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
	public RayTraceResult collisionRayTrace(@Nullable IBlockState blockState, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Vec3d start, @Nonnull Vec3d end) {
		List<RayTraceResult> list = Lists.newArrayList();

        for (AxisAlignedBB axisalignedbb : getCollisionBoxList(worldIn, pos)) {
            list.add(rayTrace(pos, start, end, axisalignedbb));
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
	
	public static Pair<EnumFacing, BlockPos> getSelectedRayTrace(EntityPlayer player) {
		RayTraceResult rayTraceResult1 = player.rayTrace(getBlockReachDistance(player), 1F); //Minecraft.getMinecraft().getRenderPartialTicks());
		
		BlockPos pos = rayTraceResult1.getBlockPos();
		
		TileEntity tile = player.world.getTileEntity(pos);
		if(tile instanceof TileEntityPipeBase) {
			TileEntityPipeBase tileEntityPipeBase = (TileEntityPipeBase) tile;
			HashMap<RayTraceResult, EnumFacing> list = new HashMap<>();
			for(EnumFacing side : tileEntityPipeBase.getAttachments().keySet()) {
		        AxisAlignedBB box = getBox(side);
		        RayTraceResult result = BlockHelper.rayTrace(player, getBlockReachDistance(player), rayTraceResult1.getBlockPos(), box);
		        if(result != null) list.put(result, side);
			}
			
			RayTraceResult returnRayTraceResult = null;
	        double lastDistance = Double.MAX_VALUE;
	        
	        for (RayTraceResult raytraceresult : list.keySet()) {
	            if (raytraceresult != null) {
	                double distance = raytraceresult.hitVec.squareDistanceTo(player.getPositionEyes(1F));
	                if (distance < lastDistance) {
	                	returnRayTraceResult = raytraceresult;
	                	lastDistance = distance;
	                }
	            }
	        }
	        return returnRayTraceResult == null ? null : new Pair<>(list.get(returnRayTraceResult), pos);
		} else
			return null;
	}
	
	public static float getBlockReachDistance(EntityPlayer player) {
        float attrib = (float) player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        return player.capabilities.isCreativeMode ? attrib : attrib - 0.5F;
    }

	@SideOnly(Side.CLIENT)
	@Nullable
	public static Pair<EnumFacing, BlockPos> getSelectedRayTrace() {
		return getSelectedRayTrace(Minecraft.getMinecraft().player);
	}
	
	@Override
	public boolean hasTileEntity(@Nullable IBlockState state) {
		return super.hasTileEntity(state);
	}
	
	@Override
	public void neighborChanged(@Nullable IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nullable Block blockIn, @Nullable BlockPos fromPos) {
		if(!worldIn.isRemote) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if(tile instanceof TileEntityPipeBase) {
				((TileEntityPipeBase) tile).updateAttachments();
			}
		}
	}
	
	@Override
	public void onNeighborChange(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull BlockPos neighbor) {
		super.onNeighborChange(world, pos, neighbor);
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityPipeBase) {
			((TileEntityPipeBase) tile).updateAttachments();
		}
	}
	
	@Override
	@Nonnull
	public BlockFaceShape getBlockFaceShape(@Nullable IBlockAccess worldIn, @Nullable IBlockState state, @Nullable BlockPos pos, @Nullable EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public boolean onBlockActivated(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nullable IBlockState state, @Nonnull EntityPlayer playerIn,
									@Nullable EnumHand hand, @Nullable EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TileEntityPipeBase) {
			for(EnumFacing side : ((TileEntityPipeBase) tile).getAttachments().keySet()) {
				if(getBox(side).grow(0.02D).contains(new Vec3d(hitX, hitY, hitZ))) {
					playerIn.openGui(BigMachines.INSTANCE, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
					return true;
				}
			}
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	@Nonnull
	public AxisAlignedBB getBoundingBox(@Nullable IBlockState state, @Nullable IBlockAccess source, @Nullable BlockPos pos) {
		return getBox(null);
		//return new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	}
	
	@Override
	public boolean isNormalCube(@Nullable IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isFullCube(@Nullable IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(@Nullable IBlockState state) {
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(@Nullable IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public boolean hasCustomBreakingProgress(IBlockState state) {
		return true;
	}
	
}
