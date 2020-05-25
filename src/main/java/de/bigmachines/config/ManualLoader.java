package de.bigmachines.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

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
    		//List<IResource> files = Minecraft.getMinecraft().getResourceManager().getAllResources(new ResourceLocation(Reference.MOD_ID, "assets/bigmachines/manual/"));
	    	final List<File> files = FileHelper.getResourcesFolder("/assets/bigmachines/manual/");
	    	//for (IResource resource : files) {
	    	for (File resource : files) {
	    		System.out.println(resource.getName());
	    		//System.out.println(resource.getResourceLocation());
	    		//if (FileHelper.getExtension(resource.getResourceLocation().getResourcePath()).equals("json")) {
	    		if (FileHelper.getExtension(resource.getName()).equals("json")) {
	    			//tabs.add(gson.fromJson(new BufferedReader(new InputStreamReader(resource.getInputStream())), ManualTab.class));
	    			tabs.add(gson.fromJson(new BufferedReader(new FileReader(resource)), ManualTab.class));
	    		}
	    	}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
}
