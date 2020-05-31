package de.bigmachines.gui.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class SlotValidated extends Slot {
	
	final ISlotValidator validator;
	
	public SlotValidated(IInventory inventoryIn, int index, int xPosition, int yPosition, ISlotValidator validator) {
		super(inventoryIn, index, xPosition, yPosition);
		this.validator = validator;
	}
	
	@Override
	public boolean isItemValid(@Nonnull ItemStack stack) {
		return this.validator.isItemValid(stack);
	}
	
}
