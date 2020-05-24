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
	
	public List<ManualContent> getContents() {
		return new ArrayList<ManualContent>(contents);
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
			
			final ManualTab mtab = new ManualTab(new ResourceLocation(icon.split(":")[0], icon.split(":")[1]), title);
			
			if(tab.has("contents")) {
				final JsonArray contentsJson = tab.get("contents").getAsJsonArray();
				for (JsonElement obj : contentsJson) {
					final JsonObject jobj = obj.getAsJsonObject();
					final String contents = jobj.has("contents") ? jobj.get("contents").getAsString() : "";
					final boolean inline = jobj.has("inline") ? jobj.get("inline").getAsBoolean() : false;
					if (jobj.has("type"))
						switch (jobj.get("type").getAsString()) {
						case "title":
							mtab.addContents(new ManualContent.ManualTitle(contents, inline));
							break;
						case "text":
							mtab.addContents(new ManualContent.ManualText(contents, inline));
							break;
						case "crafting":
							
							break;
						default:
							break;
						}
				}
			}
			
			return mtab;
		}
		
	}
	
}
