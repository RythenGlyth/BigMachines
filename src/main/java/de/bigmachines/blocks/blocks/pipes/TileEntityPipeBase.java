package de.bigmachines.blocks.blocks.pipes;

import de.bigmachines.BigMachines;
import de.bigmachines.blocks.IHasGui;
import de.bigmachines.blocks.TileEntityBase;
import de.bigmachines.gui.client.GuiPipeAttachment;
import de.bigmachines.gui.container.ContainerPipeAttachment;
import de.bigmachines.gui.slots.ISlotValidator;
import de.bigmachines.network.messages.MessageChangePipeAttachmentMode;
import de.bigmachines.utils.BlockHelper;
import de.bigmachines.utils.classes.IHasRedstoneControl;
import de.bigmachines.utils.classes.Inventory;
import de.bigmachines.utils.classes.Pair;
import de.bigmachines.utils.classes.RedstoneMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class TileEntityPipeBase extends TileEntityBase implements ITickable, IHasGui {
	
	int pass;
	
	//protected HashSet<EnumFacing> attachments;
	protected final HashMap<EnumFacing, PipeAttachment> attachments;
	protected final Capability capability;
	
	public TileEntityPipeBase(Capability capability) {
		super();
		attachments = new HashMap<>();
		this.capability = capability;
	}
	
	public Capability getCapability() {
		return capability;
	}

	@Override
	public Object getGuiServer(InventoryPlayer inventory) {
		Pair<EnumFacing, BlockPos> slectedSide = BlockPipeBase.getSelectedRayTrace(inventory.player);
		if(slectedSide != null) {
			return new ContainerPipeAttachment(inventory, this, slectedSide.x);
		}
		return null;
	}

	@Override
	public Object getGuiClient(InventoryPlayer inventory) {
		Pair<EnumFacing, BlockPos> slectedSide = BlockPipeBase.getSelectedRayTrace();
		if(slectedSide != null) {
			return new GuiPipeAttachment(inventory, this, slectedSide.x);
		}
		return null;
	}

	@Override
	public void update() {
		
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
		HashMap<EnumFacing, PipeAttachment> lastAttachments = new HashMap<>(attachments);
		attachments.clear();
		for(EnumFacing side : EnumFacing.VALUES) {
			TileEntity adjacentTileEntity = BlockHelper.getAdjacentTileEntity(this, side);
			if(adjacentTileEntity != null && adjacentTileEntity.hasCapability(capability, side.getOpposite())) attachments.put(side, lastAttachments.containsKey(side) ? lastAttachments.get(side) : new PipeAttachment());
		}
		if(!lastAttachments.keySet().equals(attachments.keySet())) updated();
	}
	
	public boolean hasAttachment(EnumFacing side) {
		return attachments.containsKey(side);
	}
	
	public PipeAttachment getAttachment(EnumFacing side) {
		return attachments.get(side);
	}
    
	@Override
	@Nonnull
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getPos(), getPos().add(1, 1, 1));
	}
    
	@Override
	public void readCustomNBT(NBTTagCompound compound, boolean updatePacket) {
		attachments.clear();
		for(String key : compound.getCompoundTag("Attachments").getKeySet()) {
			attachments.put(EnumFacing.byName(key), new PipeAttachment(compound.getCompoundTag("Attachments").getCompoundTag(key)));
		}
		super.readCustomNBT(compound, updatePacket);
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound compound, boolean updatePacket) {
		NBTTagCompound attachmentTag = new NBTTagCompound();
		
		for(Map.Entry<EnumFacing, PipeAttachment> attachment : attachments.entrySet()) {
			attachmentTag.setTag(attachment.getKey().toString(), attachment.getValue().getNBTTag());
		}
		
		compound.setTag("Attachments", attachmentTag);
		
		super.writeCustomNBT(compound, updatePacket);
		
	}
	
	@Override
	public boolean hasCapability(@Nullable Capability<?> capability, @Nullable EnumFacing facing) {
		if(capability == null || this.capability == null) return false;
		if(capability.equals(this.capability)) return true;
		return super.hasCapability(capability, facing);
	}
	
	public int getRedstonePower(EnumFacing facing) {
		if (!world.isBlockLoaded(getPos())) return 0;
		
		IBlockState state = world.getBlockState(getPos());
		
		return state.getBlock().shouldCheckWeakPower(state, getWorld(), getPos(), facing) ? state.getWeakPower(getWorld(), getPos(), facing) : getWorld().getStrongPower(getPos());
	}
	
	public static class PipeAttachment implements IHasRedstoneControl {
		
		protected boolean canExtract;
		protected boolean canInsert;
		
		protected RedstoneMode redstoneMode;
		
		protected Inventory inventory;
		
		/**
		 * Filter Mode
		 * true=whitelist
		 * false=blacklist
		 */
		protected boolean whitelist;
		
		public PipeAttachment() {
			this(true, true, RedstoneMode.IGNORED, false);
		}
		
		public PipeAttachment(NBTTagCompound attachmentTag) {
			this(
					!attachmentTag.hasKey("canExtract") || attachmentTag.getBoolean("canExtract"),
					!attachmentTag.hasKey("canInsert") || attachmentTag.getBoolean("canInsert"),
					attachmentTag.hasKey("redstoneMode") ? RedstoneMode.values()[attachmentTag.getByte("redstoneMode")] : RedstoneMode.IGNORED,
					attachmentTag.hasKey("whitelist") && attachmentTag.getBoolean("whitelist")
			);
			if(attachmentTag.hasKey("Items")) this.inventory.readFromNBT(attachmentTag);
		}
		
		public Inventory getInventory() {
			return inventory;
		}
		
		public void sendUpdateToServer(BlockPos pos, EnumFacing side) {
			BigMachines.networkHandlerMain.sendToServer(new MessageChangePipeAttachmentMode(pos, side, redstoneMode, whitelist, canExtract(), canInsert()));
		}
		
		public PipeAttachment(boolean canExtract, boolean canInsert, RedstoneMode redstoneMode, boolean whitelist) {
			super();
			this.canExtract = canExtract;
			this.canInsert = canInsert;
			this.redstoneMode = redstoneMode;
			this.whitelist = whitelist;
			this.inventory = new Inventory("", 5);
		}
		
		public void setWhitelist(boolean whitelist) {
			this.whitelist = whitelist;
		}
		
		public boolean isWhitelist() {
			return whitelist;
		}

		public void setRedstoneMode(RedstoneMode redstoneMode) {
			this.redstoneMode = redstoneMode;
		}
		
		public RedstoneMode getRedstoneMode() {
			return redstoneMode;
		}
		
		@Override
		public String toString() {
			return "{\"canExtract\": " + canExtract + ", \"canExtract\": " + canInsert + "}";
		}
		
		public void setInsertationByIndex(int index) {
			switch(index) {
				case 1:
					canExtract = true;
					canInsert = false;
					break;
				case 2:
					canExtract = false;
					canInsert = true;
					break;
				case 3:
					canExtract = false;
					canInsert = false;
					break;
				default:
					canExtract = true;
					canInsert = true;
					break;
			}
		}
		
		public void setCanExtract(boolean canExtract) {
			this.canExtract = canExtract;
		}
		
		public void setCanInsert(boolean canInsert) {
			this.canInsert = canInsert;
		}
		
		public void cycleThrough(boolean direction) {
			if(canExtract && canInsert) {
				if(direction) {
					canExtract = false;
					canInsert = true;
				} else {
					canExtract = false;
					canInsert = false;
				}
			} else if(!canExtract && !canInsert) {
				if(direction) {
					canExtract = true;
					canInsert = true;
				} else {
					canExtract = true;
					canInsert = false;
				}
			} else if(canExtract && !canInsert) {
				if(direction) {
					canExtract = false;
					canInsert = false;
				} else {
					canExtract = false;
					canInsert = true;
				}
			} else if(!canExtract && canInsert) {
				if(direction) {
					canExtract = true;
					canInsert = false;
				} else {
					canExtract = true;
					canInsert = true;
				}
			}
		}
		
		public boolean canExtract() {
			return canExtract;
		}
		
		public boolean canInsert() {
			return canInsert;
		}
		
		public NBTTagCompound getNBTTag() {
			NBTTagCompound attachmentTag = new NBTTagCompound();
			if(!canExtract) attachmentTag.setBoolean("canExtract", canExtract);
			if(!canInsert) attachmentTag.setBoolean("canInsert", canInsert);
			if((redstoneMode != RedstoneMode.IGNORED)) attachmentTag.setByte("redstoneMode", (byte)redstoneMode.ordinal());
			if(whitelist) attachmentTag.setBoolean("whitelist", whitelist);
			if(!this.inventory.isEmpty()) this.inventory.writeToNBT(attachmentTag);
			return attachmentTag;
		}
		
	}
	
}
