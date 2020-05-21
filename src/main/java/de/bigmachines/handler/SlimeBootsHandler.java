package de.bigmachines.handler;

import de.bigmachines.items.items.ItemSlimeBoots;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Slime Boots Handler (not working)
 * @author RythenGlyth
 *
 */

public class SlimeBootsHandler {
	
	@SubscribeEvent
    public void onFall(LivingFallEvent e) {
		/*
    	EntityLivingBase entity = e.getEntityLiving();
		
    	if(entity == null) return;
    	if(!(entity instanceof EntityPlayer)) return;

		System.out.println(entity.motionY);
    	
    	ItemStack feetItemStack = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if(!(feetItemStack.getItem() instanceof ItemSlimeBoots)) {
        	return;
        }

        if(e.getDistance() > 2 && !entity.isSneaking()) {
        	entity.fallDistance = 0;
        	e.setDamageMultiplier(0);
        	if(entity.getEntityWorld().isRemote) {
        		entity.motionY = 15;
        		
        		entity.isAirBorne = true;
                entity.onGround = false;
        	} else {
        		e.setCanceled(true);
        	}
            entity.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1f, 1f);
        } else if(entity.isSneaking()) {
        	e.setDamageMultiplier(0.5f);
        }*/
    }
	
}
