package de.bigmachines.gui;

import de.bigmachines.blocks.IHasGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case 0:
				TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
				if(tileEntity instanceof IHasGui) {
					return ((IHasGui)tileEntity).getGuiClient(player.inventory);
				}
			}
		return null;
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case 0:
				TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
				if(tileEntity instanceof IHasGui) {
					return ((IHasGui)tileEntity).getGuiServer(player.inventory);
				}
				return null;
		}
		return null;
	}
	

	
}
