package de.bigmachines.utils;

import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;

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
		float red   = (float) (color >> 24 & 255) / 255.0F;
		float green = (float) (color >> 16 & 255) / 255.0F;
		float blue  = (float) (color >>  8 & 255) / 255.0F;
		float alpha = (float) (color >>  0 & 255) / 255.0F;
		GlStateManager.color(red, green, blue, alpha);
	}
	
	public static TextureAtlasSprite getTexture(String location) {
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location);
	}
	
	public static TextureAtlasSprite getTexture(ResourceLocation location) {
		return getTexture(location.toString());
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
