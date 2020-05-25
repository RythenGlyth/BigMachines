package de.bigmachines.items.items.manual;

import de.bigmachines.gui.client.manual.GuiManual;
import de.bigmachines.init.ModCreativeTabs;
import de.bigmachines.items.ItemBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemManual extends ItemBase {
	
	public ItemManual() {
		super("manual");
		setCreativeTab(ModCreativeTabs.modTab);
		setMaxStackSize(1);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiManual(player.getHeldItem(hand)));
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (playerIn.getHeldItem(handIn).getItem() instanceof ItemManual) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiManual(playerIn.getHeldItem(handIn)));
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}