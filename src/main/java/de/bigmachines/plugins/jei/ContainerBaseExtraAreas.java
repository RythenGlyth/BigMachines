package de.bigmachines.plugins.jei;

import de.bigmachines.gui.GuiContainerBase;
import mezz.jei.api.gui.IAdvancedGuiHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class ContainerBaseExtraAreas implements IAdvancedGuiHandler<GuiContainerBase> {

	@Override
	@Nonnull
	public Class getGuiContainerClass() {
		return GuiContainerBase.class;
	}
	
	@Override
	@Nullable
	public Object getIngredientUnderMouse(@Nullable GuiContainerBase guiContainer, int mouseX, int mouseY) {
		return null;
	}
	
	@Override
	@Nonnull
	public List<Rectangle> getGuiExtraAreas(@Nonnull GuiContainerBase guiContainer) {
		return guiContainer.getGuiExtraAreas();
	}
	
	
	
}
