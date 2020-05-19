package de.bigmachines.blocks;

import de.bigmachines.BigMachines;
import de.bigmachines.Reference;
import de.bigmachines.interfaces.IModelRegister;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

/**
 * Core Block
 * 
 * Represents any block (e. g. ore, storage block, conduit, machine)
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