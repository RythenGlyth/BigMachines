package de.bigmachines.blocks.blocks.pipes;

import de.bigmachines.BigMachines;
import de.bigmachines.blocks.IHasGui;
import de.bigmachines.blocks.TileEntityBase;
import de.bigmachines.gui.client.GuiPipeAttachment;
import de.bigmachines.gui.container.ContainerPipeAttachment;
import de.bigmachines.network.messages.MessageChangePipeAttachmentMode;
import de.bigmachines.utils.BlockHelper;
import de.bigmachines.utils.NBTHelper;
import de.bigmachines.utils.classes.IHasRedstoneControl;
import de.bigmachines.utils.classes.Inventory;
import de.bigmachines.utils.classes.Pair;
import de.bigmachines.utils.classes.RedstoneMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
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

	private PipeNetwork network;
	
	public TileEntityPipeBase(final Capability capability) {
		attachments = new HashMap<>();
		this.capability = capability;
	}
	
	public Capability getCapability() {
		return capability;
	}

	@Override
	public Object getGuiServer(final InventoryPlayer inventory) {
		final Pair<EnumFacing, BlockPos> selectedSide = BlockPipeBase.getSelectedRayTrace(inventory.player);
		if(selectedSide != null) {
			return new ContainerPipeAttachment(inventory, this, selectedSide.x);
		}
		return null;
	}

	@Override
	public Object getGuiClient(final InventoryPlayer inventory) {
		final Pair<EnumFacing, BlockPos> selectedSide = BlockPipeBase.getSelectedRayTrace();
		if(selectedSide != null) {
			return new GuiPipeAttachment(inventory, this, selectedSide.x);
		}
		return null;
	}

	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(@Nonnull final NBTTagCompound compound) {
		if (network != null) {
			NBTTagCompound networkTag = new NBTTagCompound();
			networkTag.setTag("root", NBTHelper.writeBlockPosToTag(network.getRoot().getPos()));

			if (this.equals(network.getRoot())) {
				networkTag.setTag("data", network.rootCompound());
			}

		    compound.setTag("network", networkTag);
		} // FIXME else init network
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(@Nonnull final NBTTagCompound compound) {
		if (compound.hasKey("network")) {
			NBTTagCompound networkTag = compound.getCompoundTag("network");
			NBTTagCompound networkRootTag = networkTag.getCompoundTag("root");
			BlockPos rootPos = NBTHelper.readTagToBlockPos(networkRootTag);

			if (this.getPos().equals(rootPos)) {
				if (this.getNetwork() == null) {
					// TODO create it
				}
			} else {
				TileEntity root = world.getTileEntity(rootPos);
				PipeNetwork network = ((TileEntityPipeBase) root).getNetwork();
				if (network == null) {
					// TODO create it
				}
				network = ((TileEntityPipeBase) root).getNetwork();
				this.network = network;
			}

		} // FIXME else init network
		super.readFromNBT(compound);
	}

	@Override
	public void update() {
		
	}
	
	@Override
	public boolean shouldRenderInPass(final int pass) {
		this.pass = pass;
		return true;
	}
	
	public HashMap<EnumFacing, PipeAttachment> getAttachments() {
		return attachments;
	}
	
	public void onBlockPlaced(final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
		updateAttachments();

		if (!getWorld().isRemote) {

			if (network != null) throw new RuntimeException("pipe is placed with network initialized, this shouldn't be a thing!");

			for (final EnumFacing side : EnumFacing.values()) {
				if (hasAttachment(side) && BlockHelper.getAdjacentTileEntity(this, side) instanceof TileEntityPipeBase) {
					final TileEntityPipeBase other = (TileEntityPipeBase) BlockHelper.getAdjacentTileEntity(this, side);
					if (network == null) { // add this pipe to other's network
						other.network.insert(other, this);
						network = other.network;
					} else { // merge this network into the other's network
						network.mergeInto(this, other, other.network);
					}
				}
			}
			if (network == null) // no pipe neighbors found, initialize new network6
				network = new PipeNetwork(capability, this);

		}
	}

	public void onBlockBroken(final IBlockState state) {
		if (!getWorld().isRemote) {
			network.remove(this);
		}
	}

	public void onBlockClicked(final EntityPlayer player) {
	    if (!getWorld().isRemote) {
			if (network == null) System.out.println("network is null");
			else network.debugInfo(this);
		}
	}

	protected void setNetwork(final PipeNetwork network) {
		this.network = network;
	}

	protected PipeNetwork getNetwork() {
		return network;
	}

	public void updateAttachments() {
		final HashMap<EnumFacing, PipeAttachment> lastAttachments = new HashMap<>(attachments);
		attachments.clear();
		for(final EnumFacing side : EnumFacing.VALUES) {
			final TileEntity adjacentTileEntity = BlockHelper.getAdjacentTileEntity(this, side);
			if(adjacentTileEntity != null && adjacentTileEntity.hasCapability(capability, side.getOpposite())) attachments.put(side, lastAttachments.containsKey(side) ? lastAttachments.get(side) : new PipeAttachment());
		}
		if(!lastAttachments.keySet().equals(attachments.keySet())) {
			updated();

			if (!world.isRemote) {
				for (final EnumFacing side : EnumFacing.values()) {
				    final TileEntity adj = BlockHelper.getAdjacentTileEntity(this, side);
					if (hasAttachment(side) && !(adj instanceof TileEntityPipeBase))
						network.addModule(this, adj);
				}
			}
		}

	}

	public boolean hasAttachment(final EnumFacing side) {
		return attachments.containsKey(side);
	}

	public PipeAttachment getAttachment(final EnumFacing side) {
		return attachments.get(side);
	}

	@Override
	@Nonnull
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getPos(), getPos().add(1, 1, 1));
	}

	@Override
	public void readCustomNBT(final NBTTagCompound compound, final boolean updatePacket) {
		attachments.clear();
		for(final String key : compound.getCompoundTag("Attachments").getKeySet()) {
			attachments.put(EnumFacing.byName(key), new PipeAttachment(compound.getCompoundTag("Attachments").getCompoundTag(key)));
		}
		super.readCustomNBT(compound, updatePacket);
	}
	
	@Override
	public void writeCustomNBT(final NBTTagCompound compound, final boolean updatePacket) {
		final NBTTagCompound attachmentTag = new NBTTagCompound();
		
		for(final Map.Entry<EnumFacing, PipeAttachment> attachment : attachments.entrySet()) {
			attachmentTag.setTag(attachment.getKey().toString(), attachment.getValue().getNBTTag());
		}
		
		compound.setTag("Attachments", attachmentTag);
		
		super.writeCustomNBT(compound, updatePacket);
		
	}
	
	@Override
	public boolean hasCapability(@Nullable final Capability<?> capability, @Nullable final EnumFacing facing) {
		if(capability == null || this.capability == null) return false;
		if(capability.equals(this.capability)) return true;
		return super.hasCapability(capability, facing);
	}
	
	public int getRedstonePower(final EnumFacing facing) {
		if (!world.isBlockLoaded(getPos())) return 0;
		
		final IBlockState state = world.getBlockState(getPos());
		
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
		
		public PipeAttachment(final NBTTagCompound attachmentTag) {
			this(
					!attachmentTag.hasKey("canExtract") || attachmentTag.getBoolean("canExtract"),
					!attachmentTag.hasKey("canInsert") || attachmentTag.getBoolean("canInsert"),
					attachmentTag.hasKey("redstoneMode") ? RedstoneMode.values()[attachmentTag.getByte("redstoneMode")] : RedstoneMode.IGNORED,
					attachmentTag.hasKey("whitelist") && attachmentTag.getBoolean("whitelist")
			);
			if(attachmentTag.hasKey("Items")) inventory.readFromNBT(attachmentTag);
		}
		
		public Inventory getInventory() {
			return inventory;
		}
		
		public void sendUpdateToServer(final BlockPos pos, final EnumFacing side) {
			BigMachines.networkHandlerMain.sendToServer(new MessageChangePipeAttachmentMode(pos, side, redstoneMode, whitelist, canExtract(), canInsert()));
		}
		
		public PipeAttachment(final boolean canExtract, final boolean canInsert, final RedstoneMode redstoneMode, final boolean whitelist) {
			this.canExtract = canExtract;
			this.canInsert = canInsert;
			this.redstoneMode = redstoneMode;
			this.whitelist = whitelist;
			inventory = new Inventory("", 5);
		}
		
		public void setWhitelist(final boolean whitelist) {
			this.whitelist = whitelist;
		}
		
		public boolean isWhitelist() {
			return whitelist;
		}

		public void setRedstoneMode(final RedstoneMode redstoneMode) {
			this.redstoneMode = redstoneMode;
		}
		
		public RedstoneMode getRedstoneMode() {
			return redstoneMode;
		}
		
		@Override
		public String toString() {
			return "{\"canExtract\": " + canExtract + ", \"canExtract\": " + canInsert + "}";
		}
		
		public void setInsertationByIndex(final int index) {
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
		
		public void setCanExtract(final boolean canExtract) {
			this.canExtract = canExtract;
		}
		
		public void setCanInsert(final boolean canInsert) {
			this.canInsert = canInsert;
		}
		
		public void cycleThrough(final boolean direction) {
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
			final NBTTagCompound attachmentTag = new NBTTagCompound();
			if(!canExtract) attachmentTag.setBoolean("canExtract", canExtract);
			if(!canInsert) attachmentTag.setBoolean("canInsert", canInsert);
			if((redstoneMode != RedstoneMode.IGNORED)) attachmentTag.setByte("redstoneMode", (byte)redstoneMode.ordinal());
			if(whitelist) attachmentTag.setBoolean("whitelist", whitelist);
			if(!inventory.isEmpty()) inventory.writeToNBT(attachmentTag);
			return attachmentTag;
		}
		
	}
	
}
