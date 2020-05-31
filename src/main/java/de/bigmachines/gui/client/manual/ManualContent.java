package de.bigmachines.gui.client.manual;

import de.bigmachines.utils.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.List;

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
	 * @param tooltips list of tooltips on parent gui to add tooltips to
	 * @return the height of the drawn block
	 */
	public abstract int draw(int left, int top, int mouseX, int mouseY, int width, float partialTicks, float zLevel, List<String> tooltips);
	
	static class ManualTitle extends ManualContent {
		
		private final static int SCALE = 2;

		public ManualTitle(String input) {
			super(input);
		}

		@Override
		public int draw(int left, int top, int mousex, int mousey, int width, float partialticks, float zLevel, List<String> tooltips) {
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
		public int draw(int left, int top, int mouseX, int mouseY, int width, float partialTicks, float zLevel, List<String> tooltips) {
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
		private static final RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

		public ManualCrafting(String input) {
			super(input);
		}

		@Override
		public int draw(int left, int top, int mouseX, int mouseY, int width, float partialTicks, float zLevel, List<String> tooltips) {
			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.pushMatrix();
			GlStateManager.color(1, 1, 1, 1);
			Minecraft.getMinecraft().getTextureManager().bindTexture(CRAFTING_GRID_TEXTURE);
			left = (width - 118) / 2;
			RenderHelper.drawTexturedModalRect(left, top, 118, 56, 28, 15, 256, 256, zLevel);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			IRecipe r = CraftingManager.getRecipe(new ResourceLocation(content));
			if (r instanceof ShapedRecipes) drawRecipe((ShapedRecipes) r, left, top);
			else if (r instanceof ShapelessRecipes) drawRecipe((ShapelessRecipes) r, left, top);
			else {
				System.out.println("trying to draw unsupported recipe:");
				System.out.println(r instanceof ShapelessOreRecipe);
				System.out.println(r instanceof ShapedOreRecipe);
				System.out.println(r);
				throw new UnsupportedOperationException("not implemented");
			}
			GlStateManager.popMatrix();

			hover(mouseX, mouseY, left, top, zLevel, r, tooltips);


			// needed?
			net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();

			return top + 56 + 4;
		}

		private static void drawRecipe(ShapelessRecipes r, int left, int top) {
			System.out.println("trying to draw shapeless");
			throw new UnsupportedOperationException("not implemented");
		}

		private static void drawRecipe(ShapedRecipes r, int left, int top) {
			renderItem.renderItemAndEffectIntoGUI(r.getRecipeOutput(), left + 95 + 1, top + 19 + 1);
			renderItem.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, r.getRecipeOutput(), left + 95, top + 19, "");
			NonNullList<Ingredient> is = r.getIngredients();

			for (int x = 0; x < r.recipeWidth; x++) for (int y = 0; y < r.recipeHeight; y++) {
				final Ingredient i = is.get(y * r.recipeWidth + x);

				if (i.getMatchingStacks().length == 0) continue;
				final ItemStack itemStack = i.getMatchingStacks()[(int) (Minecraft.getMinecraft().player.getEntityWorld().getTotalWorldTime() / 40 % i.getMatchingStacks().length)];
				renderItem.renderItemAndEffectIntoGUI(itemStack, left + 2 + x * 18, top + 2 + y * 18);
				renderItem.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, itemStack, left + 2 + x * 18, top + 2 + y * 18, "");
			}
		}

		private static void hover(int mouseX, int mouseY, int left, int top, float zLevel, IRecipe r, List<String> tooltips) {
			GlStateManager.pushMatrix();
			if (mouseX >= left && mouseY >= top) {
				final int hoverX = (mouseX - left) / 18;
				final int hoverY = (mouseY - top) / 18;
				int j1 = -1, k1 = -1;
				if (hoverX < 3 && hoverY < 3) {
					j1 = hoverX * 18 + left + 2;
					k1 = hoverY * 18 + top + 2;

					// FIXME this magic 3 should *certainly* not be here; we have to instead somehow check for recipe width or idk
					final ItemStack[] in = r.getIngredients().get(hoverY * 3 + hoverX).getMatchingStacks();
					if (in.length > 0)
						tooltips.addAll(in[(int) (Minecraft.getMinecraft().player.getEntityWorld().getTotalWorldTime() / 40 % in.length)].getTooltip(
								Minecraft.getMinecraft().player,
								Minecraft.getMinecraft().gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL));
				} else if (mouseX >= left + 95 && mouseX < left + 113 && mouseY >= top + 19 && mouseY < top + 37) {
					j1 = left + 1 + 95;
					k1 = top + 1 + 19;
					tooltips.addAll(r.getRecipeOutput().getTooltip(Minecraft.getMinecraft().player,
							Minecraft.getMinecraft().gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL));
				}
				if (j1 != -1) {
					GlStateManager.disableAlpha();
					GlStateManager.disableLighting();
					GlStateManager.disableDepth();
					GlStateManager.colorMask(true, true, true, false);
					RenderHelper.drawGradientRect(j1, k1, j1 + 16, k1 + 16, 0x80ffffff, 0x80ffffff, zLevel);

					GlStateManager.colorMask(true, true, true, true);
					GlStateManager.enableAlpha();
					GlStateManager.enableLighting();
					GlStateManager.enableDepth();
				}
			}
			GlStateManager.popMatrix();
			GlStateManager.resetColor();
		}

	}
}
