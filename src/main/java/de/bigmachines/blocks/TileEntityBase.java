package de.bigmachines.blocks;

import de.bigmachines.utils.BlockHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public abstract class TileEntityBase extends TileEntity {
	
	public TileEntityBase() {
		super();
	}
	
	//sync and dirty (save later)
	public void updated() {
		BlockHelper.callBlockUpdate(this.getWorld(), this.getPos());
		markDirty();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		
		this.readCustomNBT(compound, false);
		super.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		this.writeCustomNBT(compound, false);
		super.writeToNBT(compound);
		
		return compound;
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
	}
	
	
	//Synchronise between Client and Server
	@Override
	public NBTTagCompound getUpdateTag() {
		System.out.println("----------------------------------GET-UPDATETAG");
		NBTTagCompound compound = super.getUpdateTag();
		if(compound == null) compound = new NBTTagCompound();
		this.writeCustomNBT(compound, true);
		return compound;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound compound = new NBTTagCompound();
		this.writeCustomNBT(compound, true);
		return new SPacketUpdateTileEntity(getPos(), -1, compound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		this.readCustomNBT(pkt.getNbtCompound(), true);
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		this.readCustomNBT(tag, true);
	}

	
	public void writeCustomNBT(NBTTagCompound compound, boolean updatePacket) {
		
	}
	
	public void readCustomNBT(NBTTagCompound compound, boolean updatePacket) {
		
	}
	
	
}