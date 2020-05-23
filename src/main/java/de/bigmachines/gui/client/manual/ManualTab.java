package de.bigmachines.gui.client.manual;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ResourceLocation;

public class ManualTab {
	private ResourceLocation icon;
	private String title;
	private List<ManualContent> contents;
	
	public ManualTab(ResourceLocation icon, String title) {
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
	
}
