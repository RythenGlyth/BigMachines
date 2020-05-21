package de.bigmachines.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class CapabilityHotFluid {
	
	@CapabilityInject(IHotFluidHandler.class)
	public static Capability<IHotFluidHandler> HOT_FLUID = null;
	
	public static void register()
    {
        /*CapabilityManager.INSTANCE.register(IHotFluidHandler.class, new IStorage<IHotFluidHandler>()*/
    }
	
}
