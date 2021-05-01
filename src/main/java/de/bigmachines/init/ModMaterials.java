package de.bigmachines.init;

import de.bigmachines.blocks.blocks.LayerBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;

public class ModMaterials {
	
	//METALS
	
		//ingots
		public static final RegistryObject<Item> copper_ingot = ModItems.ITEMS.register("copper_ingot", () -> new Item(new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> lead_ingot = ModItems.ITEMS.register("lead_ingot", () -> new Item(new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> tin_ingot = ModItems.ITEMS.register("tin_ingot", () -> new Item(new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> silver_ingot = ModItems.ITEMS.register("silver_ingot", () -> new Item(new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> aluminum_ingot = ModItems.ITEMS.register("aluminum_ingot", () -> new Item(new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		
		//nuggets
		public static final RegistryObject<Item> copper_nugget = ModItems.ITEMS.register("copper_nugget", () -> new Item(new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> lead_nugget = ModItems.ITEMS.register("lead_nugget", () -> new Item(new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> tin_nugget = ModItems.ITEMS.register("tin_nugget", () -> new Item(new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> silver_nugget = ModItems.ITEMS.register("silver_nugget", () -> new Item(new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> aluminum_nugget = ModItems.ITEMS.register("aluminum_nugget", () -> new Item(new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
	
		//Ores
		public static final RegistryObject<Block> copper_ore = ModBlocks.BLOCKS.register("copper_ore", () -> new Block(AbstractBlock.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(3.0F)));
		public static final RegistryObject<Block> lead_ore = ModBlocks.BLOCKS.register("lead_ore", () -> new Block(AbstractBlock.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(3.0F)));
		public static final RegistryObject<Block> tin_ore = ModBlocks.BLOCKS.register("tin_ore", () -> new Block(AbstractBlock.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(3.0F)));
		public static final RegistryObject<Block> silver_ore = ModBlocks.BLOCKS.register("silver_ore", () -> new Block(AbstractBlock.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(3.0F)));
		public static final RegistryObject<Block> aluminum_ore = ModBlocks.BLOCKS.register("aluminum_ore", () -> new Block(AbstractBlock.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(3.0F)));
		
		//Ores Items
		public static RegistryObject<Item> copper_ore_item = ModItems.ITEMS.register("copper_ore", () -> new BlockItem(copper_ore.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static RegistryObject<Item> lead_ore_item = ModItems.ITEMS.register("lead_ore", () -> new BlockItem(lead_ore.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static RegistryObject<Item> tin_ore_item = ModItems.ITEMS.register("tin_ore", () -> new BlockItem(tin_ore.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static RegistryObject<Item> silver_ore_item = ModItems.ITEMS.register("silver_ore", () -> new BlockItem(silver_ore.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static RegistryObject<Item> aluminum_ore_item = ModItems.ITEMS.register("aluminum_ore", () -> new BlockItem(aluminum_ore.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
	
		//StorageBlocks
		public static final RegistryObject<Block> copper_block = ModBlocks.BLOCKS.register("copper_block", () -> new Block(AbstractBlock.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(3.0F)));
		public static final RegistryObject<Block> lead_block = ModBlocks.BLOCKS.register("lead_block", () -> new Block(AbstractBlock.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(3.0F)));
		public static final RegistryObject<Block> tin_block = ModBlocks.BLOCKS.register("tin_block", () -> new Block(AbstractBlock.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(3.0F)));
		public static final RegistryObject<Block> silver_block = ModBlocks.BLOCKS.register("silver_block", () -> new Block(AbstractBlock.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(3.0F)));
		public static final RegistryObject<Block> aluminum_block = ModBlocks.BLOCKS.register("aluminum_block", () -> new Block(AbstractBlock.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(3.0F)));
		
		//StorageBlocks Items
		public static RegistryObject<Item> copper_block_item = ModItems.ITEMS.register("copper_block", () -> new BlockItem(copper_block.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static RegistryObject<Item> lead_block_item = ModItems.ITEMS.register("lead_block", () -> new BlockItem(lead_block.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static RegistryObject<Item> tin_block_item = ModItems.ITEMS.register("tin_block", () -> new BlockItem(tin_block.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));	
		public static RegistryObject<Item> silver_block_item = ModItems.ITEMS.register("silver_block", () -> new BlockItem(silver_block.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static RegistryObject<Item> aluminum_block_item = ModItems.ITEMS.register("aluminum_block", () -> new BlockItem(aluminum_block.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		
	//Stones
		//NORMAL
		public static final RegistryObject<Block> blackstone = ModBlocks.BLOCKS.register("blackstone", () -> new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 10.0F)));
		public static final RegistryObject<Block> limestone = ModBlocks.BLOCKS.register("limestone", () -> new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 10.0F)));
		public static final RegistryObject<Block> marble = ModBlocks.BLOCKS.register("marble", () -> new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 10.0F)));
		public static final RegistryObject<Block> slate = ModBlocks.BLOCKS.register("slate", () -> new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 10.0F)));
		//NORMAL Items
		public static final RegistryObject<Item> blackstone_item = ModItems.ITEMS.register("blackstone", () -> new BlockItem(blackstone.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> limestone_item = ModItems.ITEMS.register("limestone", () -> new BlockItem(limestone.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> marble_item = ModItems.ITEMS.register("marble", () -> new BlockItem(marble.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> slate_item = ModItems.ITEMS.register("slate", () -> new BlockItem(slate.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));

		//SMOOTH
		public static final RegistryObject<Block> smooth_blackstone = ModBlocks.BLOCKS.register("smooth_blackstone", () -> new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2F, 10.0F)));
		public static final RegistryObject<Block> smooth_limestone = ModBlocks.BLOCKS.register("smooth_limestone", () -> new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2F, 10.0F)));
		public static final RegistryObject<Block> smooth_marble = ModBlocks.BLOCKS.register("smooth_marble", () -> new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2F, 10.0F)));
		public static final RegistryObject<Block> smooth_slate = ModBlocks.BLOCKS.register("smooth_slate", () -> new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2F, 10.0F)));
		//SMOOTH Items
		public static final RegistryObject<Item> smooth_blackstone_item = ModItems.ITEMS.register("smooth_blackstone", () -> new BlockItem(smooth_blackstone.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> smooth_limestone_item = ModItems.ITEMS.register("smooth_limestone", () -> new BlockItem(smooth_limestone.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> smooth_marble_item = ModItems.ITEMS.register("smooth_marble", () -> new BlockItem(smooth_marble.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> smooth_slate_item = ModItems.ITEMS.register("smooth_slate", () -> new BlockItem(smooth_slate.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));

		//BRICK
		public static final RegistryObject<Block> blackstone_bricks = ModBlocks.BLOCKS.register("blackstone_bricks", () -> new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 10.0F)));
		public static final RegistryObject<Block> limestone_bricks = ModBlocks.BLOCKS.register("limestone_bricks", () -> new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 10.0F)));
		public static final RegistryObject<Block> marble_bricks = ModBlocks.BLOCKS.register("marble_bricks", () -> new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 10.0F)));
		public static final RegistryObject<Block> slate_bricks = ModBlocks.BLOCKS.register("slate_bricks", () -> new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 10.0F)));
		//BRICK Items
		public static final RegistryObject<Item> blackstone_bricks_item = ModItems.ITEMS.register("blackstone_bricks", () -> new BlockItem(blackstone_bricks.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> limestone_bricks_item = ModItems.ITEMS.register("limestone_bricks", () -> new BlockItem(limestone_bricks.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> marble_bricks_item = ModItems.ITEMS.register("marble_bricks", () -> new BlockItem(marble_bricks.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> slate_bricks_item = ModItems.ITEMS.register("slate_bricks", () -> new BlockItem(slate_bricks.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
	
	//LAYERS
		//NORMAL
		public static RegistryObject<Block> blackstone_layers = ModBlocks.BLOCKS.register("blackstone_layers", () -> new LayerBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 10.0F)));
		public static RegistryObject<Block> limestone_layers = ModBlocks.BLOCKS.register("limestone_layers", () -> new LayerBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 10.0F)));
		public static RegistryObject<Block> marble_layers = ModBlocks.BLOCKS.register("marble_layers", () -> new LayerBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 10.0F)));
		public static RegistryObject<Block> slate_layers = ModBlocks.BLOCKS.register("slate_layers", () -> new LayerBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 10.0F)));
		public static RegistryObject<Block> glass_layers = ModBlocks.BLOCKS.register("glass_layers", () -> new LayerBlock(AbstractBlock.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn((BlockState state, IBlockReader reader, BlockPos pos, EntityType<?> entity) -> false).isViewBlocking((BlockState state, IBlockReader reader, BlockPos pos) -> false).isSuffocating((BlockState state, IBlockReader reader, BlockPos pos) -> false)));
		//NORMAL Items
		public static final RegistryObject<Item> blackstone_layers_item = ModItems.ITEMS.register("blackstone_layers", () -> new BlockItem(blackstone_layers.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> limestone_layers_item = ModItems.ITEMS.register("limestone_layers", () -> new BlockItem(limestone_layers.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> marble_layers_item = ModItems.ITEMS.register("marble_layers", () -> new BlockItem(marble_layers.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> slate_layers_item = ModItems.ITEMS.register("slate_layers", () -> new BlockItem(slate_layers.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> glass_layers_item = ModItems.ITEMS.register("glass_layers", () -> new BlockItem(glass_layers.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		
		//SMOOTH
		public static RegistryObject<Block> smooth_blackstone_layers = ModBlocks.BLOCKS.register("smooth_blackstone_layers", () -> new LayerBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2F, 10.0F)));
		public static RegistryObject<Block> smooth_limestone_layers = ModBlocks.BLOCKS.register("smooth_limestone_layers", () -> new LayerBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2F, 10.0F)));
		public static RegistryObject<Block> smooth_marble_layers = ModBlocks.BLOCKS.register("smooth_marble_layers", () -> new LayerBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2F, 10.0F)));
		public static RegistryObject<Block> smooth_slate_layers = ModBlocks.BLOCKS.register("smooth_slate_layers", () -> new LayerBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2F, 10.0F)));
		//NORMAL Items
		public static final RegistryObject<Item> smooth_blackstone_layers_item = ModItems.ITEMS.register("smooth_blackstone_layers", () -> new BlockItem(smooth_blackstone_layers.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> smooth_limestone_layers_item = ModItems.ITEMS.register("smooth_limestone_layers", () -> new BlockItem(smooth_limestone_layers.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> smooth_marble_layers_item = ModItems.ITEMS.register("smooth_marble_layers", () -> new BlockItem(smooth_marble_layers.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> smooth_slate_layers_item = ModItems.ITEMS.register("smooth_slate_layers", () -> new BlockItem(smooth_slate_layers.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		
		//BRICK
		public static RegistryObject<Block> blackstone_bricks_layers = ModBlocks.BLOCKS.register("blackstone_bricks_layers", () -> new LayerBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 10.0F)));
		public static RegistryObject<Block> limestone_bricks_layers = ModBlocks.BLOCKS.register("limestone_bricks_layers", () -> new LayerBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 10.0F)));
		public static RegistryObject<Block> marble_bricks_layers = ModBlocks.BLOCKS.register("marble_bricks_layers", () -> new LayerBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 10.0F)));
		public static RegistryObject<Block> slate_bricks_layers = ModBlocks.BLOCKS.register("slate_bricks_layers", () -> new LayerBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 10.0F)));
		//BRICK Items
		public static final RegistryObject<Item> blackstone_bricks_layers_item = ModItems.ITEMS.register("blackstone_bricks_layers", () -> new BlockItem(blackstone_bricks_layers.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> limestone_bricks_layers_item = ModItems.ITEMS.register("limestone_bricks_layers", () -> new BlockItem(limestone_bricks_layers.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> marble_bricks_layers_item = ModItems.ITEMS.register("marble_bricks_layers", () -> new BlockItem(marble_bricks_layers.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
		public static final RegistryObject<Item> slate_bricks_layers_item = ModItems.ITEMS.register("slate_bricks_layers", () -> new BlockItem(slate_bricks_layers.get(), new Item.Properties().tab(ModItemGroups.MATERIAL_GROUP)));
	
	
	public static void init() {
	}
	
}
