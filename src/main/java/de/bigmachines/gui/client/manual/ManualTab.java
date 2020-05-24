package de.bigmachines.gui.client.manual;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import de.bigmachines.Reference;
import net.minecraft.util.ResourceLocation;

public class ManualTab {
	private ResourceLocation icon;
	private String title;
	private List<ManualContent> contents;
	
	private ManualTab(ResourceLocation icon, String title) {
		this.icon = icon;
		this.title = title;
		this.contents = new ArrayList<ManualContent>();
	}
	
	public boolean addContents(ManualContent c) {
		return contents.add(c);
	}
	
	public ResourceLocation getIcon() {
		return icon;
	}
	
	public String getTitle() {
		return title;
	}
	
	public static class ManualDeserializer implements JsonDeserializer<ManualTab> {

		@Override
		public ManualTab deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			final JsonObject tab = json.getAsJsonObject();
			final String icon = tab.has("icon") ? tab.get("icon").getAsString() : "";
			final String title = tab.has("title") ? tab.get("title").getAsString() : "";
			
			final ManualTab mtab = new ManualTab(new ResourceLocation(icon), title);
			
			if(tab.has("contents")) {
				JsonArray contentsJson = tab.get("contents").getAsJsonArray();
				/*contentsJson.forEach(contentJson -> {
					//mtab.addContents(c)
				});*/
				
			}
			
			return mtab;
		}
		
	}
	
}
