package de.bigmachines.blocks;

import de.bigmachines.interfaces.IInitializer;
import net.minecraft.block.material.Material;
import net.minecraftforge.oredict.OreDictionary;

public class BlockBaseOreDict extends BlockBase implements IInitializer {
	
	String oreDict;
	
	public BlockBaseOreDict(Material materialIn, String name, String oreDict) {
		super(materialIn, name);
		
		this.oreDict = oreDict;
	}

	@Override
	public void postRegister() {
		OreDictionary.registerOre(oreDict, this);
	}
	
	
	
}
