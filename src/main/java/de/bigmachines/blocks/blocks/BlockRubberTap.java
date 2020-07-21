package de.bigmachines.blocks.blocks;

import de.bigmachines.blocks.BlockBase;
import de.bigmachines.init.ModBlocks;
import de.bigmachines.init.ModCreativeTabs;
import de.bigmachines.init.ModFluids;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

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
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		BlockPos rubberPos = pos.offset(state.getValue(FACING));
		IBlockState rubberIBlockState = worldIn.getBlockState(rubberPos);
		if (rubberIBlockState.getBlock() != ModBlocks.blockRubberLog) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);
		if (hand != null) {
			if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
				IFluidHandlerItem handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
				if (handler != null) {
					IBlockState iBlockState = worldIn.getBlockState(pos);
					if (iBlockState.getBlock() == this) {
						BlockPos rubberPos = pos.offset(iBlockState.getValue(FACING));
						IBlockState rubberIBlockState = worldIn.getBlockState(rubberPos);
						if (rubberIBlockState.getBlock() == ModBlocks.blockRubberLog) {
            				
							/*System.out.println(maxDrain);
            				
            				int maxAccept = handler.fill(fluidStack, false);
            				
            				int amount = (int)(Math.floor(((double)maxAccept) / 500));
            				
            				System.out.println(amount);
            				System.out.println(amount * 500);
            				
            				fluidStack.amount = amount * 500;
            				int acctuallyDrained = handler.fill(fluidStack, true);

							System.out.println(acctuallyDrained);*/
							
							int thisMaxDrain = rubberIBlockState.getValue(BlockRubberLog.RUBBER_LEVEL).intValue() * 500;
							FluidStack fluidStack = new FluidStack(ModFluids.rubber_latex, thisMaxDrain);
							
							FluidActionResult result = FluidUtil.tryFillContainerAndStow(stack, new IFluidHandler() {
								
								@Override
								public IFluidTankProperties[] getTankProperties() {
									return null;
								}
								
								@Override
								public int fill(FluidStack resource, boolean doFill) {
									return 0;
								}
								
								@Override
								public FluidStack drain(int maxDrain, boolean doDrain) {
									int thisMaxDrain = rubberIBlockState.getValue(BlockRubberLog.RUBBER_LEVEL).intValue() * 500;
									FluidStack fluidStack = new FluidStack(ModFluids.rubber_latex, thisMaxDrain);
									
									fluidStack.amount = Math.min(fluidStack.amount, maxDrain);
									
									if (doDrain)
										worldIn.setBlockState(rubberPos, rubberIBlockState.withProperty(BlockRubberLog.RUBBER_LEVEL, Math.max(0, Math.min(3, rubberIBlockState.getValue(BlockRubberLog.RUBBER_LEVEL).intValue() - ((int) Math.ceil((double) fluidStack.amount / 500))))));
									
									return fluidStack;
								}
								
								@Override
								public FluidStack drain(FluidStack resource, boolean doDrain) {
									int thisMaxDrain = rubberIBlockState.getValue(BlockRubberLog.RUBBER_LEVEL).intValue() * 500;
									FluidStack fluidStack = new FluidStack(ModFluids.rubber_latex, thisMaxDrain);
									
									fluidStack.amount = Math.min(fluidStack.amount, resource.amount);
									
									if (doDrain)
										worldIn.setBlockState(rubberPos, rubberIBlockState.withProperty(BlockRubberLog.RUBBER_LEVEL, Math.max(0, Math.min(3, rubberIBlockState.getValue(BlockRubberLog.RUBBER_LEVEL).intValue() - ((int) Math.ceil((double) fluidStack.amount / 500))))));
									
									return fluidStack;
								}
							}, new PlayerInvWrapper(playerIn.inventory), thisMaxDrain, playerIn, true);
							
							if (result.success) {
								playerIn.setHeldItem(hand, result.getResult());
							}
							
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		if (side == EnumFacing.DOWN || side == EnumFacing.UP) return false;
		Block block = worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock();
		if (block != ModBlocks.blockRubberLog) return false;
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
		switch ((EnumFacing) state.getValue(FACING)) {
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
