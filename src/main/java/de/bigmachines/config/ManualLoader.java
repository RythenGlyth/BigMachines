package de.bigmachines.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import de.bigmachines.gui.client.manual.ManualTab;
import de.bigmachines.utils.FileHelper;

public class ManualLoader {
    private static final List<ManualTab> tabs = new ArrayList<ManualTab>();
    public static Gson gson;

    public static List<ManualTab> getTabs() {
		return new ArrayList<ManualTab>(tabs);
	}
    
    public static void init() {
    	gson = new GsonBuilder()
    			.registerTypeAdapter(ManualTab.class, new ManualTab.ManualDeserializer())
    	.create();
    	
    	try {
	    	final List<File> files = FileHelper.getResourcesFolder("/assets/bigmachines/manual/");
	    	for (File f : files)
	    		if (FileHelper.getExtension(f).equals("json")) {
	    			tabs.add(gson.fromJson(new BufferedReader(new FileReader(f)), ManualTab.class));
	    		}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
}
