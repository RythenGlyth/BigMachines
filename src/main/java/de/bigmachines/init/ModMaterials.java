package de.bigmachines.init;

import de.bigmachines.blocks.BlockBase;
import de.bigmachines.items.ItemBase;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ModMaterials {

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

    public static void preInit() {
        // ingots

        ingot_copper = new ItemBase("ingot_copper");
        ingot_lead = new ItemBase("ingot_lead");
        ingot_tin = new ItemBase("ingot_tin");
        ingot_silver = new ItemBase("ingot_silver");
        ingot_aluminium = new ItemBase("ingot_aluminium");

        ingot_copper.setCreativeTab(ModCreativeTabs.materialsTab);
        ingot_lead.setCreativeTab(ModCreativeTabs.materialsTab);
        ingot_tin.setCreativeTab(ModCreativeTabs.materialsTab);
        ingot_silver.setCreativeTab(ModCreativeTabs.materialsTab);
        ingot_aluminium.setCreativeTab(ModCreativeTabs.materialsTab);
        
        OreDictionary.registerOre("ingotCopper", ingot_copper);
        OreDictionary.registerOre("ingotLead", ingot_lead);
        OreDictionary.registerOre("ingotTin", ingot_tin);
        OreDictionary.registerOre("ingotSilver", ingot_silver);
        OreDictionary.registerOre("ingotAluminium", ingot_aluminium);

        ModItems.ITEMS.add(ingot_copper);
        ModItems.ITEMS.add(ingot_lead);
        ModItems.ITEMS.add(ingot_tin);
        ModItems.ITEMS.add(ingot_silver);
        ModItems.ITEMS.add(ingot_aluminium);

        //nuggets

        nugget_copper = new ItemBase("nugget_copper");
        nugget_lead = new ItemBase("nugget_lead");
        nugget_tin = new ItemBase("nugget_tin");
        nugget_silver = new ItemBase("nugget_silver");
        nugget_aluminium = new ItemBase("nugget_aluminium");

        nugget_copper.setCreativeTab(ModCreativeTabs.materialsTab);
        nugget_lead.setCreativeTab(ModCreativeTabs.materialsTab);
        nugget_tin.setCreativeTab(ModCreativeTabs.materialsTab);
        nugget_silver.setCreativeTab(ModCreativeTabs.materialsTab);
        nugget_aluminium.setCreativeTab(ModCreativeTabs.materialsTab);

        OreDictionary.registerOre("nuggetCopper", nugget_copper);
        OreDictionary.registerOre("nuggetLead", nugget_lead);
        OreDictionary.registerOre("nuggetTin", nugget_tin);
        OreDictionary.registerOre("nuggetSilver", nugget_silver);
        OreDictionary.registerOre("nuggetAluminium", nugget_aluminium);

        ModItems.ITEMS.add(nugget_copper);
        ModItems.ITEMS.add(nugget_lead);
        ModItems.ITEMS.add(nugget_tin);
        ModItems.ITEMS.add(nugget_silver);
        ModItems.ITEMS.add(nugget_aluminium);

        // Ores

        ore_copper = new BlockBase(Material.ROCK, "ore_copper");
        ore_lead = new BlockBase(Material.ROCK, "ore_lead");
        ore_tin = new BlockBase(Material.ROCK, "ore_tin");
        ore_silver = new BlockBase(Material.ROCK, "ore_silver");
        ore_aluminium = new BlockBase(Material.ROCK, "ore_aluminium");

        ore_copper.setCreativeTab(ModCreativeTabs.materialsTab);
        ore_lead.setCreativeTab(ModCreativeTabs.materialsTab);
        ore_tin.setCreativeTab(ModCreativeTabs.materialsTab);
        ore_silver.setCreativeTab(ModCreativeTabs.materialsTab);
        ore_aluminium.setCreativeTab(ModCreativeTabs.materialsTab);

        OreDictionary.registerOre("oreCopper", ore_copper);
        OreDictionary.registerOre("oreLead", ore_lead);
        OreDictionary.registerOre("oreTin", ore_tin);
        OreDictionary.registerOre("oreSilver", ore_silver);
        OreDictionary.registerOre("oreAluminium", ore_aluminium);

        ModBlocks.BLOCKS.add(ore_copper);
        ModBlocks.BLOCKS.add(ore_lead);
        ModBlocks.BLOCKS.add(ore_tin);
        ModBlocks.BLOCKS.add(ore_silver);
        ModBlocks.BLOCKS.add(ore_aluminium);

        // storage blocks

        block_copper = new BlockBase(Material.IRON, "block_copper");
        block_lead = new BlockBase(Material.IRON, "block_lead");
        block_tin = new BlockBase(Material.IRON, "block_tin");
        block_silver = new BlockBase(Material.IRON, "block_silver");
        block_aluminium = new BlockBase(Material.IRON, "block_aluminium");

        block_copper.setCreativeTab(ModCreativeTabs.materialsTab);
        block_lead.setCreativeTab(ModCreativeTabs.materialsTab);
        block_tin.setCreativeTab(ModCreativeTabs.materialsTab);
        block_silver.setCreativeTab(ModCreativeTabs.materialsTab);
        block_aluminium.setCreativeTab(ModCreativeTabs.materialsTab);

        OreDictionary.registerOre("blockCopper", block_copper);
        OreDictionary.registerOre("blockLead", block_lead);
        OreDictionary.registerOre("blockTin", block_tin);
        OreDictionary.registerOre("blockSilver", block_silver);
        OreDictionary.registerOre("blockAluminium", block_aluminium);

        ModBlocks.BLOCKS.add(block_copper);
        ModBlocks.BLOCKS.add(block_lead);
        ModBlocks.BLOCKS.add(block_tin);
        ModBlocks.BLOCKS.add(block_silver);
        ModBlocks.BLOCKS.add(block_aluminium);
    }


    
}