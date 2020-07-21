package de.bigmachines.blocks.blocks.pipes.fluidpipe;

import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase;
import de.bigmachines.utils.classes.RedstoneMode;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;

public class TileEntityFluidPipe extends TileEntityPipeBase {
	
	public TileEntityFluidPipe() {
		super(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public void writeCustomNBT(final NBTTagCompound compound, final boolean updatePacket) {
		super.writeCustomNBT(compound, updatePacket);
	}
	
	@Override
	public void readCustomNBT(final NBTTagCompound compound, final boolean updatePacket) {
		super.readCustomNBT(compound, updatePacket);
	}
	
	@Override
	public boolean hasCapability(@Nonnull final Capability<?> capability, @Nonnull final EnumFacing facing) {
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
		if (capability.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
			if (facing == null) {
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new PipeFluidStorage(this, null, null));
			}
			if (hasAttachment(facing)) {
				final PipeAttachment pipeAttachment = getAttachment(facing);
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new PipeFluidStorage(this, pipeAttachment, facing));
			}
		}
		return super.getCapability(capability, facing);
	}
	
	public FluidStack getContents() {
		if (getNetwork() == null) return null;
		return getNetwork().getContents(this);
	}
	
	public static class PipeFluidStorage implements IFluidHandler, IFluidTankProperties {
		
		private final PipeAttachment attachment;
		private final TileEntityFluidPipe tileEntityFluidPipe;
		private final EnumFacing facing;
		
		public PipeFluidStorage(final TileEntityFluidPipe tileEntityFluidPipe, final PipeAttachment attachment, final EnumFacing facing) {
			this.tileEntityFluidPipe = tileEntityFluidPipe;
			this.attachment = attachment;
			this.facing = facing;
		}
		
		@Override
		public FluidStack getContents() {
			if (tileEntityFluidPipe.getNetwork() == null) return null;
			//System.out.println(tileEntityFluidPipe.getNetwork().getContents(tileEntityFluidPipe)); null because server client desync
			return tileEntityFluidPipe.getNetwork().getContents(tileEntityFluidPipe);
		}
		
		@Override
		public int getCapacity() {
			return tileEntityFluidPipe.maxContents();
		}
		
		@Override
		public boolean canFill() {
			if (attachment == null) return true;
			if (attachment.canInsert()) {
				if (attachment.getRedstoneMode() == RedstoneMode.IGNORED) return true;
				boolean returning = tileEntityFluidPipe.getRedstonePower(facing) > 0;
				if (attachment.getRedstoneMode() == RedstoneMode.NEEDS_INVERTED) returning = !returning;
				return returning;
			}
			return false;
		}
		
		@Override
		public boolean canDrain() {
			if (attachment == null) return true;
			if (attachment.canExtract()) {
				if (attachment.getRedstoneMode() == RedstoneMode.IGNORED) return true;
				boolean returning = tileEntityFluidPipe.getRedstonePower(facing) > 0;
				if (attachment.getRedstoneMode() == RedstoneMode.NEEDS_INVERTED) returning = !returning;
				return returning;
			}
			return false;
		}
		
		@Override
		public boolean canFillFluidType(final FluidStack fluidStack) {
			if (!canFill()) return false;
			final FluidStack contents = getContents();
			if (contents == null || contents.amount == 0) return true;
			return contents.isFluidEqual(fluidStack);
		}
		
		@Override
		public boolean canDrainFluidType(final FluidStack fluidStack) {
			if (!canDrain()) return false;
			final FluidStack contents = getContents();
			if (contents == null || contents.amount == 0) return false;
			return contents.isFluidEqual(fluidStack);
		}
		
		@Override
		public IFluidTankProperties[] getTankProperties() {
			return new IFluidTankProperties[] {this};
		}
		
		@Override
		public int fill(final FluidStack resource, final boolean doFill) {
			return attachment.canInsert() && canFill() ? tileEntityFluidPipe.getNetwork().fill(tileEntityFluidPipe, resource, doFill) : 0;
		}
		
		@Override
		public FluidStack drain(final FluidStack resource, final boolean doDrain) {
			if (!canDrainFluidType(resource)) return null;
			return drain(resource.amount, doDrain);
		}
		
		@Override
		public FluidStack drain(final int maxDrain, final boolean doDrain) {
			if (getContents() == null || !attachment.canExtract() || !canDrain()) return null;
			return tileEntityFluidPipe.getNetwork().currentContents.drain(tileEntityFluidPipe, maxDrain, doDrain);
		}
		
	}
	
}
