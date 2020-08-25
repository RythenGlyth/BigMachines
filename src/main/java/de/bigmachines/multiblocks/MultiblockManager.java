package de.bigmachines.multiblocks;

import java.util.ArrayList;

import net.minecraft.util.ResourceLocation;

public class MultiblockManager {
	
	public static ArrayList<MultiblockBase> multiblocks = new ArrayList<MultiblockBase>();
	
	public static void registerMultiblock(MultiblockBase multiblock) {
		multiblocks.add(multiblock);
	}
	
	public static MultiblockBase getMultiBlock(ResourceLocation id) {
		for(MultiblockBase multiblock : multiblocks) {
			if(multiblock.getID().equals(id)) return multiblock;
		}
		return null;
	}
	
}