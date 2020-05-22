package de.bigmachines.blocks;

import javax.annotation.Nullable;

import de.bigmachines.utils.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidStorage implements IFluidHandler, IFluidTankProperties {
	private final int capacity;
	private final int maxReceive;
	private final int maxExtract;
	private final boolean canReceive;
	private final boolean canExtract;

	private FluidStack contents;

	public FluidStorage(int capacity) {
		this(capacity, capacity);
	}

	public FluidStorage(int capacity, int maxTransfer) {
		this(capacity, capacity, maxTransfer);
	}

	public FluidStorage(int capacity, boolean canReceive, boolean canExtract) {
		this(capacity, capacity, canReceive, canExtract);
	}

	public FluidStorage(int capacity, int maxReceive, int maxExtract) {
		this(capacity, maxReceive, maxExtract, true, true);
	}

	/**
	 * Default constructor for FluidStorage
	 * 
	 * @param capacity max fluid capacity
	 * @param maxTransfer max values for both receive & extract
	 * @param canReceive
	 * @param canExtract
	 */
	public FluidStorage(int capacity, int maxTransfer, boolean canReceive, boolean canExtract) {
		this(capacity, maxTransfer, maxTransfer, canReceive, canExtract);
	}

	public FluidStorage(int capacity, int maxReceive, int maxExtract, boolean canReceive, boolean canExtract) {
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
		this.canReceive = canReceive;
		this.canExtract = canExtract;
	}
    
	public void writeToNBT(NBTTagCompound tag) {
        contents.writeToNBT(tag);
	}
	
	public void readFromNBT(NBTTagCompound tag) {
		contents = FluidStack.loadFluidStackFromNBT(tag);
	}
    
	@Override
	public IFluidTankProperties[] getTankProperties() {
		return new IFluidTankProperties[] {this};
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
        if (!canFillFluidType(resource)) return 0;
        final int fill = MathHelper.min(resource.amount, maxReceive, contents.amount);
        if (doFill)
        	contents.amount += fill;
        return fill;
	}

    @Nullable
	@Override
	public FluidStack drain(final FluidStack resource, final boolean doDrain) {
        if (!canDrainFluidType(resource)) return null;
        return drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
        final int drain = MathHelper.min(maxDrain, maxExtract, contents.amount);
        if (doDrain)
        	contents.amount -= drain;
        final FluidStack drained = contents.copy();
        drained.amount = drain;
        return drained;
	}

	@Override
	public FluidStack getContents() {
        return contents.copy();
	}

	@Override
	public int getCapacity() {
        return capacity;
	}

	@Override
	public boolean canFill() {
        return canReceive;
	}

	@Override
	public boolean canDrain() {
        return canExtract;
	}

	@Override
	public boolean canFillFluidType(FluidStack fluidStack) {
		return fluidStack.isFluidEqual(contents);
	}

	@Override
	public boolean canDrainFluidType(FluidStack fluidStack) {
        return fluidStack.isFluidEqual(contents);
	}
}