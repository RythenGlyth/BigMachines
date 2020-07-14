package de.bigmachines.items.items.manual;

import de.bigmachines.init.ModCreativeTabs;
import de.bigmachines.init.ModItems;
import de.bigmachines.items.ItemBase;
import de.bigmachines.items.items.ItemArmorWest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemPlate extends ItemBase {
	private final double ratio;
	
	public ItemPlate(final String name, final double ratio) {
		super(name);
		this.ratio = ratio;
		setMaxStackSize(8);
		setCreativeTab(ModCreativeTabs.modTab);
	}
	
	@Override
	@Nonnull
	public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		System.out.println("item use");
		@Nullable ItemStack playerArmor = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		if (playerArmor != null && playerArmor.getItem().equals(ModItems.armorWest)) {
			if (((ItemArmorWest) playerArmor.getItem()).addPlate(playerArmor,ratio)) {
				player.getHeldItem(hand).shrink(1);
				return EnumActionResult.SUCCESS;
			}
		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	protected double getRatio() {
		return ratio;
	}
}
