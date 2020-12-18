package de.bigmachines.init;

import de.bigmachines.Reference;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
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
			for(ConfiguredFeature<?, ?> cf : ModFeatures.features) {
				event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, cf);
			}
		}
    }
	
}
