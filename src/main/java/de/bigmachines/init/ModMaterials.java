package de.bigmachines.init;

import de.bigmachines.blocks.BlockBase;
import de.bigmachines.blocks.BlockBaseOreDict;
import de.bigmachines.items.ItemBase;
import de.bigmachines.items.ItemBaseOreDict;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ModMaterials {

	public static ItemBase[] ingots;
	public static ItemBase[] nuggets;
	public static BlockBase[] ores;
	public static BlockBase[] blocks;

	public static void preInit() {
		// ingots
		ingots = new ItemBase[] {
				new ItemBaseOreDict("ingot_aluminium", "ingotAluminium"),
				new ItemBaseOreDict("ingot_copper", "ingotCopper"),
				new ItemBaseOreDict("ingot_lead", "ingotLead"),
				new ItemBaseOreDict("ingot_tin", "ingotTin"),
				new ItemBaseOreDict("ingot_silver", "ingotSilver")
		};

		for (ItemBase ingot : ingots) {
			ingot.setCreativeTab(ModCreativeTabs.materialsTab);
			ModItems.ITEMS.add(ingot);
		}

		/*
		
		needs uncommenting the correct function in ItemBaseOreDict

        for (ItemBase ingot : ingots)
        	OreDictionary.registerOre(((ItemBaseOreDict) ingot).getOreDict(), ingot);

        would replace this:

        OreDictionary.registerOre("ingotCopper", ingot_copper);
        OreDictionary.registerOre("ingotLead", ingot_lead);
        OreDictionary.registerOre("ingotTin", ingot_tin);
        OreDictionary.registerOre("ingotSilver", ingot_silver);
        OreDictionary.registerOre("ingotAluminium", ingot_aluminium);*/

		//nuggets

		nuggets = new ItemBase[] {
				new ItemBaseOreDict("nugget_aluminium", "nuggetAluminium"),
				new ItemBaseOreDict("nugget_copper", "nuggetCopper"),
				new ItemBaseOreDict("nugget_lead", "nuggetLead"),
				new ItemBaseOreDict("nugget_tin", "nuggetTin"),
				new ItemBaseOreDict("nugget_silver", "nuggetSilver")
		};

		for (ItemBase nugget : nuggets) {
			nugget.setCreativeTab(ModCreativeTabs.materialsTab);
			ModItems.ITEMS.add(nugget);
		}

		/*OreDictionary.registerOre("nuggetCopper", nugget_copper);
        OreDictionary.registerOre("nuggetLead", nugget_lead);
        OreDictionary.registerOre("nuggetTin", nugget_tin);
        OreDictionary.registerOre("nuggetSilver", nugget_silver);
        OreDictionary.registerOre("nuggetAluminium", nugget_aluminium);*/

		// Ores

		ores = new BlockBase[] {
				new BlockBaseOreDict(Material.ROCK, "ore_aluminium", "oreAluminium"),
				new BlockBaseOreDict(Material.ROCK, "ore_copper", "oreCopper"),
				new BlockBaseOreDict(Material.ROCK, "ore_lead", "ore_lead"),
				new BlockBaseOreDict(Material.ROCK, "ore_tin", "oreTin"),
				new BlockBaseOreDict(Material.ROCK, "ore_silver", "oreSilver")
		};

		for (BlockBase ore : ores) {
			ore.setCreativeTab(ModCreativeTabs.materialsTab);
			ModBlocks.BLOCKS.add(ore);
		}

		/*OreDictionary.registerOre("oreCopper", ore_copper);
        OreDictionary.registerOre("oreLead", ore_lead);
        OreDictionary.registerOre("oreTin", ore_tin);
        OreDictionary.registerOre("oreSilver", ore_silver);
        OreDictionary.registerOre("oreAluminium", ore_aluminium);*/

		// storage blocks

		blocks = new BlockBase[] {
				new BlockBaseOreDict(Material.IRON, "block_aluminium", "blockAluminium"),
				new BlockBaseOreDict(Material.IRON, "block_copper", "blockCopper"),
				new BlockBaseOreDict(Material.IRON, "block_lead", "blockLead"),
				new BlockBaseOreDict(Material.IRON, "block_tin", "blockTin"),
				new BlockBaseOreDict(Material.IRON, "block_silver", "blockSilver")
		};

		for (BlockBase block : blocks) {
			block.setCreativeTab(ModCreativeTabs.materialsTab);
			ModBlocks.BLOCKS.add(block);
		}

		/*OreDictionary.registerOre("blockCopper", block_copper);
        OreDictionary.registerOre("blockLead", block_lead);
        OreDictionary.registerOre("blockTin", block_tin);
        OreDictionary.registerOre("blockSilver", block_silver);
        OreDictionary.registerOre("blockAluminium", block_aluminium);*/
	}

}