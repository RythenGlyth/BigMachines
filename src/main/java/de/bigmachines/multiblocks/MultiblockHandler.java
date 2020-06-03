package de.bigmachines.multiblocks;

import java.util.ArrayList;

import net.minecraft.util.ResourceLocation;

public class MultiblockHandler {
	
	public static ArrayList<IMultiblock> multiblocks = new ArrayList<IMultiblock>();
	
	public static void registerMultiblock(IMultiblock multiblock) {
		multiblocks.add(multiblock);
	}
	
	public static IMultiblock getMultiBlock(ResourceLocation id) {
		for(IMultiblock multiblock : multiblocks) {
			if(multiblock.getID().equals(id)) return multiblock;
		}
		return null;
	}
	
	public static ArrayList<IMultiblock> getMultiblocks() {
		return multiblocks;
	}
	
}
