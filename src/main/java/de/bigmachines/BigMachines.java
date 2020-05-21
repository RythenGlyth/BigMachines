package de.bigmachines;

import java.io.File;

import de.bigmachines.config.Config;
import de.bigmachines.config.WorldGenerationConfig;
import de.bigmachines.handler.CooktimeDisplay;
import de.bigmachines.handler.HUDTickHandler;
import de.bigmachines.handler.ItemInformationHandler;
import de.bigmachines.handler.SlimeBootsHandler;
import de.bigmachines.init.ModBlocks;
import de.bigmachines.init.ModCreativeTabs;
import de.bigmachines.init.ModItems;
import de.bigmachines.init.ModKeybinds;
import de.bigmachines.init.ModMaterials;
import de.bigmachines.init.ModTileEntities;
import de.bigmachines.items.items.ItemWrench;
import de.bigmachines.proxy.CommonProxy;
import de.bigmachines.world.ModWorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Main
 * @author RythenGlyth
 *
 */

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY)
public class BigMachines {
	
	@Instance
	public static BigMachines INSTANCE;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public BigMachines() {
		INSTANCE = this;
	}
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent e) {
		Config.init(e.getModConfigurationDirectory());
		WorldGenerationConfig.init(new File(e.getModConfigurationDirectory(), Reference.MOD_ID));
		
        MinecraftForge.EVENT_BUS.register(proxy);

        MinecraftForge.EVENT_BUS.register(new ModItems());
		MinecraftForge.EVENT_BUS.register(new ModBlocks());
        MinecraftForge.EVENT_BUS.register(new HUDTickHandler());
		MinecraftForge.EVENT_BUS.register(new ItemInformationHandler());
		MinecraftForge.EVENT_BUS.register(new SlimeBootsHandler());
		MinecraftForge.EVENT_BUS.register(new CooktimeDisplay());
		MinecraftForge.EVENT_BUS.register(new ItemWrench.ScrollHandler());
		
		ModCreativeTabs.init();
		ModItems.preInit();
		ModBlocks.preInit();
		ModMaterials.preInit();
		ModKeybinds.init();
		ModTileEntities.init();

		proxy.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init();
		
		WorldGenerationConfig.loadConfig();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		GameRegistry.registerWorldGenerator(new ModWorldGenerator(), 0);
		proxy.postInit();
	}

	
}
