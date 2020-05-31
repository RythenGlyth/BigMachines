package de.bigmachines.gui.client.manual;

import de.bigmachines.utils.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public abstract class ManualContent {
	protected final String content;
	
	public ManualContent(final String input) {
		this.content = input;
	}
	
	/**
	 * Draws a terminal content to the Manual.
	 * 
	 * @param left where this section should start when inline
	 * @param top where this section starts.
	 * @param mouseX current mouse X location for highlighting hovered areas
	 * @param mouseY current mouse Y location
	 * @param partialTicks time between last game tick and current render "partial" tick
	 * @return the height of the drawn block
	 */
	public abstract int draw(int left, int top, int mouseX, int mouseY, int width, float partialTicks, float zLevel);
	
	static class ManualTitle extends ManualContent {
		
		private final static int SCALE = 2;

		public ManualTitle(String input) {
			super(input);
		}

		@Override
		public int draw(int left, int top, int mousex, int mousey, int width, float partialticks, float zLevel) {
			// 0x ff 00 00 00 alpha r g b
			GlStateManager.pushMatrix();
			GlStateManager.translate(left, top, 0);
			GlStateManager.scale(SCALE, SCALE, 1);
			Minecraft.getMinecraft().fontRenderer.drawSplitString(content, 0, 0, 1000, 0xff000000);
			GlStateManager.popMatrix();
			return top + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * SCALE;
		}
		
	}
	
	static class ManualText extends ManualContent {

		public ManualText(String input) {
			super(input);
		}

		@Override
		public int draw(int left, int top, int mouseX, int mouseY, int width, float partialTicks, float zLevel) {
			GlStateManager.pushMatrix();
			Minecraft.getMinecraft().fontRenderer.drawSplitString(content, left, top, 1000, 0xff000000);
			//Minecraft.getMinecraft().fontRenderer.drawString(content, left, top, 0xff000000);
			//GlStateManager.disableAlpha();
			GlStateManager.popMatrix();
			return top + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
		}
		
	}
	
	static class ManualCrafting extends ManualContent {
		
		private static final ResourceLocation CRAFTING_GRID_TEXTURE = new ResourceLocation("textures/gui/container/crafting_table.png");

		public ManualCrafting(String input) {
			super(input);
		}

		@Override
		public int draw(int left, int top, int mouseX, int mouseY, int width, float partialTicks, float zLevel) {
			//Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/crafting_table.png"));
			GlStateManager.color(1, 1, 1, 1);
			Minecraft.getMinecraft().getTextureManager().bindTexture(CRAFTING_GRID_TEXTURE);
			left = (width - 56) / 2;
			RenderHelper.drawTexturedModalRect(left, top, 56, 56, 28, 15, 256, 256, zLevel);
			if (mouseX >= left && mouseY >= top) {
				final int hoverX = (mouseX - left) / 18;
				final int hoverY = (mouseY - top) / 18;
				if (hoverX < 3 && hoverY < 3) {
					GlStateManager.disableAlpha();
					GlStateManager.disableLighting();
	                GlStateManager.disableDepth();
	                int j1 = hoverX * 18 + left + 2;
	                int k1 = hoverY * 18 + top + 2;
	                GlStateManager.colorMask(true, true, true, false);
	                RenderHelper.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433, zLevel);
	                GlStateManager.colorMask(true, true, true, true);
	                GlStateManager.enableLighting();
	                GlStateManager.enableDepth();
				}
			}
			GlStateManager.resetColor();
			return top + 56 + 4;
		}
		
	}
}
