package de.bigmachines.init;

import de.bigmachines.blocks.BlockBase;
import de.bigmachines.blocks.BlockBaseOreDict;
import de.bigmachines.blocks.blocks.BlockCrystal;
import de.bigmachines.blocks.blocks.BlockFloor;
import de.bigmachines.items.ItemBase;
import de.bigmachines.items.ItemBaseOreDict;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

import java.util.HashMap;

public final class ModMaterials {

    public static ItemBase ingot_copper;
    public static ItemBase ingot_lead;
    public static ItemBase ingot_tin;
    public static ItemBase ingot_silver;
    public static ItemBase ingot_aluminum;

    public static ItemBase nugget_copper;
    public static ItemBase nugget_lead;
    public static ItemBase nugget_tin;
    public static ItemBase nugget_silver;
    public static ItemBase nugget_aluminum;

    public static BlockBase ore_copper;
    public static BlockBase ore_lead;
    public static BlockBase ore_tin;
    public static BlockBase ore_silver;
    public static BlockBase ore_aluminum;

    public static BlockBase block_copper;
    public static BlockBase block_lead;
    public static BlockBase block_tin;
    public static BlockBase block_silver;
    public static BlockBase block_aluminum;
    
	public static BlockBase pink_dirt;
	
	
	
	public static BlockBase blackstone;
	public static BlockBase blackstone_smooth;
	public static BlockBase blackstone_brick;
	
	public static BlockBase limestone;
	public static BlockBase limestone_smooth;
	public static BlockBase limestone_brick;
	
	public static BlockBase marble;
	public static BlockBase marble_smooth;
	public static BlockBase marble_brick;
	
	public static BlockBase slate;
	public static BlockBase slate_smooth;
	public static BlockBase slate_brick;
	
	public static BlockFloor floor_blackstone;
	public static BlockFloor floor_blackstone_smooth;
	public static BlockFloor floor_blackstone_brick;
	
	public static BlockFloor floor_limestone;
	public static BlockFloor floor_limestone_smooth;
	public static BlockFloor floor_limestone_brick;
	
	public static BlockFloor floor_marble;
	public static BlockFloor floor_marble_smooth;
	public static BlockFloor floor_marble_brick;
	
	public static BlockFloor floor_slate;
	public static BlockFloor floor_slate_smooth;
	public static BlockFloor floor_slate_brick;
	
	public static BlockFloor floor_glass;
	

	public static BlockCrystal crystal;
	
	public static HashMap<Block, BlockFloor> floorBlocks = new HashMap<Block, BlockFloor>();

    public static void preInit() {
        initMetals();
        initStones();
        
        
    	pink_dirt = (BlockBase) new BlockBaseOreDict(Material.GROUND, "pink_dirt", "dirt").setSoundType(SoundType.GROUND).setHardness(0.5F).setCreativeTab(ModCreativeTabs.materialsTab);
    	ModBlocks.BLOCKS.add(pink_dirt);
    	
    	crystal = new BlockCrystal();
    	ModBlocks.BLOCKS.add(crystal);
    	
    }

