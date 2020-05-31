package de.bigmachines.init;

import de.bigmachines.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * CreativeTab Registry
 * 
 * @author RythenGlyth
 */

public final class ModCreativeTabs {
    
    public static CreativeTabs modTab;
    public static CreativeTabs materialsTab;
	
	public static void init() {
		modTab = new CreativeTabs(Reference.MOD_ID + "." + "mainTab") {

			@Override
			@Nonnull
			public ItemStack getTabIconItem() {
				return new ItemStack(ModItems.wrench);
			}
		};
		materialsTab = new CreativeTabs(Reference.MOD_ID + "." + "materialsTab") {
			
			@Override
			@Nonnull
			public ItemStack getTabIconItem() {
				return new ItemStack(ModMaterials.ingot_lead);
			}
		};
	}

}