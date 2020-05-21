package de.bigmachines.init;

import de.bigmachines.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 * CreativeTab Registry
 * 
 * @author RythenGlyth
 */

public class ModCreativeTabs {
    
    public static CreativeTabs modTab;
    public static CreativeTabs materialsTab;
	
	public static void init() {
		modTab = new CreativeTabs(Reference.MOD_ID + "." + "mainTab") {
			
			@Override
			public ItemStack getTabIconItem() {
				return new ItemStack(ModItems.wrench);
			}
		};
		materialsTab = new CreativeTabs(Reference.MOD_ID + "." + "materialsTab") {
			
			@Override
			public ItemStack getTabIconItem() {
				return new ItemStack(ModMaterials.ingot_lead);
			}
		};
	}

}