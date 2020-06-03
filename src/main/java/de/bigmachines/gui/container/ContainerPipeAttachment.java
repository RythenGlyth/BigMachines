package de.bigmachines.gui.container;

import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase;
import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase.PipeAttachment;
import de.bigmachines.gui.slots.ISlotValidator;
import de.bigmachines.gui.slots.SlotGhost;
import de.bigmachines.utils.classes.Inventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class ContainerPipeAttachment extends ContainerBase {
	
	private final TileEntityPipeBase tileEntityPipeBase;
	private final Inventory inventory;
	private final EnumFacing side;
	
	public ContainerPipeAttachment(InventoryPlayer inventory, TileEntityPipeBase tileEntityPipeBase, EnumFacing side) {
		super();
		this.tileEntityPipeBase = tileEntityPipeBase;
		this.side = side;
		
        final PipeAttachment attachment = tileEntityPipeBase.getAttachment(side);
        this.inventory = attachment.getInventory();
        
		addSlotToContainer(new SlotGhost(this.inventory, 0, 44 + 18 * 0, 43));
		addSlotToContainer(new SlotGhost(this.inventory, 1, 44 + 18 * 1, 43));
		addSlotToContainer(new SlotGhost(this.inventory, 2, 44 + 18 * 2, 43));
		addSlotToContainer(new SlotGhost(this.inventory, 3, 44 + 18 * 3, 43));
		addSlotToContainer(new SlotGhost(this.inventory, 4, 44 + 18 * 4, 43));
        
		addPlayerInventorySlots(inventory);
	}
	
	@Override
	protected int getInventorySize() {
		return inventory.getSizeInventory();
	}
	
	protected static int getPlayerInventoryVerticalOffset() {
		return 75;
	}
	
	protected static int getPlayerInventoryHorizontalOffset() {
		return 8;
	}
	
}
