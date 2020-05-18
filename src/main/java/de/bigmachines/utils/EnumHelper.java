package de.bigmachines.utils;

import java.util.Arrays;

public class EnumHelper {
	
	public static String[] getNames(Class<? extends Enum<?>> e) {
		return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
	}
	
}
