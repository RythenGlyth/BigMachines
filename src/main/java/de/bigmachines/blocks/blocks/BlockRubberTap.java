package de.bigmachines.blocks.blocks;

import de.bigmachines.blocks.BlockBase;
import de.bigmachines.init.ModBlocks;
import de.bigmachines.init.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRubberTap extends BlockBase {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final AxisAlignedBB BOUNDING_BOX_NORTH = new AxisAlignedBB(0.3125D, 0.3125D, 0D, 0.6875D, 0.625D, 0.375D);
    public static final AxisAlignedBB BOUNDING_BOX_SOUTH = new AxisAlignedBB(0.3125D, 0.3125D, 0.625D, 0.6875D, 0.625D, 1D);
    public static final AxisAlignedBB BOUNDING_BOX_WEST = new AxisAlignedBB(0D, 0.3125D, 0.3125D, 0.375D, 0.625D, 0.6875D);
    public static final AxisAlignedBB BOUNDING_BOX_EAST = new AxisAlignedBB(0.625D, 0.3125D, 0.3125D, 1D, 0.625D, 0.6875D);
	
	public BlockRubberTap() {
		super(Material.ROCK, "rubber_tap");
		setCreativeTab(ModCreativeTabs.modTab);
	}

    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
    	return false;
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
    	return getDefaultState().withProperty(FACING, facing.getOpposite());
    }
    
    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
    	if(side == EnumFacing.DOWN || side == EnumFacing.UP) return false;
    	Block block = worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock();
    	if(block != ModBlocks.blockRubberLog) return false;
    	return super.canPlaceBlockOnSide(worldIn, pos, side);
    }
    
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return getBox(state);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return getBox(blockState);
	}
	
	public AxisAlignedBB getBox(IBlockState state) {
		switch ((EnumFacing)state.getValue(FACING)) {
			case NORTH:
	        default:
	            return BOUNDING_BOX_NORTH;
	        case SOUTH:
	            return BOUNDING_BOX_SOUTH;
	        case WEST:
	            return BOUNDING_BOX_WEST;
	        case EAST:
	            return BOUNDING_BOX_EAST;
		}
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.Plane.HORIZONTAL.facings()[meta % EnumFacing.Plane.HORIZONTAL.facings().length]);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}
	
}
