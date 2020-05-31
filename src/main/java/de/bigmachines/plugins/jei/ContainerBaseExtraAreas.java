package de.bigmachines.plugins.jei;

import de.bigmachines.gui.GuiContainerBase;
import mezz.jei.api.gui.IAdvancedGuiHandler;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;

public class ContainerBaseExtraAreas implements IAdvancedGuiHandler<GuiContainerBase> {

	@Override
	@Nonnull
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
