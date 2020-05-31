package de.bigmachines.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public final class CapabilityHotFluid {

	@CapabilityInject(IHotFluidHandler.class)
	public static final Capability<IHotFluidHandler> HOT_FLUID = null;
	
	public static void register()
    {
        /*CapabilityManager.INSTANCE.register(IHotFluidHandler.class, new IStorage<IHotFluidHandler>()*/
    }
	
}
