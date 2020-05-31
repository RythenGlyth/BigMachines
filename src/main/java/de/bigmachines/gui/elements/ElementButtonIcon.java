package de.bigmachines.gui.elements;

import de.bigmachines.Reference;
import de.bigmachines.gui.GuiContainerBase;
import de.bigmachines.utils.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class ElementButtonIcon extends Element {
	protected int u;
	protected int v;
	
	public boolean selected;
	public boolean disabled;
	
	protected Runnable onPress;

	public ElementButtonIcon(GuiContainerBase gui, int posX, int posY, int u, int v) {
		super(gui, posX, posY);
		this.u = u;
		this.v = v;
        texture = new ResourceLocation(Reference.MOD_ID, "textures/gui/elements/buttons.png");
	}
	
	public void setOnPress(Runnable  onPress) {
		this.onPress = onPress;
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		GlStateManager.color(1f, 1f, 1f, 1f);
		RenderHelper.drawTexturedModalRect(posX, posY, 16, 16, u, v, 256, 256, gui.getZLevel());
	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float partialTick) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		if(selected) {
			RenderHelper.drawTexturedModalRect(posX, posY, 16, 16, 48, 0, 256, 256, gui.getZLevel());
		} else if(disabled) {
			RenderHelper.drawTexturedModalRect(posX, posY, 16, 16, 32, 0, 256, 256, gui.getZLevel());
		} else if(mouseX >= posX && mouseX < posX + 16 && mouseY >= posY && mouseY < posY + 16) {
			RenderHelper.drawTexturedModalRect(posX, posY, 16, 16, 16, 0, 256, 256, gui.getZLevel());
		} else {
			RenderHelper.drawTexturedModalRect(posX, posY, 16, 16, 0, 0, 256, 256, gui.getZLevel());
		}
	}
	
	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		boolean clicked = !disabled && (mouseX >= posX && mouseX < posX + 16 && mouseY >= posY && mouseY < posY + 16);
		if(clicked) {
			if(onPress != null) onPress.run();
			gui.mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0F, 1.0F);
		}
		return clicked;
	}
	
}
