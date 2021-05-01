package de.bigmachines.init;

import de.bigmachines.items.items.ItemWrench;
import de.bigmachines.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

	public static RegistryObject<Item> wrench_item = ITEMS.register("wrench", () -> new ItemWrench());

	// pink items:
	public static RegistryObject<Item> pink_flint_item = ITEMS.register("pink_flint", () -> new Item(new Item.Properties().tab(ModItemGroups.MOD_GROUP)));
	public static RegistryObject<Item> pink_clay_ball_item = ITEMS.register("pink_clay_ball", () -> new Item(new Item.Properties().tab(ModItemGroups.MOD_GROUP)));
	public static RegistryObject<Item> pink_brick_item = ITEMS.register("pink_brick", () -> new Item(new Item.Properties().tab(ModItemGroups.MOD_GROUP)));
	
	public static void init() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
