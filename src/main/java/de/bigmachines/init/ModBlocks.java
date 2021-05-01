package de.bigmachines.init;

import de.bigmachines.Reference;
import de.bigmachines.blocks.BaseBlockItem;
import de.bigmachines.blocks.blocks.RubberLogBlock;
import de.bigmachines.blocks.blocks.machines.MachineCasing;
import de.bigmachines.blocks.blocks.reactor.ReactorCoreBlock;
import de.bigmachines.world.feature.RubberTree;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
	
	//RUBBER
	public static RegistryObject<Block> rubber_sapling = BLOCKS.register("rubber_sapling",
		() -> new SaplingBlock(
			new RubberTree(),
			AbstractBlock.Properties.of(Material.PLANT)
				.randomTicks()
				.noCollission()
				.instabreak()
				.sound(SoundType.GRASS)
		)
	);
	public static RegistryObject<Item> rubber_sapling_item = ModItems.ITEMS.register("rubber_sapling", () -> new BaseBlockItem(rubber_sapling.get()));
	
	public static RegistryObject<Block> rubber_log = BLOCKS.register("rubber_log", () -> new RubberLogBlock());
	public static RegistryObject<Item> rubber_log_item = ModItems.ITEMS.register("rubber_log", () -> new BaseBlockItem(rubber_log.get()));
	
	public static RegistryObject<Block> rubber_leaves = BLOCKS.register("rubber_leaves", () -> new LeavesBlock(Block.Properties.copy(Blocks.OAK_LEAVES)));
	public static RegistryObject<Item> rubber_leaves_item = ModItems.ITEMS.register("rubber_leaves", () -> new BaseBlockItem(rubber_leaves.get()));

	public static RegistryObject<Block> machine_casing = BLOCKS.register("machine_casing", () -> new MachineCasing());
	public static RegistryObject<Item> machine_casing_item = ModItems.ITEMS.register("machine_casing", () -> new BaseBlockItem(machine_casing.get()));
	
	public static RegistryObject<Block> reactor_core = BLOCKS.register("reactor_core", () -> new ReactorCoreBlock());
	public static RegistryObject<Item> reactor_core_item = ModItems.ITEMS.register("reactor_core",
			  () -> new BaseBlockItem(reactor_core.get()));
	
	public static void init() {
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
}
