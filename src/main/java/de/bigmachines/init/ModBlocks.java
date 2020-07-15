package de.bigmachines.init;

import java.util.ArrayList;

import de.bigmachines.blocks.BlockBase;
import de.bigmachines.blocks.blocks.pipes.fluidpipe.BlockFluidPipe;
import de.bigmachines.blocks.blocks.pipes.heatpipe.BlockHeatPipe;
import de.bigmachines.interfaces.IInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Block Registry
 * 
 * @author RythenGlyth
 *
 */

public class ModBlocks {
	
	public static final ArrayList<BlockBase> BLOCKS = new ArrayList<>();

	public static BlockHeatPipe heatPipe;
	public static BlockFluidPipe fluidPipe;

    public static void preInit() {
        heatPipe = new BlockHeatPipe();
        BLOCKS.add(heatPipe);
        fluidPipe = new BlockFluidPipe();
        BLOCKS.add(fluidPipe); 
    }
    
    @SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
        BLOCKS.forEach(block -> {
			event.getRegistry().register(block.getItemBlock());
			if(block instanceof IInitializer && FMLCommonHandler.instance().getSide().equals(Side.CLIENT)) ((IInitializer)block).postRegister();
		});
		
	}
	
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		BLOCKS.forEach(block -> {
			event.getRegistry().register(block);
		});
	}

}