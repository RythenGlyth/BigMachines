package de.bigmachines.gui;

import java.util.List;

import de.bigmachines.Reference;
import de.bigmachines.config.Config;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ModGuiConfig extends GuiConfig {

	public ModGuiConfig(GuiScreen parentScreen) {
		super(parentScreen, Config.getConfigElements(), Reference.MOD_ID, false, false, I18n.format(Reference.MOD_ID + ".config.title"));
	}
	
	
	
}
