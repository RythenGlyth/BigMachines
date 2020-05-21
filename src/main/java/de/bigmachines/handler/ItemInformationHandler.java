package de.bigmachines.handler;

import java.util.ArrayList;
import java.util.List;

import de.bigmachines.Reference;
import de.bigmachines.config.WorldGenerationConfig;
import de.bigmachines.items.IInfoProviderAlt;
import de.bigmachines.items.IInfoProviderCtrl;
import de.bigmachines.items.IInfoProviderShift;
import de.bigmachines.utils.KeyHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;

public class ItemInformationHandler {
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onTooltip(ItemTooltipEvent e) {
		List<String> toolips = new ArrayList<String>();
		
		if(e.getItemStack().getItem() instanceof IInfoProviderShift) {
			addShiftInfo((IInfoProviderShift)e.getItemStack().getItem(), e.getItemStack(), e.getEntityPlayer(), toolips, e.getFlags());
		}
		
		if(e.getItemStack().getItem() instanceof IInfoProviderAlt) {
			addAltInfo((IInfoProviderAlt)e.getItemStack().getItem(), e.getItemStack(), e.getEntityPlayer(), toolips, e.getFlags());
		}
		
		if(e.getItemStack().getItem() instanceof IInfoProviderCtrl) {
			addStrgInfo((IInfoProviderCtrl)e.getItemStack().getItem(), e.getItemStack(), e.getEntityPlayer(), toolips, e.getFlags());
		}
		
		e.getToolTip().addAll(1, toolips);
	}
	
	public static void addShiftInfo(IInfoProviderShift item, ItemStack stack, EntityPlayer player, List<String> tooltip, ITooltipFlag flag) {
		if(KeyHelper.isShiftKeyDown()) {
			item.addShiftInformation(stack, player, tooltip, flag);
		} else {
			tooltip.add(I18n.format("info."+ Reference.MOD_ID + ".press_shift"));
		}
	}
	
	public static void addAltInfo(IInfoProviderAlt item, ItemStack stack, EntityPlayer player, List<String> tooltip, ITooltipFlag flag) {
		if(KeyHelper.isAltKeyDown()) {
			item.addAltInformation(stack, player, tooltip, flag);
		} else {
			tooltip.add(I18n.format("info."+ Reference.MOD_ID + ".press_alt"));
		}
	}
	
	public static void addStrgInfo(IInfoProviderCtrl item, ItemStack stack, EntityPlayer player, List<String> tooltip, ITooltipFlag flag) {
		if(KeyHelper.isControlKeyDown()) {
			item.addStrgInformation(stack, player, tooltip, flag);
		} else {
			tooltip.add(I18n.format("info."+ Reference.MOD_ID + ".press_strg"));
		}
	}

	
}
