package com.example.fooddeliveryapplication.Model;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    private static CurrencyFormatter formatter = null;
    private NumberFormat VNFormatCurrency;

    private CurrencyFormatter() {
        VNFormatCurrency = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    }
    public static CurrencyFormatter getFormatter() {
        if (formatter == null) {
            formatter = new CurrencyFormatter();
        }
        return formatter;
    }

    public String format(Double plainText) {
        return VNFormatCurrency.format(plainText);
    }
}
