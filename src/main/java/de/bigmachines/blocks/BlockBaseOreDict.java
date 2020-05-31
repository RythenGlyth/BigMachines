package de.bigmachines.blocks;

import de.bigmachines.interfaces.IInitializer;
import net.minecraft.block.material.Material;
import net.minecraftforge.oredict.OreDictionary;

public class BlockBaseOreDict extends BlockBase implements IInitializer {
	
	final String oreDict;
	
	public BlockBaseOreDict(Material materialIn, String name, String oreDict) {
		super(materialIn, name);
		
		this.oreDict = oreDict;
	}

	@Override
	public void postRegister() {
//		ItemStack ore = new ItemStack(super.itemBlock, 1);
//		System.out.println("ore is null: " + (ore == null));
//		System.out.println("ore is empty: " + ore.isEmpty());
//		System.out.println("next error: " + ore.getItem().delegate.name());
//		IRegistryDelegate d = ore.getItem().delegate;
		
//		OreDictionary.registerOre(oreDict, ore);
		OreDictionary.registerOre(oreDict, getItemBlock());
	}
	
	
	
}
