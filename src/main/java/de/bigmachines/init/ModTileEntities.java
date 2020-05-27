package de.bigmachines.init;

import de.bigmachines.Reference;
import de.bigmachines.blocks.blocks.pipes.TileEntitySpecialRendererPipeBase;
import de.bigmachines.blocks.blocks.pipes.fluidpipe.TileEntityFluidPipe;
import de.bigmachines.blocks.blocks.pipes.heatpipe.TileEntityHeatPipe;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModTileEntities {
	
	public static void init() {
		GameRegistry.registerTileEntity(TileEntityHeatPipe.class, new ResourceLocation(Reference.MOD_ID, "pipe_heat"));
		GameRegistry.registerTileEntity(TileEntityFluidPipe.class, new ResourceLocation(Reference.MOD_ID, "pipe_fluid"));
		
		if(FMLCommonHandler.instance().getSide().equals(Side.CLIENT)) initSpecialRenderers();
	}
	
	@SideOnly(Side.CLIENT)
	public static void initSpecialRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHeatPipe.class, new TileEntitySpecialRendererPipeBase(new ResourceLocation(Reference.MOD_ID, "textures/block/pipe_heat.png"), 32D));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidPipe.class, new TileEntitySpecialRendererPipeBase(new ResourceLocation(Reference.MOD_ID, "textures/block/pipe_fluid.png"), 32D));
	}
	
}
