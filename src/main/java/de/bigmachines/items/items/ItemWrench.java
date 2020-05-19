package de.bigmachines.items.items;

import de.bigmachines.init.ModCreativeTabs;
import de.bigmachines.init.ModKeybinds;
import de.bigmachines.interfaces.IModelRegister;
import de.bigmachines.items.ItemBase;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWrench extends ItemBase {

	public ItemWrench() {
		super("wrench");
		setCreativeTab(ModCreativeTabs.modTab);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return getDamage(stack) > 0;
	}
	
	/*@Override
	public String getUnlocalizedName(ItemStack stack) {
		String unlocalizedName = super.getUnlocalizedName(stack);
		switch(stack.getMetadata()) {
			case 0: //Copper
				return unlocalizedName + "_copper";
			case 1: //Aluminium
				return unlocalizedName + "_aluminium";
			case 2: //Lead
				return unlocalizedName + "_lead";
			case 3: //Silver
				return unlocalizedName + "_silver";
			case 4: //Tin
				return unlocalizedName + "_tin";
			default:
				return unlocalizedName;
		}
	}*/
	
	@Override
	public int getMaxDamage(ItemStack stack) {
		/*switch(stack.getMetadata()) {
			case 0: //Copper
				return 200;
			case 1: //Aluminium
				return 300;
			case 2: //Lead
				return 400;
			case 3: //Silver
				return 400;
			case 4: //Tin
				return 300;
		}*/
		return 1000;
	}
	
	/*@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
            items.add(new ItemStack(this, 1, 0)); //Copper
            items.add(new ItemStack(this, 1, 1)); //Aluminium
            items.add(new ItemStack(this, 1, 2)); //Lead
            items.add(new ItemStack(this, 1, 3)); //Silver
            items.add(new ItemStack(this, 1, 4)); //Tin
        }
	}*/

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		/*ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "type=copper")); //Copper
		ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(getRegistryName(), "type=aluminium")); //Aluminium
		ModelLoader.setCustomModelResourceLocation(this, 2, new ModelResourceLocation(getRegistryName(), "type=lead")); //Lead
		ModelLoader.setCustomModelResourceLocation(this, 3, new ModelResourceLocation(getRegistryName(), "type=silver")); //Silver
		ModelLoader.setCustomModelResourceLocation(this, 4, new ModelResourceLocation(getRegistryName(), "type=tin")); //Tin*/
	}
	
	public static class ScrollHandler {
		
		@SubscribeEvent
		public void onScroll(MouseEvent e) {
			if(ModKeybinds.wrench.isKeyDown()) {
				int dwheel = e.getDwheel();
				if(dwheel > 0) {
					System.out.println(dwheel);
					e.setCanceled(true);
				} else if(dwheel < 0) {
					System.out.println(dwheel);
					e.setCanceled(true);
				}
			}
		}
		
	}
	
	
}
