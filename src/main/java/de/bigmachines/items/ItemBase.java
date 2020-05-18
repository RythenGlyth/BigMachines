package de.bigmachines.items;

import de.bigmachines.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/** 
 * Core Item
 * 
 * @author RythenGlyth
 * 
 */

public class ItemBase extends Item {
    
    protected String name;
    
    public ItemBase(String name) {
        setName(name);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, getName()));
		setUnlocalizedName(Reference.MOD_ID + "." + getName());
    }
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
    }
    
}