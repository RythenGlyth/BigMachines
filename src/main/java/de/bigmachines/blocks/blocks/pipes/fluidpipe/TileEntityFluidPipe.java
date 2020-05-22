package de.bigmachines.blocks.blocks.pipes.fluidpipe;

import de.bigmachines.blocks.FluidStorage;
import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase;
import de.bigmachines.blocks.blocks.pipes.heatpipe.TileEntityHeatPipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileEntityFluidPipe extends TileEntityPipeBase {
	
	FluidStorage fluidStorage;
	
	public TileEntityFluidPipe() {
		super(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
		fluidStorage = new FluidStorage(1000);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidStorage);
		return super.getCapability(capability, facing);
	}
	
	
	
}
