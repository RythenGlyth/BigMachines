package de.bigmachines.gui.container;

import javax.annotation.Nullable;

import de.bigmachines.gui.slots.SlotGhost;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBase extends Container {

	
	protected void addPlayerInventorySlots(InventoryPlayer inventoryPlayer) {
		int xOffset = getPlayerInventoryHorizontalOffset();
		int yOffset = getPlayerInventoryVerticalOffset();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
		}
	}
	
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		Slot slot = slotId < 0 ? null : this.inventorySlots.get(slotId);
		if (slot instanceof SlotGhost) {
			if (dragType == 2) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.putStack(player.inventory.getItemStack().isEmpty() ? ItemStack.EMPTY : player.inventory.getItemStack().copy());
			}
			return player.inventory.getItemStack();
		}
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}
	
	@Override
	public boolean canInteractWith(@Nullable EntityPlayer playerIn) {
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack stackInSlot = slot.getStack();
			stack = stackInSlot.copy();
			
			if(!merge(index, stackInSlot)) {
				return ItemStack.EMPTY;
			}
			
			slot.onSlotChange(stackInSlot, stack);
			
			if (stackInSlot.getCount() <= 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.putStack(stackInSlot);
			}
			if (stackInSlot.getCount() == stack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(playerIn, stackInSlot);
		}
		return stack; 
	}
	
	protected boolean merge(int slotIndex, ItemStack stack) {

		int invBase = getInventorySize();
		int invFull = inventorySlots.size();

		if (slotIndex < invBase) {
			return mergeItemStack(stack, invBase, invFull, true);
		}
		return mergeItemStack(stack, 0, invBase, false);
	}

	
	protected int getInventorySize() {
		return 0;
	}

	protected static int getPlayerInventoryVerticalOffset() {
		return 75;
	}
	
	protected static int getPlayerInventoryHorizontalOffset() {
		return 8;
	}
	
	
	
}
