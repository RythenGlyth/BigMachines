package de.bigmachines.init;

import de.bigmachines.Reference;
import de.bigmachines.blocks.BaseBlockItem;
import de.bigmachines.blocks.blocks.RubberSaplingBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
	
	//RUBBER
	public static RegistryObject<Block> RUBBER_SAPLING_BLOCK = BLOCKS.register("rubber_sapling", RubberSaplingBlock::new);
	public static RegistryObject<Item> RUBBER_SAPLING_BLOCK_ITEM = ModItems.ITEMS.register("rubber_sapling", () -> new BaseBlockItem(RUBBER_SAPLING_BLOCK.get()));
	
	public static void init() {
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
}
