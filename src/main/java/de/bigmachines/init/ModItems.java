package de.bigmachines.init;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Item Registry
 * 
 * @author RythenGlyth
 *
 */

public class ModItems {
    
    public static ArrayList<Item> ITEMS = new ArrayList<Item>();

    public static void preInit() {
        
    }
    
    @SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
        ITEMS.forEach(item -> {
			event.getRegistry().register(item);
		});
		
	}

}