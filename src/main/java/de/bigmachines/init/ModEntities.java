package de.bigmachines.init;

import java.util.ArrayList;

import de.bigmachines.Reference;
import de.bigmachines.entities.EntitySpaceshipOfficer;
import de.bigmachines.interfaces.IInitializer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

public class ModEntities {
    
    public static void preInit() {
    	int id = 0;
    	EntitySpaceshipOfficer.initialize(id++);
    }
    
}
