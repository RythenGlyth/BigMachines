package de.bigmachines.blocks.blocks;

import de.bigmachines.blocks.BlockBase;
import de.bigmachines.blocks.BlockBaseOreDict;
import de.bigmachines.init.ModCreativeTabs;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockPinkDirt extends BlockBaseOreDict {

	public BlockPinkDirt() {
		super(Material.GROUND, "pink_dirt", "dirt");
		setHardness(.5F);
		setSoundType(SoundType.GROUND);
		setCreativeTab(ModCreativeTabs.modTab);
//		setHarvestLevel("shovel", 0);

	}
}
