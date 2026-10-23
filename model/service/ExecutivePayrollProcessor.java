package service;

import model.AttendanceRecord;
import model.Employee;
import model.PayrollRecord;

public class ExecutivePayrollProcessor extends PayrollCalculator {
    private double executiveBonus;

    public ExecutivePayrollProcessor(double executiveBonus) {
        super();
        this.executiveBonus = executiveBonus;
    }

    @Override
    public PayrollRecord processPayroll(Employee employee, AttendanceRecord attendance) {
        PayrollRecord baseRecord = super.processPayroll(employee, attendance);
        double adjustedGross = baseRecord.getGrossPay() + executiveBonus;
        return new PayrollRecord(
            employee.getEmployeeId(),
            employee.getFirstName() + " " + employee.getLastName(),
            adjustedGross,
            baseRecord.getSssDeduction(),
            baseRecord.getPhilHealthDeduction(),
            baseRecord.getPagIbigDeduction(),
            baseRecord.getTaxDeduction()
        );
    }
}