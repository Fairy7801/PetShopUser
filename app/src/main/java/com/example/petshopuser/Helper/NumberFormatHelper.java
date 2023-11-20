package com.example.petshopuser.Helper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatHelper {
    private static final DecimalFormat decimalFormat;

    static {
        decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
    }

    public static String formatNumber(double number) {
        return decimalFormat.format(number);
    }
}
