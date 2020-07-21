package de.bigmachines.init;

import de.bigmachines.fluids.BlockFluidBase;
import de.bigmachines.fluids.FluidBase;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {
	
	public static Fluid rubber_latex;
	public static BlockFluidBase rubber_latex_block;
	
	public static void preInit() {
		registerFluids();
		registerFluidBlocks();
		registerFluidBuckets();
	}
	
	public static void registerFluids() {
		rubber_latex = new FluidBase("rubber_latex").setDensity(921).setRarity(EnumRarity.COMMON).setViscosity(4600);
		FluidRegistry.registerFluid(rubber_latex);
	}
	
	public static void registerFluidBlocks() {
		rubber_latex_block = (BlockFluidBase) new BlockFluidBase(rubber_latex, MapColor.SNOW, "rubber_latex").setQuantaPerBlock(4);
		ModBlocks.BLOCKS.add(rubber_latex_block);
	}
	
	public static void registerFluidBuckets() {
		FluidRegistry.addBucketForFluid(rubber_latex);
	}
	
}
