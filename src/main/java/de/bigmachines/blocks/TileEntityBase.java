package de.bigmachines.blocks;

import de.bigmachines.utils.BlockHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

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
	@Nonnull
	public void readFromNBT(@Nonnull NBTTagCompound compound) {
		
		this.readCustomNBT(compound, false);
		super.readFromNBT(compound);
	}
	
	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
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
	@Nonnull
	public NBTTagCompound getUpdateTag() {
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
	public void onDataPacket(@Nonnull NetworkManager net, @Nonnull SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		this.readCustomNBT(pkt.getNbtCompound(), true);
	}
	
	@Override
	public void handleUpdateTag(@Nonnull NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		this.readCustomNBT(tag, true);
	}

	
	public void writeCustomNBT(NBTTagCompound compound, boolean updatePacket) {
		
	}
	
	public void readCustomNBT(NBTTagCompound compound, boolean updatePacket) {
		
	}
	
	
}
