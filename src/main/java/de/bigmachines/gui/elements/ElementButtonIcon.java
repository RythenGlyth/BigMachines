package de.bigmachines.gui.elements;

import de.bigmachines.Reference;
import de.bigmachines.gui.GuiContainerBase;
import net.minecraft.util.ResourceLocation;

// TODO FIXME
public class ElementButtonIcon extends Element {
	private final int u;
	private final int v;

	public ElementButtonIcon(GuiContainerBase gui, int posX, int posY, int u, int v) {
		super(gui, posX, posY);
		this.u = u;
		this.v = v;
		// TODO texture
		this.texture = new ResourceLocation(Reference.MOD_ID + "textures/gui/elements/buttons.png");
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float partialTick) {
		// TODO Auto-generated method stub
		
	}

}
