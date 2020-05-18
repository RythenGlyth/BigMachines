package de.bigmachines.blocks;

import de.bigmachines.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

/**
 * Core Block
 * 
 * @author RythenGlyth
 *
 */

public class BlockBase extends Block {
    
	protected ItemBlock itemBlock;
	
    protected String name;
    
    public BlockBase(Material materialIn, String name) {
        super(materialIn);
		setName(name);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, getName()));
		setUnlocalizedName(Reference.MOD_ID + "." + getName());
		this.itemBlock = new ItemBlockBase(this);
    }
    
    public ItemBlock getItemBlock() {
		return itemBlock;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}