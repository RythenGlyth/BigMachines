package de.bigmachines.items.items;

import de.bigmachines.init.ModCreativeTabs;
import de.bigmachines.items.ItemBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemManual extends ItemBase {

	public ItemManual() {
		super("manual");
		setCreativeTab(ModCreativeTabs.modTab);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return EnumActionResult.PASS;
	}
	
}