package de.bigmachines.proxy;

import de.bigmachines.config.ManualLoader;
import de.bigmachines.entities.EntitySpaceshipOfficer;
import de.bigmachines.handler.*;
import de.bigmachines.handler.hud.HUDTickHandler;
import de.bigmachines.init.ModKeybinds;
import de.bigmachines.interfaces.IModelRegister;
import de.bigmachines.items.items.ItemWrench;
import de.bigmachines.render.RenderEntitySpaceshipOfficer;
import net.minecraft.block.BlockLeaves;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

/**
 * Main class for Client
 * 
 * @author RythenGlyth
 *
 */

public class ClientProxy implements CommonProxy {

	private static final ArrayList<IModelRegister> modelList = new ArrayList<>();

	@Override
	public void preInit() {
		MinecraftForge.EVENT_BUS.register(new PipeOutlineHandler());
		MinecraftForge.EVENT_BUS.register(new HarvestLevelDisplay());
		MinecraftForge.EVENT_BUS.register(new CooktimeDisplay());
        MinecraftForge.EVENT_BUS.register(new HUDTickHandler());
		MinecraftForge.EVENT_BUS.register(new ItemInformationHandler());
		MinecraftForge.EVENT_BUS.register(new ItemWrench.ScrollHandler());
		
		ModKeybinds.init();
		
		RenderingRegistry.registerEntityRenderingHandler(EntitySpaceshipOfficer.class, RenderEntitySpaceshipOfficer::new);
		
	}

	@Override
	public void init() {
		ManualLoader.init();
	}

	@Override
	public void postInit() {

	}

	@SubscribeEvent
	public void registerItems(ModelRegistryEvent event) {
		modelList.forEach(IModelRegister::registerModels);
	}
	
	public void addIModelRegister(IModelRegister modelRegister) {
		modelList.add(modelRegister);
	}

	@Override
	public void setGraphicsLevel(BlockLeaves block, boolean fancyEnabled) {
		block.setGraphicsLevel(fancyEnabled);
	}

}
