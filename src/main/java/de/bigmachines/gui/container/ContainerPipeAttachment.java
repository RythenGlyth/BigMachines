package de.bigmachines.gui.container;

import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class ContainerPipeAttachment extends Container {
	
	private final TileEntityPipeBase tileEntityPipeBase;
	private final EnumFacing side;
	
	public ContainerPipeAttachment(InventoryPlayer inventory, TileEntityPipeBase tileEntityPipeBase, EnumFacing side) {
		super();
		this.tileEntityPipeBase = tileEntityPipeBase;
		this.side = side;

		addPlayerInventorySlots(inventory);

		//addSlotToContainer(new SlotGhost(inventoryIn, index, xPosition, yPosition))
	}
	
	@Override
	public boolean canInteractWith(@Nullable EntityPlayer playerIn) {
		return true;
	}
	
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
	
	
	protected static int getPlayerInventoryVerticalOffset() {
		return 75;
	}
	
	protected static int getPlayerInventoryHorizontalOffset() {
		return 8;
	}
	
}
