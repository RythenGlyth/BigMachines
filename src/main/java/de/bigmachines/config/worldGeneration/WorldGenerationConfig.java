package de.bigmachines.config.worldGeneration;

import java.io.File;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.bigmachines.Reference;
import de.bigmachines.config.Config;
import net.minecraftforge.common.config.Configuration;

public class WorldGenerationConfig {
	
	private static Gson gson;

    private static File worldGenerationConfig;
    private static File worldGenerationConfigDir;
    
    public static List<WorldGenerator> generators;
    
    public static void init(File worldGenerationConfigDir) {
    	gson = new GsonBuilder().setPrettyPrinting().create();
    	WorldGenerator.registerGenerators();
		if (Config.config == null) {
			worldGenerationConfigDir = new File(worldGenerationConfigDir, Reference.MOD_ID);
			worldGenerationConfigDir.mkdir();
            WorldGenerationConfig.worldGenerationConfigDir = worldGenerationConfigDir;
            WorldGenerationConfig.worldGenerationConfig = new File(worldGenerationConfigDir, "worldGeneration.json");
            loadConfig();
        }
	}
    
    public static void loadConfig() {
    	generators.clear();
    }
	
}
