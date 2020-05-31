package de.bigmachines.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Interface for Item which provide a info on alt press
 * @author RythenGlyth
 *
 */

public interface IInfoProviderAlt {
	
	void addAltInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> toolTip, ITooltipFlag flags);

}
