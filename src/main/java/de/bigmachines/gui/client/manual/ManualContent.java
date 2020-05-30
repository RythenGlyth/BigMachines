package de.bigmachines.gui.client.manual;

import de.bigmachines.utils.classes.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public abstract class ManualContent {
	protected final String content;
	protected final boolean inline;
	
	public ManualContent(final String input, final boolean inline) {
		this.content = input;
		this.inline = inline;
	}
	
	/**
	 * Draws a terminal content to the Manual.
	 * 
	 * @param left where this section should start when inline
	 * @param top where this section starts.
	 * @param mouseX
	 * @param mouseY
	 * @param partialTicks
	 * @return Pair. The first one stores the width of the drawn block, if this block is inline. 
	 * 	The second integer stores the height of the block 
	 */
	public abstract Pair<Integer, Integer> draw(int left, int top, int mouseX, int mouseY, float partialTicks);
	
	static class ManualTitle extends ManualContent {
		
		private final static int SCALE = 2;

		public ManualTitle(String input, boolean inline) {
			super(input, inline);
		}

		@Override
		public Pair<Integer, Integer> draw(int left, int top, int mousex, int mousey, float partialticks) {
			// 0x ff 00 00 00 alpha r g b
			GlStateManager.pushMatrix();
			GlStateManager.translate(left, top, 0);
			GlStateManager.scale(SCALE, SCALE, 1);
			// FIXME
			Minecraft.getMinecraft().fontRenderer.drawSplitString(content, 0, 0, 1000, 0xff000000);
			GlStateManager.popMatrix();
			return new Pair<Integer, Integer>(inline ? left + Minecraft.getMinecraft().fontRenderer.getStringWidth(content) * SCALE : left, 
					top + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * SCALE);
		}
		
	}
	
	static class ManualText extends ManualContent {

		public ManualText(String input, boolean inline) {
			super(input, inline);
		}

		@Override
		public Pair<Integer, Integer> draw(int left, int top, int mouseX, int mouseY, float partialTicks) {
			GlStateManager.pushMatrix();
			Minecraft.getMinecraft().fontRenderer.drawSplitString(content, left, top, 1000, 0xff000000);
			GlStateManager.popMatrix();
			return new Pair<Integer, Integer>(inline ? left + Minecraft.getMinecraft().fontRenderer.getStringWidth(content) : left, 
					top + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT);
		}
		
	}
}
