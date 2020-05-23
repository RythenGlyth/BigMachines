package de.bigmachines.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import de.bigmachines.gui.elements.Element;
import de.bigmachines.utils.RenderHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class GuiContainerBase extends GuiContainer {
	
	protected boolean drawTitle = true;
	protected boolean drawInventory = true;
	protected int mouseX = 0;
	protected int mouseY = 0;
	
	protected String name;
	protected ResourceLocation texture;

	protected ArrayList<Element> elements = new ArrayList<>();
	
	protected List<String> tooltips = new LinkedList<>();
	protected boolean drawTooltips = true;
	
	public GuiContainerBase(Container container) {
		super(container);
	}
	
	public GuiContainerBase(Container container, ResourceLocation texture) {
		super(container);
		this.texture = texture;
	}
    
    public float getZLevel() {
    	return zLevel;
    }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		updateElements();
		
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if (drawTooltips && mc.player.inventory.getItemStack().isEmpty()) {
			addTooltips(tooltips);
			drawTooltip(tooltips);
			
			this.mouseX = mouseX - guiLeft;
			this.mouseY = mouseY - guiTop;
		}
		
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	public void updateElements() {
		
	}
	
	public Element addElement(Element element) {
		elements.add(element);
		return element;
	}
	
	public void drawTooltip(List<String> list) {
		drawHoveringText(list, mouseX + guiLeft, mouseY + guiTop, fontRenderer);
		tooltips.clear();
	}
	
	public void addTooltips(List<String> tooltip) {
		Element element = getElementAtPosition(mouseX, mouseY);
		
		if (element != null && element.isVisible()) {
			element.addTooltip(tooltip);
		}
	}
	
	protected Element getElementAtPosition(int mouseX, int mouseY) {
		for (int i = elements.size() - 1; i >= 0; i--) {
			Element element = elements.get(i);
			if (element.intersectsWith(mouseX, mouseY)) {
				return element;
			}
		}
		return null;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		if (drawTitle & name != null) {
			fontRenderer.drawString(I18n.format(name), getCenteredOffset(I18n.format(name)), 6, 0x404040);
		}
		if (drawInventory) {
			fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 3, 0x404040);
		}
		
		this.mouseX = mouseX - guiLeft;
		this.mouseY = mouseY - guiTop;
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(guiLeft, guiTop, 0.0F);
		drawElementsForeground(mouseX, mouseY);
		GlStateManager.popMatrix();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1, 1, 1, 1);
		mc.renderEngine.bindTexture(texture);
		
		if (xSize > 256 || ySize > 256) {
			RenderHelper.drawTexturedModalRect(guiLeft, guiTop, xSize, ySize, 0, 0, 512, 512, zLevel);
		} else {
			RenderHelper.drawTexturedModalRect(guiLeft, guiTop, xSize, ySize, 0, 0, 256, 256, zLevel);
		}
		
		this.mouseX = mouseX - guiLeft;
		this.mouseY = mouseY - guiTop;
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(guiLeft, guiTop, 0.0F);
		drawElementsBackground(mouseX, mouseY, partialTicks);
		GlStateManager.popMatrix();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		elements.forEach(element -> {
			element.onKeyTyped(typedChar, keyCode);
		});
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		int mouseX = (Mouse.getEventX() * width / mc.displayWidth) - guiLeft;
		int mouseY = (height - Mouse.getEventY() * height / mc.displayHeight - 1)  - guiTop;
		
		int dWheel = Mouse.getEventDWheel(); 
		if (dWheel != 0) {
			elements.forEach(element -> {
				element.onMouseWheel(mouseX, mouseY, dWheel);
			});
		}
		
		super.handleMouseInput();
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		elements.forEach(element -> {
			element.mouseReleased(mouseX, mouseY, state);
		});
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		elements.forEach(element -> {
			element.mouseClicked(mouseX, mouseY, mouseButton);
		});
		super.mouseClicked(mouseX, mouseY, mouseButton);
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
	
	protected int getCenteredOffset(String string) {
		return this.getCenteredOffset(string, xSize / 2);
	}

	protected int getCenteredOffset(String string, int xPos) {
		return xPos - (fontRenderer.getStringWidth(string) / 2);
	}

}
