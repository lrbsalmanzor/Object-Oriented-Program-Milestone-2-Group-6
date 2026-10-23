package service;

import model.Employee;

public class ContributionCalculator {

    public double computeSSS(double grossPay) {
        return SSSCalculator.calculateContribution(grossPay);
    }

    public double computePhilHealth(double basicSalary) {
        return PhilHealthCalculator.calculateContribution(basicSalary);
    }

    public double computePagIBIG(double basicSalary) {
        return PagIBIGCalculator.calculateContribution(basicSalary);
    }

    public double computeTax(double taxableIncome) {
        return TaxCalculator.calculateTax(taxableIncome);
    }
}