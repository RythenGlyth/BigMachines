package de.bigmachines.blocks.blocks.pipes.fluidpipe;

import de.bigmachines.blocks.FluidStorage;
import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase;
import de.bigmachines.utils.RedstoneMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityFluidPipe extends TileEntityPipeBase {
	
	FluidStorage fluidStorage;
	
	public TileEntityFluidPipe() {
		super(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
		fluidStorage = new FluidStorage(1000);
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
	public void writeCustomNBT(NBTTagCompound compound, boolean updatePacket) {
		super.writeCustomNBT(compound, updatePacket);
		fluidStorage.writeToNBT(compound);
	}
	
	@Override
	public void readCustomNBT(NBTTagCompound compound, boolean updatePacket) {
		super.readCustomNBT(compound, updatePacket);
		fluidStorage.readFromNBT(compound);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
			if(facing == null) {
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidStorage);
			}
			if(hasAttachment(facing)) {
				PipeAttachment pipeAttachment = getAttachment(facing);
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new AttachmentFluidStorage(this, pipeAttachment, facing));
			}
		}
		return super.getCapability(capability, facing);
	}
	
	public static class AttachmentFluidStorage implements IFluidHandler, IFluidTankProperties {
		
		private PipeAttachment attachment;
		private TileEntityFluidPipe tileEntityFluidPipe;
		private EnumFacing facing;
		
		public AttachmentFluidStorage(TileEntityFluidPipe tileEntityFluidPipe, PipeAttachment attachment, EnumFacing facing) {
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
		public boolean canFillFluidType(FluidStack fluidStack) {
			return canFill() && tileEntityFluidPipe.fluidStorage.canFillFluidType(fluidStack);
		}

		@Override
		public boolean canDrainFluidType(FluidStack fluidStack) {
			return canDrain() && tileEntityFluidPipe.fluidStorage.canDrainFluidType(fluidStack);
		}

		@Override
		public IFluidTankProperties[] getTankProperties() {
			return tileEntityFluidPipe.fluidStorage.getTankProperties();
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			return attachment.canInsert() && canFill() ? tileEntityFluidPipe.fluidStorage.fill(resource, doFill) : 0;
		}

		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain) {
			if (!canDrainFluidType(resource)) return null;
	        return drain(resource.amount, doDrain);
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			if(tileEntityFluidPipe.fluidStorage.getContents() == null || !attachment.canExtract() || !canDrain()) return new FluidStack(FluidRegistry.WATER, 0);
	        return tileEntityFluidPipe.fluidStorage.drain(maxDrain, doDrain);
		}
		
		
		
	}
	
}
