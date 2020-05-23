package de.bigmachines.blocks.blocks.pipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import de.bigmachines.blocks.TileEntityBase;
import de.bigmachines.utils.BlockHelper;
import de.bigmachines.utils.NBTHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityPipeBase extends TileEntityBase {
	
	public int pass;
	
	//protected HashSet<EnumFacing> attachments;
	protected HashMap<EnumFacing, PipeAttachment> attachments;
	protected Capability capability;
	
	public TileEntityPipeBase(Capability capability) {
		super();
		attachments = new HashMap<EnumFacing, PipeAttachment>();
		this.capability = capability;
	}
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		this.pass = pass;
		return true;
	}
	
	public HashMap<EnumFacing, PipeAttachment> getAttachments() {
		return attachments;
	}
	
	public void onBlockPlaced(IBlockState state, EntityLivingBase placer, ItemStack stack) {
		updateAttachments();
	}
	
	public void updateAttachments() {
		HashMap<EnumFacing, PipeAttachment> lastAttachments = new HashMap<EnumFacing, PipeAttachment>(attachments);
		attachments.clear();
		for(EnumFacing side : EnumFacing.VALUES) {
			TileEntity adjacentTileEntity = BlockHelper.getAdjacentTileEntity(this, side);
			if(adjacentTileEntity != null && adjacentTileEntity.hasCapability(capability, side.getOpposite())) attachments.put(side, lastAttachments.containsKey(side) ? lastAttachments.get(side) : new PipeAttachment());
		}
		if(!lastAttachments.keySet().equals(getAttachments().keySet())) updated();
	}
	
	public boolean hasAttachment(EnumFacing side) {
		return attachments.containsKey(side);
	}
	
	public PipeAttachment getAttachment(EnumFacing side) {
		return attachments.get(side);
	}
    
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getPos(), getPos().add(1, 1, 1));
	}
    
	@Override
	public void readCustomNBT(NBTTagCompound compound, boolean updatePacket) {
		attachments.clear();
		for(String key : compound.getCompoundTag("Attachments").getKeySet()) {
			attachments.put(EnumFacing.byName(key), new PipeAttachment(compound.getCompoundTag("Attachments").getCompoundTag(key)));
		}
		/*byte attachmentBytes = compound.getByte("Attachments");
		if((attachmentBytes & 1 << 0) > 0) attachments.put(EnumFacing.DOWN, new PipeAttachment());
		if((attachmentBytes & 1 << 1) > 0) attachments.put(EnumFacing.EAST, new PipeAttachment());
		if((attachmentBytes & 1 << 2) > 0) attachments.put(EnumFacing.NORTH, new PipeAttachment());
		if((attachmentBytes & 1 << 3) > 0) attachments.put(EnumFacing.SOUTH, new PipeAttachment());
		if((attachmentBytes & 1 << 4) > 0) attachments.put(EnumFacing.UP, new PipeAttachment());
		if((attachmentBytes & 1 << 5) > 0) attachments.put(EnumFacing.WEST, new PipeAttachment());*/
		
		super.readCustomNBT(compound, updatePacket);
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound compound, boolean updatePacket) {
		/*compound.setByte("Attachments", (byte)(
				  (attachments.contains(EnumFacing.DOWN) ?  1 << 0 : 0x0)
				| (attachments.contains(EnumFacing.EAST) ?  1 << 1 : 0x0)
				| (attachments.contains(EnumFacing.NORTH) ? 1 << 2 : 0x0)
				| (attachments.contains(EnumFacing.SOUTH) ? 1 << 3 : 0x0)
				| (attachments.contains(EnumFacing.UP) ?    1 << 4 : 0x0)
				| (attachments.contains(EnumFacing.WEST) ?  1 << 5 : 0x0)
		));*/
		/*compound.setByte("Attachments", (byte)NBTHelper.writeBooleansToInt(
				attachments.containsKey(EnumFacing.DOWN),
				attachments.containsKey(EnumFacing.EAST),
				attachments.containsKey(EnumFacing.NORTH),
				attachments.containsKey(EnumFacing.SOUTH),
				attachments.containsKey(EnumFacing.UP),
				attachments.containsKey(EnumFacing.WEST)
		));*/
		NBTTagCompound attachments = new NBTTagCompound();
		
		for(Map.Entry<EnumFacing, PipeAttachment> attachment : getAttachments().entrySet()) {
			attachments.setTag(attachment.getKey().toString(), attachment.getValue().getNBTTag());
		}
		
		compound.setTag("Attachments", attachments);
		super.writeCustomNBT(compound, updatePacket);
		
	}
	
	public static class PipeAttachment {
		
		protected boolean canExtract;
		protected boolean canInsert;
		
		public PipeAttachment() {
			this(true, true);
		}
		
		public PipeAttachment(NBTTagCompound attachmentTag) {
			this(
					attachmentTag.hasKey("canExtract") ? attachmentTag.getBoolean("canExtract") : true,
					attachmentTag.hasKey("canInsert") ? attachmentTag.getBoolean("canInsert") : true
			);
		}
		
		public PipeAttachment(boolean canExtract, boolean canInsert) {
			this.canExtract = canExtract;
			this.canInsert = canInsert;
		}
		
		public boolean canExtract() {
			return canExtract;
		}
		
		public boolean canInsert() {
			return canInsert;
		}
		
		public NBTTagCompound getNBTTag() {
			NBTTagCompound attachmentTag = new NBTTagCompound();
			attachmentTag.setBoolean("canExtract", canExtract);
			attachmentTag.setBoolean("canInsert", canInsert);
			return attachmentTag;
		}
		
	}
	
}
