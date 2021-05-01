package de.bigmachines;

import de.bigmachines.config.WorldGenerationConfig;
import de.bigmachines.init.ModBlocks;
import de.bigmachines.init.ModItemGroups;
import de.bigmachines.init.ModItems;
import de.bigmachines.init.ModMaterials;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MOD_ID)
public class BigMachines {
	
	public BigMachines() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        ModMaterials.init();
		ModBlocks.init();
		ModItems.init();
		ModItemGroups.init();
		//ModFeatures.init();
		
		WorldGenerationConfig.init();
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		//event.enqueueWork(ModFeatures::init);
		event.enqueueWork(WorldGenerationConfig::loadConfig);
	}
	
	private void doClientStuff(final FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(ModBlocks.rubber_sapling.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModMaterials.glass_layers.get(), RenderType.cutout());
	}
	
	private void enqueueIMC(final InterModEnqueueEvent event) {
		
	}
	
	private void processIMC(final InterModProcessEvent event) {
		
	}
	
}
