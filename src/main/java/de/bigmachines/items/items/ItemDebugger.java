package de.bigmachines.items.items;

import de.bigmachines.blocks.blocks.pipes.fluidpipe.TileEntityFluidPipe;
import de.bigmachines.init.ModBlocks;
import de.bigmachines.init.ModCreativeTabs;
import de.bigmachines.init.ModKeybinds;
import de.bigmachines.items.IInfoProviderShift;
import de.bigmachines.items.ItemBase;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class ItemDebugger extends ItemBase implements IInfoProviderShift {
	
	public ItemDebugger() {
		super("debugger");
		setCreativeTab(ModCreativeTabs.modTab);
		setMaxStackSize(1);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		Block clickedAt = worldIn.getBlockState(pos).getBlock();
		
		if (clickedAt.equals(ModBlocks.fluidPipe)) {
			System.out.println("clicked on fluid pipe, updating once:");
			TileEntityFluidPipe fluidPipe = (TileEntityFluidPipe) worldIn.getTileEntity(pos);
			if (fluidPipe.getNetwork() == null) System.out.println("horseshit");
			else fluidPipe.getNetwork().update();
		}
		/*
		System.out.println("clicked on stone");
		ItemStack debugger = player.getHeldItem(hand);
		if (wrench.getItem() instanceof ItemWrench) {
			System.out.println("using a wrench");
			NBTTagCompound compound = wrench.hasTagCompound() ? wrench.getTagCompound() : new NBTTagCompound();
			if (compound.hasKey("debug-first-pos")) {
				System.out.println("first person is set:");
				BlockPos firstpos = NBTHelper.readTagToBlockPos(compound.getCompoundTag("debug-first-pos"));
				EnumFacing connecting = BlockHelper.getConnectingFace(firstpos, pos);
				player.sendMessage(new TextComponentString(connecting == null ? "no connection" : connecting.toString()));
				System.out.println(connecting);
			} else compound.setTag("debug-first-pos", NBTHelper.writeBlockPosToTag(pos));
			wrench.setTagCompound(compound);
		}
		*/
		
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	public void setMode(ItemStack stack, Mode mode) {
		NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
		tag.setString("mode", mode.toString());
		stack.setTagCompound(tag);
	}
	
	public Mode getMode(ItemStack stack) {
		NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
		if(tag.hasKey("mode")) {
			try {
				return Mode.valueOf(tag.getString("mode"));
			} catch(Exception ex) {}
		}
		return Mode.READ_NBT;
	}
	
	public static class ScrollHandler {
		
		@SubscribeEvent
		public void onScroll(MouseEvent e) {
			if(ModKeybinds.toolKey.isKeyDown()) {
				if(Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemDebugger) {
					int dwheel = e.getDwheel();
					if(dwheel != 0) {
						e.setCanceled(true);
						
					}
				}
			}
		}
	}
	
	public static enum Mode {
		
		UPDATE_TILEENTITY("update_tileentity"),
		READ_NBT("read_nbt");
		
		private String name;
		
		Mode(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
	}

	@Override
	public void addShiftInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> toolTip, ITooltipFlag flags) {
		toolTip.add(I18n.format("info.bigmachines.debugger.shift1"));
		toolTip.add(I18n.format("info.bigmachines.debugger.shift2"));
	}
}
