package de.bigmachines.items;

import java.util.List;

import net.minecraft.item.ItemStack;

/**
 * Interface for Items which provide a hud
 * @author RythenGlyth
 *
 */

public interface IHUDInfoProvider {

	void addHUDInfo(List<String> info, ItemStack chestplate);
	
	
	
}
