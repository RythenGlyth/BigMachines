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
			drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize, 512, 512);
		} else {
			drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
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

	/**
	 * Draw a not streched texture
	 * @param x position of painting
	 * @param y position of painting
	 * @param width of painting
	 * @param height of painting
	 * @param u in texture
	 * @param v in texture
	 * @param texW the full texture width
	 * @param texH the full texture height
	 */
	public void drawTexturedModalRect(int x, int y, int width, int height, int u, int v, float texW, float texH) {
		this.drawSizedTexturedModalRect(x, y, width, height, u, v, (u + width), (v + height), texW, texH);
	}
	
	/**
	 * Draw texture
	 * @param x position of painting
	 * @param y position of painting
	 * @param width of painting
	 * @param height of painting
	 * @param minU in texture
	 * @param minV in texture
	 * @param maxU in texture
	 * @param maxV in texture
	 * @param texW the full texture width
	 * @param texH the full texture height
	 */
	public void drawSizedTexturedModalRect(int x, int y, int width, int height, float minU, float minV, float maxU, float maxV, float texW, float texH) {
        final float uScale = 1f / texW;
        final float vScale = 1f / texH;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x        , y + height, this.zLevel).tex(minU * uScale, (maxV * vScale)).endVertex();
        buffer.pos(x + width, y + height, this.zLevel).tex(maxU * uScale, (maxV * vScale)).endVertex();
        buffer.pos(x + width, y         , this.zLevel).tex(maxU * uScale, (minV * vScale)).endVertex();
        buffer.pos(x        , y         , this.zLevel).tex(minU * uScale, (minV * vScale)).endVertex();
        tessellator.draw();
    }
	
	/**
	 * Draw Texture Tiled (with 16 width of each tile)
	 * @param x position of painting
	 * @param y position of painting
	 * @param width of painting
	 * @param height of painting
	 * @param minU in texture
	 * @param minV in texture
	 * @param texW the full texture width
	 * @param texH the full texture height
	 */
	public void drawTiledTexture(int x, int y, int width, int height, float minU, float minV, float texW, float texH) {
		this.drawTiledTexture(x, y, width, height, 16, 16, minU, minV, minU + 16, minV + 16, texW, texH);
	}
	
	/**
	 * Draw Texture Tiled
	 * @param x position of painting
	 * @param y position of painting
	 * @param width of painting
	 * @param height of painting
	 * @param drawWidth of each tile
	 * @param drawHeight of each tile
	 * @param minU in texture
	 * @param minV in texture
	 * @param maxU in texture
	 * @param maxV in texture
	 * @param texW the full texture width
	 * @param texH the full texture height
	 */
	public void drawTiledTexture(int x, int y, int width, int height, int drawWidth, int drawHeight, float minU, float minV, float maxU, float maxV, float texW, float texH) {
		int i;
		int j;

		for (i = 0; i < width; i += drawWidth) {
			for (j = 0; j < height; j += drawHeight) {
				drawSizedTexturedModalRect(x + i, y + j, Math.min(width - i, drawWidth), Math.min(height - j, drawHeight), minU, minV, maxU, maxV, texW, texH);
			}
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	/**
	 * Draw Texture Tiled of TextureAtlasSprite
	 * @param x position of painting
	 * @param y position of painting
	 * @param width of painting
	 * @param height of painting
	 * @param icon the TextureAtlasSprite of texture
	 */
	public void drawTiledTextureIcon(int x, int y, int width, int height, TextureAtlasSprite icon) {
		drawTiledTexture(x, y, width, height, 16, 16, icon.getMinU(), icon.getMinV(), icon.getMaxU(), icon.getMaxV(), 1, 1);
	}
	
	public void drawFluid(int x, int y, FluidStack fluid, int width, int height) {
		if (fluid == null) return;
		
		GL11.glPushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		RenderHelper.setBlockTextureSheet();
		int color = fluid.getFluid().getColor(fluid);
		RenderHelper.setColorFromInt(color);
		drawTiledTextureIcon(x, y, width, height, RenderHelper.getTexture(fluid.getFluid().getStill(fluid)));
		GL11.glPopMatrix();
	}
	
	protected int getCenteredOffset(String string) {
		return this.getCenteredOffset(string, xSize / 2);
	}

	protected int getCenteredOffset(String string, int xPos) {
		return xPos - (fontRenderer.getStringWidth(string) / 2);
	}

	
	
}
