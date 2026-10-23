package service;

import model.AttendanceRecord;
import model.Employee;
import model.PayrollRecord;

public class PayrollCalculator {
    private ContributionCalculator contributionCalculator;

    public PayrollCalculator() {
        this.contributionCalculator = new ContributionCalculator();
    }

    public PayrollRecord processPayroll(Employee employee, AttendanceRecord attendance) {
        // 1. Compute Gross Pay
        double grossPay = employee.calculateGrossPay(
            attendance.getHoursWorked(), 
            attendance.getOvertimeHours(), 
            0.0
        );

        // 2. Statutory Deductions
        double sss = contributionCalculator.computeSSS(grossPay);
        double philHealth = contributionCalculator.computePhilHealth(employee.getBasicSalary());
        double pagIbig = contributionCalculator.computePagIBIG(employee.getBasicSalary());

        // 3. Taxable Income
        double totalStatutory = sss + philHealth + pagIbig;
        double taxableIncome = Math.max(0.0, grossPay - totalStatutory);

        // 4. Withholding Tax
        double tax = contributionCalculator.computeTax(taxableIncome);

        // 5. Construct and Return Final Payroll Record
        String fullName = employee.getFirstName() + " " + employee.getLastName();
        return new PayrollRecord(
            employee.getEmployeeId(),
            fullName,
            grossPay,
            sss,
            philHealth,
            pagIbig,
            tax
        );
    }
}