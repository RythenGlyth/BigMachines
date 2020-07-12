package de.bigmachines.utils;

import net.minecraft.nbt.NBTTagCompound;
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
	
}
