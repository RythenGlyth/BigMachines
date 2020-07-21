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

	public BlockPipeBase(final String name) {
		super(Material.GLASS, name);
		setCreativeTab(ModCreativeTabs.modTab);
		setHardness(0.5F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
	}
	
	@Override
	public void onBlockPlacedBy(@Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nonnull final IBlockState state, @Nonnull final EntityLivingBase placer, @Nonnull final ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		@Nullable final TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileEntityPipeBase)
			((TileEntityPipeBase) tile).onBlockPlaced(state, placer, stack);
	}

	@Override
	public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
		@Nullable final TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileEntityPipeBase)
			((TileEntityPipeBase) tile).onBlockBroken(state);

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public void onBlockClicked(final World worldIn, final BlockPos pos, final EntityPlayer playerIn) {
		super.onBlockClicked(worldIn, pos, playerIn);

		@Nullable final TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileEntityPipeBase)
			((TileEntityPipeBase) tile).onBlockClicked(playerIn);
	}

	@Override
	public boolean canConnectRedstone(@Nullable final IBlockState state, @Nullable final IBlockAccess world, @Nullable final BlockPos pos, @Nullable final EnumFacing side) {
		return true;
	}
	
	@Override
	public void addCollisionBoxToList(@Nullable final IBlockState state, @Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nonnull final AxisAlignedBB entityBox,
									  @Nonnull final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
		final List<AxisAlignedBB> boxes = getCollisionBoxList(worldIn, pos);
		for (final AxisAlignedBB box : boxes) addCollisionBoxToList(pos, entityBox, collidingBoxes, box);
	}
	
	public static List<AxisAlignedBB> getCollisionBoxList(@Nonnull final World worldIn, @Nonnull final BlockPos pos) {
		final List<AxisAlignedBB> collidingBoxes = new ArrayList<>();
		
		collidingBoxes.add(box_base);
		
		final TileEntity tile = worldIn.getTileEntity(pos);

		if (tile instanceof TileEntityPipeBase) {
			final TileEntityPipeBase tileEntityPipeBase = (TileEntityPipeBase) tile;
			for(final EnumFacing side : tileEntityPipeBase.getAttachments().keySet()) {
				collidingBoxes.add(getBox(side));
			}
		}
		
		return collidingBoxes;
	}
	
	@Nonnull
	public static AxisAlignedBB getBox(@Nullable final EnumFacing side) {
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
	public RayTraceResult collisionRayTrace(@Nullable final IBlockState blockState, @Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nonnull final Vec3d start, @Nonnull final Vec3d end) {
		final List<RayTraceResult> list = Lists.newArrayList();

        for (final AxisAlignedBB axisalignedbb : getCollisionBoxList(worldIn, pos)) {
            list.add(rayTrace(pos, start, end, axisalignedbb));
        }
        
        RayTraceResult returnRayTraceResult = null;
        double lastDistance = 0.0D;
        
        for (final RayTraceResult raytraceresult : list) {
            if (raytraceresult != null) {
                final double distance = raytraceresult.hitVec.squareDistanceTo(end);
                if (distance > lastDistance) {
                	returnRayTraceResult = raytraceresult;
                	lastDistance = distance;
                }
            }
        }
        
		return returnRayTraceResult;
	}
	
	public static Pair<EnumFacing, BlockPos> getSelectedRayTrace(final EntityPlayer player) {
		final RayTraceResult rayTraceResult1 = player.rayTrace(BlockHelper.getBlockReachDistance(player), 1F); //Minecraft.getMinecraft().getRenderPartialTicks());
		
		final BlockPos pos = rayTraceResult1.getBlockPos();
		
		final TileEntity tile = player.world.getTileEntity(pos);
		if (tile instanceof TileEntityPipeBase) {
			final TileEntityPipeBase tileEntityPipeBase = (TileEntityPipeBase) tile;
			final HashMap<RayTraceResult, EnumFacing> list = new HashMap<>();
			for (final EnumFacing side : tileEntityPipeBase.getAttachments().keySet()) {
				final AxisAlignedBB box = getBox(side);
				final RayTraceResult result = BlockHelper.rayTrace(player, BlockHelper.getBlockReachDistance(player), rayTraceResult1.getBlockPos(), box);
				if (result != null) list.put(result, side);
			}
			
			RayTraceResult returnRayTraceResult = null;
	        double lastDistance = Double.MAX_VALUE;
	        
	        for (final RayTraceResult raytraceresult : list.keySet()) {
	            if (raytraceresult != null) {
	                final double distance = raytraceresult.hitVec.squareDistanceTo(player.getPositionEyes(1F));
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
	
	@SideOnly(Side.CLIENT)
	@Nullable
	public static Pair<EnumFacing, BlockPos> getSelectedRayTrace() {
		return getSelectedRayTrace(Minecraft.getMinecraft().player);
	}
	
	@Override
	public boolean hasTileEntity(@Nullable final IBlockState state) {
		return super.hasTileEntity(state);
	}
	
	@Override
	public void neighborChanged(@Nullable final IBlockState state, @Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nullable final Block blockIn, @Nullable final BlockPos fromPos) {
		if(!worldIn.isRemote) {
			final TileEntity tile = worldIn.getTileEntity(pos);
			if(tile instanceof TileEntityPipeBase) {
				((TileEntityPipeBase) tile).updateAttachments();
			}
		}
	}
	
	@Override
	public void onNeighborChange(@Nonnull final IBlockAccess world, @Nonnull final BlockPos pos, @Nonnull final BlockPos neighbor) {
		super.onNeighborChange(world, pos, neighbor);
		final TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityPipeBase) {
			((TileEntityPipeBase) tile).updateAttachments();
		}
	}
	
	@Override
	@Nonnull
	public BlockFaceShape getBlockFaceShape(@Nullable final IBlockAccess worldIn, @Nullable final IBlockState state, @Nullable final BlockPos pos, @Nullable final EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public boolean onBlockActivated(@Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nullable final IBlockState state, @Nonnull final EntityPlayer playerIn,
									@Nullable final EnumHand hand, @Nullable final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		final TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TileEntityPipeBase) {
			((TileEntityPipeBase) tile).onBlockClicked(playerIn);
			if (!playerIn.isSneaking())
				for(final EnumFacing side : ((TileEntityPipeBase) tile).getAttachments().keySet()) {
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
	public AxisAlignedBB getBoundingBox(@Nullable final IBlockState state, @Nullable final IBlockAccess source, @Nullable final BlockPos pos) {
		return getBox(null);
		//return new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	}
	
	@Override
	public boolean isNormalCube(@Nullable final IBlockState state, @Nullable final IBlockAccess world, @Nullable final BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isFullCube(@Nullable final IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(@Nullable final IBlockState state) {
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(@Nullable final IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public boolean hasCustomBreakingProgress(final IBlockState state) {
		return true;
	}
	
}
