package model;

public class RegularEmployee extends Employee {
    private double riceSubsidy;
    private double clothingAllowance;
    private double phoneAllowance;

    public RegularEmployee(String employeeId, String lastName, String firstName, String status, String position, 
                           double basicSalary, double hourlyRate, double riceSubsidy, double clothingAllowance, double phoneAllowance) {
        super(employeeId, lastName, firstName, status, position, basicSalary, hourlyRate);
        this.riceSubsidy = riceSubsidy;
        this.clothingAllowance = clothingAllowance;
        this.phoneAllowance = phoneAllowance;
    }

    public double getTotalAllowances() {
        return riceSubsidy + clothingAllowance + phoneAllowance;
    }

    @Override
    public double calculateGrossPay(double hoursWorked, double overtimeHours, double manualAllowances) {
        // Regular employees include standard monthly allowances
        return super.calculateGrossPay(hoursWorked, overtimeHours, getTotalAllowances() + manualAllowances);
    }
}