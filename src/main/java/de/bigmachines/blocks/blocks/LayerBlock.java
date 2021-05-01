package de.bigmachines.blocks.blocks;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class LayerBlock extends Block {
	
	public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS_1_8;
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	
	protected static final HashMap<Direction, VoxelShape[]> SHAPES = new HashMap<Direction, VoxelShape[]>() {{
		put(Direction.DOWN, new VoxelShape[] {
			VoxelShapes.empty(),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D,  2.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D,  4.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D,  6.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D,  8.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D, 10.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D, 12.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D, 14.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D, 16.0D, 16.0D)
		});
		put(Direction.UP, new VoxelShape[] {
			VoxelShapes.empty(),
			Block.box( 0.0D, 14.0D,  0.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 0.0D, 12.0D,  0.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 0.0D, 10.0D,  0.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  8.0D,  0.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  6.0D,  0.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  4.0D,  0.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  2.0D,  0.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D, 16.0D, 16.0D)
		});
		put(Direction.NORTH, new VoxelShape[] {
			VoxelShapes.empty(),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D, 16.0D,  2.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D, 16.0D,  4.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D, 16.0D,  6.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D, 16.0D,  8.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D, 16.0D, 10.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D, 16.0D, 12.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D, 16.0D, 14.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D, 16.0D, 16.0D)
		});
		put(Direction.SOUTH, new VoxelShape[] {
			VoxelShapes.empty(),
			Block.box( 0.0D,  0.0D, 14.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  0.0D, 12.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  0.0D, 10.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  8.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  6.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  4.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  2.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D, 16.0D, 16.0D)
		});
		put(Direction.WEST, new VoxelShape[] {
			VoxelShapes.empty(),
			Block.box( 0.0D,  0.0D,  0.0D,  2.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  0.0D,  4.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  0.0D,  6.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  0.0D,  8.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 10.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 12.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 14.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D, 16.0D, 16.0D)
		});
		put(Direction.EAST, new VoxelShape[] {
			VoxelShapes.empty(),
			Block.box(14.0D,  0.0D,  0.0D, 16.0D, 16.0D, 16.0D),
			Block.box(12.0D,  0.0D,  0.0D, 16.0D, 16.0D, 16.0D),
			Block.box(10.0D,  0.0D,  0.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 8.0D,  0.0D,  0.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 6.0D,  0.0D,  0.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 4.0D,  0.0D,  0.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 2.0D,  0.0D,  0.0D, 16.0D, 16.0D, 16.0D),
			Block.box( 0.0D,  0.0D,  0.0D, 16.0D, 16.0D, 16.0D)
		});
	}};
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES.get(state.getProperty(FACING))[state.getProperty(LAYERS)];
	}
	
	@Override
	public boolean canBeReplaced(BlockState state, BlockItemUseContext useContext) {
		int i = state.getValue(LAYERS);
		if (useContext.getItemInHand().getItem() == this.asItem() && i < 8) {
			if (useContext.replacingClickedOnBlock()) {
				return useContext.getNearestLookingDirection() == state.getValue(FACING).getOpposite();
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());
		if (blockstate.is(this)) {
			int i = blockstate.getValue(LAYERS);
			return blockstate.setValue(LAYERS, Integer.valueOf(Math.min(8, i + 1)));
		} else {
			return getDefaultState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(LAYERS, 1);
		}
	}
	
	public LayerBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateDefinition.getBaseState().with(LAYERS, Integer.valueOf(1)).with(FACING, Direction.DOWN));
	}
	
	@Override
	public boolean isAir(BlockState state) {
		return true;
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(LAYERS).add(FACING);
	}
	
}
