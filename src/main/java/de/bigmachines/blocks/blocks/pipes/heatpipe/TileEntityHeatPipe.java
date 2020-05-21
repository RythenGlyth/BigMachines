package de.bigmachines.blocks.blocks.pipes.heatpipe;

import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase;
import de.bigmachines.capability.CapabilityHotFluid;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityHeatPipe extends TileEntityPipeBase {

	public TileEntityHeatPipe() {
		super(CapabilityHotFluid.HOT_FLUID);
	}
	
	
	
}
