package de.bigmachines.plugins.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class PluginJEI implements IModPlugin {
	
	@Override
	public void register(IModRegistry registry) {
		registry.addAdvancedGuiHandlers(new ContainerBaseExtraAreas());
	}
	
}
