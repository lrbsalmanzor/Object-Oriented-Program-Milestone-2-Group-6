package service;

public class PagIBIGCalculator {
    public static double calculateContribution(double basicSalary) {
        if (basicSalary <= 1500.00) {
            return basicSalary * 0.01;
        }
        // Max employee contribution capped at ₱100.00
        return Math.min(100.00, basicSalary * 0.02);
    }
}