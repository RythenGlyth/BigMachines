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
	 * @param mouseX
	 * @param mouseY
	 * @param partialTicks
	 * @return the height of the drawn block
	 */
	public abstract int draw(int left, int top, int mouseX, int mouseY, float partialTicks, float zLevel);
	
	static class ManualTitle extends ManualContent {
		
		private final static int SCALE = 2;

		public ManualTitle(String input) {
			super(input);
		}

		@Override
		public int draw(int left, int top, int mousex, int mousey, float partialticks, float zLevel) {
			// 0x ff 00 00 00 alpha r g b
			GlStateManager.pushMatrix();
			GlStateManager.translate(left, top, 0);
			GlStateManager.scale(SCALE, SCALE, 1);
			// FIXME
			//Minecraft.getMinecraft().fontRenderer.drawSplitString(content, 0, 0, 1000, 0xff000000);
			GlStateManager.popMatrix();
			return top + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * SCALE;
		}
		
	}
	
	static class ManualText extends ManualContent {

		public ManualText(String input) {
			super(input);
		}

		@Override
		public int draw(int left, int top, int mouseX, int mouseY, float partialTicks, float zLevel) {
			GlStateManager.pushMatrix();
			//Minecraft.getMinecraft().fontRenderer.drawSplitString(content, left, top, 1000, 0xff000000);
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
		public int draw(int left, int top, int mouseX, int mouseY, float partialTicks, float zLevel) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/crafting_table.png"));
			System.out.println("hey");
			// Minecraft.getMinecraft().getTextureManager().bindTexture(CRAFTING_GRID_TEXTURE);
			RenderHelper.drawTexturedModalRect(5, 5, 56, 56, 0, 0, 256, 256, zLevel);
			return 0;
		}
		
	}
}
