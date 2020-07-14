package de.bigmachines.items;

import net.minecraft.item.ItemStack;

import java.util.List;

import de.bigmachines.handler.hud.elements.HUDElement;

/**
 * Interface for Items which provide a hud
 * @author RythenGlyth
 *
 */

public interface IHUDInfoProvider {

	void addHUDInfo(List<HUDElement> info, ItemStack item);
	
}
