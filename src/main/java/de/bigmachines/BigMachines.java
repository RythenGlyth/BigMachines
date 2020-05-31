package de.bigmachines;

import de.bigmachines.config.Config;
import de.bigmachines.config.WorldGenerationConfig;
import de.bigmachines.gui.GuiHandler;
import de.bigmachines.handler.GiveItemManualHandler;
import de.bigmachines.handler.SlimeBootsHandler;
import de.bigmachines.init.*;
import de.bigmachines.network.messages.MessageChangePipeAttachmentMode;
import de.bigmachines.proxy.CommonProxy;
import de.bigmachines.world.ModWorldGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;

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
	
	public static final SimpleNetworkWrapper networkHandlerMain = NetworkRegistry.INSTANCE.newSimpleChannel(new ResourceLocation(Reference.MOD_ID, "main").toString());
	
	public BigMachines() {
		INSTANCE = this;
	}
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent ev) {
		Config.init(ev.getModConfigurationDirectory());
		WorldGenerationConfig.init(new File(ev.getModConfigurationDirectory(), Reference.MOD_ID));
		
        MinecraftForge.EVENT_BUS.register(proxy);

        MinecraftForge.EVENT_BUS.register(new ModItems());
		MinecraftForge.EVENT_BUS.register(new ModBlocks());
		MinecraftForge.EVENT_BUS.register(new SlimeBootsHandler());
		MinecraftForge.EVENT_BUS.register(new Config());
		MinecraftForge.EVENT_BUS.register(new GiveItemManualHandler());
		
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
		
		ModCreativeTabs.init();
		ModItems.preInit();
		ModBlocks.preInit();
		ModMaterials.preInit();
		ModTileEntities.init();

		proxy.preInit();
		
		int messageID = 0;
		networkHandlerMain.registerMessage(MessageChangePipeAttachmentMode.Handler.class, MessageChangePipeAttachmentMode.class, messageID++, Side.SERVER);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent ev) {
		proxy.init();
		
		WorldGenerationConfig.loadConfig();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent ev) {
		GameRegistry.registerWorldGenerator(new ModWorldGenerator(), 0);
		proxy.postInit();
	}

	
}
