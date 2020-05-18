package de.bigmachines.blocks;

import de.bigmachines.BigMachines;
import de.bigmachines.interfaces.IModelRegister;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockBase extends ItemBlock implements IModelRegister {

    public ItemBlockBase(Block block) {
        super(block);
        setRegistryName(block.getRegistryName());
		BigMachines.proxy.addIModelRegister(this);
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		System.out.println(this);
		System.out.println(getRegistryName());
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
    
}