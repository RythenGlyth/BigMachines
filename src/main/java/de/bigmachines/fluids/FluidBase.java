package de.bigmachines.fluids;

import de.bigmachines.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidBase extends Fluid {
	
	public FluidBase(String name) {
		super(name, new ResourceLocation(Reference.MOD_ID, "block/fluids/" + name + "_still"), new ResourceLocation(Reference.MOD_ID, "block/fluids/" + name + "_flow"));
	}

}
