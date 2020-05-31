package de.bigmachines.items;

import de.bigmachines.BigMachines;
import de.bigmachines.Reference;
import de.bigmachines.interfaces.IModelRegister;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** 
 * Core Item
 * 
 * @author RythenGlyth
 * 
 */

public class ItemBase extends Item implements IModelRegister {
    
    protected String name;
    
    public ItemBase(String name) {
        this.name = name;
		setRegistryName(new ResourceLocation(Reference.MOD_ID, this.name));
		setUnlocalizedName(Reference.MOD_ID + "." + this.name);
		BigMachines.proxy.addIModelRegister(this);
    }
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
    
}