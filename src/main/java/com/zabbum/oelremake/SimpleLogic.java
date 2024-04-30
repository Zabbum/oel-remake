package com.zabbum.oelremake;

public class SimpleLogic {

    // Check if provided number is valid
    public static boolean isValid(String value, int minValue, int[] maxValues) {
        int valueInt;

        // Check if it is a number
        try {
            valueInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }

        // Check isn't smaller than minimal value
        if (valueInt < minValue) {
            return false;
        }

        // Check if isn't higher than any of the max numbers
        for (int maxValue : maxValues) {
            if (valueInt > maxValue) {
                return false;
            }
        }

        return true;
    }
}
