package de.bigmachines.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.core.util.Loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import de.bigmachines.Reference;
import de.bigmachines.utils.FileHelper;
import de.bigmachines.world.ModWorldGenerator;
import de.bigmachines.world.WorldGeneratorBase;
import de.bigmachines.world.WorldGeneratorMineable;

public class WorldGenerationConfig {
	
	public static Gson gson;

    private static File worldGenerationConfig;
    private static File worldGenerationConfigDir;
    
    public static void init(File worldGenerationConfigDir) {
    	gson = new GsonBuilder()
    			.setPrettyPrinting()
    			.registerTypeAdapter(WorldGeneratorMineable.class, new WorldGeneratorMineable.WorldGeneratorMineableDeserializer())
    			.create();
    	WorldGeneratorBase.registerGenerators();
		
    	WorldGenerationConfig.worldGenerationConfigDir = worldGenerationConfigDir;
		WorldGenerationConfig.worldGenerationConfigDir.mkdir();
        WorldGenerationConfig.worldGenerationConfig = new File(worldGenerationConfigDir, "worldGeneration.json");
        if(!WorldGenerationConfig.worldGenerationConfig.exists()) {
        	
        	FileHelper.copyFileUsingStreamAndLoader("assets/" + Reference.MOD_ID + "/world/worldGeneration.json", WorldGenerationConfig.worldGenerationConfig);
        }
	}
    
    public static void loadConfig() {
    	ModWorldGenerator.generators.clear();
		try {
	    	JsonReader reader = new JsonReader(new FileReader(WorldGenerationConfig.worldGenerationConfig));
	    	JsonArray json = gson.fromJson(reader, JsonArray.class);
	    	json.forEach(generatorEl -> {
	    		JsonObject generator = generatorEl.getAsJsonObject();
	    		if(generator.has("type")) {
	    			try {
						if(WorldGeneratorBase.worldGeneratorTypes.containsKey(generator.get("type").getAsString())) {
							
							System.out.println("--------------------------------------------------------------------------------");
							System.out.println(generator.get("type"));
							System.out.println(WorldGeneratorBase.worldGeneratorTypes.get(generator.get("type").getAsString()));
							
							WorldGeneratorBase gen = WorldGenerationConfig.gson.fromJson(generator, WorldGeneratorBase.worldGeneratorTypes.get(generator.get("type").getAsString()));
							
							ModWorldGenerator.addGenerator(gen);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
	    		}
	    	});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			WorldGenerationConfig.init(WorldGenerationConfig.worldGenerationConfigDir);
		}
    }
	
}
