package de.bigmachines.handler;

import de.bigmachines.blocks.blocks.pipes.BlockPipeBase;
import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase;
import de.bigmachines.utils.RenderHelper;
import de.bigmachines.utils.RenderHelper.Line3D;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PipeOutlineHandler {
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onBlockHighlight(DrawBlockHighlightEvent event) {
		RayTraceResult target = event.getTarget();
		if (target.typeOfHit.equals(Type.BLOCK)) {
			TileEntity tile = event.getPlayer().world.getTileEntity(target.getBlockPos());
			if (tile instanceof TileEntityPipeBase) {
				event.setCanceled(true);
				TileEntityPipeBase tileEntityPipeBase = (TileEntityPipeBase) tile;
				
				Set<Line3D> blacklist = new HashSet<>();
				
				Set<Line3D> lines = new HashSet<>(Arrays.asList(RenderHelper.getLinesFromCube(BlockPipeBase.getBox(null))));
				
				for (EnumFacing side : tileEntityPipeBase.getAttachments().keySet()) {
					AxisAlignedBB box = BlockPipeBase.getBox(side);
					
					boolean isBox = !event.getPlayer().isSneaking() && box.grow(0.01D).contains(target.hitVec.subtract(tileEntityPipeBase.getPos().getX(), tileEntityPipeBase.getPos().getY(), tileEntityPipeBase.getPos().getZ()));
					
					if (isBox) {
						lines.clear();
						blacklist.clear();
					}
					
					for (Line3D line : RenderHelper.getLinesFromCube(box)) {
						if (!lines.add(line)) {
							blacklist.add(line);
						}
					}
					
					if (isBox) break;
					
				}
				lines.removeAll(blacklist);
				blacklist = null;
				
				GL11.glPushMatrix();
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
				GlStateManager.glLineWidth(2.0F);
				GlStateManager.disableTexture2D();
				GlStateManager.depthMask(false);
				
				double x = event.getPlayer().lastTickPosX + (event.getPlayer().posX - event.getPlayer().lastTickPosX) * event.getPartialTicks();
				double y = event.getPlayer().lastTickPosY + (event.getPlayer().posY - event.getPlayer().lastTickPosY) * event.getPartialTicks();
				double z = event.getPlayer().lastTickPosZ + (event.getPlayer().posZ - event.getPlayer().lastTickPosZ) * event.getPartialTicks();
				
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder bufferbuilder = tessellator.getBuffer();
				bufferbuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
				
				GL11.glTranslated(target.getBlockPos().getX() - x, target.getBlockPos().getY() - y, target.getBlockPos().getZ() - z);
				
				for (Line3D line : lines) {
					line.addVertices(bufferbuilder);
				}
				
				tessellator.draw();
				
				GlStateManager.depthMask(true);
				GlStateManager.enableTexture2D();
				GlStateManager.disableBlend();
				
				GL11.glPopMatrix();
				
			}
		}
	}
	
}
