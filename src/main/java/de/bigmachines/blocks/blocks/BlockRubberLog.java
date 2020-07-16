package de.bigmachines.blocks.blocks;

import de.bigmachines.Reference;
import de.bigmachines.blocks.IBlockBase;
import de.bigmachines.blocks.ItemBlockBase;
import de.bigmachines.init.ModCreativeTabs;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

public class BlockRubberLog extends BlockLog implements IBlockBase {

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
	
	
	
}
