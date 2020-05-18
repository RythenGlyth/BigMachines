package de.bigmachines.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Interface for Item which provide a info on ctrl press
 * @author RythenGlyth
 *
 */

public interface IInfoProviderCtrl {
	
	void addStrgInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> toolTip, ITooltipFlag flags);
	
}