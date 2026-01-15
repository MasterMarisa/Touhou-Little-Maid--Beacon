package com.mastermarisa.maidbeacon.utils;

public class RomanConverter {
    private static final String[] THOUSANDS = {"", "M", "MM", "MMM"};
    private static final String[] HUNDREDS = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
    private static final String[] TENS = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
    private static final String[] UNITS = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

    public static String intToRoman(int num) {
        if (num < 1 || num > 3999) {
            return "Invalid";
        }

        return THOUSANDS[num / 1000] +
                HUNDREDS[(num % 1000) / 100] +
                TENS[(num % 100) / 10] +
                UNITS[num % 10];
    }
}