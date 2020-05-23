package de.bigmachines.gui.slots;

import net.minecraft.item.ItemStack;

public interface ISlotValidator {
	
	boolean isItemValid(ItemStack stack);
	
}