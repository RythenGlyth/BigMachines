package de.bigmachines.blocks.blocks;

import de.bigmachines.Reference;
import de.bigmachines.blocks.IBlockBase;
import de.bigmachines.blocks.ItemBlockBase;
import de.bigmachines.init.ModBlocks;
import de.bigmachines.init.ModCreativeTabs;
import de.bigmachines.interfaces.IInitializer;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Random;

public class BlockRubberLog extends BlockLog implements IBlockBase, IInitializer {
	
	public static final PropertyInteger RUBBER_LEVEL = PropertyInteger.create("rubber_level", 0, 3);
	
	protected ItemBlock itemBlock;
	
	protected String name;
	
	public BlockRubberLog() {
		this.name = "rubber_log";
		setRegistryName(new ResourceLocation(Reference.MOD_ID, this.name));
		setUnlocalizedName(Reference.MOD_ID + "." + this.name);
		this.itemBlock = new ItemBlockBase(this);
		this.setCreativeTab(ModCreativeTabs.materialsTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(RUBBER_LEVEL, 0).withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
		this.setTickRandomly(true);
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (rand.nextInt(8) == 0) {
			if (hasLeaves(worldIn, pos)) {
				int rubber_level = state.getValue(RUBBER_LEVEL).intValue();
				if (rubber_level < 3) {
					worldIn.setBlockState(pos, state.withProperty(RUBBER_LEVEL, rubber_level + 1), 2);
				}
			}
		}
		super.updateTick(worldIn, pos, state, rand);
	}
	
	private boolean hasLeaves(World worldIn, BlockPos pos) {
		for (BlockPos.MutableBlockPos mutableBlockPos : BlockPos.getAllInBoxMutable(pos.add(-4, -4, -4), pos.add(4, 4, 4))) {
			if (worldIn.getBlockState(mutableBlockPos).getBlock() == ModBlocks.blockRubberLeaves) {
				return true;
			}
		}
		
		return net.minecraftforge.common.FarmlandWaterManager.hasBlockWaterTicket(worldIn, pos);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(RUBBER_LEVEL, Integer.valueOf(meta % 4)).withProperty(LOG_AXIS, BlockLog.EnumAxis.values()[meta >> 2]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		meta |= state.getValue(RUBBER_LEVEL);
		meta |= state.getValue(LOG_AXIS).ordinal() << 2;
		return meta;
	}
	
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {RUBBER_LEVEL, LOG_AXIS});
	}
	
	@Override
	public ItemBlock getItemBlock() {
		return itemBlock;
	}
	
	@Override
	public void postRegister() {
		OreDictionary.registerOre("logWood", this.itemBlock);
	}
	
}
