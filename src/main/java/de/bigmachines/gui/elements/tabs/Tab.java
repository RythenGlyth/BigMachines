package de.bigmachines.gui.elements.tabs;

import de.bigmachines.Reference;
import de.bigmachines.gui.GuiContainerBase;
import de.bigmachines.gui.elements.Element;
import de.bigmachines.utils.RenderHelper;
import de.bigmachines.utils.classes.EnumSide;
import de.bigmachines.utils.classes.TextureIcon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Tab {

	public int posX;
	public int posY;
	
	public int offsetY;
	
	public static final int minWidth = 24;
	public static final int maxWidth = 122;
	public int currentWidth = minWidth;
	
	public static final int minHeight = 24;
	public int maxHeight = 122;
	public int currentHeight = minHeight;
	
	public int ticksToExpand = 10;

	/** the is expanded state to which it is going */
	public boolean opening = false;
	/** is expanded */
	public boolean expanded = false;
	
	public float colorR = 1f;
	public float colorG = 1f;
	public float colorB = 1f;
	public float colorA = 1f;
	
	public final List<Element> elements = new ArrayList<>();
	
	public final GuiContainerBase gui;
	
	public EnumSide side = EnumSide.RIGHT;
	
	public final ResourceLocation tabTexture = new ResourceLocation(Reference.MOD_ID, "textures/gui/tab.png");
	
	public TextureIcon icon;
	
	public String name;
	
	public Tab(GuiContainerBase gui) {
		super();
		this.gui = gui;
	}
	
	public Tab(GuiContainerBase gui, EnumSide side) {
		super();
		this.gui = gui;
		this.side = side;
	}
	
	public Element addElement(Element element) {
		elements.add(element);
		return element;
	}
	
	/*
	 * 0xaarrggbb
	 */
	public void setColor(int color) {
		colorA = (float) (color >> 24 & 255) / 255.0F;
		colorR = (float) (color >> 16 & 255) / 255.0F;
		colorG = (float) (color >>  8 & 255) / 255.0F;
		colorB = (float) (color >>  0 & 255) / 255.0F;
	}
	
	/**
	 * set color
	 * values between 0 and 1
	 */
	public void setColor(float red, float green, float blue, float alpha) {
		colorR = red;
		colorG = green;
		colorB = blue;
		colorA = alpha;
	}
	
	public void drawForeground(int mouseX, int mouseY) {
		GlStateManager.color(1, 1, 1, 1);
		
		if(expanded) {
			gui.mc.fontRenderer.drawStringWithShadow(name, side == EnumSide.RIGHT ? posX + 22 : posX + currentWidth - 22 - gui.mc.fontRenderer.getStringWidth(name), posY + offsetY + 5, 0xffffaa00);
		}
		if(expanded) {
			GL11.glPushMatrix();
			GL11.glTranslated(posX, getTranslationY(), 0);
			
			drawElementsForeground(mouseX - posX, mouseY - getTranslationY());
			GL11.glPopMatrix();
		}
		
		if(icon != null) {
			gui.mc.renderEngine.bindTexture(icon.texture);
			RenderHelper.drawSizedTexturedModalRect(side == EnumSide.RIGHT ? gui.getXSize() + 1 : - 16 - 1, this.posY + this.offsetY + 3, 16, 16, icon.minU, icon.minV, icon.maxU, icon.maxV, icon.texW, icon.texH, gui.getZLevel());
		}
	}
	
	public void drawBackground(int mouseX, int mouseY, float partialTicks) {
		gui.mc.renderEngine.bindTexture(tabTexture);

		ticksToExpand = 15;
		
		if(side == EnumSide.RIGHT) {
			//TOP
			GlStateManager.color(colorR, colorG, colorB, colorA);
			RenderHelper.drawTiledTexture(
					posX, posY + offsetY,
					currentWidth - 8, 8,
					8, 8,
					8, 0, 16, 8,
					32, 32,
					gui.getZLevel());
			//TOP-RIGHT
			GlStateManager.color(colorR, colorG, colorB, colorA);
			RenderHelper.drawTiledTexture(
					posX + currentWidth - 8, posY + offsetY,
					8, 8,
					8, 8,
					16, 0, 24, 8,
					32, 32,
					gui.getZLevel());
			//RIGHT
			GlStateManager.color(colorR, colorG, colorB, colorA);
			RenderHelper.drawTiledTexture(
					posX + currentWidth - 8, posY + offsetY + 8,
					8, currentHeight - 16,
					8, 8,
					16, 8, 24, 16,
					32, 32,
					gui.getZLevel());
			//BOTOM-RIGHT
			GlStateManager.color(colorR, colorG, colorB, colorA);
			RenderHelper.drawTiledTexture(
					posX + currentWidth - 8, posY + offsetY + currentHeight - 8,
					8, 8,
					8, 8,
					16, 16, 24, 24,
					32, 32,
					gui.getZLevel());
			//BOTTOM
			GlStateManager.color(colorR, colorG, colorB, colorA);
			RenderHelper.drawTiledTexture(
					posX, posY + offsetY + currentHeight - 8,
					currentWidth - 8, 8,
					8, 8,
					8, 16, 16, 24,
					32, 32,
					gui.getZLevel());
			//MIDDLE
			GlStateManager.color(colorR, colorG, colorB, colorA);
			RenderHelper.drawTiledTexture(
					posX, posY + offsetY + 8,
					currentWidth - 8, currentHeight - 16,
					8, 8,
					8, 8, 16, 16,
					32, 32,
					gui.getZLevel());
		} else {
			//TOP
			GlStateManager.color(colorR, colorG, colorB, colorA);
			RenderHelper.drawTiledTexture(
					posX + 8, posY + offsetY,
					currentWidth - 8, 8,
					8, 8,
					8, 0, 16, 8,
					32, 32,
					gui.getZLevel());
			//TOP-LEFT
			GlStateManager.color(colorR, colorG, colorB, colorA);
			RenderHelper.drawTiledTexture(
					posX, posY + offsetY,
					8, 8,
					8, 8,
					0, 0, 8, 8,
					32, 32,
					gui.getZLevel());
			//LEFT
			GlStateManager.color(colorR, colorG, colorB, colorA);
			RenderHelper.drawTiledTexture(
					posX, posY + offsetY + 8,
					8, currentHeight - 16,
					8, 8,
					0, 8, 8, 16,
					32, 32,
					gui.getZLevel());
			//BOTOM-LEFT
			GlStateManager.color(colorR, colorG, colorB, colorA);
			RenderHelper.drawTiledTexture(
					posX, posY + offsetY + currentHeight - 8,
					8, 8,
					8, 8,
					0, 16, 8, 24,
					32, 32,
					gui.getZLevel());
			//BOTTOM
			GlStateManager.color(colorR, colorG, colorB, colorA);
			RenderHelper.drawTiledTexture(
					posX + 8, posY + offsetY + currentHeight - 8,
					currentWidth - 8, 8,
					8, 8,
					8, 16, 16, 24,
					32, 32,
					gui.getZLevel());
			//MIDDLE
			GlStateManager.color(colorR, colorG, colorB, colorA);
			RenderHelper.drawTiledTexture(
					posX + 8, posY + offsetY + 8,
					currentWidth - 8, currentHeight - 16,
					8, 8,
					8, 8, 16, 16,
					32, 32,
					gui.getZLevel());
		}
		if(expanded) {
			GL11.glPushMatrix();
			GL11.glTranslated(posX, getTranslationY(), 0);
			
			drawElementsBackground(mouseX - posX, mouseY - getTranslationY(), partialTicks);
			
			GL11.glPopMatrix();
		}
	}
	
	/**
	 * get x of the Translation for elements
	 */
	public int getTranslationX() {
		return posX;
	}
	
	/**
	 * get y of the Translation for elements
	 */
	public int getTranslationY() {
		return posY + offsetY;
	}
	
	public void update(int mouseX, int mouseY, int offsetY) {
		this.offsetY = offsetY;
		if(opening) {
			if(currentWidth < maxWidth) currentWidth += (maxWidth - minWidth) / ticksToExpand;
			if(currentHeight < maxHeight) currentHeight += (maxHeight - minHeight) / ticksToExpand;
		} else {
			if(currentWidth > minWidth) currentWidth -= (maxWidth - minWidth) / ticksToExpand;
			if(currentHeight > minHeight) currentHeight -= (maxHeight - minHeight) / ticksToExpand;
		}
		
		if(currentWidth > maxWidth) currentWidth = maxWidth;
		if(currentHeight > maxHeight) currentHeight = maxHeight;
		if(currentWidth < minWidth) currentWidth = minWidth;
		if(currentHeight < minHeight) currentHeight = minHeight;
		
		expanded = (currentWidth == maxWidth) && (currentHeight == maxHeight);
		
		if(side == EnumSide.RIGHT) {
			posX = gui.getXSize() - 1;
		} else {
			posX = 1 - currentWidth;
		}
		
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
	
	public void onKeyTyped(char typedChar, int keyCode) {
		if(!expanded) return;
		for(Element element : elements) {
			element.onKeyTyped(typedChar, keyCode);
		}
		/*elements.forEach(element -> {
			element.onKeyTyped(typedChar, keyCode);
		});*/
	}
	
	public void onMouseWheel(int mouseX, int mouseY, int dWheel) {
		if(!expanded) return;
		for(Element element : elements) {
			element.onMouseWheel(mouseX - posX, mouseY - getTranslationY(), dWheel);
		}
	}
	
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if(!expanded) return;
		for(Element element : elements) {
			element.mouseReleased(mouseX - posX, mouseY - getTranslationY(), state);
		}
		/*elements.forEach(element -> {
			element.mouseReleased(mouseX - this.posX, mouseY - this.posY, state);
		});*/
	}
	
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if(intersectsWith(mouseX, mouseY)) {
			if(expanded) {
				for(Element element : elements) {
					if(element.mouseClicked(mouseX - posX, mouseY - getTranslationY(), mouseButton)) {
						return true;
					}
				}
				opening = false;
			} else {
				boolean lastOpening = opening;
				gui.closeAllTabs(side);
				opening = !lastOpening;
			}
			return true;
		}
		/*if(!this.expanded && intersectsWith(mouseX, mouseY)) {
			this.opening = true;
			return true;
		}*/
		return false;
	}
	
	public boolean intersectsWith(int mouseX, int mouseY) {
		return 
				mouseX >= (posX) && mouseX < (posX) + currentWidth
			 && mouseY >= (posY + offsetY) && mouseY < (posY + offsetY) + currentHeight;
	}
	
	public void addTooltip(int mouseX, int mouseY, List<String> tooltips) {
		if(!expanded) {
			if(name != null && !name.isEmpty()) {
				tooltips.add(name);
				return;
			}
		}
		Element element = getElementAtPosition(mouseX - posX, mouseY - getTranslationY());
		if(element != null && element.isVisible()) {
			element.addTooltip(mouseX - posX, mouseY - getTranslationY(), tooltips);
		}
	}
	
	protected Element getElementAtPosition(int mouseX, int mouseY) {
		for (Element element : elements) {
			if (element.intersectsWith(mouseX, mouseY)) {
				return element;
			}
		}
		return null;
	}

	public void addGuiExtras(List<Rectangle> extras) {
		extras.add(new Rectangle(gui.getGuiLeft() + posX, gui.getGuiTop() + posY + offsetY, currentWidth, currentHeight));
	}
	
}
