package de.bigmachines.gui.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SlotGhost extends Slot {

	public SlotGhost(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean canTakeStack(@Nullable EntityPlayer playerIn) {
		return false;
	}
	
	@Override
	public boolean isItemValid(@Nullable ItemStack stack) {
		return true;
	}
	
	@Override
	public void putStack(@Nonnull ItemStack stack) {
		if (!isItemValid(stack)) return;
		
		if (!stack.isEmpty()) stack.setCount(1);
		
		inventory.setInventorySlotContents(this.getSlotIndex(), stack);
		onSlotChanged(); 
	}
	
	public static class Validated extends SlotGhost {
		
		final ISlotValidator validator;

		public Validated(IInventory inventoryIn, int index, int xPosition, int yPosition, ISlotValidator validator) {
			super(inventoryIn, index, xPosition, yPosition);
			this.validator = validator;
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return validator.isItemValid(stack);
		}
		
	}
	
}
