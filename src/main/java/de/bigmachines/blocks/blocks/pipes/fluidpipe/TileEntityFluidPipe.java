package de.bigmachines.blocks.blocks.pipes.fluidpipe;

import de.bigmachines.blocks.FluidStorage;
import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase;
import de.bigmachines.utils.classes.RedstoneMode;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;

public class TileEntityFluidPipe extends TileEntityPipeBase {
	
	final FluidStorage fluidStorage;
	
	public TileEntityFluidPipe() {
		super(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
		fluidStorage = new FluidStorage(1000);
		System.out.println("warning");
		System.out.println("warning");
		System.out.println("warning");
		System.out.println("warning");
		System.out.println("warning");
		System.out.println("warning");
		System.out.println("warning");
		System.out.println("warning");
		System.out.println("warning");
		System.out.println("warning");
		System.out.println("warning");
		System.out.println("warning");
	}
	
	public FluidStorage getFluidStorage() {
		return fluidStorage;
	}

	/**
	 * Variable for last FluidStorage to check if it has changed before sending the packet to the client
	 */
	private FluidStorage lastFluidStorage;
	
	@Override
	public void update() {
		super.update();
		
		//UPDATING ON CLIENT IF CHANGED
		if(!world.isRemote) {
			if(fluidStorage != null && fluidStorage.hasChanged()) {
				fluidStorage.setHasChanged(false);
				updated();
			}
		}
	}
	
	@Override
	public void writeCustomNBT(final NBTTagCompound compound, final boolean updatePacket) {
		super.writeCustomNBT(compound, updatePacket);
		fluidStorage.writeToNBT(compound);
	}
	
	@Override
	public void readCustomNBT(final NBTTagCompound compound, final boolean updatePacket) {
		super.readCustomNBT(compound, updatePacket);
		fluidStorage.readFromNBT(compound);
	}
	
	@Override
	public boolean hasCapability(@Nonnull final Capability<?> capability, @Nonnull final EnumFacing facing) {
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
		if(capability.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
			if(facing == null) {
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidStorage);
			}
			if(hasAttachment(facing)) {
				final PipeAttachment pipeAttachment = getAttachment(facing);
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new AttachmentFluidStorage(this, pipeAttachment, facing));
			}
		}
		return super.getCapability(capability, facing);
	}
	
	public static class AttachmentFluidStorage implements IFluidHandler, IFluidTankProperties {
		
		private final PipeAttachment attachment;
		private final TileEntityFluidPipe tileEntityFluidPipe;
		private final EnumFacing facing;
		
		public AttachmentFluidStorage(final TileEntityFluidPipe tileEntityFluidPipe, final PipeAttachment attachment, final EnumFacing facing) {
            this.tileEntityFluidPipe = tileEntityFluidPipe;
			this.attachment = attachment;
			this.facing = facing;
		}

		@Override
		public FluidStack getContents() {
			return tileEntityFluidPipe.fluidStorage.getContents();
		}

		@Override
		public int getCapacity() {
			return tileEntityFluidPipe.fluidStorage.getCapacity();
		}

		@Override
		public boolean canFill() {
			if(attachment.canInsert() && tileEntityFluidPipe.fluidStorage.canFill()) {
				if(attachment.getRedstoneMode() == RedstoneMode.IGNORED) return true;
				boolean returning = tileEntityFluidPipe.getRedstonePower(facing) > 0;
				if(attachment.getRedstoneMode() == RedstoneMode.NEEDS_INVERTED) returning = !returning;
				return returning;
			}
			return false;
		}

		@Override
		public boolean canDrain() {
			if(attachment.canExtract() && tileEntityFluidPipe.fluidStorage.canDrain()) {
				if(attachment.getRedstoneMode() == RedstoneMode.IGNORED) return true;
				boolean returning = tileEntityFluidPipe.getRedstonePower(facing) > 0;
				if(attachment.getRedstoneMode() == RedstoneMode.NEEDS_INVERTED) returning = !returning;
				return returning;
			}
			return false;
		}

		@Override
		public boolean canFillFluidType(final FluidStack fluidStack) {
			return canFill() && tileEntityFluidPipe.fluidStorage.canFillFluidType(fluidStack);
		}

		@Override
		public boolean canDrainFluidType(final FluidStack fluidStack) {
			return canDrain() && tileEntityFluidPipe.fluidStorage.canDrainFluidType(fluidStack);
		}

		@Override
		public IFluidTankProperties[] getTankProperties() {
			return tileEntityFluidPipe.fluidStorage.getTankProperties();
		}

		@Override
		public int fill(final FluidStack resource, final boolean doFill) {
			return attachment.canInsert() && canFill() ? tileEntityFluidPipe.fluidStorage.fill(resource, doFill) : 0;
		}

		@Override
		public FluidStack drain(final FluidStack resource, final boolean doDrain) {
			if (!canDrainFluidType(resource)) return null;
	        return drain(resource.amount, doDrain);
		}

		@Override
		public FluidStack drain(final int maxDrain, final boolean doDrain) {
			if(tileEntityFluidPipe.fluidStorage.getContents() == null || !attachment.canExtract() || !canDrain()) return new FluidStack(FluidRegistry.WATER, 0);
	        return tileEntityFluidPipe.fluidStorage.drain(maxDrain, doDrain);
		}
		
		
		
	}
	
}
