package de.bigmachines.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Interface for Item which provide a info on shift press
 * @author RythenGlyth
 *
 */

public interface IInfoProviderShift {
	
	void addShiftInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> toolTip, ITooltipFlag flags);
	
}