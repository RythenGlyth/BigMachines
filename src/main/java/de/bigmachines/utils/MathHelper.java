package de.bigmachines.utils;

public class MathHelper {
	
	public static int min(int... numbers) {
        int min = numbers[0];
        for(int i = 1; i < numbers.length; i++) {
            if(numbers[i] < min) min = numbers[i];
        }
        return min;
    }
	
	public static int max(int... numbers) {
        int max = numbers[0];
        for(int i = 1; i < numbers.length; i++) {
            if(numbers[i] > max) max = numbers[i];
        }
        return max;
    }
	
}
