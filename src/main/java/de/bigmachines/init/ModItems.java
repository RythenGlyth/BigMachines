package de.bigmachines.init;

import java.util.ArrayList;

import de.bigmachines.interfaces.IInitializer;
import de.bigmachines.items.items.ItemSlimeBoots;
import de.bigmachines.items.items.ItemWrench;
import de.bigmachines.items.items.manual.ItemManual;
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
	public static ItemWrench wrench;
	public static ItemManual manual;
	
    public static ArrayList<Item> ITEMS = new ArrayList<Item>();

    public static void preInit() {
    	slimeBoots = new ItemSlimeBoots();
    	ITEMS.add(slimeBoots);
    	wrench = new ItemWrench();
    	ITEMS.add(wrench);
    	
    	manual = new ItemManual();
    	ITEMS.add(manual);
    }
    
    @SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
        ITEMS.forEach(item -> {
			event.getRegistry().register(item);
			if(item instanceof IInitializer) ((IInitializer)item).postRegister();
		});
		
	}

}