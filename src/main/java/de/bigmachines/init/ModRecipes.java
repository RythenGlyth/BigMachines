package de.bigmachines.init;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {
	
    public static void preInit() {
    	GameRegistry.addSmelting(ModMaterials.ore_aluminium, new ItemStack(ModMaterials.ingot_aluminium), 1.0F);
    	GameRegistry.addSmelting(ModMaterials.ore_copper, new ItemStack(ModMaterials.ingot_copper), 1.0F);
    	GameRegistry.addSmelting(ModMaterials.ore_lead, new ItemStack(ModMaterials.ingot_lead), 1.0F);
    	GameRegistry.addSmelting(ModMaterials.ore_silver, new ItemStack(ModMaterials.ingot_silver), 1.0F);
    	GameRegistry.addSmelting(ModMaterials.ore_tin, new ItemStack(ModMaterials.ingot_tin), 1.0F);
    }
	
}
