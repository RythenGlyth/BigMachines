package de.bigmachines.utils.classes;

import net.minecraft.util.ResourceLocation;

public class TextureIcon {

	public int minU;
	public int minV;
	public int maxU;
	public int maxV;
	public ResourceLocation texture;
	public int texW;
	public int texH;
	
	public TextureIcon(int minU, int minV, int maxU, int maxV, int texW, int texH, ResourceLocation texture) {
		this.minU = minU;
		this.minV = minV;
		this.maxU = maxU;
		this.maxV = maxV;
		this.texW = texW;
		this.texH = texH;
		this.texture = texture;
	}
	
}
