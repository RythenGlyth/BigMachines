package de.bigmachines.utils;

import net.minecraft.util.EnumFacing;

public class NBTHelper {
	
	public static int writeBooleansToInt(boolean... bools) {
		int shift = 0;
		int result = 0;
		for(int i = 0; i < bools.length; i++) {
			result = result | (bools[i] ? 1 << shift : 0x0);
			shift++;
		}
		return result;
	}

	public static boolean[] readIntToBooleans(int data, int length) {
		int shift = 0;
		boolean[] result = new boolean[length];
		for(int i = 0; i < length; i++) {
			result[i] = (data & (1 << shift)) > 0;
			shift++;
		}
		return result;
	}
	
}
