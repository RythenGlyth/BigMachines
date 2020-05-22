package de.bigmachines.blocks;

import net.minecraft.entity.player.InventoryPlayer;

public interface IHasGui {

	public Object getGuiServer(InventoryPlayer inventory);

	public Object getGuiClient(InventoryPlayer inventory);
	
	
}
