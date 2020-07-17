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

	private static final double pixelWitdh = 1D / 16D;
	private final double pixelTextureWitdh;
	
	public TileEntitySpecialRendererPipeBase(final ResourceLocation texture, final double textureWidth) {
        this.texture = texture;
		pixelTextureWitdh = 1D / textureWidth;
	}
	
	@Override
	public void render(final TileEntityPipeBase te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
		//System.out.println(destroyStage);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		
		GlStateManager.disableLighting();
        
		GlStateManager.enableBlend();
		/*if(destroyStage < 0)*/ GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
		
		if(te.pass == 0) {
			
			//if(destroyStage >= 0) GlStateManager.blendFunc(GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_DST_ALPHA);
			
			bindTexture(destroyStage >= 0 ? DESTROY_STAGES[destroyStage] : texture);
			
			drawBase(true, !(destroyStage >= 0));
			for(final EnumFacing side : EnumFacing.VALUES) {
				if(te.hasAttachment(side)) drawAttachment(side, te.getAttachment(side), true, !(destroyStage >= 0));
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

		if(outside) {
			//OUTSIDE
			
			//NORTH
			buffer.pos(5 * pixelWitdh, 5 * pixelWitdh, 5 * pixelWitdh).tex(6 * pixelTextureWitdh, 12 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(12 * pixelTextureWitdh, 12 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(12 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 5 * pixelWitdh, 5 * pixelWitdh).tex(6 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
	
			//WEST
			buffer.pos(5 * pixelWitdh, 5 * pixelWitdh, 11 * pixelWitdh).tex(12 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(18 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(18 * pixelTextureWitdh, 12 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 5 * pixelWitdh, 5 * pixelWitdh).tex(12 * pixelTextureWitdh, 12 * pixelTextureWitdh).endVertex();
	
			//SOUTH
			buffer.pos(11 * pixelWitdh, 5 * pixelWitdh, 11 * pixelWitdh).tex(18 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(24 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(24 * pixelTextureWitdh, 12 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 5 * pixelWitdh, 11 * pixelWitdh).tex(18 * pixelTextureWitdh, 12 * pixelTextureWitdh).endVertex();
	
			//EAST
			buffer.pos(11 * pixelWitdh, 5 * pixelWitdh, 5 * pixelWitdh).tex(0 * pixelTextureWitdh, 12 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(6 * pixelTextureWitdh, 12 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(6 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 5 * pixelWitdh, 11 * pixelWitdh).tex(0 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
	
			//UP
			buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(6 * pixelTextureWitdh, 0 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(12 * pixelTextureWitdh, 0 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(12 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(6 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
	
			//DOWN
			buffer.pos(5 * pixelWitdh, 5 * pixelWitdh, 5 * pixelWitdh).tex(12 * pixelTextureWitdh, 0 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 5 * pixelWitdh, 5 * pixelWitdh).tex(18 * pixelTextureWitdh, 0 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 5 * pixelWitdh, 11 * pixelWitdh).tex(18 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 5 * pixelWitdh, 11 * pixelWitdh).tex(12 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
		}
		
		
		if(inside) {
			//INSIDE
			
			//NORTH
			buffer.pos(11 * pixelWitdh, 5 * pixelWitdh, 6 * pixelWitdh).tex(6 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 6 * pixelWitdh).tex(12 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 6 * pixelWitdh).tex(12 * pixelTextureWitdh, 12 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 5 * pixelWitdh, 6 * pixelWitdh).tex(6 * pixelTextureWitdh, 12 * pixelTextureWitdh).endVertex();
	
			//WEST
			buffer.pos(6 * pixelWitdh, 5 * pixelWitdh, 5 * pixelWitdh).tex(12 * pixelTextureWitdh, 12 * pixelTextureWitdh).endVertex();
			buffer.pos(6 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(18 * pixelTextureWitdh, 12 * pixelTextureWitdh).endVertex();
			buffer.pos(6 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(18 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
			buffer.pos(6 * pixelWitdh, 5 * pixelWitdh, 11 * pixelWitdh).tex(12 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
	
			//SOUTH
			buffer.pos(5 * pixelWitdh, 5 * pixelWitdh, 10 * pixelWitdh).tex(18 * pixelTextureWitdh, 12 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 10 * pixelWitdh).tex(24 * pixelTextureWitdh, 12 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 10 * pixelWitdh).tex(24 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 5 * pixelWitdh, 10 * pixelWitdh).tex(18 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
	
			//EAST
			buffer.pos(10 * pixelWitdh, 5 * pixelWitdh, 11 * pixelWitdh).tex(0 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
			buffer.pos(10 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(6 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
			buffer.pos(10 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(6 * pixelTextureWitdh, 12 * pixelTextureWitdh).endVertex();
			buffer.pos(10 * pixelWitdh, 5 * pixelWitdh, 5 * pixelWitdh).tex(0 * pixelTextureWitdh, 12 * pixelTextureWitdh).endVertex();
	
			//UP
			buffer.pos(5 * pixelWitdh, 10 * pixelWitdh, 5 * pixelWitdh).tex(6 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 10 * pixelWitdh, 5 * pixelWitdh).tex(12 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 10 * pixelWitdh, 11 * pixelWitdh).tex(12 * pixelTextureWitdh, 0 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 10 * pixelWitdh, 11 * pixelWitdh).tex(6 * pixelTextureWitdh, 0 * pixelTextureWitdh).endVertex();
	
			//DOWN
			buffer.pos(5 * pixelWitdh, 6 * pixelWitdh, 11 * pixelWitdh).tex(12 * pixelTextureWitdh, 0 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 6 * pixelWitdh, 11 * pixelWitdh).tex(18 * pixelTextureWitdh, 0 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 6 * pixelWitdh, 5 * pixelWitdh).tex(18 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 6 * pixelWitdh, 5 * pixelWitdh).tex(12 * pixelTextureWitdh, 6 * pixelTextureWitdh).endVertex();
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
		
		if(outside) {
			//OUTSIDE
			
			//NORTH
			buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(12 * pixelTextureWitdh, 21 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 16 * pixelWitdh, 5 * pixelWitdh).tex(12 * pixelTextureWitdh, 16 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 16 * pixelWitdh, 5 * pixelWitdh).tex(6 * pixelTextureWitdh, 16 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(6 * pixelTextureWitdh, 21 * pixelTextureWitdh).endVertex();
	
			//WEST
			buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(18 * pixelTextureWitdh, 21 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 16 * pixelWitdh, 11 * pixelWitdh).tex(18 * pixelTextureWitdh, 16 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 16 * pixelWitdh, 5 * pixelWitdh).tex(12 * pixelTextureWitdh, 16 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(12 * pixelTextureWitdh, 21 * pixelTextureWitdh).endVertex();
	
			//SOUTH
			buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(24 * pixelTextureWitdh, 21 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 16 * pixelWitdh, 11 * pixelWitdh).tex(24 * pixelTextureWitdh, 16 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 16 * pixelWitdh, 11 * pixelWitdh).tex(18 * pixelTextureWitdh, 16 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(18 * pixelTextureWitdh, 21 * pixelTextureWitdh).endVertex();
	
			//EAST
			buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(6 * pixelTextureWitdh, 21 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 16 * pixelWitdh, 5 * pixelWitdh).tex(6 * pixelTextureWitdh, 16 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 16 * pixelWitdh, 11 * pixelWitdh).tex(0 * pixelTextureWitdh, 16 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(0 * pixelTextureWitdh, 21 * pixelTextureWitdh).endVertex();
		}
		
		if(inside) {
			//INSIDE
			
			//NORTH
			buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 6 * pixelWitdh).tex(6 * pixelTextureWitdh, 21 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 16 * pixelWitdh, 6 * pixelWitdh).tex(6 * pixelTextureWitdh, 16 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 16 * pixelWitdh, 6 * pixelWitdh).tex(12 * pixelTextureWitdh, 16 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 6 * pixelWitdh).tex(12 * pixelTextureWitdh, 21 * pixelTextureWitdh).endVertex();
	
			//WEST
			buffer.pos(6 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(12 * pixelTextureWitdh, 21 * pixelTextureWitdh).endVertex();
			buffer.pos(6 * pixelWitdh, 16 * pixelWitdh, 5 * pixelWitdh).tex(12 * pixelTextureWitdh, 16 * pixelTextureWitdh).endVertex();
			buffer.pos(6 * pixelWitdh, 16 * pixelWitdh, 11 * pixelWitdh).tex(18 * pixelTextureWitdh, 16 * pixelTextureWitdh).endVertex();
			buffer.pos(6 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(18 * pixelTextureWitdh, 21 * pixelTextureWitdh).endVertex();
	
			//SOUTH
			buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 10 * pixelWitdh).tex(18 * pixelTextureWitdh, 21 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 16 * pixelWitdh, 10 * pixelWitdh).tex(18 * pixelTextureWitdh, 16 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 16 * pixelWitdh, 10 * pixelWitdh).tex(24 * pixelTextureWitdh, 16 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 10 * pixelWitdh).tex(24 * pixelTextureWitdh, 21 * pixelTextureWitdh).endVertex();
			
			//EAST
			buffer.pos(10 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(0 * pixelTextureWitdh, 21 * pixelTextureWitdh).endVertex();
			buffer.pos(10 * pixelWitdh, 16 * pixelWitdh, 11 * pixelWitdh).tex(0 * pixelTextureWitdh, 16 * pixelTextureWitdh).endVertex();
			buffer.pos(10 * pixelWitdh, 16 * pixelWitdh, 5 * pixelWitdh).tex(6 * pixelTextureWitdh, 16 * pixelTextureWitdh).endVertex();
			buffer.pos(10 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(6 * pixelTextureWitdh, 21 * pixelTextureWitdh).endVertex();
		}
		
		//System.out.println(Config.pipeStatusOverlayAlwaysOn);
		
		if ((Config.isPipeStatusOverlayAlwaysOn() || (Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemWrench)) && !(attachment.canExtract() && attachment.canInsert())) {
			final double statusOverlayU = attachment.canExtract() ? 0 : (attachment.canInsert() ? 6 * pixelTextureWitdh :
					  12 * pixelTextureWitdh);
			
			//OUTSIDE STATUS
			
			//NORTH
			buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(statusOverlayU + 6 * pixelTextureWitdh, 29 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 16 * pixelWitdh, 5 * pixelWitdh).tex(statusOverlayU + 6 * pixelTextureWitdh, 24 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 16 * pixelWitdh, 5 * pixelWitdh).tex(statusOverlayU, 24 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(statusOverlayU, 29 * pixelTextureWitdh).endVertex();
			
			//WEST
			buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(statusOverlayU + 6 * pixelTextureWitdh, 29 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 16 * pixelWitdh, 11 * pixelWitdh).tex(statusOverlayU + 6 * pixelTextureWitdh, 24 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 16 * pixelWitdh, 5 * pixelWitdh).tex(statusOverlayU, 24 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(statusOverlayU, 29 * pixelTextureWitdh).endVertex();

			//SOUTH
			buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(statusOverlayU + 6 * pixelTextureWitdh, 29 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 16 * pixelWitdh, 11 * pixelWitdh).tex(statusOverlayU + 6 * pixelTextureWitdh, 24 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 16 * pixelWitdh, 11 * pixelWitdh).tex(statusOverlayU, 24 * pixelTextureWitdh).endVertex();
			buffer.pos(5 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(statusOverlayU, 29 * pixelTextureWitdh).endVertex();

			//EAST
			buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 5 * pixelWitdh).tex(statusOverlayU + 6 * pixelTextureWitdh, 29 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 16 * pixelWitdh, 5 * pixelWitdh).tex(statusOverlayU + 6 * pixelTextureWitdh, 24 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 16 * pixelWitdh, 11 * pixelWitdh).tex(statusOverlayU, 24 * pixelTextureWitdh).endVertex();
			buffer.pos(11 * pixelWitdh, 11 * pixelWitdh, 11 * pixelWitdh).tex(statusOverlayU, 29 * pixelTextureWitdh).endVertex();
			
			
		}
		
		
		tessellator.draw();
		GL11.glPopMatrix();
	}
	
	public void drawFluid(final TileEntityPipeBase te) {
		bindTexture(new ResourceLocation("textures/atlas/blocks.png"));
		
		
		if(te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
		    if (te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).getTankProperties() == null ||
			te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).getTankProperties().length == 0) return;
			final IFluidTankProperties property = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).getTankProperties()[0];
			if(property.getContents() != null) {
				final double size = property.getContents().amount / (double) property.getCapacity();

				final TextureAtlasSprite fluidTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(property.getContents().getFluid().getStill().toString());
				
				drawBaseFluid(fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
				if(te.hasAttachment(EnumFacing.DOWN)) drawAttachmentFluid(EnumFacing.DOWN, fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
				if(te.hasAttachment(EnumFacing.EAST)) drawAttachmentFluid(EnumFacing.EAST, fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
				if(te.hasAttachment(EnumFacing.NORTH)) drawAttachmentFluid(EnumFacing.NORTH, fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
				if(te.hasAttachment(EnumFacing.SOUTH)) drawAttachmentFluid(EnumFacing.SOUTH, fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
				if(te.hasAttachment(EnumFacing.UP)) drawAttachmentFluid(EnumFacing.UP, fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
				if(te.hasAttachment(EnumFacing.WEST)) drawAttachmentFluid(EnumFacing.WEST, fluidTexture.getMinU(), fluidTexture.getMinV(), fluidTexture.getMaxU(), fluidTexture.getMaxV());
			}
		}
		
	}
	

	
	public void drawBaseFluid(final double minU, final double minV, final double maxU, final double maxV) {
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder buffer = tessellator.getBuffer();
		
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

		//UP
		buffer.pos(10.5 * pixelWitdh, 10.5 * pixelWitdh, 5.5 * pixelWitdh).tex(maxU, maxV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 16 * pixelWitdh, 5.5 * pixelWitdh).tex(maxU, minV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 16 * pixelWitdh, 10.5 * pixelWitdh).tex(minU, minV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 10.5 * pixelWitdh, 10.5 * pixelWitdh).tex(minU, maxV).endVertex();

		//UP
		buffer.pos(5.5 * pixelWitdh, 16 * pixelWitdh, 10.5 * pixelWitdh).tex(minU, minV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 16 * pixelWitdh, 10.5 * pixelWitdh).tex(maxU, minV).endVertex();
		buffer.pos(10.5 * pixelWitdh, 16 * pixelWitdh, 5.5 * pixelWitdh).tex(maxU, maxV).endVertex();
		buffer.pos(5.5 * pixelWitdh, 16 * pixelWitdh, 5.5 * pixelWitdh).tex(minU, maxV).endVertex();
		
		tessellator.draw();
		GL11.glPopMatrix();
	}
	
}
