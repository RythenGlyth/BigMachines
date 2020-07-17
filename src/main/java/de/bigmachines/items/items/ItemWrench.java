package de.bigmachines.items.items;

import de.bigmachines.blocks.blocks.pipes.BlockPipeBase;
import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase;
import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase.PipeAttachment;
import de.bigmachines.init.ModCreativeTabs;
import de.bigmachines.init.ModKeybinds;
import de.bigmachines.items.IInfoProviderShift;
import de.bigmachines.items.ItemBase;
import de.bigmachines.utils.classes.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemWrench extends ItemBase implements IInfoProviderShift {

	public ItemWrench() {
		super("wrench");
		setCreativeTab(ModCreativeTabs.modTab);
		setMaxStackSize(1);
	}

	@Override
	public boolean showDurabilityBar(@Nonnull ItemStack stack) {
		return getDamage(stack) > 0;
	}
	
	/*@Override
	public String getUnlocalizedName(ItemStack stack) {
		String unlocalizedName = super.getUnlocalizedName(stack);
		switch(stack.getMetadata()) {
			case 0: //Copper
				return unlocalizedName + "_copper";
			case 1: //Aluminum
				return unlocalizedName + "_aluminum";
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
	public int getMaxDamage(@Nullable ItemStack stack) {
		/*switch(stack.getMetadata()) {
			case 0: //Copper
				return 200;
			case 1: //Aluminum
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
            items.add(new ItemStack(this, 1, 1)); //Aluminum
            items.add(new ItemStack(this, 1, 2)); //Lead
            items.add(new ItemStack(this, 1, 3)); //Silver
            items.add(new ItemStack(this, 1, 4)); //Tin
        }
	}*/

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		/*ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "type=copper")); //Copper
		ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(getRegistryName(), "type=aluminum")); //Aluminum
		ModelLoader.setCustomModelResourceLocation(this, 2, new ModelResourceLocation(getRegistryName(), "type=lead")); //Lead
		ModelLoader.setCustomModelResourceLocation(this, 3, new ModelResourceLocation(getRegistryName(), "type=silver")); //Silver
		ModelLoader.setCustomModelResourceLocation(this, 4, new ModelResourceLocation(getRegistryName(), "type=tin")); //Tin*/
	}
	
	public static class ScrollHandler {
		
		@SubscribeEvent
		public void onScroll(MouseEvent e) {
			if(ModKeybinds.toolKey.isKeyDown()) {
				if(Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemWrench) {
					int dwheel = e.getDwheel();
					if(dwheel != 0) {
						e.setCanceled(true);
						Pair<EnumFacing, BlockPos> selectedSide = BlockPipeBase.getSelectedRayTrace();
						if(selectedSide != null) {
							TileEntity tile = Minecraft.getMinecraft().world.getTileEntity(selectedSide.y);
							if(tile instanceof TileEntityPipeBase) {
								TileEntityPipeBase tileEntityPipeBase = (TileEntityPipeBase) tile;
								PipeAttachment attachment = tileEntityPipeBase.getAttachment(selectedSide.x);
								attachment.cycleThrough(dwheel > 0);
								attachment.sendUpdateToServer(selectedSide.y, selectedSide.x);
								//BigMachines.networkHandlerMain.sendToServer(new MessageChangePipeAttachmentMode(selectedSide.y, selectedSide.x, attachment.getRedstoneMode(), attachment.isWhitelist(), attachment.canExtract(), attachment.canInsert()));
								
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void addShiftInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> toolTip, ITooltipFlag flags) {
		toolTip.add(I18n.format("info.bigmachines.wrench.shift"));
	}
	
	
}
