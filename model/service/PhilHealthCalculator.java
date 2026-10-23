package service;

public class PhilHealthCalculator {
    public static double calculateContribution(double basicSalary) {
        if (basicSalary <= 10000.00) return 250.00; // Floor threshold (5% split equally)
        if (basicSalary >= 100000.00) return 2500.00; // Ceiling cap
        return (basicSalary * 0.05) / 2.0;
    }
}