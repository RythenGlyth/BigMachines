package de.bigmachines.items;

import de.bigmachines.interfaces.IInitializer;
import net.minecraftforge.oredict.OreDictionary;

public class ItemBaseOreDict extends ItemBase implements IInitializer {
	
	final String oreDict;

	public ItemBaseOreDict(String name, String oreDict) {
		super(name);
		this.oreDict = oreDict;
	}

	@Override
	public void postRegister() {
		OreDictionary.registerOre(oreDict, this);
	}
	
	
	
}
