package de.bigmachines.fluids;

import de.bigmachines.BigMachines;
import de.bigmachines.Reference;
import de.bigmachines.blocks.IBlockBase;
import de.bigmachines.blocks.ItemBlockBase;
import de.bigmachines.interfaces.IModelRegister;
import de.bigmachines.utils.StateMapper;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFluidBase extends BlockFluidClassic implements IBlockBase, IModelRegister {
	
	public String name;
	public ItemBlock itemBlock;
	
	public BlockFluidBase(Fluid fluid, MapColor color, String name) {
		super(fluid, new MaterialLiquid(color));
		this.name = name;
		setUnlocalizedName(Reference.MOD_ID + ".fluid." + name);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
		this.itemBlock = new ItemBlockBase(this);
		BigMachines.proxy.addIModelRegister(this);
	}

	@Override
	public ItemBlock getItemBlock() {
		return itemBlock;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		StateMapper stateMapper = new StateMapper(name, "normal");
		ModelLoader.setCustomMeshDefinition(getItemBlock(), stateMapper);
		ModelLoader.setCustomStateMapper(this, stateMapper);
	}
	
}
