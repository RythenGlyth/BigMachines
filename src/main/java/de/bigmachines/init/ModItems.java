package de.bigmachines.init;

import de.bigmachines.interfaces.IInitializer;
import de.bigmachines.items.items.*;
import de.bigmachines.items.items.manual.ItemManual;
import de.bigmachines.items.items.manual.ItemPlate;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;

/**
 * Item Registry
 * 
 * @author RythenGlyth
 *
 */

public class ModItems {
	
	public static ItemSlimeBoots slimeBoots;
	public static ItemArmorWest armorWest;
	public static ItemPlate basicPlate;
	public static ItemWrench wrench;
	public static ItemManual manual;
	public static ItemDebugger debugger;
	public static ItemSlicer slicer;
	
	public static final ArrayList<Item> ITEMS = new ArrayList<>();
	
	public static void preInit() {
		slimeBoots = new ItemSlimeBoots();
		ITEMS.add(slimeBoots);
		armorWest = new ItemArmorWest();
		ITEMS.add(armorWest);
		basicPlate = new ItemPlate("basic", .1);
		ITEMS.add(basicPlate);
		
		debugger = new ItemDebugger();
		ITEMS.add(debugger);
		slicer = new ItemSlicer();
		ITEMS.add(slicer);
		
		wrench = new ItemWrench();
		ITEMS.add(wrench);
		manual = new ItemManual();
		ITEMS.add(manual);
	}
    
    @SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		ProgressBar bar = ProgressManager.push("Register Items", ITEMS.size());
        ITEMS.forEach(item -> {
        	bar.step(item.getRegistryName().toString());
			event.getRegistry().register(item);
			if(item instanceof IInitializer && FMLCommonHandler.instance().getSide().equals(Side.CLIENT)) ((IInitializer)item).postRegister();
		});
        ProgressManager.pop(bar);
		
	}

}