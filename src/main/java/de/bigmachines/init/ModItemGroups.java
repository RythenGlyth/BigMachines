package de.bigmachines.init;

import de.bigmachines.Reference;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroups {
	
	public static final ItemGroup MOD_GROUP = new ItemGroup(Reference.MOD_ID + "." + "mainTab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Blocks.DIRT);
        }
    };
	
	public static final ItemGroup MATERIAL_GROUP = new ItemGroup(Reference.MOD_ID + "." + "materialsTab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModMaterials.lead_ingot.get());
        }
    };
    
    public static void init() {
    	
    }
	
}
