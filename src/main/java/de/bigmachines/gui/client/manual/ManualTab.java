package de.bigmachines.gui.client.manual;

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class ManualTab {
	private final ResourceLocation icon;
	private final String title;
	private final List<ManualContent> contents;
	
	private ManualTab(ResourceLocation icon, String title) {
		super();
		this.icon = icon;
		this.title = title;
		contents = new ArrayList<>();
	}
	
	public boolean addContents(ManualContent c) {
		return contents.add(c);
	}
	
	public List<ManualContent> getContents() {
		return new ArrayList<>(contents);
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
				final JsonArray contentsJson = tab.get("contents").getAsJsonArray();
				for (JsonElement obj : contentsJson) {
					final JsonObject jobj = obj.getAsJsonObject();
					if (jobj.has("type")) {
						switch (jobj.get("type").getAsString()) {
							case "text":
								mtab.addContents(new ManualContent.ManualText(jobj.has("text") ? jobj.get("text").getAsString() : "", jobj.has("scale") ? jobj.get("scale").getAsDouble() : 0));
								break;
							case "space":
								mtab.addContents(new ManualContent.ManualSpace(jobj.has("height") ? jobj.get("height").getAsInt() : 0));
								break;
							case "crafting":
								mtab.addContents(new ManualContent.ManualCrafting(jobj.has("location") ? jobj.get("location").getAsString() : ""));
								break;
							default:
								break;
						}
					}
				}
			}
			
			return mtab;
		}
		
	}
	
}
