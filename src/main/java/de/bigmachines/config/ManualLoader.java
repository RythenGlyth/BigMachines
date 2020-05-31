package de.bigmachines.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.bigmachines.gui.client.manual.ManualTab;
import de.bigmachines.utils.FileHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class ManualLoader {
    private static final List<ManualTab> tabs = new ArrayList<>();
    public static Gson gson;

    public static List<ManualTab> getTabs() {
		return tabs;
	}
    
    public static void init() {
    	gson = new GsonBuilder()
    			.registerTypeAdapter(ManualTab.class, new ManualTab.ManualDeserializer())
    	.create();
    	
    	try {
	    	final HashMap<String, String> files = FileHelper.getResourcesFolder("/assets/bigmachines/manual/", "json");
	    	for (Entry<String, String> resource : files.entrySet()) {
	    		tabs.add(gson.fromJson(resource.getValue(), ManualTab.class));
	    	}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
}
