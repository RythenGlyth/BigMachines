package de.bigmachines.config;

import java.util.ArrayList;
import java.util.List;

import de.bigmachines.Reference;
import de.bigmachines.gui.client.manual.ManualTab;
import net.minecraft.util.ResourceLocation;

public class ManualLoader {
    private static final List<ManualTab> tabs = new ArrayList<ManualTab>();

    public static List<ManualTab> getTabs() {
		return new ArrayList<ManualTab>(tabs);
	}
    
    public static void init() {
    	for (int i = 25; i-- > 0;)
    	tabs.add(new ManualTab(new ResourceLocation(Reference.MOD_ID, "textures/item/wrench.png"), "servus" + i));
    }
}
