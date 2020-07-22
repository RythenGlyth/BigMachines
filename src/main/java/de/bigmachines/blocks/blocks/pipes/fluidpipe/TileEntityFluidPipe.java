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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityFluidPipe extends TileEntityPipeBase {
	
	@SideOnly(Side.CLIENT)
	private final ClientFluidStorage clientFluidStorage = new ClientFluidStorage();
	
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
		if (getNetwork() != null) {
			NBTTagCompound fluidTag = new NBTTagCompound();
			FluidStack contents = getNetwork().getContents(this);
			if (contents != null && contents.amount > 0) contents.writeToNBT(fluidTag);
			compound.setTag("fluid", fluidTag);
		}
	}
	
	@Override
	public void readCustomNBT(final NBTTagCompound compound, final boolean updatePacket) {
		super.readCustomNBT(compound, updatePacket);
		if (world != null && world.isRemote && compound.hasKey("fluid")) {
			NBTTagCompound fluidTag = compound.getCompoundTag("fluid");
			if (fluidTag != null) {
				FluidStack fluid = FluidStack.loadFluidStackFromNBT(fluidTag);
				clientFluidStorage.setFluid(fluid);
				System.out.println("fluid set: " + fluid);
			}
		}
	}
	
	@Override
	public boolean hasCapability(@Nonnull final Capability<?> capability, @Nonnull final EnumFacing facing) {
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
		if (capability.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
			if (facing == null) {
				if (world.isRemote) return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(clientFluidStorage);
				else return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new PipeFluidStorage(this, null, null));
			}
			if (hasAttachment(facing)) {
				if (world.isRemote) return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(clientFluidStorage);
				else {
					final PipeAttachment pipeAttachment = getAttachment(facing);
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(new PipeFluidStorage(this, pipeAttachment, facing));
				}
			}
		}
		return super.getCapability(capability, facing);
	}
	
	public FluidStack getContents() {
		if (getNetwork() == null) return null;
		return getNetwork().getContents(this);
	}
	
	@SideOnly(Side.CLIENT)
	public class ClientFluidStorage implements IFluidHandler, IFluidTankProperties {
		
		private FluidStack fluid;
		
		public void setFluid(FluidStack fluid) {
			this.fluid = fluid;
		}
		
		@Override
		public IFluidTankProperties[] getTankProperties() {
			return new IFluidTankProperties[] {this};
		}
		
		@Override
		public int fill(final FluidStack resource, final boolean doFill) {
			throw new RuntimeException("fill called on client");
		}
		
		@Nullable
		@Override
		public FluidStack drain(final FluidStack resource, final boolean doDrain) {
			throw new RuntimeException("fill called on client");
		}
		
		@Nullable
		@Override
		public FluidStack drain(final int maxDrain, final boolean doDrain) {
			return null;
		}
		
		@Nullable
		@Override
		public FluidStack getContents() {
			return fluid;
		}
		
		@Override
		public int getCapacity() {
			return TileEntityFluidPipe.this.maxContents();
		}
		
		@Override
		public boolean canFill() {
			return true;
		}
		
		@Override
		public boolean canDrain() {
			return true;
		}
		
		@Override
		public boolean canFillFluidType(final FluidStack fluidStack) {
			if (fluid == null || fluid.amount == 0) return true;
			return fluid.isFluidEqual(fluidStack);
		}
		
		@Override
		public boolean canDrainFluidType(final FluidStack fluidStack) {
			if (fluid == null || fluid.amount == 0) return false;
			return fluid.isFluidEqual(fluidStack);
		}
	}
	
	@SideOnly(Side.SERVER)
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
