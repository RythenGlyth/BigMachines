package de.bigmachines.blocks.blocks.pipes.fluidpipe;

import de.bigmachines.blocks.FluidStorage;
import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase;
import de.bigmachines.blocks.blocks.pipes.heatpipe.TileEntityHeatPipe;
import de.bigmachines.utils.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
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
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			updated();
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
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new AttachmentFluidStorage(fluidStorage, pipeAttachment));
			}
		}
		return super.getCapability(capability, facing);
	}
	
	public static class AttachmentFluidStorage implements IFluidHandler, IFluidTankProperties {
		
		private PipeAttachment attachment;
		private FluidStorage fluidStorage;
		
		public AttachmentFluidStorage(FluidStorage fluidStorage, PipeAttachment attachment) {
			this.fluidStorage = fluidStorage;
			this.attachment = attachment;
		}

		@Override
		public FluidStack getContents() {
			return fluidStorage.getContents();
		}

		@Override
		public int getCapacity() {
			return fluidStorage.getCapacity();
		}

		@Override
		public boolean canFill() {
			return attachment.canInsert() && fluidStorage.canFill();
		}

		@Override
		public boolean canDrain() {
			return attachment.canExtract() && fluidStorage.canDrain();
		}

		@Override
		public boolean canFillFluidType(FluidStack fluidStack) {
			return fluidStorage.canFillFluidType(fluidStack);
		}

		@Override
		public boolean canDrainFluidType(FluidStack fluidStack) {
			return fluidStorage.canDrainFluidType(fluidStack);
		}

		@Override
		public IFluidTankProperties[] getTankProperties() {
			return fluidStorage.getTankProperties();
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			return attachment.canInsert() ? fluidStorage.fill(resource, doFill) : 0;
		}

		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain) {
			if (!canDrainFluidType(resource)) return null;
	        return drain(resource.amount, doDrain);
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			if(fluidStorage.getContents() == null || !attachment.canExtract()) return new FluidStack(FluidRegistry.WATER, 0);
	        return fluidStorage.drain(maxDrain, doDrain);
		}
		
		
		
	}
	
}
