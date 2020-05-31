package de.bigmachines.config;

import de.bigmachines.Reference;
import de.bigmachines.utils.EnumHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration for the mod
 * @author RythenGlyth
 *
 */

public class Config {

	private static boolean pipeStatusOverlayAlwaysOn;
	private static boolean displayCookTime;
	private static boolean displayHarvestLevel;
	private static boolean showHUDWhileChatting;
	private static HUDPostitions hudPosition;
	
	public enum HUDPostitions {
		TOP_LEFT,
		TOP_CENTER,
		TOP_RIGHT,
		LEFT,
		RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT
	}

	public static boolean isPipeStatusOverlayAlwaysOn() {
		return pipeStatusOverlayAlwaysOn;
	}

	public static boolean isDisplayCookTime() {
		return displayCookTime;
	}

	public static boolean isDisplayHarvestLevel() {
		return displayHarvestLevel;
	}

	public static boolean isShowHUDWhileChatting() {
		return showHUDWhileChatting;
	}

	public static HUDPostitions getHudPosition() {
		return hudPosition;
	}

	public static Configuration config;
    private static File configDir;
	
	public static void init(File configDir) {
		if (config == null) {
            configDir = new File(configDir, Reference.MOD_ID);
            configDir.mkdir();
            Config.configDir = configDir;
            config = new Configuration(new File(configDir, Reference.MOD_ID + ".cfg"));
            loadConfig();
        }
	}
	
	public static File getConfigDir() {
        return configDir;
    }
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equalsIgnoreCase(Reference.MOD_ID)) {
            loadConfig();
        }
	}
	
	public static List<IConfigElement> getConfigElements() {
		return new ArrayList<>(new ConfigElement(config.getCategory("general")).getChildElements());
    }
	
	private static void loadConfig() {
		// deprecated because "localization should rarely happen on the server" BUT it does
		Property property = config.get("general", "displayCookTime", true);
		property.setComment(I18n.translateToLocal("config." + Reference.MOD_ID + ".displayCookTime.description"));
		property.setLanguageKey("config." + Reference.MOD_ID + ".displayCookTime.title");
        displayCookTime = property.getBoolean();
        
		property = config.get("general", "displayHarvestLevel", true);
		property.setComment(I18n.translateToLocal("config." + Reference.MOD_ID + ".displayHarvestLevel.description"));
		property.setLanguageKey("config." + Reference.MOD_ID + ".displayHarvestLevel.title");
        displayHarvestLevel = property.getBoolean();
        
		property = config.get("general", "pipeStatusOverlayAlwaysOn", true);
		property.setComment(I18n.translateToLocal("config." + Reference.MOD_ID + ".pipeStatusOverlayAlwaysOn.description"));
		property.setLanguageKey("config." + Reference.MOD_ID + ".pipeStatusOverlayAlwaysOn.title");
		pipeStatusOverlayAlwaysOn = property.getBoolean();
        
        property = config.get("general", "showHUDWhileChatting", true);
		property.setComment(I18n.translateToLocal("config." + Reference.MOD_ID + ".showHUDWhileChatting.description"));
		property.setLanguageKey("config." + Reference.MOD_ID + ".showHUDWhileChatting.title");
        showHUDWhileChatting = property.getBoolean();
        
        //property = Config.config.get("general", "hudPosition", Settings.HUDPostitions.BOTTOM_RIGHT.toString(), 
        //		I18n.translateToLocal(Reference.MOD_ID + ".config.hudPosition.description"), EnumUtils.getNames(Settings.HUDPostitions.class));
        property = config.get("general", "hudPosition", HUDPostitions.BOTTOM_RIGHT.toString());
		property.setValidValues(EnumHelper.getNames(HUDPostitions.class));
		property.setComment(I18n.translateToLocal("config." + Reference.MOD_ID + ".hudPosition.description"));
		property.setLanguageKey("config." + Reference.MOD_ID + ".hudPosition.title");
        hudPosition = HUDPostitions.valueOf(property.getString());
        
        if (config.hasChanged()) {
        	config.save();
        }
	}
}
