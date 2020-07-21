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
	protected int posX;
	protected int posY;
	protected int width;
	protected int maxHeight;
	
	public ManualContent() {
		super();
	}
	
	/**
	 * @return the height of the block
	 */
	public int updatePos(int posX, int posY, int width, int maxHeight) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.maxHeight = maxHeight;
		return 0;
	}
	
	/**
	 * Draws a terminal content to the Manual.
	 *
	 * @param mouseX       current mouse X location for highlighting hovered areas
	 * @param mouseY       current mouse Y location
	 * @param partialTicks time between last game tick and current render "partial" tick
	 * @param tooltips     list of tooltips on parent gui to add tooltips to
	 * @return the height of the drawn block
	 */
	public abstract void draw(int mouseX, int mouseY, float partialTicks, float zLevel, List<String> tooltips);
	
	/*static class ManualTitle extends ManualContent {
		
		final String text;
		
		protected final double scale;

		public ManualTitle(String text, double scale) {
			super();
			this.text = text;
			this.scale = scale;
		}
		
		public ManualTitle(String text) {
			this(text, 2);
		}

		@Override
		public void draw(int mouseX, int mouseY, float partialticks, float zLevel, List<String> tooltips) {
			// 0x ff 00 00 00 alpha r g b
			GlStateManager.pushMatrix();
			GlStateManager.translate(this.posX, this.posY, 0);
			GlStateManager.scale(scale, scale, 1);
			Minecraft.getMinecraft().fontRenderer.drawSplitString(text, 0, 0, 1000, 0xff000000);
			GlStateManager.popMatrix();
		}
		
		@Override
		public int updatePos(int posX, int posY, int width, int maxHeight) {
			super.updatePos(posX, posY, width, maxHeight);
			return ((int)(Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * scale));
		}
		
	}*/
	
	static class ManualSpace extends ManualContent {
		
		final int height;
		
		public ManualSpace(int height) {
			this.height = height;
		}
		
		@Override
		public void draw(int mouseX, int mouseY, float partialTicks, float zLevel, List<String> tooltips) {
			
		}
		
		@Override
		public int updatePos(int posX, int posY, int width, int maxHeight) {
			super.updatePos(posX, posY, width, maxHeight);
			return height;
		}
		
	}
	
	static class ManualLine extends ManualContent {
		
		final float thickness;
		final int color;
		
		public ManualLine(float thickness, int color) {
			this.thickness = thickness;
			this.color = color;
		}
		
		@Override
		public void draw(int mouseX, int mouseY, float partialTicks, float zLevel, List<String> tooltips) {
			RenderHelper.drawLine(this.posX, this.posY, this.posX + this.width, this.posY, color, thickness);
		}
		
		@Override
		public int updatePos(int posX, int posY, int width, int maxHeight) {
			super.updatePos(posX, posY, width, maxHeight);
			return (int) thickness;
		}
		
	}
	
	static class ManualText extends ManualContent {
		
		final String text;
		
		protected final double scale;
		
		public ManualText(String text, double scale) {
			super();
			this.text = text;
			this.scale = scale > 0 ? scale : 1;
		}
		
		public ManualText(String text) {
			this(text, 2);
		}
		
		@Override
		public void draw(int mouseX, int mouseY, float partialTicks, float zLevel, List<String> tooltips) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(this.posX, this.posY, 0);
			GlStateManager.scale(scale, scale, 1);
			RenderHelper.drawStringWordWrap(text, 0, 0, ((int) (width / scale)), 0xff000000, false);
			//Minecraft.getMinecraft().fontRenderer.drawSplitString(text, 0, 0, 1000, 0xff000000);
			GlStateManager.popMatrix();
		}
		
		@Override
		public int updatePos(int posX, int posY, int width, int maxHeight) {
			super.updatePos(posX, posY, width, maxHeight);
			return (int) ((Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * ((int) (Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / (width * scale)) + 1)) * scale);
		}
		
	}
	
	static class ManualCrafting extends ManualContent {
		
		private static final ResourceLocation CRAFTING_GRID_TEXTURE = new ResourceLocation("textures/gui/container/crafting_table.png");
		private static final RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		
		private final ResourceLocation location;
		
		public ManualCrafting(String location) {
			super();
			this.location = new ResourceLocation(location);
		}
		
		@Override
		public int updatePos(int posX, int posY, int width, int maxHeight) {
			super.updatePos(posX, posY, width, maxHeight);
			return 56 + 4;
		}
		
		@Override
		public void draw(int mouseX, int mouseY, float partialTicks, float zLevel, List<String> tooltips) {
			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.pushMatrix();
			GlStateManager.color(1, 1, 1, 1);
			Minecraft.getMinecraft().getTextureManager().bindTexture(CRAFTING_GRID_TEXTURE);
			//int left = (width - 118) / 2;
			RenderHelper.drawTexturedModalRect(this.posX, this.posY, 118, 56, 28, 15, 256, 256, zLevel);
			GlStateManager.popMatrix();
			
			GlStateManager.pushMatrix();
			IRecipe r = CraftingManager.getRecipe(location);
			if (r instanceof ShapedRecipes) drawRecipe((ShapedRecipes) r, this.posX, this.posY);
			else if (r instanceof ShapelessRecipes) drawRecipe((ShapelessRecipes) r, this.posX, this.posY);
			else {
				System.out.println("trying to draw unsupported recipe:");
				System.out.println(r instanceof ShapelessOreRecipe);
				System.out.println(r instanceof ShapedOreRecipe);
				System.out.println(r);
				throw new UnsupportedOperationException("not implemented");
			}
			GlStateManager.popMatrix();
			
			hover(mouseX, mouseY, this.posX, this.posY, zLevel, r, tooltips);


			// needed?
			net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
		}

		private void drawRecipe(ShapelessRecipes r, int posX, int posY) {
			System.out.println("trying to draw shapeless");
			throw new UnsupportedOperationException("not implemented");
		}

		private void drawRecipe(ShapedRecipes r, int posX, int posY) {
			renderItem.renderItemAndEffectIntoGUI(r.getRecipeOutput(), posX + 95 + 1, posY + 19 + 1);
			renderItem.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, r.getRecipeOutput(), posX + 95, posY + 19, "");
			NonNullList<Ingredient> is = r.getIngredients();

			for (int x = 0; x < r.recipeWidth; x++) for (int y = 0; y < r.recipeHeight; y++) {
				final Ingredient i = is.get(y * r.recipeWidth + x);

				if (i.getMatchingStacks().length == 0) continue;
				final ItemStack itemStack = i.getMatchingStacks()[(int) (System.currentTimeMillis() / 2000 % i.getMatchingStacks().length)];
				renderItem.renderItemAndEffectIntoGUI(itemStack, posX + 2 + x * 18, posY + 2 + y * 18);
				renderItem.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, itemStack, posX + 2 + x * 18, posY + 2 + y * 18, "");
			}
		}

		private void hover(int mouseX, int mouseY, int left, int top, float zLevel, IRecipe r, List<String> tooltips) {
			GlStateManager.pushMatrix();
			if (mouseX >= left && mouseY >= top && mouseX <= left + this.width && mouseY <= top + this.maxHeight) {
				final int hoverX = (mouseX - left) / 18;
				final int hoverY = (mouseY - top) / 18;
				int j1 = -1, k1 = -1;
				if (hoverX < 3 && hoverY < 3) {
					j1 = hoverX * 18 + left + 2;
					k1 = hoverY * 18 + top + 2;

					// FIXME this magic 3 should *certainly* not be here; we have to instead somehow check for recipe width or idk
					if(r.getIngredients().size() > hoverY * 3 + hoverX) {
						final ItemStack[] in = r.getIngredients().get(hoverY * 3 + hoverX).getMatchingStacks();
						if (in.length > 0)
							tooltips.addAll(in[(int) (System.currentTimeMillis() / 2000 % in.length)].getTooltip(
									Minecraft.getMinecraft().player,
									Minecraft.getMinecraft().gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL));
					}
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