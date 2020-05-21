package de.bigmachines.init;

import de.bigmachines.Reference;
import de.bigmachines.blocks.blocks.pipes.TileEntitySpecialRendererPipeBase;
import de.bigmachines.blocks.blocks.pipes.fluidpipe.TileEntityFluidPipe;
import de.bigmachines.blocks.blocks.pipes.heatpipe.TileEntityHeatPipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {
	
	public static void init() {
		GameRegistry.registerTileEntity(TileEntityHeatPipe.class, new ResourceLocation(Reference.MOD_ID, "pipe_heat"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHeatPipe.class, new TileEntitySpecialRendererPipeBase(new ResourceLocation(Reference.MOD_ID, "textures/block/pipe_heat.png"), 32D));
		GameRegistry.registerTileEntity(TileEntityFluidPipe.class, new ResourceLocation(Reference.MOD_ID, "pipe_fluid"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidPipe.class, new TileEntitySpecialRendererPipeBase(new ResourceLocation(Reference.MOD_ID, "textures/block/pipe_fluid.png"), 32D));
	}
	
}
