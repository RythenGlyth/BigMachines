package de.bigmachines.init;

import de.bigmachines.blocks.BlockBase;
import de.bigmachines.blocks.BlockBaseOreDict;
import de.bigmachines.items.ItemBase;
import de.bigmachines.items.ItemBaseOreDict;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public final class ModMaterials {

    public static ItemBase ingot_copper;
    public static ItemBase ingot_lead;
    public static ItemBase ingot_tin;
    public static ItemBase ingot_silver;
    public static ItemBase ingot_aluminium;

    public static ItemBase nugget_copper;
    public static ItemBase nugget_lead;
    public static ItemBase nugget_tin;
    public static ItemBase nugget_silver;
    public static ItemBase nugget_aluminium;

    public static BlockBase ore_copper;
    public static BlockBase ore_lead;
    public static BlockBase ore_tin;
    public static BlockBase ore_silver;
    public static BlockBase ore_aluminium;

    public static BlockBase block_copper;
    public static BlockBase block_lead;
    public static BlockBase block_tin;
    public static BlockBase block_silver;
    public static BlockBase block_aluminium;
    
	public static BlockBase pink_dirt;
	public static BlockBase limestone;

    public static void preInit() {
        initMetals();
    }

    private static void initMetals() {
    	// ingots

        ingot_copper = new ItemBaseOreDict("ingot_copper", "ingotCopper");
        ingot_lead = new ItemBaseOreDict("ingot_lead", "ingotLead");
        ingot_tin = new ItemBaseOreDict("ingot_tin", "ingotTin");
        ingot_silver = new ItemBaseOreDict("ingot_silver", "ingotSilver");
        ingot_aluminium = new ItemBaseOreDict("ingot_aluminium", "ingotAluminium");

        ingot_copper.setCreativeTab(ModCreativeTabs.materialsTab);
        ingot_lead.setCreativeTab(ModCreativeTabs.materialsTab);
        ingot_tin.setCreativeTab(ModCreativeTabs.materialsTab);
        ingot_silver.setCreativeTab(ModCreativeTabs.materialsTab);
        ingot_aluminium.setCreativeTab(ModCreativeTabs.materialsTab);
        
        /*OreDictionary.registerOre("ingotCopper", ingot_copper);
        OreDictionary.registerOre("ingotLead", ingot_lead);
        OreDictionary.registerOre("ingotTin", ingot_tin);
        OreDictionary.registerOre("ingotSilver", ingot_silver);
        OreDictionary.registerOre("ingotAluminium", ingot_aluminium);*/

        ModItems.ITEMS.add(ingot_copper);
        ModItems.ITEMS.add(ingot_lead);
        ModItems.ITEMS.add(ingot_tin);
        ModItems.ITEMS.add(ingot_silver);
        ModItems.ITEMS.add(ingot_aluminium);

        //nuggets

        nugget_copper = new ItemBaseOreDict("nugget_copper", "nuggetCopper");
        nugget_lead = new ItemBaseOreDict("nugget_lead", "nuggetLead");
        nugget_tin = new ItemBaseOreDict("nugget_tin", "nuggetTin");
        nugget_silver = new ItemBaseOreDict("nugget_silver", "nuggetSilver");
        nugget_aluminium = new ItemBaseOreDict("nugget_aluminium", "nuggetAluminium");

        nugget_copper.setCreativeTab(ModCreativeTabs.materialsTab);
        nugget_lead.setCreativeTab(ModCreativeTabs.materialsTab);
        nugget_tin.setCreativeTab(ModCreativeTabs.materialsTab);
        nugget_silver.setCreativeTab(ModCreativeTabs.materialsTab);
        nugget_aluminium.setCreativeTab(ModCreativeTabs.materialsTab);

        /*OreDictionary.registerOre("nuggetCopper", nugget_copper);
        OreDictionary.registerOre("nuggetLead", nugget_lead);
        OreDictionary.registerOre("nuggetTin", nugget_tin);
        OreDictionary.registerOre("nuggetSilver", nugget_silver);
        OreDictionary.registerOre("nuggetAluminium", nugget_aluminium);*/

        ModItems.ITEMS.add(nugget_copper);
        ModItems.ITEMS.add(nugget_lead);
        ModItems.ITEMS.add(nugget_tin);
        ModItems.ITEMS.add(nugget_silver);
        ModItems.ITEMS.add(nugget_aluminium);

        // Ores

        ore_copper = (BlockBase) new BlockBaseOreDict(Material.ROCK, "ore_copper", "oreCopper").setHardness(3.0F).setResistance(5.0F);
        ore_lead = (BlockBase) new BlockBaseOreDict(Material.ROCK, "ore_lead", "oreLead").setHardness(3.0F).setResistance(5.0F);
        ore_tin = (BlockBase) new BlockBaseOreDict(Material.ROCK, "ore_tin", "oreTin").setHardness(3.0F).setResistance(5.0F);
        ore_silver = (BlockBase) new BlockBaseOreDict(Material.ROCK, "ore_silver", "oreSilver").setHardness(3.0F).setResistance(5.0F);
        ore_aluminium = (BlockBase) new BlockBaseOreDict(Material.ROCK, "ore_aluminium", "oreAluminium").setHardness(3.0F).setResistance(5.0F);

        ore_copper.setCreativeTab(ModCreativeTabs.materialsTab);
        ore_lead.setCreativeTab(ModCreativeTabs.materialsTab);
        ore_tin.setCreativeTab(ModCreativeTabs.materialsTab);
        ore_silver.setCreativeTab(ModCreativeTabs.materialsTab);
        ore_aluminium.setCreativeTab(ModCreativeTabs.materialsTab);

        /*OreDictionary.registerOre("oreCopper", ore_copper);
        OreDictionary.registerOre("oreLead", ore_lead);
        OreDictionary.registerOre("oreTin", ore_tin);
        OreDictionary.registerOre("oreSilver", ore_silver);
        OreDictionary.registerOre("oreAluminium", ore_aluminium);*/

        ModBlocks.BLOCKS.add(ore_copper);
        ModBlocks.BLOCKS.add(ore_lead);
        ModBlocks.BLOCKS.add(ore_tin);
        ModBlocks.BLOCKS.add(ore_silver);
        ModBlocks.BLOCKS.add(ore_aluminium);

        // storage blocks

        block_copper = (BlockBase) new BlockBaseOreDict(Material.IRON, "block_copper", "blockCopper").setHardness(3.0F).setResistance(5.0F);
        block_lead = (BlockBase) new BlockBaseOreDict(Material.IRON, "block_lead", "blockLead").setHardness(3.0F).setResistance(5.0F);
        block_tin = (BlockBase) new BlockBaseOreDict(Material.IRON, "block_tin", "blockTin").setHardness(3.0F).setResistance(5.0F);
        block_silver = (BlockBase) new BlockBaseOreDict(Material.IRON, "block_silver", "blockSilver").setHardness(3.0F).setResistance(5.0F);
        block_aluminium = (BlockBase) new BlockBaseOreDict(Material.IRON, "block_aluminium", "blockAluminium").setHardness(3.0F).setResistance(5.0F);

        block_copper.setCreativeTab(ModCreativeTabs.materialsTab);
        block_lead.setCreativeTab(ModCreativeTabs.materialsTab);
        block_tin.setCreativeTab(ModCreativeTabs.materialsTab);
        block_silver.setCreativeTab(ModCreativeTabs.materialsTab);
        block_aluminium.setCreativeTab(ModCreativeTabs.materialsTab);

        /*OreDictionary.registerOre("blockCopper", block_copper);
        OreDictionary.registerOre("blockLead", block_lead);
        OreDictionary.registerOre("blockTin", block_tin);
        OreDictionary.registerOre("blockSilver", block_silver);
        OreDictionary.registerOre("blockAluminium", block_aluminium);*/

        ModBlocks.BLOCKS.add(block_copper);
        ModBlocks.BLOCKS.add(block_lead);
        ModBlocks.BLOCKS.add(block_tin);
        ModBlocks.BLOCKS.add(block_silver);
        ModBlocks.BLOCKS.add(block_aluminium);
        
        
    	pink_dirt = (BlockBase) new BlockBaseOreDict(Material.GROUND, "pink_dirt", "dirt").setSoundType(SoundType.GROUND).setHardness(0.5F).setCreativeTab(ModCreativeTabs.materialsTab);
    	limestone = (BlockBase) new BlockBaseOreDict(Material.ROCK, "limestone", "limestone").setSoundType(SoundType.STONE).setHardness(0.5F).setCreativeTab(ModCreativeTabs.materialsTab);
    	
    	ModBlocks.BLOCKS.add(pink_dirt);
    	ModBlocks.BLOCKS.add(limestone);
        
    }
    
}