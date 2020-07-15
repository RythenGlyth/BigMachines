package de.bigmachines.utils;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DebugHelper {
    public static <T> void printMapSortedByValue(@Nonnull Map<T, Integer> map, Function<T, String> keyPrinter) {
        final List<Map.Entry<T, Integer>> entries = new LinkedList<>(map.entrySet());
        entries.sort(Comparator.comparingInt(Map.Entry::getValue));

        for (final Map.Entry<T, Integer> entry : entries)
            System.out.println(keyPrinter.apply(entry.getKey()) + ": " + entry.getValue());
    }
    
    public static <K, V> void printMapSortedByValueProperty(@Nonnull Map<K, V> map, Function<K, String> keyPrinter,
                                                            Function<V, Integer> propertyRetriever) {
        final List<Map.Entry<K, V>> entries = new LinkedList<>(map.entrySet());
        entries.sort(Comparator.comparingInt(o -> propertyRetriever.apply(o.getValue())));
    
        for (final Map.Entry<K, V> entry : entries)
            System.out.println(keyPrinter.apply(entry.getKey()) + ": " + entry.getValue());
    }
}
