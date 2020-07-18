package de.bigmachines.blocks.blocks;

import java.util.List;
import java.util.Random;

import de.bigmachines.blocks.BlockBase;
import de.bigmachines.init.ModCreativeTabs;
import de.bigmachines.items.IInfoProviderShift;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFloor extends BlockBase {
	
	public static final PropertyInteger LAYERS = PropertyInteger.create("layers", 1, 8);
    public static final PropertyEnum<EnumBlockHalf> HALF = PropertyEnum.<EnumBlockHalf>create("half", EnumBlockHalf.class);
	
    //protected static final AxisAlignedBB bounding_box = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
	
	public BlockFloor(String name) {
		this(name, Material.ROCK, SoundType.STONE, 1.5F, 10.0F);
	}
	
	public BlockFloor(String name, Material material, SoundType soundType, float hardness, float resistance) {
		super(material, name);
        this.itemBlock = new ItemBlockFloor(this);
        this.setCreativeTab(ModCreativeTabs.materialsTab);
        setSoundType(soundType);
        setHardness(hardness);
        setResistance(resistance);
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return super.getPickBlock(state, target, world, pos, player);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState iblockstate = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(HALF, EnumBlockHalf.BOTTOM);
        
        return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double)hitY <= 0.5D) ? iblockstate : iblockstate.withProperty(HALF, EnumBlockHalf.TOP);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return getBox(state);
	}
	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return ((Integer)worldIn.getBlockState(pos).getValue(LAYERS)).intValue() < 5;
	}
	
	@Override
	public boolean isTopSolid(IBlockState state) {
		return true;
		//return (EnumBlockHalf.TOP.equals(state.getValue(HALF)) || ((Integer)state.getValue(LAYERS)).intValue() == 8);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return getBox(blockState);
	}
	
	public AxisAlignedBB getBox(IBlockState blockState) {
		int i = ((Integer)blockState.getValue(LAYERS)).intValue();
		boolean bottom = EnumBlockHalf.BOTTOM.equals(blockState.getValue(HALF));
        return bottom ? new AxisAlignedBB(0, 0, 0, 1, (double)i * 0.125D, 1) : new AxisAlignedBB(0, 1 - (double)i * 0.125D, 0, 1, 1, 1);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return this.blockMaterial.equals(Material.GLASS) ? BlockRenderLayer.CUTOUT : super.getBlockLayer();
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    	if(this.blockMaterial.equals(Material.GLASS)) {
            IBlockState otherIblockstate = blockAccess.getBlockState(pos.offset(side));
            Block otherBlock = otherIblockstate.getBlock();
            if(otherBlock == Blocks.GLASS) return false;
            //if(true) return otherBlock == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
            if(otherBlock.getMaterial(otherIblockstate).equals(Material.GLASS)) {
                if(otherBlock == this) {
                	int layers = blockState.getValue(LAYERS);
                	EnumBlockHalf half = blockState.getValue(HALF);
                	int otherLayers = otherIblockstate.getValue(LAYERS);
                	EnumBlockHalf otherHalf = otherIblockstate.getValue(HALF);
                	
                	if(side == EnumFacing.DOWN && (half == EnumBlockHalf.BOTTOM || layers == 8) && (otherHalf == EnumBlockHalf.TOP || otherLayers == 8))
                		return false;
                	if(side == EnumFacing.UP && (half == EnumBlockHalf.TOP || layers == 8) && (otherHalf == EnumBlockHalf.BOTTOM || otherLayers == 8))
                		return false;
                	if(half == otherHalf && layers <= otherLayers) return false;
                	if(otherLayers == 8) return false;
                }
            }
    	}
    	return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return (Integer)state.getValue(LAYERS).intValue();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(LAYERS, Integer.valueOf((meta % 8) + 1)).withProperty(HALF, EnumBlockHalf.values()[meta / 8]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
        return ((Integer)state.getValue(LAYERS)).intValue() - 1 + state.getValue(HALF).ordinal() * 0b100;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {LAYERS, HALF});
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return (face == EnumFacing.DOWN && EnumBlockHalf.BOTTOM.equals(state.getValue(HALF))) || (face == EnumFacing.UP && EnumBlockHalf.TOP.equals(state.getValue(HALF))) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
	
	public static enum EnumBlockHalf implements IStringSerializable {
		BOTTOM("bottom"),
		TOP("top");
		
		 private final String name;

        private EnumBlockHalf(String name) {
            this.name = name;
        }
        
		@Override
        public String toString() {
            return this.name;
        }

		@Override
        public String getName() {
            return this.name;
        }
	}
	
}
