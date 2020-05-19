package de.bigmachines.init;

import org.lwjgl.input.Keyboard;

import de.bigmachines.Reference;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModKeybinds {
	
	public static KeyBinding wrench;
	
	public static void init() {
		wrench = new KeyBinding("key." + Reference.MOD_ID + ".wrench", Keyboard.KEY_LSHIFT, "key.categories." + Reference.MOD_ID);
		ClientRegistry.registerKeyBinding(wrench);
	}
	
}
