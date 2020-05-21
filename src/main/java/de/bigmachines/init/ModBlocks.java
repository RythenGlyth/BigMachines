package de.bigmachines.init;

import java.util.ArrayList;

import de.bigmachines.blocks.BlockBase;
import de.bigmachines.interfaces.IInitializer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Block Registry
 * 
 * @author RythenGlyth
 *
 */

public class ModBlocks {
	
	public static ArrayList<BlockBase> BLOCKS = new ArrayList<BlockBase>();

    public static void preInit() {
        
    }
    
    @SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
        BLOCKS.forEach(block -> {
			event.getRegistry().register(block.getItemBlock());
			if(block instanceof IInitializer) ((IInitializer)block).postRegister();
		});
		
	}
	
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		BLOCKS.forEach(block -> {
			event.getRegistry().register(block);
		});
	}

}