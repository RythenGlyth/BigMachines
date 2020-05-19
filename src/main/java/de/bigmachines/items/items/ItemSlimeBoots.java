package de.bigmachines.items.items;

import java.util.List;

import de.bigmachines.BigMachines;
import de.bigmachines.Reference;
import de.bigmachines.interfaces.IModelRegister;
import de.bigmachines.items.IInfoProviderShift;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Slime Boots (not working)
 * @author RythenGlyth
 *
 */

public class ItemSlimeBoots extends ItemArmor implements IInfoProviderShift, IModelRegister {
    
    protected String name;

    public ItemSlimeBoots() {
        super(EnumHelper.addArmorMaterial("slime", "slime_boots", 0, new int[] {3, 6, 8, 3}, 10, SoundEvents.BLOCK_SLIME_PLACE, 0), 2, EntityEquipmentSlot.FEET);
        
        this.name = "slime_boots";
		setRegistryName(new ResourceLocation(Reference.MOD_ID, this.name));
		setUnlocalizedName(Reference.MOD_ID + "." + this.name);
		
        BigMachines.proxy.addIModelRegister(this);
    }
    
    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
    	if(player == null) return;
    	
        if(!(itemStack.getItem() instanceof ItemSlimeBoots)) {
        	return;
        }

        if(/*player. < 0 &&*/ player.fallDistance > 1 && !player.isSneaking()) {
        	if(player.onGround) {
        		System.out.println("sas");
            	player.fallDistance = 0;
            	
            	System.out.println("as");
        		player.motionY *= -0.9;
        		
        		player.isAirBorne = true;
        		player.onGround = false;
        		
            	player.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1f, 1f);
        	}
        }
    }
    
	@Override
	public void addShiftInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> toolTip, ITooltipFlag flags) {
		toolTip.add(I18n.format("info.bigmachines.slimeBoots.shift"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
    
}