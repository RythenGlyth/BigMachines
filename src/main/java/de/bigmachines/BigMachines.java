package de.bigmachines;

import de.bigmachines.init.ModBlocks;
import de.bigmachines.init.ModCreativeTabs;
import de.bigmachines.init.ModItems;
import de.bigmachines.init.ModMaterials;
import de.bigmachines.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Main
 * @author RythenGlyth
 *
 */

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
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
        System.out.println("-------------------------------------------BIGMACHINES----LOADING-------------------------------");
        MinecraftForge.EVENT_BUS.register(proxy);

        MinecraftForge.EVENT_BUS.register(new ModItems());
		MinecraftForge.EVENT_BUS.register(new ModBlocks());
		
		ModCreativeTabs.init();
		ModItems.preInit();
		ModBlocks.preInit();
		ModMaterials.preInit();

		proxy.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {

		proxy.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {

		proxy.postInit();
	}

	
}