    private static void initStones() {
    	blackstone = (BlockBase) new BlockBaseOreDict(Material.ROCK, "blackstone", "blackstone").setSoundType(SoundType.STONE).setHardness(1.5F).setResistance(10.0F).setCreativeTab(ModCreativeTabs.materialsTab);
    	blackstone_smooth = (BlockBase) new BlockBaseOreDict(Material.ROCK, "blackstone_smooth", "blackstone_smooth").setSoundType(SoundType.STONE).setHardness(1.5F).setResistance(10.0F).setCreativeTab(ModCreativeTabs.materialsTab);
    	blackstone_brick = (BlockBase) new BlockBaseOreDict(Material.ROCK, "blackstone_brick", "blackstone_brick").setSoundType(SoundType.STONE).setHardness(1.5F).setResistance(10.0F).setCreativeTab(ModCreativeTabs.materialsTab);
    	ModBlocks.BLOCKS.add(blackstone);
    	ModBlocks.BLOCKS.add(blackstone_smooth);
        ModBlocks.BLOCKS.add(blackstone_brick);
        
    	limestone = (BlockBase) new BlockBaseOreDict(Material.ROCK, "limestone", "limestone").setSoundType(SoundType.STONE).setHardness(1.5F).setResistance(10.0F).setCreativeTab(ModCreativeTabs.materialsTab);
    	limestone_smooth = (BlockBase) new BlockBaseOreDict(Material.ROCK, "limestone_smooth", "limestone_smooth").setSoundType(SoundType.STONE).setHardness(1.5F).setResistance(10.0F).setCreativeTab(ModCreativeTabs.materialsTab);
    	limestone_brick = (BlockBase) new BlockBaseOreDict(Material.ROCK, "limestone_brick", "limestone_brick").setSoundType(SoundType.STONE).setHardness(1.5F).setResistance(10.0F).setCreativeTab(ModCreativeTabs.materialsTab);
    	ModBlocks.BLOCKS.add(limestone);
    	ModBlocks.BLOCKS.add(limestone_smooth);
        ModBlocks.BLOCKS.add(limestone_brick);
        
    	marble = (BlockBase) new BlockBaseOreDict(Material.ROCK, "marble", "marble").setSoundType(SoundType.STONE).setHardness(1.5F).setResistance(10.0F).setCreativeTab(ModCreativeTabs.materialsTab);
    	marble_smooth = (BlockBase) new BlockBaseOreDict(Material.ROCK, "marble_smooth", "marble_smooth").setSoundType(SoundType.STONE).setHardness(1.5F).setResistance(10.0F).setCreativeTab(ModCreativeTabs.materialsTab);
    	marble_brick = (BlockBase) new BlockBaseOreDict(Material.ROCK, "marble_brick", "marble_brick").setSoundType(SoundType.STONE).setHardness(1.5F).setResistance(10.0F).setCreativeTab(ModCreativeTabs.materialsTab);
    	ModBlocks.BLOCKS.add(marble);
    	ModBlocks.BLOCKS.add(marble_smooth);
        ModBlocks.BLOCKS.add(marble_brick);
        
    	slate = (BlockBase) new BlockBaseOreDict(Material.ROCK, "slate", "slate").setSoundType(SoundType.STONE).setHardness(1.5F).setResistance(10.0F).setCreativeTab(ModCreativeTabs.materialsTab);
    	slate_smooth = (BlockBase) new BlockBaseOreDict(Material.ROCK, "slate_smooth", "slate_smooth").setSoundType(SoundType.STONE).setHardness(1.5F).setResistance(10.0F).setCreativeTab(ModCreativeTabs.materialsTab);
    	slate_brick = (BlockBase) new BlockBaseOreDict(Material.ROCK, "slate_brick", "slate_brick").setSoundType(SoundType.STONE).setHardness(1.5F).setResistance(10.0F).setCreativeTab(ModCreativeTabs.materialsTab);
    	ModBlocks.BLOCKS.add(slate);
    	ModBlocks.BLOCKS.add(slate_smooth);
        ModBlocks.BLOCKS.add(slate_brick);
        

    	floor_blackstone = new BlockFloor("floor_blackstone");
    	floor_blackstone_smooth = new BlockFloor("floor_blackstone_smooth");
    	floor_blackstone_brick = new BlockFloor("floor_blackstone_brick");
        ModBlocks.BLOCKS.add(floor_blackstone);
        ModBlocks.BLOCKS.add(floor_blackstone_smooth);
        ModBlocks.BLOCKS.add(floor_blackstone_brick);
        floorBlocks.put(blackstone, floor_blackstone);
        floorBlocks.put(blackstone_smooth, floor_blackstone_smooth);
        floorBlocks.put(blackstone_brick, floor_blackstone_brick);
        
    	floor_limestone = new BlockFloor("floor_limestone");
    	floor_limestone_smooth = new BlockFloor("floor_limestone_smooth");
    	floor_limestone_brick = new BlockFloor("floor_limestone_brick");
        ModBlocks.BLOCKS.add(floor_limestone);
        ModBlocks.BLOCKS.add(floor_limestone_smooth);
        ModBlocks.BLOCKS.add(floor_limestone_brick);
        floorBlocks.put(limestone, floor_limestone);
        floorBlocks.put(limestone_smooth, floor_limestone_smooth);
        floorBlocks.put(limestone_brick, floor_limestone_brick);
        
    	floor_marble = new BlockFloor("floor_marble");
    	floor_marble_smooth = new BlockFloor("floor_marble_smooth");
    	floor_marble_brick = new BlockFloor("floor_marble_brick");
        ModBlocks.BLOCKS.add(floor_marble);
        ModBlocks.BLOCKS.add(floor_marble_smooth);
        ModBlocks.BLOCKS.add(floor_marble_brick);
        floorBlocks.put(marble, floor_marble);
        floorBlocks.put(marble_smooth, floor_marble_smooth);
        floorBlocks.put(marble_brick, floor_marble_brick);
        
    	floor_slate = new BlockFloor("floor_slate");
    	floor_slate_smooth = new BlockFloor("floor_slate_smooth");
    	floor_slate_brick = new BlockFloor("floor_slate_brick");
        ModBlocks.BLOCKS.add(floor_slate);
        ModBlocks.BLOCKS.add(floor_slate_smooth);
        ModBlocks.BLOCKS.add(floor_slate_brick);
        floorBlocks.put(slate, floor_slate);
        floorBlocks.put(slate_smooth, floor_slate_smooth);
        floorBlocks.put(slate_brick, floor_slate_brick);

    	floor_glass = new BlockFloor("floor_glass", Material.GLASS, SoundType.GLASS, 0.3F, 0F);
        ModBlocks.BLOCKS.add(floor_glass);
        floorBlocks.put(Blocks.GLASS, floor_glass);
        
    }

