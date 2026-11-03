package com.motorph.payroll;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PayrollRecord {

    private final String employeeID;
    private final String payPeriod;

    private final double grossPay;
    private final double sss;
    private final double philHealth;
    private final double pagIBIG;
    private final double withholdingTax;
    private final double netPay;

    public PayrollRecord(
            String employeeID,
            String payPeriod,
            double grossPay,
            double sss,
            double philHealth,
            double pagIBIG,
            double withholdingTax,
            double netPay) {

        this.employeeID =
                Employee.requireText(employeeID, "Employee ID");

        this.payPeriod =
                Employee.requireText(payPeriod, "Pay period");

        this.grossPay = money(grossPay, "Gross pay");
        this.sss = money(sss, "SSS");
        this.philHealth = money(philHealth, "PhilHealth");
        this.pagIBIG = money(pagIBIG, "Pag-IBIG");
        this.withholdingTax = money(withholdingTax, "Tax");
        this.netPay = money(netPay, "Net pay");

        double expectedNet =
                this.grossPay - getTotalDeductions();

        if (Math.abs(expectedNet - this.netPay) > 0.01) {
            throw new IllegalArgumentException(
                    "Net pay does not equal gross pay minus deductions.");
        }
    }

    private static double money(double value, String name) {

        if (!Double.isFinite(value) || value < 0) {
            throw new IllegalArgumentException(
                    name + " must be non-negative.");
        }

        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String getPayPeriod() {
        return payPeriod;
    }

    public double getGrossPay() {
        return grossPay;
    }

    public double getSSS() {
        return sss;
    }

    public double getPhilHealth() {
        return philHealth;
    }

    public double getPagIBIG() {
        return pagIBIG;
    }

    public double getTax() {
        return withholdingTax;
    }

    public double getWithholdingTax() {
        return withholdingTax;
    }

    public double getTotalDeductions() {
        return sss + philHealth + pagIBIG + withholdingTax;
    }

    public double getNetPay() {
        return netPay;
    }
}