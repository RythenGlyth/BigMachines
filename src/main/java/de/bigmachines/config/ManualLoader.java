package de.bigmachines.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.jline.utils.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import de.bigmachines.Reference;
import de.bigmachines.gui.client.manual.ManualTab;
import de.bigmachines.utils.FileHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

public class ManualLoader {
    private static final List<ManualTab> tabs = new ArrayList<ManualTab>();
    public static Gson gson;

    public static List<ManualTab> getTabs() {
		return tabs;
	}
    
    public static void init() {
		System.out.println("init");
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
