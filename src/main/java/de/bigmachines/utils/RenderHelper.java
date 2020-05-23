package de.bigmachines.utils;

import java.util.Objects;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fluids.FluidStack;

public class RenderHelper {
	
	public static final ResourceLocation MC_BLOCK_SHEET = new ResourceLocation("textures/atlas/blocks.png");
	
	public static void setBlockTextureSheet() {
		Minecraft.getMinecraft().renderEngine.bindTexture(MC_BLOCK_SHEET);
	}
	
	public static void setColorFromInt(int color) {
		float red   = (float) (color >> 16 & 255) / 255.0F;
		float green = (float) (color >>  8 & 255) / 255.0F;
		float blue  = (float) (color >>  0 & 255) / 255.0F;
		GlStateManager.color(red, green, blue, 1.0F);
	}
	
	public static void setColorFromIntAlpha(int color) {
		float alpha = (float) (color >> 24 & 255) / 255.0F;
		float red   = (float) (color >> 16 & 255) / 255.0F;
		float green = (float) (color >>  8 & 255) / 255.0F;
		float blue  = (float) (color >>  0 & 255) / 255.0F;
		GlStateManager.color(red, green, blue, alpha);
	}
	
	public static TextureAtlasSprite getTexture(String location) {
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location);
	}
	
	public static TextureAtlasSprite getTexture(ResourceLocation location) {
		return getTexture(location.toString());
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
     * @param zLevel depth coordinate
	 */
	public static void drawTexturedModalRect(int x, int y, int width, int height, int u, int v, float texW, float texH, double zLevel) {
		drawSizedTexturedModalRect(x, y, width, height, u, v, (u + width), (v + height), texW, texH, zLevel);
	}
	
	public static void drawTexturedModalRectOnHead(int x, int y, int width, int height, int u, int v, float texW, float texH, double zLevel) {
		GlStateManager.translate(x + width / 2, y + height / 2, zLevel);
		GlStateManager.rotate(180, 0, 0, 1);
		drawSizedTexturedModalRect(- width / 2, + height / 2, width, height, u, v, (u + width), (v + height), texW, texH, zLevel);
		GlStateManager.rotate(180, 0, 0, -1);
		GlStateManager.translate( - (x + width / 2), - (y + height / 2), -zLevel);
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
     * @param zLevel depth coordinate
	 */
	public static void drawSizedTexturedModalRect(int x, int y, int width, int height, float minU, float minV, float maxU, float maxV, float texW, float texH, double zLevel) {
        final float uScale = 1f / texW;
        final float vScale = 1f / texH;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x        , y + height, zLevel).tex(minU * uScale, (maxV * vScale)).endVertex();
        buffer.pos(x + width, y + height, zLevel).tex(maxU * uScale, (maxV * vScale)).endVertex();
        buffer.pos(x + width, y         , zLevel).tex(maxU * uScale, (minV * vScale)).endVertex();
        buffer.pos(x        , y         , zLevel).tex(minU * uScale, (minV * vScale)).endVertex();
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
     * @param zLevel depth coordinate
	 */
	public static void drawTiledTexture(int x, int y, int width, int height, float minU, float minV, float texW, float texH, double zLevel) {
		drawTiledTexture(x, y, width, height, 16, 16, minU, minV, minU + 16, minV + 16, texW, texH, zLevel);
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
     * @param zLevel depth coordinate
	 */
	public static void drawTiledTexture(int x, int y, int width, int height, int drawWidth, int drawHeight, float minU, float minV, float maxU, float maxV, float texW, float texH, double zLevel) {
		int i;
		int j;

		for (i = 0; i < width; i += drawWidth) {
			for (j = 0; j < height; j += drawHeight) {
				drawSizedTexturedModalRect(x + i, y + j, Math.min(width - i, drawWidth), Math.min(height - j, drawHeight), minU, minV, maxU, maxV, texW, texH, zLevel);
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
     * @param zLevel depth coordinate
	 */
	public static void drawTiledTextureIcon(int x, int y, int width, int height, TextureAtlasSprite icon, double zLevel) {
		drawTiledTexture(x, y, width, height, 16, 16, icon.getMinU(), icon.getMinV(), icon.getMaxU(), icon.getMaxV(), 1, 1, zLevel);
	}
	
	public static void drawFluid(int x, int y, FluidStack fluid, int width, int height, double zLevel) {
		if (fluid == null) return;
		
		GL11.glPushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		RenderHelper.setBlockTextureSheet();
		int color = fluid.getFluid().getColor(fluid);
		RenderHelper.setColorFromInt(color);
		drawTiledTextureIcon(x, y, width, height, RenderHelper.getTexture(fluid.getFluid().getStill(fluid)), zLevel);
		GL11.glPopMatrix();
	}
	
	public static Line3D[] getLinesFromCube(AxisAlignedBB cube) {
		Line3D[] lines = new Line3D[] {
			new Line3D(cube.minX, cube.minY, cube.minZ, cube.maxX, cube.minY, cube.minZ),
			new Line3D(cube.minX, cube.minY, cube.minZ, cube.minX, cube.maxY, cube.minZ),
			new Line3D(cube.minX, cube.minY, cube.minZ, cube.minX, cube.minY, cube.maxZ),
			
			new Line3D(cube.maxX, cube.maxY, cube.minZ, cube.minX, cube.maxY, cube.minZ),
			new Line3D(cube.maxX, cube.maxY, cube.minZ, cube.maxX, cube.minY, cube.minZ),
			new Line3D(cube.maxX, cube.maxY, cube.minZ, cube.maxX, cube.maxY, cube.maxZ),
			
			new Line3D(cube.minX, cube.maxY, cube.maxZ, cube.maxX, cube.maxY, cube.maxZ),
			new Line3D(cube.minX, cube.maxY, cube.maxZ, cube.minX, cube.minY, cube.maxZ),
			new Line3D(cube.minX, cube.maxY, cube.maxZ, cube.minX, cube.maxY, cube.minZ),
			
			new Line3D(cube.maxX, cube.minY, cube.maxZ, cube.minX, cube.minY, cube.maxZ),
			new Line3D(cube.maxX, cube.minY, cube.maxZ, cube.maxX, cube.maxY, cube.maxZ),
			new Line3D(cube.maxX, cube.minY, cube.maxZ, cube.maxX, cube.minY, cube.minZ),
		};
		return lines;
	}
	
	/**
	 * Line3D, not directional
	 * @author RythenGlyth
	 *
	 */
	public static class Line3D {
		
		public final double x1, y1, z1, x2, y2, z2;
		
		public Line3D(double x1, double y1, double z1, double x2, double y2, double z2) {
			if(x1 < x2 || (x1 == x2 && (y1 < y2 || (y1 == y2 && z1 < z2)))) {
				this.x1 = x1;
				this.y1 = y1;
				this.z1 = z1;
				this.x2 = x2;
				this.y2 = y2;
				this.z2 = z2;
			} else {
				this.x1 = x2;
				this.y1 = y2;
				this.z1 = z2;
				this.x2 = x1;
				this.y2 = y1;
				this.z2 = z1;
			}
			
		}
		
		@Override
		public String toString() {
			return "Line3D (): [[" + x1 + "," + y1 + "," + z1 + "],[" + x2 + "," + y2 + "," + z2 + "]]";
		}
		
		@Override
		public boolean equals(Object obj) {
			if(this == obj) return true;
			if(obj == null) return false;
			if(obj instanceof Line3D) {
				Line3D other = (Line3D) obj;
				return (this.x1 == other.x1 || this.x1 == other.x2)
						&& (this.y1 == other.y1 || this.y1 == other.y2)
						&& (this.z1 == other.z1 || this.z1 == other.z2)
						&& (this.x2 == other.x2 || this.x2 == other.x1)
						&& (this.y2 == other.y2 || this.y2 == other.y1)
						&& (this.z2 == other.z2 || this.z2 == other.z1);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(x1, y1, z1, x2, y2, z2);
		}
		
		public void addVertices(BufferBuilder bufferbuilder) {
	        bufferbuilder.pos(x1, y1, z1).color(0, 0, 0, 0f).endVertex();
	        bufferbuilder.pos(x2, y2, z2).color(0, 0, 0, 0.4f).endVertex();
		}
		
	}
	
}
