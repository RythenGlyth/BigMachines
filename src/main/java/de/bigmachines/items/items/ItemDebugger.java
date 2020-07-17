package de.bigmachines.items.items;

import de.bigmachines.blocks.blocks.pipes.fluidpipe.TileEntityFluidPipe;
import de.bigmachines.init.ModBlocks;
import de.bigmachines.init.ModCreativeTabs;
import de.bigmachines.items.ItemBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDebugger extends ItemBase {
	public ItemDebugger() {
		super("bm_debugger");
		setCreativeTab(ModCreativeTabs.modTab);
		setMaxStackSize(1);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
	                                  EnumFacing facing, float hitX, float hitY, float hitZ) {
		Block clickedAt = worldIn.getBlockState(pos).getBlock();
		
		if (clickedAt.equals(ModBlocks.fluidPipe)) {
			System.out.println("clicked on fluid pipe, updating once:");
			TileEntityFluidPipe fluidPipe = (TileEntityFluidPipe) worldIn.getTileEntity(pos);
			fluidPipe.update();
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
}
