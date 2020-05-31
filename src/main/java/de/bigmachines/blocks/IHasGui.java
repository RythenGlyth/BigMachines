package de.bigmachines.blocks;

import net.minecraft.entity.player.InventoryPlayer;

public interface IHasGui {

	Object getGuiServer(InventoryPlayer inventory);

	Object getGuiClient(InventoryPlayer inventory);
	
	
}
