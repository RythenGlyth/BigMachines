package de.bigmachines.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerPipeAttachment extends Container {

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
}
