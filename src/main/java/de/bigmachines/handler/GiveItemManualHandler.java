package de.bigmachines.handler;

import de.bigmachines.init.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GiveItemManualHandler {
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent e)  { //TODO only when first join
		if(e.getEntity() instanceof EntityPlayer) {
			System.out.println("onEntityJoinWorld");
			((EntityPlayer)e.getEntity()).inventory.addItemStackToInventory(new ItemStack(ModItems.manual, 1));
		}
	}
	
}
