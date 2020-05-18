package de.bigmachines.handler;

import java.util.ArrayList;
import java.util.List;

import de.bigmachines.Reference;
import de.bigmachines.config.Config;
import de.bigmachines.items.IInfoProviderShift;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CooktimeDisplay {
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onTooltip(ItemTooltipEvent e) {
		if(Config.displayCookTime && TileEntityFurnace.isItemFuel(e.getItemStack())) {
			List<String> toolips = new ArrayList<String>();
			ItemInformationHandler.addShiftInfo(new IInfoProviderShift() {
				@Override
				public void addShiftInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> toolTip, ITooltipFlag flags) {
					toolTip.add(I18n.format("info." + Reference.MOD_ID + ".burntime") + ": " + TileEntityFurnace.getItemBurnTime(e.getItemStack()));
				}
			}, e.getItemStack(), e.getEntityPlayer(), toolips, e.getFlags());
			e.getToolTip().addAll(1, toolips);
			//e.getToolTip().add(KeyHelper.isControlKeyDown() ? (I18n.format("info.testmod.burntime") + ": " + TileEntityFurnace.getItemBurnTime(e.getItemStack())) : I18n.format("info.testmod.press_strg"));
		}
	}	
}