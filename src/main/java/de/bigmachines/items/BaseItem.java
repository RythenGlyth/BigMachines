package de.bigmachines.items;

import de.bigmachines.init.ModItemGroups;
import net.minecraft.item.Item;

public class BaseItem extends Item {

	public BaseItem(Properties properties) {
		super(properties);
	}
	
	public BaseItem() {
		this(new Item.Properties()
			.tab(ModItemGroups.MOD_GROUP)
		);
	}
	
	
}
