package de.bigmachines.gui.elements;

import java.util.List;

import de.bigmachines.gui.GuiContainerBase;
import net.minecraft.util.ResourceLocation;

public abstract class Element {
	
	protected GuiContainerBase gui;
	protected ResourceLocation texture;
	
	protected int posX;
	protected int posY;

	protected int sizeX;
	protected int sizeY;

	protected int textureW = 256;
	protected int textureH = 256;
	
	public Element(GuiContainerBase gui, int posX, int posY) {
		this.gui = gui;
		this.posX = posX;
		this.posY = posY;
	}

	public Element(GuiContainerBase gui, int posX, int posY, int width, int height) {
		this.gui = gui;
		this.posX = posX;
		this.posY = posY;
		this.sizeX = width;
		this.sizeY = height;
	}
	
	public abstract void drawForeground(int mouseX, int mouseY);
	
	public abstract void drawBackground(int mouseX, int mouseY, float partialTick);
	
	public Element setSize(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		return this;
	}
	
	public Element setTexture(ResourceLocation texture) {
		this.texture = texture;
		return this;
	}
	
	public Element setTexture(ResourceLocation texture, int textureW, int textureH) {
		this.texture = texture;
		this.textureW = textureW;
		this.textureH = textureH;
		return this;
	}
	
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		return false;
	}
	
	public void mouseReleased(int mouseX, int mouseY, int state) {
		
	}
	
	public boolean onMouseWheel(int mouseX, int mouseY, int dWheel) {
		return false;
	}

	public boolean onKeyTyped(char typedChar, int keyCode) {
		return false;
	}
	
	public void addTooltip(List<String> list) {
		
	}
	
	public boolean isVisible() {
		return true;
	}
	
	public boolean intersectsWith(int mouseX, int mouseY) {
		return mouseX >= this.posX && mouseX < this.posX + this.sizeX && mouseY >= this.posY && mouseY < this.posY + this.sizeY;
	}
	
	public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, float textureW, float textureH) {
		gui.drawTexturedModalRect(x, y, width, height, u, v, textureW, textureH);
	}

	
}