    private static void initMetals() {
    	// ingots

        ingot_copper = new ItemBaseOreDict("ingot_copper", "ingotCopper");
        ingot_lead = new ItemBaseOreDict("ingot_lead", "ingotLead");
        ingot_tin = new ItemBaseOreDict("ingot_tin", "ingotTin");
        ingot_silver = new ItemBaseOreDict("ingot_silver", "ingotSilver");
        ingot_aluminum = new ItemBaseOreDict("ingot_aluminum", "ingotAluminum");

        ingot_copper.setCreativeTab(ModCreativeTabs.materialsTab);
        ingot_lead.setCreativeTab(ModCreativeTabs.materialsTab);
        ingot_tin.setCreativeTab(ModCreativeTabs.materialsTab);
        ingot_silver.setCreativeTab(ModCreativeTabs.materialsTab);
        ingot_aluminum.setCreativeTab(ModCreativeTabs.materialsTab);
        
        /*OreDictionary.registerOre("ingotCopper", ingot_copper);
        OreDictionary.registerOre("ingotLead", ingot_lead);
        OreDictionary.registerOre("ingotTin", ingot_tin);
        OreDictionary.registerOre("ingotSilver", ingot_silver);
        OreDictionary.registerOre("ingotAluminum", ingot_aluminum);*/

        ModItems.ITEMS.add(ingot_copper);
        ModItems.ITEMS.add(ingot_lead);
        ModItems.ITEMS.add(ingot_tin);
        ModItems.ITEMS.add(ingot_silver);
        ModItems.ITEMS.add(ingot_aluminum);

        //nuggets

        nugget_copper = new ItemBaseOreDict("nugget_copper", "nuggetCopper");
        nugget_lead = new ItemBaseOreDict("nugget_lead", "nuggetLead");
        nugget_tin = new ItemBaseOreDict("nugget_tin", "nuggetTin");
        nugget_silver = new ItemBaseOreDict("nugget_silver", "nuggetSilver");
        nugget_aluminum = new ItemBaseOreDict("nugget_aluminum", "nuggetAluminum");

        nugget_copper.setCreativeTab(ModCreativeTabs.materialsTab);
        nugget_lead.setCreativeTab(ModCreativeTabs.materialsTab);
        nugget_tin.setCreativeTab(ModCreativeTabs.materialsTab);
        nugget_silver.setCreativeTab(ModCreativeTabs.materialsTab);
        nugget_aluminum.setCreativeTab(ModCreativeTabs.materialsTab);

        /*OreDictionary.registerOre("nuggetCopper", nugget_copper);
        OreDictionary.registerOre("nuggetLead", nugget_lead);
        OreDictionary.registerOre("nuggetTin", nugget_tin);
        OreDictionary.registerOre("nuggetSilver", nugget_silver);
        OreDictionary.registerOre("nuggetAluminum", nugget_aluminum);*/

        ModItems.ITEMS.add(nugget_copper);
        ModItems.ITEMS.add(nugget_lead);
        ModItems.ITEMS.add(nugget_tin);
        ModItems.ITEMS.add(nugget_silver);
        ModItems.ITEMS.add(nugget_aluminum);

        // Ores

        ore_copper = (BlockBase) new BlockBaseOreDict(Material.ROCK, "ore_copper", "oreCopper").setSoundType(SoundType.STONE).setHardness(3.0F).setResistance(5.0F);
        ore_lead = (BlockBase) new BlockBaseOreDict(Material.ROCK, "ore_lead", "oreLead").setSoundType(SoundType.STONE).setHardness(3.0F).setResistance(5.0F);
        ore_tin = (BlockBase) new BlockBaseOreDict(Material.ROCK, "ore_tin", "oreTin").setSoundType(SoundType.STONE).setHardness(3.0F).setResistance(5.0F);
        ore_silver = (BlockBase) new BlockBaseOreDict(Material.ROCK, "ore_silver", "oreSilver").setSoundType(SoundType.STONE).setHardness(3.0F).setResistance(5.0F);
        ore_aluminum = (BlockBase) new BlockBaseOreDict(Material.ROCK, "ore_aluminum", "oreAluminum").setSoundType(SoundType.STONE).setHardness(3.0F).setResistance(5.0F);

        ore_copper.setCreativeTab(ModCreativeTabs.materialsTab);
        ore_lead.setCreativeTab(ModCreativeTabs.materialsTab);
        ore_tin.setCreativeTab(ModCreativeTabs.materialsTab);
        ore_silver.setCreativeTab(ModCreativeTabs.materialsTab);
        ore_aluminum.setCreativeTab(ModCreativeTabs.materialsTab);

        /*OreDictionary.registerOre("oreCopper", ore_copper);
        OreDictionary.registerOre("oreLead", ore_lead);
        OreDictionary.registerOre("oreTin", ore_tin);
        OreDictionary.registerOre("oreSilver", ore_silver);
        OreDictionary.registerOre("oreAluminum", ore_aluminum);*/

        ModBlocks.BLOCKS.add(ore_copper);
        ModBlocks.BLOCKS.add(ore_lead);
        ModBlocks.BLOCKS.add(ore_tin);
        ModBlocks.BLOCKS.add(ore_silver);
        ModBlocks.BLOCKS.add(ore_aluminum);

        // storage blocks

        block_copper = (BlockBase) new BlockBaseOreDict(Material.IRON, "block_copper", "blockCopper").setSoundType(SoundType.STONE).setHardness(3.0F).setResistance(5.0F);
        block_lead = (BlockBase) new BlockBaseOreDict(Material.IRON, "block_lead", "blockLead").setSoundType(SoundType.STONE).setHardness(3.0F).setResistance(5.0F);
        block_tin = (BlockBase) new BlockBaseOreDict(Material.IRON, "block_tin", "blockTin").setSoundType(SoundType.STONE).setHardness(3.0F).setResistance(5.0F);
        block_silver = (BlockBase) new BlockBaseOreDict(Material.IRON, "block_silver", "blockSilver").setSoundType(SoundType.STONE).setHardness(3.0F).setResistance(5.0F);
        block_aluminum = (BlockBase) new BlockBaseOreDict(Material.IRON, "block_aluminum", "blockAluminum").setSoundType(SoundType.STONE).setHardness(3.0F).setResistance(5.0F);

        block_copper.setCreativeTab(ModCreativeTabs.materialsTab);
        block_lead.setCreativeTab(ModCreativeTabs.materialsTab);
        block_tin.setCreativeTab(ModCreativeTabs.materialsTab);
        block_silver.setCreativeTab(ModCreativeTabs.materialsTab);
        block_aluminum.setCreativeTab(ModCreativeTabs.materialsTab);

        /*OreDictionary.registerOre("blockCopper", block_copper);
        OreDictionary.registerOre("blockLead", block_lead);
        OreDictionary.registerOre("blockTin", block_tin);
        OreDictionary.registerOre("blockSilver", block_silver);
        OreDictionary.registerOre("blockAluminum", block_aluminum);*/

        ModBlocks.BLOCKS.add(block_copper);
        ModBlocks.BLOCKS.add(block_lead);
        ModBlocks.BLOCKS.add(block_tin);
        ModBlocks.BLOCKS.add(block_silver);
        ModBlocks.BLOCKS.add(block_aluminum);
        
    }
    
}