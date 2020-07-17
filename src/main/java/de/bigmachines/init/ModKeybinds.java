package de.bigmachines.init;

import de.bigmachines.Reference;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public final class ModKeybinds {
	
	public static KeyBinding toolKey;
	
	public static void init() {
		toolKey = new KeyBinding("key." + Reference.MOD_ID + ".toolKey", Keyboard.KEY_LMENU, "key.categories." + Reference.MOD_ID);
		ClientRegistry.registerKeyBinding(toolKey);
	}
	
}
