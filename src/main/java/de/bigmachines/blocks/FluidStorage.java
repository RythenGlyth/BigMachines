package de.bigmachines.blocks;

import de.bigmachines.utils.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class FluidStorage implements IFluidHandler, IFluidTankProperties {
	private final int capacity;
	private final int maxReceive;
	private final int maxExtract;
	private final boolean canReceive;
	private final boolean canExtract;
	private boolean hasChanged;

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
	 * @param canReceive whether this fluid storage can receive every fluid at any time
	 * @param canExtract whether this fluid storage can give out any fluid at any time
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
        if(contents != null) contents.writeToNBT(tag);
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
		if(!(contents == null || contents.amount <= 0) && !canFillFluidType(resource)) {
			return 0;
		}
        final int fill = MathHelper.min(resource.amount, (contents == null ? maxReceive : Math.min(getCapacity() - getAmount(), maxReceive)));
        if (doFill) {
        	if(contents == null || contents.amount <= 0) {
        		contents = new FluidStack(resource.getFluid(), fill);
        	}
        	contents.amount += fill;
        	hasChanged = true;
        }
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
		if(contents == null) return new FluidStack(FluidRegistry.WATER, 0);
        final int drain = MathHelper.min(maxDrain, maxExtract, contents.amount);
        if (doDrain) {
        	contents.amount -= drain;
        	hasChanged = true;
        }
        final FluidStack drained = contents.copy();
        drained.amount = drain;
        return drained;
	}
	
	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
	}

	public int getAmount() {
		FluidStack contents = getContents();
        return contents == null ? 0 : contents.amount;
	}

	@Override
	public FluidStack getContents() {
        return contents == null ? null : contents.copy();
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
		if(contents == null || contents.amount <= 0) {
			return true;
		} else return fluidStack.isFluidEqual(contents);
	}

	@Override
	public boolean canDrainFluidType(FluidStack fluidStack) {
        return fluidStack.isFluidEqual(contents);
	}
	
	public boolean hasChanged() {
		return hasChanged;
	}
}
