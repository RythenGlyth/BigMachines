package de.bigmachines.blocks;

import de.bigmachines.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

/**
 * Core Block
 * 
 * Represents any block (e. g. ore, storage block, conduit, machine)
 * 
 * @author RythenGlyth
 *
 */

public class BlockBase extends Block {
    
	protected final ItemBlock itemBlock;
	
    protected String name;
    
    public BlockBase(final Material materialIn, final String name) {
        super(materialIn);
		this.name = name;
		setRegistryName(new ResourceLocation(Reference.MOD_ID, this.name));
		setUnlocalizedName(Reference.MOD_ID + "." + this.name);
		itemBlock = new ItemBlockBase(this);
    }
    
    @Override
	public Block setSoundType(final SoundType sound) {
		return super.setSoundType(sound);
	}
    
    public ItemBlock getItemBlock() {
		return itemBlock;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}