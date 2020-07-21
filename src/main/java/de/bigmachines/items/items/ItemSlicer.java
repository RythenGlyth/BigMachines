package de.bigmachines.items.items;

import de.bigmachines.blocks.blocks.BlockFloor;
import de.bigmachines.blocks.blocks.BlockFloor.EnumBlockHalf;
import de.bigmachines.init.ModCreativeTabs;
import de.bigmachines.init.ModMaterials;
import de.bigmachines.items.IInfoProviderShift;
import de.bigmachines.items.ItemBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemSlicer extends ItemBase implements IInfoProviderShift {
	
	public ItemSlicer() {
		super("slicer");
		setCreativeTab(ModCreativeTabs.modTab);
		setMaxStackSize(1);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();
		
		if (block instanceof BlockFloor) {
			int layers = iblockstate.getValue(BlockFloor.LAYERS).intValue();
			
			worldIn.setBlockState(pos, layers > 1 ? iblockstate.withProperty(BlockFloor.LAYERS, layers - 1) : Blocks.AIR.getDefaultState(), 2);
			
			block.spawnAsEntity(worldIn, pos, new ItemStack(((BlockFloor) block).getItemBlock(), 1));
			
			return EnumActionResult.SUCCESS;
		} else if (ModMaterials.floorBlocks.containsKey(block)) {
			
			EnumBlockHalf half = EnumBlockHalf.BOTTOM;
			
			if (facing == EnumFacing.DOWN || (facing != EnumFacing.UP && hitY < 0.5D)) {
				half = EnumBlockHalf.TOP;
			}
			
			worldIn.setBlockState(pos, ModMaterials.floorBlocks.get(block).getDefaultState().withProperty(BlockFloor.LAYERS, 7).withProperty(BlockFloor.HALF, half), 2);
			
			block.spawnAsEntity(worldIn, pos, new ItemStack(ModMaterials.floorBlocks.get(block).getItemBlock(), 1));
			
			return EnumActionResult.SUCCESS;
		}
		
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public void addShiftInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> toolTip, ITooltipFlag flags) {
		toolTip.add(I18n.format("info.bigmachines.slicer.shift"));
		
	}
	
}
