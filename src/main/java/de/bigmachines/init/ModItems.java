package de.bigmachines.init;

import java.util.ArrayList;

import de.bigmachines.interfaces.IInitializer;
import de.bigmachines.items.ItemSlimeBoots;
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
    
	public static ItemSlimeBoots slimeBoots;
	
    public static ArrayList<Item> ITEMS = new ArrayList<Item>();

    public static void preInit() {
    	slimeBoots = new ItemSlimeBoots();
    	ITEMS.add(slimeBoots);
    }
    
    @SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
        ITEMS.forEach(item -> {
        	System.out.println(item);
			event.getRegistry().register(item);
			if(item instanceof IInitializer) ((IInitializer)item).postRegister();
		});
		
	}

}