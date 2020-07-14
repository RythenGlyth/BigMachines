package de.bigmachines.handler.hud;

import de.bigmachines.config.Config;
import de.bigmachines.handler.hud.elements.HUDElement;
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

import java.util.ArrayList;
import java.util.List;

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
				List<HUDElement> hudElements = new ArrayList<>();
				
				ItemStack head = mc.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
				if (head != null && head.getItem() instanceof IHUDInfoProvider) {
					IHUDInfoProvider provider = (IHUDInfoProvider) head.getItem();
					provider.addHUDInfo(hudElements, head);
				}
				
				ItemStack chestplate = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
				if (chestplate != null && chestplate.getItem() instanceof IHUDInfoProvider) {
					IHUDInfoProvider provider = (IHUDInfoProvider) chestplate.getItem();
					provider.addHUDInfo(hudElements, chestplate);
				}
				
				ItemStack legs = mc.player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
				if (legs != null && legs.getItem() instanceof IHUDInfoProvider) {
					IHUDInfoProvider provider = (IHUDInfoProvider) legs.getItem();
					provider.addHUDInfo(hudElements, legs);
				}
				
				ItemStack feet = mc.player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
				if (feet != null && feet.getItem() instanceof IHUDInfoProvider) {
					IHUDInfoProvider provider = (IHUDInfoProvider) feet.getItem();
					provider.addHUDInfo(hudElements, feet);
				}
				
				ItemStack main_hand = mc.player.getHeldItem(EnumHand.MAIN_HAND);
				if (main_hand != null && main_hand.getItem() instanceof IHUDInfoProvider) {
					IHUDInfoProvider provider = (IHUDInfoProvider) main_hand.getItem();
					provider.addHUDInfo(hudElements, main_hand);
				}
				
				ItemStack off_hand = mc.player.getHeldItem(EnumHand.OFF_HAND);
				if (off_hand != null && off_hand.getItem() instanceof IHUDInfoProvider) {
					IHUDInfoProvider provider = (IHUDInfoProvider) off_hand.getItem();
					provider.addHUDInfo(hudElements, off_hand);
				}
				
				
				if (hudElements.isEmpty()) {
					return;
				}
				
				//TODO HUD Position + elements

				GL11.glPushMatrix();
				mc.entityRenderer.setupOverlayRendering();
				
				int i = 0;
				for (HUDElement el : hudElements) {
					el.draw();
					//mc.fontRenderer.drawString(s, 5, 5, 0xffffff);
					//RenderUtils.drawStringAtHUDPosition(s, Settings.hudPosition, mc.fontRenderer, Config.HUDOffsetX, Config.HUDOffsetY, Config.HUDScale, 0xeeeeee, true, i);
					i++;
				}

				GL11.glPopMatrix();
			}
		}
	}
	
}
