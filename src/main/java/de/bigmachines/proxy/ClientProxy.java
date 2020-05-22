package de.bigmachines.proxy;

import java.util.ArrayList;

import de.bigmachines.handler.CooktimeDisplay;
import de.bigmachines.handler.HarvestLevelDisplay;
import de.bigmachines.handler.PipeOutlineHandler;
import de.bigmachines.interfaces.IModelRegister;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Main class for Client
 * 
 * @author RythenGlyth
 *
 */

public class ClientProxy implements CommonProxy {

	private static ArrayList<IModelRegister> modelList = new ArrayList<>();

	@Override
	public void preInit() {
		MinecraftForge.EVENT_BUS.register(new PipeOutlineHandler());
		MinecraftForge.EVENT_BUS.register(new HarvestLevelDisplay());
		MinecraftForge.EVENT_BUS.register(new CooktimeDisplay());

	}

	@Override
	public void init() {

	}

	@Override
	public void postInit() {

	}

	@SubscribeEvent
	public void registerItems(ModelRegistryEvent event) {
		modelList.forEach(modelRegister -> {
			modelRegister.registerModels();
		});
	}
	
	public void addIModelRegister(IModelRegister modelRegister) {
		modelList.add(modelRegister);
	}

}
