package de.bigmachines.blocks.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import de.bigmachines.Reference;
import de.bigmachines.blocks.IBlockBase;
import de.bigmachines.blocks.ItemBlockBase;
import de.bigmachines.init.ModCreativeTabs;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRubberLeaves extends BlockLeaves implements IBlockBase {
    
	protected ItemBlock itemBlock;
	
    protected String name;
	
	public BlockRubberLeaves() {
		this.name = "rubber_leaves";
		setRegistryName(new ResourceLocation(Reference.MOD_ID, this.name));
		setUnlocalizedName(Reference.MOD_ID + "." + this.name);
		this.itemBlock = new ItemBlockBase(this);
        this.setCreativeTab(ModCreativeTabs.materialsTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(DECAYABLE, Boolean.valueOf((meta >> 0 & 1) == 1)).withProperty(CHECK_DECAY, (meta >> 1 & 1) == 1);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		if(state.getValue(DECAYABLE)) meta |= 0b0001;
		if(state.getValue(CHECK_DECAY)) meta |= 0b0010;
		return meta;
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return super.getItemDropped(state, rand, fortune); //TODO ITEMSAPPLING
	}

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {CHECK_DECAY, DECAYABLE});
    }
    
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS)
        {
            player.addStat(StatList.getBlockStats(this));
            spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this), 1, 0));
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return NonNullList.withSize(1, new ItemStack(this, 1, 0));
	}

	@Override
	public EnumType getWoodType(int meta) {
		return null;
	}

	@Override
	public ItemBlock getItemBlock() {
		return itemBlock;
	}
	
	
	
}
