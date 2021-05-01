package de.bigmachines.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import de.bigmachines.Reference;
import de.bigmachines.init.ModFeatures;
import de.bigmachines.util.FileHelper;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.loading.FMLPaths;

public class WorldGenerationConfig {
	
	private static Logger logger = LogManager.getLogger();
	
	public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	private static File worldGenerationConfig;
     private static File worldGenerationConfigDir;
	
     private static final int currentFileVersion = 1;
    
	public static void init() {
		worldGenerationConfigDir = new File(FMLPaths.CONFIGDIR.get().toFile(), Reference.MOD_ID);
		worldGenerationConfigDir.mkdirs();
		
		 worldGenerationConfig = new File(worldGenerationConfigDir, "worldGeneration.json");
        if(!worldGenerationConfig.exists()) {
        	FileHelper.copyFileUsingStreamAndLoader("data/" + Reference.MOD_ID + "/world/worldGeneration.json", worldGenerationConfig);
        }
	}
	
	public static void loadConfig() {
		ModFeatures.features.clear();
		ModFeatures.registerDefaults();
		try {
			JsonReader reader = new JsonReader(new FileReader(worldGenerationConfig));
	    	try {
	    		JsonObject json = gson.fromJson(reader, JsonObject.class);
		    	if(json.has("file-version") && json.get("file-version").getAsInt() == currentFileVersion) {
			    	JsonObject jsonArr = json.get("features").getAsJsonObject();
			    	jsonArr.entrySet().forEach(generatorEl -> {
			    		JsonObject generator = generatorEl.getValue().getAsJsonObject();
			    		if(generator.has("type")) {
			    			try {
			    				switch(generator.get("type").getAsString()) {
				    				case "ore":;
				    					if(generator.has("block") && generator.has("max-per-chunk") && generator.has("max-vein-size") && generator.has("max-height")) {
	    									ModFeatures.register(
				    							generatorEl.getKey(),
				    							new ConfiguredFeature<OreFeatureConfig, Feature<OreFeatureConfig>>(
			    									Feature.ORE,
			    									new OreFeatureConfig(
		    											OreFeatureConfig.FillerBlockType.NATURAL_STONE,
		    											NBTUtil.readBlockState(JsonToNBT.parseTag(generator.get("block").toString())),
		    											generator.get("max-per-chunk").getAsInt()
			    									)
			    								).decorated(
		    										Placement.RANGE.configured(
	    												ModFeatures.topRangeConfig(
    														generator.has("min-height") ? generator.get("min-height").getAsInt() : 0,
    														generator.get("max-height").getAsInt()
    													)
	    											)
		    									)
				    							.squared() //RANDOMISE (X/Z) IN CHUNK
				    							.count(generator.get("max-vein-size").getAsInt()),
				    							GenerationStage.Decoration.UNDERGROUND_ORES);
				    					} else {
					    					logger.error("In " + generatorEl.getKey() + " in worldgeneration.json is something wrong");
				    					}
				    					break;
			    				}
								/*if(WorldGeneratorBase.worldGeneratorTypes.containsKey()) {
									
									WorldGeneratorBase gen = gson.fromJson(generator, WorldGeneratorBase.worldGeneratorTypes.get(generator.get("type").getAsString()));
									
									if(gen != null) ModWorldGenerator.addGenerator(gen);
								}*/
							} catch (Exception e) {
								e.printStackTrace();
		    					logger.error("In " + generatorEl.getKey() + " in worldgeneration.json is something wrong");
							}
			    		}
			    	});
					reader.close();
		    	} else {
					reader.close();
					logger.error("Error reading worldgeneration.json, old file version, current file version is: " + currentFileVersion);
		    	}
	    	} catch (JsonSyntaxException e) {
				reader.close();
				logger.error("Error reading worldgeneration.json");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			init();
			logger.error("worldgeneration.json not found");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error reading worldgeneration.json");
		}
	}
	
}
