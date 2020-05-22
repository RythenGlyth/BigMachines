package de.bigmachines.blocks.blocks.pipes;

import org.lwjgl.opengl.GL11;

import de.bigmachines.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileEntitySpecialRendererPipeBase extends TileEntitySpecialRenderer<TileEntityPipeBase> {
	
	private ResourceLocation texture;
	
	private double textureWidth;
	private static double pixelWitdh = 1D / 16D;
	private double pixelTextureWitdh;
	
	public TileEntitySpecialRendererPipeBase(ResourceLocation texture, double textureWidth) {
		this.texture = texture;
		this.textureWidth = textureWidth;
		this.pixelTextureWitdh = 1D / textureWidth;
	}
	
	@Override
	public void render(TileEntityPipeBase te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		
		GlStateManager.disableLighting();
		
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		if(te.pass == 0) {
			bindTexture(texture);
			
			drawBase();
			if(te.hasAttachment(EnumFacing.DOWN)) drawAttachment(EnumFacing.DOWN);
			if(te.hasAttachment(EnumFacing.EAST)) drawAttachment(EnumFacing.EAST);
			if(te.hasAttachment(EnumFacing.NORTH)) drawAttachment(EnumFacing.NORTH);
			if(te.hasAttachment(EnumFacing.SOUTH)) drawAttachment(EnumFacing.SOUTH);
			if(te.hasAttachment(EnumFacing.UP)) drawAttachment(EnumFacing.UP);
			if(te.hasAttachment(EnumFacing.WEST)) drawAttachment(EnumFacing.WEST);
		} else {
			drawFluid(new FluidStack(FluidRegistry.WATER, 1), te);
		}
		
		
		GlStateManager.enableLighting();
		
		GlStateManager.disableBlend();
		
		GlStateManager.popMatrix();
	}
	
	public void drawBase() {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		//OUTSIDE
		
		//NORTH
		buffer.pos(5 * pixelWitdh, 5 * pixelWitdh, 5 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 12 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 12 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 5 * pixelWitdh, 5 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();

		//WEST
		buffer.pos(5 * pixelWitdh, 5 * pixelWitdh, 11 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 12 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 5 * pixelWitdh, 5 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 12 * this.pixelTextureWitdh).endVertex();

		//SOUTH
		buffer.pos(11 * pixelWitdh, 5 * pixelWitdh, 11 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(24 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(24 * this.pixelTextureWitdh, 12 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 5 * pixelWitdh, 11 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 12 * this.pixelTextureWitdh).endVertex();

		//EAST
		buffer.pos(11 * pixelWitdh, 5 * pixelWitdh, 5 * pixelWitdh).tex(0 * this.pixelTextureWitdh, 12 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 12 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 5 * pixelWitdh, 11 * pixelWitdh).tex(0 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();

		//UP
		buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 0 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 0 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();

		//DOWN
		buffer.pos(5 * pixelWitdh, 5 * pixelWitdh, 5 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 0 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 5 * pixelWitdh, 5 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 0 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 5 * pixelWitdh, 11 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 5 * pixelWitdh, 11 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		
		//INSIDE
		
		//NORTH
		buffer.pos(11 * pixelWitdh, 5 * pixelWitdh, 6 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 6 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 6 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 12 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 5 * pixelWitdh, 6 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 12 * this.pixelTextureWitdh).endVertex();

		//WEST
		buffer.pos(6 * pixelWitdh, 5 * pixelWitdh, 5 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 12 * this.pixelTextureWitdh).endVertex();
		buffer.pos(6 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 12 * this.pixelTextureWitdh).endVertex();
		buffer.pos(6 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		buffer.pos(6 * pixelWitdh, 5 * pixelWitdh, 11 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();

		//SOUTH
		buffer.pos(5 * pixelWitdh, 5 * pixelWitdh, 10 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 12 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 10 * pixelWitdh).tex(24 * this.pixelTextureWitdh, 12 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 10 * pixelWitdh).tex(24 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 5 * pixelWitdh, 10 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();

		//EAST
		buffer.pos(10 * pixelWitdh, 5 * pixelWitdh, 11 * pixelWitdh).tex(0 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		buffer.pos(10 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		buffer.pos(10 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 12 * this.pixelTextureWitdh).endVertex();
		buffer.pos(10 * pixelWitdh, 5 * pixelWitdh, 5 * pixelWitdh).tex(0 * this.pixelTextureWitdh, 12 * this.pixelTextureWitdh).endVertex();

		//UP
		buffer.pos(5 * pixelWitdh, 10 * pixelWitdh, 5 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 10 * pixelWitdh, 5 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 10 * pixelWitdh, 11 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 0 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 10 * pixelWitdh, 11 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 0 * this.pixelTextureWitdh).endVertex();

		//DOWN
		buffer.pos(5 * pixelWitdh, 6 * pixelWitdh, 11 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 0 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 6 * pixelWitdh, 11 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 0 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 6 * pixelWitdh, 5 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 6 * pixelWitdh, 5 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 6 * this.pixelTextureWitdh).endVertex();
		
		tessellator.draw();
	}
	
	public void drawAttachment(EnumFacing side) {
		GL11.glPushMatrix();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		GL11.glTranslated(0.5, 0.5, 0.5);
		switch (side) {
			case NORTH:
				GL11.glRotated(90, -1, 0, 0);
				break;
			case WEST:
				GL11.glRotated(90, 0, 0, 1);
				break;
			case SOUTH:
				GL11.glRotated(90, 1, 0, 0);
				break;
			case EAST:
				GL11.glRotated(90, 0, 0, -1);
				break;
			case DOWN:
				GL11.glRotated(180, 1, 0, 0);
				break;
			case UP:
				break;
		}
		
		GL11.glTranslated(-0.5, -0.5, -0.5);
		
		//OUTSIDE
		
		//NORTH
		buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 21 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 16 * pixelWitdh, 5 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 16 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 16 * pixelWitdh, 5 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 16 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 21 * this.pixelTextureWitdh).endVertex();

		//WEST
		buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 21 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 16 * pixelWitdh, 11 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 16 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 16 * pixelWitdh, 5 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 16 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 21 * this.pixelTextureWitdh).endVertex();

		//SOUTH
		buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(24 * this.pixelTextureWitdh, 21 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 16 * pixelWitdh, 11 * pixelWitdh).tex(24 * this.pixelTextureWitdh, 16 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 16 * pixelWitdh, 11 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 16 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 21 * this.pixelTextureWitdh).endVertex();

		//EAST
		buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 21 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 16 * pixelWitdh, 5 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 16 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 16 * pixelWitdh, 11 * pixelWitdh).tex(0 * this.pixelTextureWitdh, 16 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(0 * this.pixelTextureWitdh, 21 * this.pixelTextureWitdh).endVertex();
		
		
		//INSIDE
		
		//NORTH
		buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 6 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 21 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 16 * pixelWitdh, 6 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 16 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 16 * pixelWitdh, 6 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 16 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 6 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 21 * this.pixelTextureWitdh).endVertex();

		//WEST
		buffer.pos(6 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 21 * this.pixelTextureWitdh).endVertex();
		buffer.pos(6 * pixelWitdh, 16 * pixelWitdh, 5 * pixelWitdh).tex(12 * this.pixelTextureWitdh, 16 * this.pixelTextureWitdh).endVertex();
		buffer.pos(6 * pixelWitdh, 16 * pixelWitdh, 11 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 16 * this.pixelTextureWitdh).endVertex();
		buffer.pos(6 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 21 * this.pixelTextureWitdh).endVertex();

		//SOUTH
		buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 10 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 21 * this.pixelTextureWitdh).endVertex();
		buffer.pos(5 * pixelWitdh, 16 * pixelWitdh, 10 * pixelWitdh).tex(18 * this.pixelTextureWitdh, 16 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 16 * pixelWitdh, 10 * pixelWitdh).tex(24 * this.pixelTextureWitdh, 16 * this.pixelTextureWitdh).endVertex();
		buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 10 * pixelWitdh).tex(24 * this.pixelTextureWitdh, 21 * this.pixelTextureWitdh).endVertex();

		//EAST
		buffer.pos(10 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(0 * this.pixelTextureWitdh, 21 * this.pixelTextureWitdh).endVertex();
		buffer.pos(10 * pixelWitdh, 16 * pixelWitdh, 11 * pixelWitdh).tex(0 * this.pixelTextureWitdh, 16 * this.pixelTextureWitdh).endVertex();
		buffer.pos(10 * pixelWitdh, 16 * pixelWitdh, 5 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 16 * this.pixelTextureWitdh).endVertex();
		buffer.pos(10 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(6 * this.pixelTextureWitdh, 21 * this.pixelTextureWitdh).endVertex();
		
		tessellator.draw();
		GL11.glPopMatrix();
	}
	
	public void drawFluid(FluidStack stack, TileEntityPipeBase te) {
		bindTexture(new ResourceLocation("textures/atlas/blocks.png"));
		TextureAtlasSprite fluidTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(stack.getFluid().getStill().toString());
		
		drawBaseFluid(fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
		if(te.hasAttachment(EnumFacing.DOWN)) drawAttachmentFluid(EnumFacing.DOWN, fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
		if(te.hasAttachment(EnumFacing.EAST)) drawAttachmentFluid(EnumFacing.EAST, fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
		if(te.hasAttachment(EnumFacing.NORTH)) drawAttachmentFluid(EnumFacing.NORTH, fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
		if(te.hasAttachment(EnumFacing.SOUTH)) drawAttachmentFluid(EnumFacing.SOUTH, fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
		if(te.hasAttachment(EnumFacing.UP)) drawAttachmentFluid(EnumFacing.UP, fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
		if(te.hasAttachment(EnumFacing.WEST)) drawAttachmentFluid(EnumFacing.WEST, fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
		
	}
	

	
	public void drawBaseFluid(double minU, double minV, double maxU, double maxV) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		//OUTSIDE
		
		//NORTH
		buffer.pos(5.5 * pixelWitdh, 5.5 * pixelWitdh, 5.5 * pixelWitdh).tex(minU, maxV).endVertex();
		buffer.pos(5.5 * pixelWitdh, 10.5 * pixelWitdh, 5.5 * pixelWitdh).tex(maxU, maxV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 10.5 * pixelWitdh, 5.5 * pixelWitdh).tex(maxU, minV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 5.5 * pixelWitdh, 5.5 * pixelWitdh).tex(minU, minV).endVertex();

		//WEST
		buffer.pos(5.5 * pixelWitdh, 5.5 * pixelWitdh, 10.5 * pixelWitdh).tex(minU, maxV).endVertex();
		buffer.pos(5.5 * pixelWitdh, 10.5 * pixelWitdh, 10.5 * pixelWitdh).tex(maxU, maxV).endVertex();
		buffer.pos(5.5 * pixelWitdh, 10.5 * pixelWitdh, 5.5 * pixelWitdh).tex(maxU, minV).endVertex();
		buffer.pos(5.5 * pixelWitdh, 5.5 * pixelWitdh, 5.5 * pixelWitdh).tex(minU, minV).endVertex();

		//SOUTH
		buffer.pos(10.5 * pixelWitdh, 5.5 * pixelWitdh, 10.5 * pixelWitdh).tex(minU, minV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 10.5 * pixelWitdh, 10.5 * pixelWitdh).tex(maxU, minV).endVertex();
		buffer.pos(5.5 * pixelWitdh, 10.5 * pixelWitdh, 10.5 * pixelWitdh).tex(maxU, maxV).endVertex();
		buffer.pos(5.5 * pixelWitdh, 5.5 * pixelWitdh, 10.5 * pixelWitdh).tex(minU, maxV).endVertex();

		//EAST
		buffer.pos(10.5 * pixelWitdh, 5.5 * pixelWitdh, 5.5 * pixelWitdh).tex(minU, maxV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 10.5 * pixelWitdh, 5.5 * pixelWitdh).tex(maxU, maxV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 10.5 * pixelWitdh, 10.5 * pixelWitdh).tex(maxU, minV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 5.5 * pixelWitdh, 10.5 * pixelWitdh).tex(minU, minV).endVertex();

		//UP
		buffer.pos(5.5 * pixelWitdh, 10.5 * pixelWitdh, 10.5 * pixelWitdh).tex(minU, minV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 10.5 * pixelWitdh, 10.5 * pixelWitdh).tex(maxU, minV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 10.5 * pixelWitdh, 5.5 * pixelWitdh).tex(maxU, maxV).endVertex();
		buffer.pos(5.5 * pixelWitdh, 10.5 * pixelWitdh, 5.5 * pixelWitdh).tex(minU, maxV).endVertex();

		//DOWN
		buffer.pos(5.5 * pixelWitdh, 5.5 * pixelWitdh, 5.5 * pixelWitdh).tex(minU, minV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 5.5 * pixelWitdh, 5.5 * pixelWitdh).tex(maxU, minV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 5.5 * pixelWitdh, 10.5 * pixelWitdh).tex(maxU, maxV).endVertex();
		buffer.pos(5.5 * pixelWitdh, 5.5 * pixelWitdh, 10.5 * pixelWitdh).tex(minU, maxV).endVertex();
		
		tessellator.draw();
	}
	
	public void drawAttachmentFluid(EnumFacing side, double minU, double minV, double maxU, double maxV) {
		GL11.glPushMatrix();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		GL11.glTranslated(0.5, 0.5, 0.5);
		switch (side) {
			case NORTH:
				GL11.glRotated(90, -1, 0, 0);
				break;
			case WEST:
				GL11.glRotated(90, 0, 0, 1);
				break;
			case SOUTH:
				GL11.glRotated(90, 1, 0, 0);
				break;
			case EAST:
				GL11.glRotated(90, 0, 0, -1);
				break;
			case DOWN:
				GL11.glRotated(180, 1, 0, 0);
				break;
			case UP:
				break;
		}
		
		GL11.glTranslated(-0.5, -0.5, -0.5);
		
		//OUTSIDE
		
		//NORTH
		buffer.pos(5.5 * pixelWitdh, 10.5 * pixelWitdh, 5.5 * pixelWitdh).tex(maxU, maxV).endVertex();
		buffer.pos(5.5 * pixelWitdh, 16 * pixelWitdh, 5.5 * pixelWitdh).tex(maxU, minV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 16 * pixelWitdh, 5.5 * pixelWitdh).tex(minU, minV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 10.5 * pixelWitdh, 5.5 * pixelWitdh).tex(minU, maxV).endVertex();

		//WEST
		buffer.pos(5.5 * pixelWitdh, 10.5 * pixelWitdh, 10.5 * pixelWitdh).tex(maxU, maxV).endVertex();
		buffer.pos(5.5 * pixelWitdh, 16 * pixelWitdh, 10.5 * pixelWitdh).tex(maxU, minV).endVertex();
		buffer.pos(5.5 * pixelWitdh, 16 * pixelWitdh, 5.5 * pixelWitdh).tex(minU, minV).endVertex();
		buffer.pos(5.5 * pixelWitdh, 10.5 * pixelWitdh, 5.5 * pixelWitdh).tex(minU, maxV).endVertex();

		//SOUTH
		buffer.pos(10.5 * pixelWitdh, 10.5 * pixelWitdh, 10.5 * pixelWitdh).tex(maxU, maxV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 16 * pixelWitdh, 10.5 * pixelWitdh).tex(maxU, minV).endVertex();
		buffer.pos(5.5 * pixelWitdh, 16 * pixelWitdh, 10.5 * pixelWitdh).tex(minU, minV).endVertex();
		buffer.pos(5.5 * pixelWitdh, 10.5 * pixelWitdh, 10.5 * pixelWitdh).tex(minU, maxV).endVertex();

		//EAST
		buffer.pos(10.5 * pixelWitdh, 10.5 * pixelWitdh, 5.5 * pixelWitdh).tex(maxU, maxV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 16 * pixelWitdh, 5.5 * pixelWitdh).tex(maxU, minV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 16 * pixelWitdh, 10.5 * pixelWitdh).tex(minU, minV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 10.5 * pixelWitdh, 10.5 * pixelWitdh).tex(minU, maxV).endVertex();
		
		tessellator.draw();
		GL11.glPopMatrix();
	}
	
}
