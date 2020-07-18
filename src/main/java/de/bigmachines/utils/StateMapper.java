package de.bigmachines.utils;

import de.bigmachines.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * mainly needed for fluids
 * 
 * @author RythenGlyth
 *
 */

public class StateMapper extends StateMapperBase implements ItemMeshDefinition {
	
	public ModelResourceLocation location;
	
	public StateMapper(String resourcePath, String modelName) {
		this.location = new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, resourcePath), modelName);
	}
	
	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack) {
		return location;
	}

	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
		return location;
	}

}
