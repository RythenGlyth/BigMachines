package de.bigmachines.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.bigmachines.Reference;
import de.bigmachines.utils.EnumHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.text.translation.LanguageMap;
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

	public static boolean pipeStatusOverlayAlwaysOn;
	public static boolean displayCookTime;
	public static boolean displayHarvestLevel;
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
        	System.out.println("saving");
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
		System.out.println(I18n.class);
		
		// deprecated because "localization should rarely happen on the server" BUT it does
		Property property = Config.config.get("general", "displayCookTime", true);
		property.setComment(I18n.translateToLocal("config." + Reference.MOD_ID + ".displayCookTime.description"));
		property.setLanguageKey("config." + Reference.MOD_ID + ".displayCookTime.title");
        displayCookTime = property.getBoolean();
        
		property = Config.config.get("general", "displayHarvestLevel", true);
		property.setComment(I18n.translateToLocal("config." + Reference.MOD_ID + ".displayHarvestLevel.description"));
		property.setLanguageKey("config." + Reference.MOD_ID + ".displayHarvestLevel.title");
        displayHarvestLevel = property.getBoolean();
        
		property = Config.config.get("general", "pipeStatusOverlayAlwaysOn", true);
		property.setComment(I18n.translateToLocal("config." + Reference.MOD_ID + ".pipeStatusOverlayAlwaysOn.description"));
		property.setLanguageKey("config." + Reference.MOD_ID + ".pipeStatusOverlayAlwaysOn.title");
		pipeStatusOverlayAlwaysOn = property.getBoolean();
        
        property = Config.config.get("general", "showHUDWhileChatting", true);
		property.setComment(I18n.translateToLocal("config." + Reference.MOD_ID + ".showHUDWhileChatting.description"));
		property.setLanguageKey("config." + Reference.MOD_ID + ".showHUDWhileChatting.title");
        showHUDWhileChatting = property.getBoolean();
        
        //property = Config.config.get("general", "hudPosition", Settings.HUDPostitions.BOTTOM_RIGHT.toString(), 
        //		I18n.translateToLocal(Reference.MOD_ID + ".config.hudPosition.description"), EnumUtils.getNames(Settings.HUDPostitions.class));
        property = Config.config.get("general", "hudPosition", HUDPostitions.BOTTOM_RIGHT.toString());
		property.setValidValues(EnumHelper.getNames(HUDPostitions.class));
		property.setComment(I18n.translateToLocal("config." + Reference.MOD_ID + ".hudPosition.description"));
		property.setLanguageKey("config." + Reference.MOD_ID + ".hudPosition.title");
        hudPosition = HUDPostitions.valueOf(property.getString());
        
        if (Config.config.hasChanged()) {
        	Config.config.save();
        }
	}
}
