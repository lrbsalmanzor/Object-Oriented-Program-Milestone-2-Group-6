package com.motorph.payroll;

public final class PhilHealthCalculator {

    public double computePhilHealth(double grossPay) {

        if (!Double.isFinite(grossPay) || grossPay < 0) {
            throw new IllegalArgumentException(
                    "Gross pay must be non-negative.");
        }

        double salaryBase = Math.max(
                10000.0,
                Math.min(grossPay, 60000.0)
        );

        return round((salaryBase * 0.03) / 2.0);
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}