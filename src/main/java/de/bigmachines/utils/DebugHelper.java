package de.bigmachines.utils;

import java.util.*;
import java.util.function.Function;

public class DebugHelper {
    public static <T> void printMapSortedByValue(Map<T, Integer> map, Function<T, String> keyPrinter) {
        final List<Map.Entry<T, Integer>> entries = new LinkedList<>(map.entrySet());
        entries.sort((Comparator<Map.Entry<?, Integer>>) (o1, o2) -> o1.getValue() - o2.getValue());

        for (final Map.Entry<T, Integer> entry : entries)
            System.out.println(keyPrinter.apply(entry.getKey()) + ", " + entry.getValue() + " away");
    }
}
