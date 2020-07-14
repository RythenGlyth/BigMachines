package de.bigmachines.items.items;

import de.bigmachines.BigMachines;
import de.bigmachines.Reference;
import de.bigmachines.init.ModCreativeTabs;
import de.bigmachines.interfaces.IModelRegister;
import de.bigmachines.items.IInfoProviderShift;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.EnumHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemArmorWest extends ItemArmor implements IInfoProviderShift, IModelRegister, ISpecialArmor {
	protected final String name = "armor_west";
	
		public ItemArmorWest() {
			super(
					  EnumHelper.addArmorMaterial("armor_west", "armor_west",
							    0, new int[] {3, 3, 3, 3}, 10,
							    SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0),
					  2, EntityEquipmentSlot.CHEST
			);
			setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
			setUnlocalizedName(Reference.MOD_ID + "." + name);
			BigMachines.proxy.addIModelRegister(this);
			setCreativeTab(ModCreativeTabs.modTab);
		}
	
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0,
				  new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
	public boolean addPlate(final ItemStack armor, final double ratio) {
		NBTTagCompound compound = armor.hasTagCompound() ? armor.getTagCompound() : new NBTTagCompound();
		compound.setDouble("plateCount", compound.hasKey("plateCount") ? compound.getDouble("plateCount") + ratio : ratio);
		armor.setTagCompound(compound);
		return true;
	}
	
	@Override
	public void addShiftInformation(final ItemStack itemStack, final EntityPlayer entityPlayer, final List<String> toolTip, final ITooltipFlag flags) {
		NBTTagCompound compound = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
		double ratio = compound.hasKey("plateCount") ? compound.getDouble("plateCount") : 0;
		toolTip.add(I18n.format("info.bigmachines.armor_west.shift") + " " + String.format("%.3f", ratio));
	}
	
	@Nullable
	@Override
	public ModelBiped getArmorModel(final EntityLivingBase entityLiving, final ItemStack itemStack, final EntityEquipmentSlot armorSlot, final ModelBiped _default) {
		return super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
	}
	
	@Override
	public ArmorProperties getProperties(final EntityLivingBase player, @Nonnull final ItemStack armor, final DamageSource source, final double damage, final int slot) {
			// ratio: 1 no damge, 0 all damage
		return new ArmorProperties(1, 1, Integer.MAX_VALUE);
	}
	
	@Override
	public int getArmorDisplay(final EntityPlayer player, @Nonnull final ItemStack armor, final int slot) {
		return 8;
	}
	
	@Override
	public void damageArmor(final EntityLivingBase entity, @Nonnull final ItemStack stack, final DamageSource source, final int damage, final int slot) {
		System.out.println("damage");
	}

}
