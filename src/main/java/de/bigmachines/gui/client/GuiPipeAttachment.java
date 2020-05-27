package de.bigmachines.gui.client;

import de.bigmachines.Reference;
import de.bigmachines.gui.GuiContainerBase;
import de.bigmachines.gui.container.ContainerPipeAttachment;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiPipeAttachment extends GuiContainerBase {
	
	public GuiPipeAttachment() {
		super(new ContainerPipeAttachment(), new ResourceLocation(Reference.MOD_ID, "textures/gui/duct.png"));
	}
	
	
	
}
