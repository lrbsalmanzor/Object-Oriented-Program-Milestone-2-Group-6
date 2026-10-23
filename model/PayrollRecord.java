package model;

public class PayrollRecord {
    private String employeeId;
    private String employeeName;
    private double grossPay;
    private double sssDeduction;
    private double philHealthDeduction;
    private double pagIbigDeduction;
    private double taxDeduction;
    private double totalDeductions;
    private double netPay;

    public PayrollRecord(String employeeId, String employeeName, double grossPay, double sssDeduction, 
                         double philHealthDeduction, double pagIbigDeduction, double taxDeduction) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.grossPay = grossPay;
        this.sssDeduction = sssDeduction;
        this.philHealthDeduction = philHealthDeduction;
        this.pagIbigDeduction = pagIbigDeduction;
        this.taxDeduction = taxDeduction;
        this.totalDeductions = sssDeduction + philHealthDeduction + pagIbigDeduction + taxDeduction;
        this.netPay = grossPay - this.totalDeductions;
    }

    public String getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public double getGrossPay() { return grossPay; }
    public double getSssDeduction() { return sssDeduction; }
    public double getPhilHealthDeduction() { return philHealthDeduction; }
    public double getPagIbigDeduction() { return pagIbigDeduction; }
    public double getTaxDeduction() { return taxDeduction; }
    public double getTotalDeductions() { return totalDeductions; }
    public double getNetPay() { return netPay; }
}