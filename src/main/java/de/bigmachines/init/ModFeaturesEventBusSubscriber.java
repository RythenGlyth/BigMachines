package de.bigmachines.init;

import java.util.Map.Entry;

import de.bigmachines.Reference;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ModFeaturesEventBusSubscriber {
	
	public static void init() {
		
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
    public static void biomeLoadingAdd(final BiomeLoadingEvent event) {
		if(event.getCategory() != Biome.Category.NONE && event.getCategory() != Biome.Category.NETHER && event.getCategory() != Biome.Category.THEEND) {
			for(Entry<ConfiguredFeature<?, ?>, Decoration> cf : ModFeatures.features.entrySet()) {
				event.getGeneration().addFeature(cf.getValue(), cf.getKey());
			}
		}
    }
	
}
