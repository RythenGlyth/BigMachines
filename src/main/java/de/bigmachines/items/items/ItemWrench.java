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
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public boolean showDurabilityBar(@Nonnull ItemStack stack) {
		return getDamage(stack) > 0;
	}
	
	@Override
	public int getMaxDamage(@Nullable ItemStack stack) {
		return 1000;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
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
		toolTip.add(I18n.format("info.bigmachines.wrench.shift1"));
		toolTip.add(I18n.format("info.bigmachines.wrench.shift2"));
	}
	
	
}
