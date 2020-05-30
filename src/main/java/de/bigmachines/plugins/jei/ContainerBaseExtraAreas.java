package de.bigmachines.plugins.jei;

import java.awt.Rectangle;
import java.util.List;

import de.bigmachines.gui.GuiContainerBase;
import mezz.jei.api.gui.IAdvancedGuiHandler;

public class ContainerBaseExtraAreas implements IAdvancedGuiHandler<GuiContainerBase> {

	@Override
	public Class getGuiContainerClass() {
		return GuiContainerBase.class;
	}
	
	@Override
	public Object getIngredientUnderMouse(GuiContainerBase guiContainer, int mouseX, int mouseY) {
		return null;
	}
	
	@Override
	public List<Rectangle> getGuiExtraAreas(GuiContainerBase guiContainer) {
		return guiContainer.getGuiExtraAreas();
	}
	
	
	
}
