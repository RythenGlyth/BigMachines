package de.bigmachines.config;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import de.bigmachines.Reference;
import de.bigmachines.utils.FileHelper;
import de.bigmachines.world.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public final class WorldGenerationConfig {
	
	public static Gson gson = new GsonBuilder().setPrettyPrinting()
			  .registerTypeAdapter(WorldGeneratorMineable.class, new WorldGeneratorMineable.WorldGeneratorMineableDeserializer())
			  .registerTypeAdapter(WorldGeneratorStructure.class, new WorldGeneratorStructure.WorldGeneratorStructureDeserializer())
			  .registerTypeAdapter(WorldGeneratorRubberTree.class, new WorldGeneratorRubberTree.WorldGeneratorRubberTreeDeserializer())
			  .registerTypeAdapter(WorldGeneratorCaveWall.class, new WorldGeneratorCaveWall.WorldGeneratorCaveWallDeserializer())
			  .create();

    private static File worldGenerationConfig;
    private static File worldGenerationConfigDir;
    
    private static final int currentFileVersion = 1;
    
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
	    	try {
	    		JsonObject json = gson.fromJson(reader, JsonObject.class);
		    	if(json.has("file-version") && json.get("file-version").getAsInt() == currentFileVersion) {
			    	JsonArray jsonArr = json.get("generators").getAsJsonArray();
			    	jsonArr.forEach(generatorEl -> {
			    		JsonObject generator = generatorEl.getAsJsonObject();
			    		if(generator.has("type")) {
			    			try {
								if(WorldGeneratorBase.worldGeneratorTypes.containsKey(generator.get("type").getAsString())) {
									
									WorldGeneratorBase gen = gson.fromJson(generator, WorldGeneratorBase.worldGeneratorTypes.get(generator.get("type").getAsString()));
									
									if(gen != null) ModWorldGenerator.addGenerator(gen);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
			    		}
			    	});
					reader.close();
		    	} else {
					reader.close();
					recreateFileAndReload();
		    	}
	    	} catch (JsonSyntaxException e) {
				reader.close();
				recreateFileAndReload();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			init(worldGenerationConfigDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static void recreateFileAndReload() {
		try {
			Files.move(worldGenerationConfig, new File(worldGenerationConfig.getAbsolutePath() +  ".old"));
			init(worldGenerationConfigDir);
			loadConfig();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
	
}
