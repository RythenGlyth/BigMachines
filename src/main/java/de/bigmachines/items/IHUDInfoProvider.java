package de.bigmachines.items;

import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Interface for Items which provide a hud
 * @author RythenGlyth
 *
 */

public interface IHUDInfoProvider {

	void addHUDInfo(List<String> info, ItemStack chestplate);
	
}
