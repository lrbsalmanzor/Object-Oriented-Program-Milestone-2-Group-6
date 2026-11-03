package com.motorph.payroll;

public final class SSSCalculator {

    public double computeSSS(double grossPay) {

        requireNonNegative(grossPay);

        final int minimumSalary = 3250;
        final int maximumSalary = 24750;

        final double minimumContribution = 135.0;
        final double maximumContribution = 1125.0;

        final int salaryStep = 500;
        final double contributionStep = 22.50;

        if (grossPay < minimumSalary) {
            return minimumContribution;
        }

        if (grossPay >= maximumSalary) {
            return maximumContribution;
        }

        int upperBracket = 3750;
        double contribution = 157.50;

        while (grossPay > upperBracket) {
            upperBracket += salaryStep;
            contribution += contributionStep;
        }

        return round(contribution);
    }

    private static void requireNonNegative(double amount) {

        if (!Double.isFinite(amount) || amount < 0) {
            throw new IllegalArgumentException(
                    "Gross pay must be non-negative.");
        }
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}