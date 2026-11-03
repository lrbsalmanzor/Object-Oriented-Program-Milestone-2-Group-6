package com.motorph.payroll;

public final class PagIBIGCalculator {

    public double computePagIBIG(double grossPay) {

        if (!Double.isFinite(grossPay) || grossPay < 0) {
            throw new IllegalArgumentException(
                    "Gross pay must be non-negative.");
        }

        double rate;

        if (grossPay >= 1000.0 && grossPay <= 1500.0) {
            rate = 0.01;

        } else if (grossPay > 1500.0) {
            rate = 0.02;

        } else {
            return 0.0;
        }

        return round(Math.min(grossPay * rate, 100.0));
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}