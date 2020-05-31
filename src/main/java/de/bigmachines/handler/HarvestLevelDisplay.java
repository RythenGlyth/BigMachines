package de.bigmachines.handler;

import de.bigmachines.config.Config;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class HarvestLevelDisplay {
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onTooltip(ItemTooltipEvent e) {
		if(Config.isDisplayHarvestLevel() && e.getItemStack().getItem() instanceof ItemTool) {
			ItemTool tool = (ItemTool) e.getItemStack().getItem();
			List<String> toolips = new ArrayList<>();
			ItemInformationHandler.addShiftInfo((itemStack, entityPlayer, toolTip, flags) -> {
				toolTip.add("Harvest Level: " + tool.getHarvestLevel(e.getItemStack(), tool.getToolClasses(e.getItemStack()).iterator().next(), null, null));
			}, e.getItemStack(), e.getEntityPlayer(), toolips, e.getFlags());
			e.getToolTip().addAll(1, toolips);
		}
	}
	
}
