package de.bigmachines.handler;

import de.bigmachines.init.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GiveItemManualHandler {
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent e) {
		if(e.getEntity() instanceof EntityPlayer && !(e.getEntity().getEntityData().hasKey("HasBook") && e.getEntity().getEntityData().getBoolean("HasBook"))) {
			((EntityPlayer)e.getEntity()).inventory.addItemStackToInventory(new ItemStack(ModItems.manual, 1));
			e.getEntity().getEntityData().setBoolean("HasBook", true);
		}
	}
	
}
