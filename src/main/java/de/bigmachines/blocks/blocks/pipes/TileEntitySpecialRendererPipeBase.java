package de.bigmachines.blocks.blocks.pipes;

import de.bigmachines.config.Config;
import de.bigmachines.items.items.ItemWrench;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.lwjgl.opengl.GL11;

public class TileEntitySpecialRendererPipeBase extends TileEntitySpecialRenderer<TileEntityPipeBase> {
	
	private final ResourceLocation texture;
	
	private static final double pixelWidth = 1D / 16D;
	private final double pixelTextureWidth;
	
	public TileEntitySpecialRendererPipeBase(final ResourceLocation texture, final double textureWidth) {
		super();
		this.texture = texture;
		pixelTextureWidth = 1D / textureWidth;
	}
	
	@Override
	public void render(final TileEntityPipeBase te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
		//System.out.println(destroyStage);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		
		GlStateManager.disableLighting();
		
		GlStateManager.enableBlend();
		/*if(destroyStage < 0)*/
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		if (te.pass == 0) {
			
			//if(destroyStage >= 0) GlStateManager.blendFunc(GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_DST_ALPHA);
			
			bindTexture(destroyStage >= 0 ? DESTROY_STAGES[destroyStage] : texture);
			
			drawBase(true, !(destroyStage >= 0));
			for (final EnumFacing side : EnumFacing.VALUES) {
				if (te.hasAttachment(side)) drawAttachment(side, te.getAttachment(side), true, !(destroyStage >= 0));
			}
			
		} else {
			drawFluid(te);
		}
		
		GlStateManager.enableLighting();
		
		GlStateManager.disableBlend();
		
		GlStateManager.popMatrix();
	}
	
