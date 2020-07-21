package de.bigmachines.handler;

import de.bigmachines.config.Config;
import de.bigmachines.config.Config.HUDPostitions;
import de.bigmachines.items.IHUDInfoProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class HUDTickHandler {
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(receiveCanceled = true)
	public void onOverlayRender(RenderGameOverlayEvent.Post event) {
		if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
			return;
		}
		if (mc.player != null) {
			if ((mc.currentScreen == null || Config.isShowHUDWhileChatting() && mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiContainerCreative) && !mc.gameSettings.hideGUI && !mc.gameSettings.showDebugInfo) {
				
				GL11.glPushMatrix();
				mc.entityRenderer.setupOverlayRendering();
				HUDPostitions hudPosition = Config.getHudPosition();
				
				int heightNeeded = 0;
				
				ItemStack head = mc.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
				if (head != null && head.getItem() instanceof IHUDInfoProvider) {
					IHUDInfoProvider provider = (IHUDInfoProvider) head.getItem();
					heightNeeded += provider.drawHUDInfo(head, hudPosition, heightNeeded, event.getPartialTicks());
				}
				
				ItemStack chestplate = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
				if (chestplate != null && chestplate.getItem() instanceof IHUDInfoProvider) {
					IHUDInfoProvider provider = (IHUDInfoProvider) chestplate.getItem();
					heightNeeded += provider.drawHUDInfo(head, hudPosition, heightNeeded, event.getPartialTicks());
				}
				
				ItemStack legs = mc.player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
				if (legs != null && legs.getItem() instanceof IHUDInfoProvider) {
					IHUDInfoProvider provider = (IHUDInfoProvider) legs.getItem();
					heightNeeded += provider.drawHUDInfo(head, hudPosition, heightNeeded, event.getPartialTicks());
				}
				
				ItemStack feet = mc.player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
				if (feet != null && feet.getItem() instanceof IHUDInfoProvider) {
					IHUDInfoProvider provider = (IHUDInfoProvider) feet.getItem();
					heightNeeded += provider.drawHUDInfo(head, hudPosition, heightNeeded, event.getPartialTicks());
				}
				
				ItemStack main_hand = mc.player.getHeldItem(EnumHand.MAIN_HAND);
				if (main_hand != null && main_hand.getItem() instanceof IHUDInfoProvider) {
					IHUDInfoProvider provider = (IHUDInfoProvider) main_hand.getItem();
					heightNeeded += provider.drawHUDInfo(head, hudPosition, heightNeeded, event.getPartialTicks());
				}
				
				ItemStack off_hand = mc.player.getHeldItem(EnumHand.OFF_HAND);
				if (off_hand != null && off_hand.getItem() instanceof IHUDInfoProvider) {
					IHUDInfoProvider provider = (IHUDInfoProvider) off_hand.getItem();
					heightNeeded += provider.drawHUDInfo(head, hudPosition, heightNeeded, event.getPartialTicks());
				}

				GL11.glPopMatrix();
			}
		}
	}
	
}
