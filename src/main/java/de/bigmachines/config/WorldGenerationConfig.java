package de.bigmachines.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import de.bigmachines.Reference;
import de.bigmachines.utils.FileHelper;
import de.bigmachines.world.ModWorldGenerator;
import de.bigmachines.world.WorldGeneratorBase;
import de.bigmachines.world.WorldGeneratorMineable;
import de.bigmachines.world.WorldGeneratorStructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public final class WorldGenerationConfig {
	
	public static Gson gson = new GsonBuilder().setPrettyPrinting()
			.registerTypeAdapter(WorldGeneratorMineable.class, new WorldGeneratorMineable.WorldGeneratorMineableDeserializer())
			.registerTypeAdapter(WorldGeneratorStructure.class, new WorldGeneratorStructure.WorldGeneratorStructureDeserializer())
			.create();

    private static File worldGenerationConfig;
    private static File worldGenerationConfigDir;
    
    public static void init(File worldGenerationConfigDir) {
		WorldGeneratorBase.registerGenerators();
		
    	WorldGenerationConfig.worldGenerationConfigDir = worldGenerationConfigDir;
		WorldGenerationConfig.worldGenerationConfigDir.mkdir();
        worldGenerationConfig = new File(worldGenerationConfigDir, "worldGeneration.json");
        if(!worldGenerationConfig.exists()) {
        	
        	FileHelper.copyFileUsingStreamAndLoader("assets/" + Reference.MOD_ID + "/world/worldGeneration.json", worldGenerationConfig);
        }
	}
    
    public static void loadConfig() {
    	ModWorldGenerator.generators.clear();
		try {
	    	JsonReader reader = new JsonReader(new FileReader(worldGenerationConfig));
	    	JsonArray json = gson.fromJson(reader, JsonArray.class);
	    	json.forEach(generatorEl -> {
	    		JsonObject generator = generatorEl.getAsJsonObject();
	    		if(generator.has("type")) {
	    			try {
						if(WorldGeneratorBase.worldGeneratorTypes.containsKey(generator.get("type").getAsString())) {
							
							WorldGeneratorBase gen = gson.fromJson(generator, WorldGeneratorBase.worldGeneratorTypes.get(generator.get("type").getAsString()));
							
							ModWorldGenerator.addGenerator(gen);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
	    		}
	    	});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			init(worldGenerationConfigDir);
		}
    }
	
}
