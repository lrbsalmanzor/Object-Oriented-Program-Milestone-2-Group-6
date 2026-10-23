package model;

public class ContractualEmployee extends Employee {

    public ContractualEmployee(String employeeId, String lastName, String firstName, String status, String position, double basicSalary, double hourlyRate) {
        super(employeeId, lastName, firstName, status, position, basicSalary, hourlyRate);
    }

    @Override
    public double calculateGrossPay(double hoursWorked, double overtimeHours, double manualAllowances) {
        // Contractual employees do not receive regular monthly allowances
        return (hoursWorked * getHourlyRate()) + manualAllowances;
    }
}