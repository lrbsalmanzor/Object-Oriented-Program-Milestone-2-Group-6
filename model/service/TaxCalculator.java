package service;

public class TaxCalculator {
    public static double calculateTax(double taxableIncome) {
        if (taxableIncome <= 20833.00) {
            return 0.00;
        } else if (taxableIncome <= 33332.00) {
            return (taxableIncome - 20833.00) * 0.15;
        } else if (taxableIncome <= 66666.00) {
            return 1875.00 + (taxableIncome - 33332.00) * 0.20;
        } else if (taxableIncome <= 166666.00) {
            return 8541.80 + (taxableIncome - 66666.00) * 0.25;
        } else if (taxableIncome <= 666666.00) {
            return 33541.80 + (taxableIncome - 166666.00) * 0.30;
        } else {
            return 183541.80 + (taxableIncome - 666666.00) * 0.35;
        }
    }
}