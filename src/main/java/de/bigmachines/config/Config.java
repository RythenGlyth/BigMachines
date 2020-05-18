package de.bigmachines.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.bigmachines.Reference;
import de.bigmachines.utils.EnumHelper;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Configuration for the mod
 * @author RythenGlyth
 *
 */

public class Config {
	
	public static boolean displayCookTime;
	public static boolean showHUDWhileChatting;
	public static HUDPostitions hudPosition;
	
	public enum HUDPostitions {
		TOP_LEFT,
		TOP_CENTER,
		TOP_RIGHT,
		LEFT,
		RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT
	}

	
	
	public static Configuration config;
    private static File configDir;
	
	public static void init(File configDir) {
		if (Config.config == null) {
            configDir = new File(configDir, Reference.MOD_ID);
            configDir.mkdir();
            Config.configDir = configDir;
            Config.config = new Configuration(new File(configDir, Reference.MOD_ID + ".cfg"));
            loadConfig();
        }
	}
	
	public static File getConfigDir() {
        return Config.configDir;
    }
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equalsIgnoreCase(Reference.MOD_ID)) {
            loadConfig();
        }
	}
	
	public static List<IConfigElement> getConfigElements() {
        final List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.addAll(new ConfigElement(Config.config.getCategory("general")).getChildElements());
        return list;
    }
	
	private static void loadConfig() {
		Property property = Config.config.get("general", "displayCookTime", true);
		property.setComment(I18n.format(Reference.MOD_ID + ".config.displayCookTime.description"));
		property.setLanguageKey(Reference.MOD_ID + ".config.displayCookTime.title");
        displayCookTime = property.getBoolean();
        
        property = Config.config.get("general", "showHUDWhileChatting", true);
		property.setComment(I18n.format(Reference.MOD_ID + ".config.showHUDWhileChatting.description"));
		property.setLanguageKey(Reference.MOD_ID + ".config.showHUDWhileChatting.title");
        showHUDWhileChatting = property.getBoolean();
        
        //property = Config.config.get("general", "hudPosition", Settings.HUDPostitions.BOTTOM_RIGHT.toString(), 
        //		I18n.translateToLocal(Reference.MOD_ID + ".config.hudPosition.description"), EnumUtils.getNames(Settings.HUDPostitions.class));
        property = Config.config.get("hud", "hudPosition", HUDPostitions.BOTTOM_RIGHT.toString());
		property.setValidValues(EnumHelper.getNames(HUDPostitions.class));
		property.setComment(I18n.format(Reference.MOD_ID + ".config.hudPosition.description"));
		property.setLanguageKey(Reference.MOD_ID + ".config.hudPosition.title");
        hudPosition = HUDPostitions.valueOf(property.getString());
        
        
        if (Config.config.hasChanged()) {
        	Config.config.save();
        }
	}

	
}