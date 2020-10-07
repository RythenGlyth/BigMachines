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
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
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
	
	static {
		FluidRegistry.enableUniversalBucket();
	}
	
	public BigMachines() {
		super();
		INSTANCE = this;
	}
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent ev) {
		OBJLoader.INSTANCE.addDomain(Reference.MOD_ID);
		
		ProgressBar bar = ProgressManager.push("Register Configs", 2);
		bar.step("Config");
		Config.init(ev.getModConfigurationDirectory());
		bar.step("WorldGenerationConfig");
		WorldGenerationConfig.init(new File(ev.getModConfigurationDirectory(), Reference.MOD_ID));
		ProgressManager.pop(bar);

		ProgressBar bar2 = ProgressManager.push("Register Events", 7);

		bar2.step("Proxy");
        MinecraftForge.EVENT_BUS.register(proxy);

		bar2.step("ModItems");
        MinecraftForge.EVENT_BUS.register(new ModItems());
		bar2.step("ModBlocks");
		MinecraftForge.EVENT_BUS.register(new ModBlocks());
		bar2.step("ModEntities");
		MinecraftForge.EVENT_BUS.register(new ModEntities());
		bar2.step("SlimeBootsHandler");
		MinecraftForge.EVENT_BUS.register(new SlimeBootsHandler());
		bar2.step("Config");
		MinecraftForge.EVENT_BUS.register(new Config());
		bar2.step("GiveItemManualHandler");
		MinecraftForge.EVENT_BUS.register(new GiveItemManualHandler());
		
		ProgressManager.pop(bar2);
		
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());

		ProgressBar bar3 = ProgressManager.push("Initialize Things", 7);

		bar3.step("CreativeTabs");
		ModCreativeTabs.init();
		bar3.step("Items");
		ModItems.preInit();
		bar3.step("Blocks");
		ModBlocks.preInit();
		bar3.step("Fluids");
		ModFluids.preInit();
		bar3.step("Entities");
		ModEntities.preInit();
		bar3.step("Materials");
		ModMaterials.preInit();
		bar3.step("TileEntities");
		ModTileEntities.init();
		
		ProgressManager.pop(bar3);
		
		proxy.preInit();
		
		int messageID = 0;
		networkHandlerMain.registerMessage(MessageChangePipeAttachmentMode.Handler.class, MessageChangePipeAttachmentMode.class, messageID++, Side.SERVER);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent ev) {
		proxy.init();
		
		ModRecipes.preInit();
		
		WorldGenerationConfig.loadConfig();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent ev) {
		GameRegistry.registerWorldGenerator(new ModWorldGenerator(), 0);
		proxy.postInit();
	}

	
}
