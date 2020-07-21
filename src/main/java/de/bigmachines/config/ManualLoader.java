package de.bigmachines.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.bigmachines.gui.client.manual.ManualTab;
import de.bigmachines.utils.FileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

public final class ManualLoader {
    private static final List<ManualTab> tabs = new ArrayList<>();
    static final Gson gson = new GsonBuilder()
			.registerTypeAdapter(ManualTab.class, new ManualTab.ManualDeserializer()).create();

    public static List<ManualTab> getTabs() {
		return tabs;
	}
    
    public static void init() {
	    tabs.clear();
	    try {
		    TreeMap<String, String> files = FileHelper.getResourcesFolder("/assets/bigmachines/manual/", "json");
		    for (Entry<String, String> resource : files.entrySet()) {
			    tabs.add(gson.fromJson(resource.getValue(), ManualTab.class));
		    }
	    } catch (Exception ex) {
		    ex.printStackTrace();
	    }
    }
}
