package com.akira.skillmaster.utils;

import org.apache.commons.lang3.Validate;

import java.text.DecimalFormat;

public class NumberUtils {
    private static final DecimalFormat formatter = new DecimalFormat("0.##");

    public static String toSignedNumber(int integer) {
        if (integer < 0) return String.valueOf(integer);
        else return "+" + integer;
    }

    public static String toSignedNumber(double decimal) {
        if (decimal < 0) return String.valueOf(decimal);
        else return "+" + decimal;
    }

    public static Double parseDouble(String source) {
        try {
            return Double.parseDouble(source);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer parseInteger(String source) {
        try {
            return Integer.parseInt(source);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String toPercent(double value, double max) {
        Validate.isTrue(max >= value, "The value cannot be greater than the max level.");
        return formatter.format(value / max) + "%";
    }

    public static DecimalFormat getFormatter() {
        return formatter;
    }
}
