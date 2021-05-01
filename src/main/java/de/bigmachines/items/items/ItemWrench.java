package de.bigmachines.items.items;

import de.bigmachines.items.BaseItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import de.bigmachines.init.ModItemGroups;

import javax.annotation.Nonnull;

public class ItemWrench extends BaseItem {

	public ItemWrench() {
		super((new Item.Properties()).
				durability(-1).stacksTo(1).tab(ModItemGroups.MOD_GROUP));
	}

	@Override
	public boolean showDurabilityBar(@Nonnull ItemStack stack) {
		return false;
	}
	
}
