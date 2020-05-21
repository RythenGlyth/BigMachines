package de.bigmachines.blocks.blocks.pipes;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import de.bigmachines.blocks.TileEntityBase;
import de.bigmachines.utils.BlockHelper;
import de.bigmachines.utils.NBTHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityPipeBase extends TileEntityBase {
	
	protected List<EnumFacing> attachments;
	protected Capability capability;
	
	public TileEntityPipeBase(Capability capability) {
		super();
		attachments = new ArrayList<EnumFacing>();
		this.capability = capability;
	}
	
	public List<EnumFacing> getAttachments() {
		return attachments;
	}
	
	public void onBlockPlaced(IBlockState state, EntityLivingBase placer, ItemStack stack) {
		updateAttachments();
	}
	
	public void updateAttachments() {
		List<EnumFacing> lastAttachments = new ArrayList<>(attachments);
		attachments.clear();
		for(EnumFacing side : EnumFacing.VALUES) {
			TileEntity adjacentTileEntity = BlockHelper.getAdjacentTileEntity(this, side);
			if(adjacentTileEntity != null && adjacentTileEntity.hasCapability(capability, side.getOpposite())) attachments.add(side);
		}
		if(!lastAttachments.equals(attachments)) updated();
	}
	
	public boolean hasAttachment(EnumFacing side) {
		//System.out.println(attachments);
		return attachments.contains(side);
	}
	
	@Override
	public void readCustomNBT(NBTTagCompound compound, boolean updatePacket) {
		byte attachmentBytes = compound.getByte("Attachments");
		attachments.clear();
		if((attachmentBytes & 1 << 0) > 0) attachments.add(EnumFacing.DOWN);
		if((attachmentBytes & 1 << 1) > 0) attachments.add(EnumFacing.EAST);
		if((attachmentBytes & 1 << 2) > 0) attachments.add(EnumFacing.NORTH);
		if((attachmentBytes & 1 << 3) > 0) attachments.add(EnumFacing.SOUTH);
		if((attachmentBytes & 1 << 4) > 0) attachments.add(EnumFacing.UP);
		if((attachmentBytes & 1 << 5) > 0) attachments.add(EnumFacing.WEST);
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
		compound.setByte("Attachments", (byte)NBTHelper.writeBooleansToInt(
				attachments.contains(EnumFacing.DOWN),
				attachments.contains(EnumFacing.EAST),
				attachments.contains(EnumFacing.NORTH),
				attachments.contains(EnumFacing.SOUTH),
				attachments.contains(EnumFacing.UP),
				attachments.contains(EnumFacing.WEST)
		));
		super.writeCustomNBT(compound, updatePacket);
		
	}
	
}