	public void drawBase(final boolean outside, final boolean inside) {
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		if (outside) {
			//OUTSIDE
			
			//NORTH
			buffer.pos(5 * pixelWidth, 5 * pixelWidth, 5 * pixelWidth).tex(6 * pixelTextureWidth, 12 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 11 * pixelWidth, 5 * pixelWidth).tex(12 * pixelTextureWidth, 12 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 11 * pixelWidth, 5 * pixelWidth).tex(12 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 5 * pixelWidth, 5 * pixelWidth).tex(6 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			
			//WEST
			buffer.pos(5 * pixelWidth, 5 * pixelWidth, 11 * pixelWidth).tex(12 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 11 * pixelWidth, 11 * pixelWidth).tex(18 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 11 * pixelWidth, 5 * pixelWidth).tex(18 * pixelTextureWidth, 12 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 5 * pixelWidth, 5 * pixelWidth).tex(12 * pixelTextureWidth, 12 * pixelTextureWidth).endVertex();
			
			//SOUTH
			buffer.pos(11 * pixelWidth, 5 * pixelWidth, 11 * pixelWidth).tex(18 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 11 * pixelWidth, 11 * pixelWidth).tex(24 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 11 * pixelWidth, 11 * pixelWidth).tex(24 * pixelTextureWidth, 12 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 5 * pixelWidth, 11 * pixelWidth).tex(18 * pixelTextureWidth, 12 * pixelTextureWidth).endVertex();
			
			//EAST
			buffer.pos(11 * pixelWidth, 5 * pixelWidth, 5 * pixelWidth).tex(0 * pixelTextureWidth, 12 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 11 * pixelWidth, 5 * pixelWidth).tex(6 * pixelTextureWidth, 12 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 11 * pixelWidth, 11 * pixelWidth).tex(6 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 5 * pixelWidth, 11 * pixelWidth).tex(0 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			
			//UP
			buffer.pos(5 * pixelWidth, 11 * pixelWidth, 11 * pixelWidth).tex(6 * pixelTextureWidth, 0 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 11 * pixelWidth, 11 * pixelWidth).tex(12 * pixelTextureWidth, 0 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 11 * pixelWidth, 5 * pixelWidth).tex(12 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 11 * pixelWidth, 5 * pixelWidth).tex(6 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			
			//DOWN
			buffer.pos(5 * pixelWidth, 5 * pixelWidth, 5 * pixelWidth).tex(12 * pixelTextureWidth, 0 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 5 * pixelWidth, 5 * pixelWidth).tex(18 * pixelTextureWidth, 0 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 5 * pixelWidth, 11 * pixelWidth).tex(18 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 5 * pixelWidth, 11 * pixelWidth).tex(12 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
		}
		
		if (inside) {
			//INSIDE
			
			//NORTH
			buffer.pos(11 * pixelWidth, 5 * pixelWidth, 6 * pixelWidth).tex(6 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 11 * pixelWidth, 6 * pixelWidth).tex(12 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 11 * pixelWidth, 6 * pixelWidth).tex(12 * pixelTextureWidth, 12 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 5 * pixelWidth, 6 * pixelWidth).tex(6 * pixelTextureWidth, 12 * pixelTextureWidth).endVertex();
			
			//WEST
			buffer.pos(6 * pixelWidth, 5 * pixelWidth, 5 * pixelWidth).tex(12 * pixelTextureWidth, 12 * pixelTextureWidth).endVertex();
			buffer.pos(6 * pixelWidth, 11 * pixelWidth, 5 * pixelWidth).tex(18 * pixelTextureWidth, 12 * pixelTextureWidth).endVertex();
			buffer.pos(6 * pixelWidth, 11 * pixelWidth, 11 * pixelWidth).tex(18 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			buffer.pos(6 * pixelWidth, 5 * pixelWidth, 11 * pixelWidth).tex(12 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			
			//SOUTH
			buffer.pos(5 * pixelWidth, 5 * pixelWidth, 10 * pixelWidth).tex(18 * pixelTextureWidth, 12 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 11 * pixelWidth, 10 * pixelWidth).tex(24 * pixelTextureWidth, 12 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 11 * pixelWidth, 10 * pixelWidth).tex(24 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 5 * pixelWidth, 10 * pixelWidth).tex(18 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			
			//EAST
			buffer.pos(10 * pixelWidth, 5 * pixelWidth, 11 * pixelWidth).tex(0 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			buffer.pos(10 * pixelWidth, 11 * pixelWidth, 11 * pixelWidth).tex(6 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			buffer.pos(10 * pixelWidth, 11 * pixelWidth, 5 * pixelWidth).tex(6 * pixelTextureWidth, 12 * pixelTextureWidth).endVertex();
			buffer.pos(10 * pixelWidth, 5 * pixelWidth, 5 * pixelWidth).tex(0 * pixelTextureWidth, 12 * pixelTextureWidth).endVertex();
			
			//UP
			buffer.pos(5 * pixelWidth, 10 * pixelWidth, 5 * pixelWidth).tex(6 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 10 * pixelWidth, 5 * pixelWidth).tex(12 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 10 * pixelWidth, 11 * pixelWidth).tex(12 * pixelTextureWidth, 0 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 10 * pixelWidth, 11 * pixelWidth).tex(6 * pixelTextureWidth, 0 * pixelTextureWidth).endVertex();
			
			//DOWN
			buffer.pos(5 * pixelWidth, 6 * pixelWidth, 11 * pixelWidth).tex(12 * pixelTextureWidth, 0 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 6 * pixelWidth, 11 * pixelWidth).tex(18 * pixelTextureWidth, 0 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 6 * pixelWidth, 5 * pixelWidth).tex(18 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 6 * pixelWidth, 5 * pixelWidth).tex(12 * pixelTextureWidth, 6 * pixelTextureWidth).endVertex();
		}
		
		tessellator.draw();
	}
	
	public void drawAttachment(final EnumFacing side, final TileEntityPipeBase.PipeAttachment attachment, final boolean outside, final boolean inside) {
		GL11.glPushMatrix();
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder buffer = tessellator.getBuffer();
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
		
		if (outside) {
			//OUTSIDE
			
			//NORTH
			buffer.pos(5 * pixelWidth, 11 * pixelWidth, 5 * pixelWidth).tex(12 * pixelTextureWidth, 21 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 16 * pixelWidth, 5 * pixelWidth).tex(12 * pixelTextureWidth, 16 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 16 * pixelWidth, 5 * pixelWidth).tex(6 * pixelTextureWidth, 16 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 11 * pixelWidth, 5 * pixelWidth).tex(6 * pixelTextureWidth, 21 * pixelTextureWidth).endVertex();
			
			//WEST
			buffer.pos(5 * pixelWidth, 11 * pixelWidth, 11 * pixelWidth).tex(18 * pixelTextureWidth, 21 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 16 * pixelWidth, 11 * pixelWidth).tex(18 * pixelTextureWidth, 16 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 16 * pixelWidth, 5 * pixelWidth).tex(12 * pixelTextureWidth, 16 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 11 * pixelWidth, 5 * pixelWidth).tex(12 * pixelTextureWidth, 21 * pixelTextureWidth).endVertex();
			
			//SOUTH
			buffer.pos(11 * pixelWidth, 11 * pixelWidth, 11 * pixelWidth).tex(24 * pixelTextureWidth, 21 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 16 * pixelWidth, 11 * pixelWidth).tex(24 * pixelTextureWidth, 16 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 16 * pixelWidth, 11 * pixelWidth).tex(18 * pixelTextureWidth, 16 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 11 * pixelWidth, 11 * pixelWidth).tex(18 * pixelTextureWidth, 21 * pixelTextureWidth).endVertex();
			
			//EAST
			buffer.pos(11 * pixelWidth, 11 * pixelWidth, 5 * pixelWidth).tex(6 * pixelTextureWidth, 21 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 16 * pixelWidth, 5 * pixelWidth).tex(6 * pixelTextureWidth, 16 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 16 * pixelWidth, 11 * pixelWidth).tex(0 * pixelTextureWidth, 16 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 11 * pixelWidth, 11 * pixelWidth).tex(0 * pixelTextureWidth, 21 * pixelTextureWidth).endVertex();
		}
		
		if (inside) {
			//INSIDE
			
			//NORTH
			buffer.pos(11 * pixelWidth, 11 * pixelWidth, 6 * pixelWidth).tex(6 * pixelTextureWidth, 21 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 16 * pixelWidth, 6 * pixelWidth).tex(6 * pixelTextureWidth, 16 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 16 * pixelWidth, 6 * pixelWidth).tex(12 * pixelTextureWidth, 16 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 11 * pixelWidth, 6 * pixelWidth).tex(12 * pixelTextureWidth, 21 * pixelTextureWidth).endVertex();
			
			//WEST
			buffer.pos(6 * pixelWidth, 11 * pixelWidth, 5 * pixelWidth).tex(12 * pixelTextureWidth, 21 * pixelTextureWidth).endVertex();
			buffer.pos(6 * pixelWidth, 16 * pixelWidth, 5 * pixelWidth).tex(12 * pixelTextureWidth, 16 * pixelTextureWidth).endVertex();
			buffer.pos(6 * pixelWidth, 16 * pixelWidth, 11 * pixelWidth).tex(18 * pixelTextureWidth, 16 * pixelTextureWidth).endVertex();
			buffer.pos(6 * pixelWidth, 11 * pixelWidth, 11 * pixelWidth).tex(18 * pixelTextureWidth, 21 * pixelTextureWidth).endVertex();
			
			//SOUTH
			buffer.pos(5 * pixelWidth, 11 * pixelWidth, 10 * pixelWidth).tex(18 * pixelTextureWidth, 21 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 16 * pixelWidth, 10 * pixelWidth).tex(18 * pixelTextureWidth, 16 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 16 * pixelWidth, 10 * pixelWidth).tex(24 * pixelTextureWidth, 16 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 11 * pixelWidth, 10 * pixelWidth).tex(24 * pixelTextureWidth, 21 * pixelTextureWidth).endVertex();
			
			//EAST
			buffer.pos(10 * pixelWidth, 11 * pixelWidth, 11 * pixelWidth).tex(0 * pixelTextureWidth, 21 * pixelTextureWidth).endVertex();
			buffer.pos(10 * pixelWidth, 16 * pixelWidth, 11 * pixelWidth).tex(0 * pixelTextureWidth, 16 * pixelTextureWidth).endVertex();
			buffer.pos(10 * pixelWidth, 16 * pixelWidth, 5 * pixelWidth).tex(6 * pixelTextureWidth, 16 * pixelTextureWidth).endVertex();
			buffer.pos(10 * pixelWidth, 11 * pixelWidth, 5 * pixelWidth).tex(6 * pixelTextureWidth, 21 * pixelTextureWidth).endVertex();
		}
		
		//System.out.println(Config.pipeStatusOverlayAlwaysOn);
		
		if ((Config.isPipeStatusOverlayAlwaysOn() || (Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemWrench)) && !(attachment.canExtract() && attachment.canInsert())) {
			final double statusOverlayU = attachment.canExtract() ? 0 : (attachment.canInsert() ? 6 * pixelTextureWidth :
					  12 * pixelTextureWidth);
			
			//OUTSIDE STATUS
			
			//NORTH
			buffer.pos(5 * pixelWidth, 11 * pixelWidth, 5 * pixelWidth).tex(statusOverlayU + 6 * pixelTextureWidth, 29 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 16 * pixelWidth, 5 * pixelWidth).tex(statusOverlayU + 6 * pixelTextureWidth, 24 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 16 * pixelWidth, 5 * pixelWidth).tex(statusOverlayU, 24 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 11 * pixelWidth, 5 * pixelWidth).tex(statusOverlayU, 29 * pixelTextureWidth).endVertex();
			
			//WEST
			buffer.pos(5 * pixelWidth, 11 * pixelWidth, 11 * pixelWidth).tex(statusOverlayU + 6 * pixelTextureWidth, 29 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 16 * pixelWidth, 11 * pixelWidth).tex(statusOverlayU + 6 * pixelTextureWidth, 24 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 16 * pixelWidth, 5 * pixelWidth).tex(statusOverlayU, 24 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 11 * pixelWidth, 5 * pixelWidth).tex(statusOverlayU, 29 * pixelTextureWidth).endVertex();
			
			//SOUTH
			buffer.pos(11 * pixelWidth, 11 * pixelWidth, 11 * pixelWidth).tex(statusOverlayU + 6 * pixelTextureWidth, 29 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 16 * pixelWidth, 11 * pixelWidth).tex(statusOverlayU + 6 * pixelTextureWidth, 24 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 16 * pixelWidth, 11 * pixelWidth).tex(statusOverlayU, 24 * pixelTextureWidth).endVertex();
			buffer.pos(5 * pixelWidth, 11 * pixelWidth, 11 * pixelWidth).tex(statusOverlayU, 29 * pixelTextureWidth).endVertex();
			
			//EAST
			buffer.pos(11 * pixelWidth, 11 * pixelWidth, 5 * pixelWidth).tex(statusOverlayU + 6 * pixelTextureWidth, 29 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 16 * pixelWidth, 5 * pixelWidth).tex(statusOverlayU + 6 * pixelTextureWidth, 24 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 16 * pixelWidth, 11 * pixelWidth).tex(statusOverlayU, 24 * pixelTextureWidth).endVertex();
			buffer.pos(11 * pixelWidth, 11 * pixelWidth, 11 * pixelWidth).tex(statusOverlayU, 29 * pixelTextureWidth).endVertex();
			
		}
		
		tessellator.draw();
		GL11.glPopMatrix();
	}
	
	public void drawFluid(final TileEntityPipeBase te) {
		bindTexture(new ResourceLocation("textures/atlas/blocks.png"));
		
		if (te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
			if (te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).getTankProperties() == null ||
					  te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).getTankProperties().length == 0) return;
			final IFluidTankProperties property = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).getTankProperties()[0];
			if (property.getContents() != null) {
				// only on client
				final double size = property.getContents().amount / (double) property.getCapacity();
				
				final TextureAtlasSprite fluidTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(property.getContents().getFluid().getStill().toString());
				
				drawBaseFluid(fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
				if (te.hasAttachment(EnumFacing.DOWN))
					drawAttachmentFluid(EnumFacing.DOWN, fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
				if (te.hasAttachment(EnumFacing.EAST))
					drawAttachmentFluid(EnumFacing.EAST, fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
				if (te.hasAttachment(EnumFacing.NORTH))
					drawAttachmentFluid(EnumFacing.NORTH, fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
				if (te.hasAttachment(EnumFacing.SOUTH))
					drawAttachmentFluid(EnumFacing.SOUTH, fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
				if (te.hasAttachment(EnumFacing.UP))
					drawAttachmentFluid(EnumFacing.UP, fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
				if (te.hasAttachment(EnumFacing.WEST))
					drawAttachmentFluid(EnumFacing.WEST, fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
			}
		}
		
	}
	
	public void drawBaseFluid(final double minU, final double minV, final double maxU, final double maxV) {
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder buffer = tessellator.getBuffer();
		
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		//OUTSIDE
		
		//NORTH
		buffer.pos(5.5 * pixelWidth, 5.5 * pixelWidth, 5.5 * pixelWidth).tex(minU, maxV).endVertex();
		buffer.pos(5.5 * pixelWidth, 10.5 * pixelWidth, 5.5 * pixelWidth).tex(maxU, maxV).endVertex();
		buffer.pos(10.5 * pixelWidth, 10.5 * pixelWidth, 5.5 * pixelWidth).tex(maxU, minV).endVertex();
		buffer.pos(10.5 * pixelWidth, 5.5 * pixelWidth, 5.5 * pixelWidth).tex(minU, minV).endVertex();
		
		//WEST
		buffer.pos(5.5 * pixelWidth, 5.5 * pixelWidth, 10.5 * pixelWidth).tex(minU, maxV).endVertex();
		buffer.pos(5.5 * pixelWidth, 10.5 * pixelWidth, 10.5 * pixelWidth).tex(maxU, maxV).endVertex();
		buffer.pos(5.5 * pixelWidth, 10.5 * pixelWidth, 5.5 * pixelWidth).tex(maxU, minV).endVertex();
		buffer.pos(5.5 * pixelWidth, 5.5 * pixelWidth, 5.5 * pixelWidth).tex(minU, minV).endVertex();
		
		//SOUTH
		buffer.pos(10.5 * pixelWidth, 5.5 * pixelWidth, 10.5 * pixelWidth).tex(minU, minV).endVertex();
		buffer.pos(10.5 * pixelWidth, 10.5 * pixelWidth, 10.5 * pixelWidth).tex(maxU, minV).endVertex();
		buffer.pos(5.5 * pixelWidth, 10.5 * pixelWidth, 10.5 * pixelWidth).tex(maxU, maxV).endVertex();
		buffer.pos(5.5 * pixelWidth, 5.5 * pixelWidth, 10.5 * pixelWidth).tex(minU, maxV).endVertex();
		
		//EAST
		buffer.pos(10.5 * pixelWidth, 5.5 * pixelWidth, 5.5 * pixelWidth).tex(minU, maxV).endVertex();
		buffer.pos(10.5 * pixelWidth, 10.5 * pixelWidth, 5.5 * pixelWidth).tex(maxU, maxV).endVertex();
		buffer.pos(10.5 * pixelWidth, 10.5 * pixelWidth, 10.5 * pixelWidth).tex(maxU, minV).endVertex();
		buffer.pos(10.5 * pixelWidth, 5.5 * pixelWidth, 10.5 * pixelWidth).tex(minU, minV).endVertex();
		
		//UP
		buffer.pos(5.5 * pixelWidth, 10.5 * pixelWidth, 10.5 * pixelWidth).tex(minU, minV).endVertex();
		buffer.pos(10.5 * pixelWidth, 10.5 * pixelWidth, 10.5 * pixelWidth).tex(maxU, minV).endVertex();
		buffer.pos(10.5 * pixelWidth, 10.5 * pixelWidth, 5.5 * pixelWidth).tex(maxU, maxV).endVertex();
		buffer.pos(5.5 * pixelWidth, 10.5 * pixelWidth, 5.5 * pixelWidth).tex(minU, maxV).endVertex();
		
		//DOWN
		buffer.pos(5.5 * pixelWidth, 5.5 * pixelWidth, 5.5 * pixelWidth).tex(minU, minV).endVertex();
		buffer.pos(10.5 * pixelWidth, 5.5 * pixelWidth, 5.5 * pixelWidth).tex(maxU, minV).endVertex();
		buffer.pos(10.5 * pixelWidth, 5.5 * pixelWidth, 10.5 * pixelWidth).tex(maxU, maxV).endVertex();
		buffer.pos(5.5 * pixelWidth, 5.5 * pixelWidth, 10.5 * pixelWidth).tex(minU, maxV).endVertex();
		
		tessellator.draw();
	}
	
	public void drawAttachmentFluid(final EnumFacing side, final double minU, final double minV, final double maxU, final double maxV) {
		GL11.glPushMatrix();
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder buffer = tessellator.getBuffer();
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
		buffer.pos(5.5 * pixelWidth, 10.5 * pixelWidth, 5.5 * pixelWidth).tex(maxU, maxV).endVertex();
		buffer.pos(5.5 * pixelWidth, 16 * pixelWidth, 5.5 * pixelWidth).tex(maxU, minV).endVertex();
		buffer.pos(10.5 * pixelWidth, 16 * pixelWidth, 5.5 * pixelWidth).tex(minU, minV).endVertex();
		buffer.pos(10.5 * pixelWidth, 10.5 * pixelWidth, 5.5 * pixelWidth).tex(minU, maxV).endVertex();
		
		//WEST
		buffer.pos(5.5 * pixelWidth, 10.5 * pixelWidth, 10.5 * pixelWidth).tex(maxU, maxV).endVertex();
		buffer.pos(5.5 * pixelWidth, 16 * pixelWidth, 10.5 * pixelWidth).tex(maxU, minV).endVertex();
		buffer.pos(5.5 * pixelWidth, 16 * pixelWidth, 5.5 * pixelWidth).tex(minU, minV).endVertex();
		buffer.pos(5.5 * pixelWidth, 10.5 * pixelWidth, 5.5 * pixelWidth).tex(minU, maxV).endVertex();
		
		//SOUTH
		buffer.pos(10.5 * pixelWidth, 10.5 * pixelWidth, 10.5 * pixelWidth).tex(maxU, maxV).endVertex();
		buffer.pos(10.5 * pixelWidth, 16 * pixelWidth, 10.5 * pixelWidth).tex(maxU, minV).endVertex();
		buffer.pos(5.5 * pixelWidth, 16 * pixelWidth, 10.5 * pixelWidth).tex(minU, minV).endVertex();
		buffer.pos(5.5 * pixelWidth, 10.5 * pixelWidth, 10.5 * pixelWidth).tex(minU, maxV).endVertex();
		
		//EAST
		buffer.pos(10.5 * pixelWidth, 10.5 * pixelWidth, 5.5 * pixelWidth).tex(maxU, maxV).endVertex();
		buffer.pos(10.5 * pixelWidth, 16 * pixelWidth, 5.5 * pixelWidth).tex(maxU, minV).endVertex();
		buffer.pos(10.5 * pixelWidth, 16 * pixelWidth, 10.5 * pixelWidth).tex(minU, minV).endVertex();
		buffer.pos(10.5 * pixelWidth, 10.5 * pixelWidth, 10.5 * pixelWidth).tex(minU, maxV).endVertex();
		
		//UP
		buffer.pos(10.5 * pixelWidth, 10.5 * pixelWidth, 5.5 * pixelWidth).tex(maxU, maxV).endVertex();
		buffer.pos(10.5 * pixelWidth, 16 * pixelWidth, 5.5 * pixelWidth).tex(maxU, minV).endVertex();
		buffer.pos(10.5 * pixelWidth, 16 * pixelWidth, 10.5 * pixelWidth).tex(minU, minV).endVertex();
		buffer.pos(10.5 * pixelWidth, 10.5 * pixelWidth, 10.5 * pixelWidth).tex(minU, maxV).endVertex();
		
		//UP
		buffer.pos(5.5 * pixelWidth, 16 * pixelWidth, 10.5 * pixelWidth).tex(minU, minV).endVertex();
		buffer.pos(10.5 * pixelWidth, 16 * pixelWidth, 10.5 * pixelWidth).tex(maxU, minV).endVertex();
		buffer.pos(10.5 * pixelWidth, 16 * pixelWidth, 5.5 * pixelWidth).tex(maxU, maxV).endVertex();
		buffer.pos(5.5 * pixelWidth, 16 * pixelWidth, 5.5 * pixelWidth).tex(minU, maxV).endVertex();
		
		tessellator.draw();
		GL11.glPopMatrix();
	}
	
}
