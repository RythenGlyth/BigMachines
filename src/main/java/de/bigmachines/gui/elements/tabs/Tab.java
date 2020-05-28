package de.bigmachines.gui.elements.tabs;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import de.bigmachines.gui.GuiContainerBase;
import de.bigmachines.gui.elements.Element;
import de.bigmachines.utils.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class Tab {
	
	public int y;
	
	public int minWidth = 22;
	public int maxWidth = 22;
	
	public int minHeight = 22;
	public int maxHeight = 22;
	
	public int ticksToExpand = 10;
	public boolean expanded = false;
	
	public float colorR = 1f;
	public float colorG = 1f;
	public float colorB = 1f;
	public float colorA = 1f;
	
	public List<Element> elements = new ArrayList<Element>();
	
	public GuiContainerBase gui;
	
	/**
	 * Side to display
	 * true = right, false = left
	 */
	public boolean side = true;
	
	public Tab(GuiContainerBase gui, int y) {
		this.y = y;
	}
	
	/*
	 * 0xaarrggbb
	 */
	public void setColor(int color) {
		colorA = (float) (color >> 24 & 255) / 255.0F;
		colorR   = (float) (color >> 16 & 255) / 255.0F;
		colorG = (float) (color >>  8 & 255) / 255.0F;
		colorB  = (float) (color >>  0 & 255) / 255.0F;
	}
	
	/**
	 * set color
	 * values between 0 and 1
	 */
	public void setColor(float red, float green, float blue, float alpha) {
		this.colorR = red;
		this.colorG = green;
		this.colorB = blue;
		this.colorA = alpha;
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		GlStateManager.color(1, 1, 1, 1);
		
		drawElementsForeground(mouseX - gui.getGuiLeft() - gui.getXSize(), mouseY - gui.getGuiTop() - this.y);
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(colorR, colorG, colorB, colorA);
		//gui.mc.renderEngine.bindTexture(texture);
		
		drawElementsBackground(mouseX - gui.getGuiLeft() - gui.getXSize(), mouseY - gui.getGuiTop() - this.y, partialTicks);
	}
	
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		elements.forEach(element -> {
			element.onKeyTyped(typedChar, keyCode);
		});
	}
	
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		elements.forEach(element -> {
			element.mouseReleased(mouseX - gui.getGuiLeft() - gui.getXSize(), mouseY - gui.getGuiTop() - this.y, state);
		});
	}
	
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		elements.forEach(element -> {
			element.mouseClicked(mouseX - gui.getGuiLeft() - gui.getXSize(), mouseY - gui.getGuiTop() - this.y, mouseButton);
		});
	}
	
	public void drawElementsForeground(int mouseX, int mouseY) {
		elements.forEach(element -> {
			element.drawForeground(mouseX, mouseY);
		});
	}
	
	public void drawElementsBackground(int mouseX, int mouseY, float partialTicks) {
		elements.forEach(element -> {
			element.drawBackground(mouseX, mouseY, partialTicks);
		});
	}
	
	/**
	 * @param x relative
	 * @return absolute
	 */
	public int absoluteToRelativeX(int x) {
		return x;
	}
	
	/**
	 * @param y relative
	 * @return absolute
	 */
	public int absoluteToRelativeY(int y) {
		return y;
	}
	
	/**
	 * @param x absolute
	 * @return relative
	 */
	public int relativeToAbsoluteX(int x) {
		return x;
	}
	
	/**
	 * @param y absolute
	 * @return relative
	 */
	public int relativeToAbsoluteY(int y) {
		return y;
	}

	public void addGuiExtras(List<Rectangle> extras) {
		//extras.add(new Rectangle());
	}
	
	public boolean clickInTab(int mouseX, int mouseY) {
		return false;
	}
	
}
