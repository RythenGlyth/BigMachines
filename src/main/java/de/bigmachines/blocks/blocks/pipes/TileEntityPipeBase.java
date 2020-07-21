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
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class TileEntityPipeBase extends TileEntityBase implements ITickable, IHasGui {
	
	int pass;
	
	//protected HashSet<EnumFacing> attachments;
	protected final HashMap<EnumFacing, PipeAttachment> attachments;
	protected final Capability<?> capability;
	
	//private PipeNetworkTemplate template; // only set on root
	//private BlockPos rootPos; // set on all when template is set too
	private NBTTagCompound compound;
	
	private PipeNetwork network;
	//private static TileEntityPipeBase last;
	
	public TileEntityPipeBase(final Capability<?> capability) {
		attachments = new HashMap<>();
		this.capability = capability;
	}
	
	public Capability<?> getCapability() {
		return capability;
	}
	
	@Override
	public Object getGuiServer(final InventoryPlayer inventory) {
		final Pair<EnumFacing, BlockPos> selectedSide = BlockPipeBase.getSelectedRayTrace(inventory.player);
		if (selectedSide != null) {
			return new ContainerPipeAttachment(inventory, this, selectedSide.x);
		}
		return null;
	}
	
	@Override
	public Object getGuiClient(final InventoryPlayer inventory) {
		final Pair<EnumFacing, BlockPos> selectedSide = BlockPipeBase.getSelectedRayTrace();
		if (selectedSide != null) {
			return new GuiPipeAttachment(inventory, this, selectedSide.x);
		}
		return null;
	}
	
	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(@Nonnull final NBTTagCompound compound) {
		if (!world.isRemote) {
			if (network != null && network.getRoot() != null) {
				final NBTTagCompound networkTag = new NBTTagCompound();
				networkTag.setTag("root", NBTHelper.writeBlockPosToTag(network.getRoot().getPos()));
				
				if (equals(network.getRoot())) {
					networkTag.setTag("data", network.rootCompound());
				}
				
				compound.setTag("network", networkTag);
			}
		} // we dont have to init the network here, since we take the assumption that any pipe always has a network
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(@Nonnull final NBTTagCompound compound) {
		if (FMLCommonHandler.instance().getEffectiveSide().isServer() && this.compound == null) {
			this.compound = compound; // this is so stupid, I can't believe it actually works.
		}
		
		super.readFromNBT(compound);
	}
	
	@Override
	public void update() {
		if (!world.isRemote && network == null && compound != null) {
			final NBTTagCompound networkTag = compound.getCompoundTag("network");
			final NBTTagCompound networkRootTag = networkTag.getCompoundTag("root");
			final BlockPos rootPos = NBTHelper.readTagToBlockPos(networkRootTag);
			
			if (rootPos.equals(getPos())) { // I am root, init network on me
				network = new PipeNetwork(capability, (TileEntityPipeBase) world.getTileEntity(rootPos));
				// from here on network != null
				final NBTTagCompound networkDataTag = networkTag.getCompoundTag("data");
				final NBTTagList pipeList = networkDataTag.getTagList("pipeList", 10);
				final NBTTagList moduleList = networkDataTag.getTagList("moduleList", 10);
				
				for (int i = 0; i < pipeList.tagCount(); i++) {
					final NBTTagCompound connection = pipeList.getCompoundTagAt(i);
					final BlockPos a = NBTHelper.readTagToBlockPos(connection.getCompoundTag("a"));
					final BlockPos b = NBTHelper.readTagToBlockPos(connection.getCompoundTag("b"));
					network.insert((TileEntityPipeBase) world.getTileEntity(a),
							  (TileEntityPipeBase) world.getTileEntity(b));
				}
				
				for (int i = 0; i < moduleList.tagCount(); i++) {
					final NBTTagCompound module = moduleList.getCompoundTagAt(i);
					final TileEntity a = world.getTileEntity(
							  NBTHelper.readTagToBlockPos(module.getCompoundTag("a")));
					final TileEntity b = world.getTileEntity(
							  NBTHelper.readTagToBlockPos(module.getCompoundTag("b")));
					if (a instanceof TileEntityPipeBase && !(b instanceof TileEntityPipeBase))
						network.addModule((TileEntityPipeBase) a, b);
					else if (!(a instanceof TileEntityPipeBase) && b instanceof TileEntityPipeBase)
						network.addModule((TileEntityPipeBase) b, a);
					else
						throw new RuntimeException("wrong module @ " + a + " and " + b);
				}
				
			} else { // I am not root, init network on root
				// FIXME this might lead to an infinite loop on startup and might be unneeded
				final TileEntityPipeBase other = (TileEntityPipeBase) world.getTileEntity(rootPos);
				if (other != null) {
					other.update();
					network = other.network;
				}
			}
		}
		
		if (network != null && network.getRoot().equals(this)) {
			// on every root tick, update the entire network.
			//network.update();
		}
	}
	
	@Override
	public boolean shouldRenderInPass(final int pass) {
		this.pass = pass;
		return true;
	}
	
	/**
	 * Checks whether:
	 * 1. this and the other pipe are connected
	 * 2. this pipe has the conection set to allow insert
	 * 3. the other pipe has the connection set to allow extract
	 *
	 * @param other the other pipe
	 * @return whether this pipe can insert into the other
	 */
	protected boolean canInsertIn(@Nullable final TileEntityPipeBase other) {
		if (other == null) return false;
		final EnumFacing connectingFacing = BlockHelper.getConnectingFace(getPos(), other.getPos());
		if (connectingFacing == null) return false;
		if (!getAttachment(connectingFacing).canExtract()) return false;
		if (!other.getAttachment(connectingFacing.getOpposite()).canInsert()) return false;
		//if (other.getAttachment())
		return true;
	}
	
	// TODO disconnect pipes using a wrench
	// TODO colored pipes that don't connect (compare AE2 cables)
	public boolean isConnectedTo(@Nullable final TileEntityPipeBase other) {
		if (other == null) return false;
		final EnumFacing connectingFacing = BlockHelper.getConnectingFace(getPos(), other.getPos());
		if (connectingFacing == null) return false;
		return hasAttachment(connectingFacing);
	}
	
	/**
	 * The amount this pipe can hold at once / transport per tick of the specified fluid.
	 *
	 * @return how much this pipe can transport of the specified fluid
	 */
	public int maxContents() {
		// TODO different pipe tiers?
		return 1000;
	}
	
	public HashMap<EnumFacing, PipeAttachment> getAttachments() {
		return attachments;
	}
	
	public void onBlockPlaced(final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
		updateAttachments(); // the attachments should be updated as the block is placed, but the network is required for the attachments
		// however the attachment updates have to be made known to the network as well FIXME
		
		if (!getWorld().isRemote) {
			
			if (network != null) throw new RuntimeException("pipe is placed with network initialized, this shouldn't be a thing!");
			
			for (final EnumFacing side : EnumFacing.values()) {
				if (hasAttachment(side) && BlockHelper.getAdjacentTileEntity(this, side) instanceof TileEntityPipeBase) {
					final TileEntityPipeBase other = (TileEntityPipeBase) BlockHelper.getAdjacentTileEntity(this, side);
					if (network == null) { // add this pipe to other's network
						other.network.insert(other, this);
						network = other.network;
					} else { // merge this network into the other's network
						network.mergeInto(other, this, other.network);
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
	
	@Nullable
	public PipeNetwork getNetwork() {
		//if (network == null) network = new PipeNetwork(capability, this);
		return network;
	}
	
	public void updateAttachments() {
		final HashMap<EnumFacing, PipeAttachment> lastAttachments = new HashMap<>(attachments);
		attachments.clear();
		for (final EnumFacing side : EnumFacing.VALUES) {
			final TileEntity adjacentTileEntity = BlockHelper.getAdjacentTileEntity(this, side);
			if (adjacentTileEntity != null && adjacentTileEntity.hasCapability(capability, side.getOpposite()))
				attachments.put(side, lastAttachments.containsKey(side) ? lastAttachments.get(side) : new PipeAttachment());
		}
		if (!lastAttachments.keySet().equals(attachments.keySet())) {
			updated();
			
			if (!world.isRemote) {
				for (final EnumFacing side : EnumFacing.values()) {
					final TileEntity adj = BlockHelper.getAdjacentTileEntity(this, side);
					if (hasAttachment(side) && !(adj instanceof TileEntityPipeBase))
						getNetwork().addModule(this, adj);
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
		for (final String key : compound.getCompoundTag("Attachments").getKeySet()) {
			attachments.put(EnumFacing.byName(key), new PipeAttachment(compound.getCompoundTag("Attachments").getCompoundTag(key)));
		}
		super.readCustomNBT(compound, updatePacket);
	}
	
	@Override
	public void writeCustomNBT(final NBTTagCompound compound, final boolean updatePacket) {
		final NBTTagCompound attachmentTag = new NBTTagCompound();
		
		for (final Map.Entry<EnumFacing, PipeAttachment> attachment : attachments.entrySet()) {
			attachmentTag.setTag(attachment.getKey().toString(), attachment.getValue().getNBTTag());
		}
		
		compound.setTag("Attachments", attachmentTag);
		
		super.writeCustomNBT(compound, updatePacket);
		
	}
	
	@Override
	public boolean hasCapability(@Nullable final Capability<?> capability, @Nullable final EnumFacing facing) {
		if (capability == null || this.capability == null) return false;
		if (capability.equals(this.capability)) return true;
		return super.hasCapability(capability, facing);
	}
	
	public int getRedstonePower(final EnumFacing facing) {
		if (!world.isBlockLoaded(getPos())) return 0;
		
		final IBlockState state = world.getBlockState(getPos());
		
		return state.getBlock().shouldCheckWeakPower(state, getWorld(), getPos(), facing) ? state.getWeakPower(getWorld(), getPos(), facing) : getWorld().getStrongPower(getPos());
	}
	
	public static class PipeAttachment implements IHasRedstoneControl {
		
		private boolean canExtract;
		private boolean canInsert;
		
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
			if (attachmentTag.hasKey("Items")) inventory.readFromNBT(attachmentTag);
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
			switch (index) {
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
			if (canExtract && canInsert) {
				if (direction) {
					canExtract = false;
					canInsert = true;
				} else {
					canExtract = false;
					canInsert = false;
				}
			} else if (!canExtract && !canInsert) {
				if (direction) {
					canExtract = true;
					canInsert = true;
				} else {
					canExtract = true;
					canInsert = false;
				}
			} else if (canExtract && !canInsert) {
				if (direction) {
					canExtract = false;
					canInsert = false;
				} else {
					canExtract = false;
					canInsert = true;
				}
			} else if (!canExtract && canInsert) {
				if (direction) {
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
			if (!canExtract) attachmentTag.setBoolean("canExtract", canExtract);
			if (!canInsert) attachmentTag.setBoolean("canInsert", canInsert);
			if ((redstoneMode != RedstoneMode.IGNORED)) attachmentTag.setByte("redstoneMode", (byte) redstoneMode.ordinal());
			if (whitelist) attachmentTag.setBoolean("whitelist", whitelist);
			if (!inventory.isEmpty()) inventory.writeToNBT(attachmentTag);
			return attachmentTag;
		}
		
	}
	
	public String toString() {
		return getPos().toString();
	}
	
}
