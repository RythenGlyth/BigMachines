package de.bigmachines.utils.classes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;

public class Inventory implements IInventory {
	
	public NonNullList<ItemStack> inventory;
	String name;
	
	public Inventory(String name, int inventorySize) {
		this.name = name;
		setInventory(inventorySize);
	}
	
	public void setInventory(int inventorySize) {
		this.inventory = NonNullList.<ItemStack>withSize(inventorySize, ItemStack.EMPTY);;
	}
	
	public void readFromNBT(NBTTagCompound tag) {
		setInventory(inventory.size());
		
		ItemStackHelper.loadAllItems(tag, inventory);
		
		/*NBTTagList list = tag.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound itemTag = list.getCompoundTagAt(i);
			int slot = itemTag.getInteger("Slot");
			if (slot >= 0 && slot < inventory.size()) {
				inventory.set(slot, new ItemStack(itemTag));
			}
		}*/
	}
	
	public void writeToNBT(NBTTagCompound tag) {
		ItemStackHelper.saveAllItems(tag, inventory, false);
		/*NBTTagList list = new NBTTagList();
		for(int i = 0; i < inventory.size(); i++) {
			if (!inventory.get(i).isEmpty()) {
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setInteger("Slot", i);
				inventory.get(i).writeToNBT(itemTag);
				list.appendTag(itemTag);
			}
		}
		if (list.tagCount() > 0) {
			tag.setTag("Inventory", list);
		}*/
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean hasCustomName() {
		return name != null && name != "";
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(getName());
	}

	@Override
	public int getSizeInventory() {
		return inventory.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : inventory) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inventory.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return inventory != null && !inventory.get(index).isEmpty() ? ItemStackHelper.getAndSplit(inventory, index, count) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		if (inventory != null && !((ItemStack)inventory.get(index)).isEmpty()) {
            ItemStack itemstack = inventory.get(index);
            inventory.set(index, ItemStack.EMPTY);
            return itemstack;
        } else {
            return ItemStack.EMPTY;
        }
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (inventory != null) {
			inventory.set(index, stack);
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		inventory.clear();
	}
	
}
