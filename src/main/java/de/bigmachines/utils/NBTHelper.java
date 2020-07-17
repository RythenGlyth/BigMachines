package de.bigmachines.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLongArray;
import net.minecraft.util.math.BlockPos;

public final class NBTHelper {
	
	public static int writeBooleansToInt(final boolean... bools) {
		int shift = 0;
		int result = 0;
		for (final boolean bool : bools) {
			result |= (bool ? 1 << shift : 0x0);
			shift++;
		}
		return result;
	}

	public static boolean[] readIntToBooleans(final int data, final int length) {
		int shift = 0;
		final boolean[] result = new boolean[length];
		for(int i = 0; i < length; i++) {
			result[i] = (data & (1 << shift)) > 0;
			shift++;
		}
		return result;
	}

	public static NBTTagCompound writeBlockPosToTag(BlockPos pos) {
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("x", pos.getX());
		tag.setInteger("y", pos.getY());
		tag.setInteger("z", pos.getZ());
		return tag;
	}

	public static BlockPos readTagToBlockPos(NBTTagCompound compound) {
		return new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
	}
	
	public static String tabIndent = "  ";
	
	public static List<String> stringifyTag(NBTBase base, String outerKey, String curTabIndent) {
		LinkedList<String> lines = new LinkedList<String>();
		String outerKeyString = (outerKey != null && outerKey != "" ? (outerKey + ": ") : "");
		if(base.getId() == 0) { //TAG_End
			
		} else if(base.getId() == 10) { //TAG_Compound
			NBTTagCompound tag = (NBTTagCompound)base;
			
			Set<String> keySet = tag.getKeySet();
			int i = 0;
			if(keySet.size() > 0) {
				lines.add(curTabIndent + outerKeyString + "{");
				for(String key : keySet) {
					i++;
					List<String> sT = stringifyTag(tag.getTag(key), key, curTabIndent + tabIndent);
					if(sT.size() > 0 && i < keySet.size()) sT.set(sT.size() - 1, sT.get(sT.size() - 1) + ",");
					lines.addAll(sT);
				}
				lines.add(curTabIndent + "}");
			} else {
				lines.add(curTabIndent + outerKeyString + "{}");
			}
			
		} else if(base.getId() == 9) { //TAG_List
			NBTTagList list = (NBTTagList)base;
			if(list.hasNoTags()) {
				lines.add(curTabIndent + outerKeyString + "[]");
			} else {
				lines.add(curTabIndent + outerKeyString + "[");
				int tagCount = list.tagCount();
				for(int i = 0; i < tagCount; i++) {
					List<String> sT = stringifyTag(list.get(i), "", curTabIndent + tabIndent);
					if(sT.size() > 0 && i < tagCount - 1) sT.set(sT.size() - 1, sT.get(sT.size() - 1) + ",");
					lines.addAll(sT);
				}
				lines.add(curTabIndent + "]");
			}
		} else if(base.getId() == 11) { //TAG_Int_Array
			NBTTagIntArray intArr = (NBTTagIntArray)base;
		} else if(base.getId() == 12) { //TAG_Long_Array
			NBTTagLongArray longArr = (NBTTagLongArray)base;
		} else if(base.getId() == 7) { //TAG_Byte_Array
			NBTTagByteArray byteArr = (NBTTagByteArray)base;
		} else { //TAG_Byte, TAG_Short, TAG_Int, TAG_Long, TAG_Float, TAG_Double, TAG_String
			lines.add(curTabIndent + outerKeyString + base.toString());
		}
		return lines;
	}
	
}
