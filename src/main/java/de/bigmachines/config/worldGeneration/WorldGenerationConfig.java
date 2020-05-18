package de.bigmachines.config.worldGeneration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import de.bigmachines.Reference;
import de.bigmachines.config.Config;

public class WorldGenerationConfig {
	
	private static Gson gson;

    private static File worldGenerationConfig;
    private static File worldGenerationConfigDir;
    
    public static List<WorldGenerator> generators;
    
    public static void init(File worldGenerationConfigDir) {
    	gson = new GsonBuilder().setPrettyPrinting().create();
    	WorldGenerator.registerGenerators();
		if (Config.config == null) {
			WorldGenerationConfig.worldGenerationConfigDir = new File(worldGenerationConfigDir, Reference.MOD_ID);
			WorldGenerationConfig.worldGenerationConfigDir.mkdir();
            WorldGenerationConfig.worldGenerationConfigDir = worldGenerationConfigDir;
            WorldGenerationConfig.worldGenerationConfig = new File(worldGenerationConfigDir, "worldGeneration.json");
            loadConfig();
        }
	}
    
    public static void loadConfig() {
    	generators.clear();
		try {
	    	JsonReader reader = new JsonReader(new FileReader(WorldGenerationConfig.worldGenerationConfig));
	    	HashMap<String, Object> json = gson.fromJson(reader, HashMap.class);
	    	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			WorldGenerationConfig.init(WorldGenerationConfig.worldGenerationConfigDir);
		}
    }
	
}
