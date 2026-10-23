package service;

public class SSSCalculator {
    public static double calculateContribution(double grossPay) {
        if (grossPay <= 4250.00) return 180.00;
        if (grossPay >= 29750.00) return 1350.00;
        
        // Approximate standard SSS employee share computation
        return Math.min(1350.00, Math.max(180.00, grossPay * 0.045));
    }
}