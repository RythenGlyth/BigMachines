package de.bigmachines.blocks;

import de.bigmachines.init.ModItemGroups;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class BaseBlockItem extends BlockItem {
	
	public BaseBlockItem(Block block) {
		super(block, new Item.Properties()
				.tab(ModItemGroups.MOD_GROUP)
		);
	}
	
}
