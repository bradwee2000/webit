package com.bwee.webit.util;

public class ImportUtils {

    public static String padZeros(final int num, int digits) {
        return String.format("%0" + digits + "d", num);
    }

    public static String padZeros(final String num, int digits) {
        return padZeros(Integer.parseInt(num), digits);
    }
}
