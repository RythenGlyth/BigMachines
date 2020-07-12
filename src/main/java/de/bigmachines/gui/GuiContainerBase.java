package de.bigmachines.gui;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Mouse;

import de.bigmachines.gui.elements.Element;
import de.bigmachines.gui.elements.tabs.Tab;
import de.bigmachines.utils.RenderHelper;
import de.bigmachines.utils.classes.EnumPosition;
import de.bigmachines.utils.classes.EnumPosition.EnumSide;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiContainerBase extends GuiContainer {
	
	protected static final boolean drawTitle = true;
	protected boolean drawInventory = true;
	protected int mouseX = 0;
	protected int mouseY = 0;
	
	protected String name;
	protected ResourceLocation texture;

	protected final ArrayList<Element> elements = new ArrayList<>();
	protected final ArrayList<Tab> tabs = new ArrayList<>();
	
	protected final List<String> tooltips = new LinkedList<>();
	protected static final boolean drawTooltips = true;
	
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
		updateTabs();
		
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
		for (int i = elements.size(); i-- > 0; ) {
			Element element = elements.get(i);
			if (element.isVisible()) element.update(mouseX, mouseY);
		}
	}
	
	public void updateTabs() {
		int currentOffsetYLeft = 3;
		int currentOffsetYRight = 3;
		for (int i = tabs.size() - 1; i >= 0; i--) {
			Tab tab = tabs.get(i);
			tab.update(mouseX, mouseY, tab.side == EnumSide.LEFT ? currentOffsetYLeft : currentOffsetYRight);
			if(tab.side == EnumSide.LEFT) {
				currentOffsetYLeft += tab.currentHeight;
			} else {
				currentOffsetYRight += tab.currentHeight;
			}
		}
	}
	
	public Element addElement(Element element) {
		elements.add(element);
		return element;
	}
	
	public Tab addTab(Tab tab) {
		tabs.add(tab);
		return tab;
	}
	
	public void drawTooltip(List<String> list) {
		drawHoveringText(list, mouseX + guiLeft, mouseY + guiTop, fontRenderer);
		tooltips.clear();
	}
	
	public void addTooltips(List<String> tooltips) {
		Element element = getElementAtPosition(mouseX, mouseY);
		Tab tab = getTabAtPosition(mouseX, mouseY);

		if (tab != null) {
			tab.addTooltip(mouseX, mouseY, tooltips);
		}
		if (element != null && element.isVisible()) {
			element.addTooltip(mouseX, mouseY, tooltips);
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
	
	protected Tab getTabAtPosition(int mouseX, int mouseY) {
		for (Tab tab : tabs) {
			if (tab.intersectsWith(mouseX, mouseY)) {
				return tab;
			}
		}
		return null;
	}
	
	public void closeAllTabs(EnumPosition.EnumSide side) {
		if(side == null) {
			for (Tab tab : tabs) {
				tab.opening = false;
			}
		} else {
			for (Tab tab : tabs) {
				if(tab.side.equals(side)) tab.opening = false;
			}
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		GlStateManager.color(1, 1, 1, 1);
		if (drawTitle & name != null) {
			fontRenderer.drawString(I18n.format(name), getCenteredOffset(I18n.format(name)), 6, 0x404040);
		}
		if (drawInventory) {
			fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 3, 0x404040);
		}
		
		this.mouseX = mouseX - guiLeft;
		this.mouseY = mouseY - guiTop;
		
		drawElementsForeground(this.mouseX, this.mouseY);
		drawTabsForeground(this.mouseX, this.mouseY);
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
		drawElementsBackground(this.mouseX, this.mouseY, partialTicks);
		drawTabsBackground(this.mouseX, this.mouseY, partialTicks);
		GlStateManager.popMatrix();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		elements.forEach(element -> {
			element.onKeyTyped(typedChar, keyCode);
		});
		tabs.forEach(tab -> {
			tab.onKeyTyped(typedChar, keyCode); 
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
			tabs.forEach(tab -> {
				tab.onMouseWheel(mouseX, mouseY, dWheel);
			});
		}
		
		super.handleMouseInput();
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		elements.forEach(element -> {
			element.mouseReleased(mouseX, mouseY, state);
		});
		tabs.forEach(tab -> {
			tab.mouseReleased(mouseX, mouseY, state);
		});
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		elements.forEach(element -> {
			element.mouseClicked(mouseX - guiLeft, mouseY - guiTop, mouseButton);
		});
		tabs.forEach(tab -> {
			tab.mouseClicked(mouseX - guiLeft, mouseY - guiTop, mouseButton);
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
	
	
	public void drawTabsForeground(int mouseX, int mouseY) {
		for (Tab tab : tabs) {
			tab.drawForeground(mouseX, mouseY);
		}
	}
	
	public void drawTabsBackground(int mouseX, int mouseY, float partialTicks) {
		for (Tab tab : tabs) {
			tab.drawBackground(mouseX, mouseY, partialTicks);
		}
	}
	
	protected int getCenteredOffset(String string) {
		return getCenteredOffset(string, xSize / 2);
	}

	protected int getCenteredOffset(String string, int xPos) {
		return xPos - (fontRenderer.getStringWidth(string) / 2);
	}

	public List<Rectangle> getGuiExtraAreas() {
		List<Rectangle> extras = new ArrayList<>();
		tabs.forEach(tab -> {
			tab.addGuiExtras(extras);
		});
		return extras;
	}

}
