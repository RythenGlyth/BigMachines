package de.bigmachines.gui.elements;

import java.util.ArrayList;
import java.util.List;

import de.bigmachines.gui.GuiContainerBase;
import de.bigmachines.utils.RenderHelper;
import net.minecraft.util.ResourceLocation;

public abstract class Element {
	
	protected GuiContainerBase gui;
	protected ResourceLocation texture;
	
	public int posX;
	public int posY;

	protected int sizeX;
	protected int sizeY;

	protected int textureW = 256;
	protected int textureH = 256;
	
	public List<String> tooltips = new ArrayList<String>();
	
	public Element(GuiContainerBase gui, int posX, int posY) {
		this(gui, posX, posY, 16, 16);
	}

	public Element(GuiContainerBase gui, int posX, int posY, int width, int height) {
		this.gui = gui;
		this.posX = posX;
		this.posY = posY;
		this.sizeX = width;
		this.sizeY = height;
	}
	
	public abstract void drawForeground(int mouseX, int mouseY);
	
	public abstract void drawBackground(int mouseX, int mouseY, float partialTicks);
	
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
	
	public void onMouseWheel(int mouseX, int mouseY, int dWheel) {
		
	}

	public void onKeyTyped(char typedChar, int keyCode) {
		
	}
	
	public void addTooltip(int mouseX, int mouseY, List<String> tooltips) {
		tooltips.addAll(this.tooltips);
	}
	
	public boolean isVisible() {
		return true;
	}
	
	public boolean intersectsWith(int mouseX, int mouseY) {
		return mouseX >= this.posX && mouseX < this.posX + this.sizeX && mouseY >= this.posY && mouseY < this.posY + this.sizeY;
	}
	
	public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, float textureW, float textureH) {
		RenderHelper.drawTexturedModalRect(x, y, width, height, u, v, textureW, textureH, gui.getZLevel());
	}

	public void update(int mouseX, int mouseY) {
		
	}

	
}
