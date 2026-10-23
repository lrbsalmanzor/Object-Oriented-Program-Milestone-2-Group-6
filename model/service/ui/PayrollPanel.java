package ui;

import javax.swing.*;
import java.awt.*;
import model.AttendanceRecord;
import model.Employee;
import model.PayrollRecord;
import repository.AttendanceRepository;
import repository.EmployeeRepository;
import service.PayrollCalculator;

public class PayrollPanel extends JPanel {
    private JTextField empIdField;
    private JTextArea resultArea;
    private JButton computeButton;
    private PayrollCalculator payrollCalculator;
    private EmployeeRepository employeeRepository;
    private AttendanceRepository attendanceRepository;

    public PayrollPanel() {
        setLayout(new BorderLayout());
        payrollCalculator = new PayrollCalculator();
        employeeRepository = new EmployeeRepository("employees.csv");
        attendanceRepository = new AttendanceRepository("attendance.csv");

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Employee ID:"));
        empIdField = new JTextField(10);
        computeButton = new JButton("Compute Payroll");
        topPanel.add(empIdField);
        topPanel.add(computeButton);

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        computeButton.addActionListener(e -> processPayrollAction());
    }

    private void processPayrollAction() {
        String targetId = empIdField.getText().trim();
        Employee foundEmployee = null;
        for (Employee emp : employeeRepository.getAllEmployees()) {
            if (emp.getEmployeeId().equals(targetId)) {
                foundEmployee = emp;
                break;
            }
        }

        if (foundEmployee == null) {
            resultArea.setText("Employee ID not found.");
            return;
        }

        java.util.List<AttendanceRecord> logs = attendanceRepository.getAttendanceByEmployeeId(targetId);
        if (logs.isEmpty()) {
            resultArea.setText("No attendance records found for this employee.");
            return;
        }

        AttendanceRecord attendance = logs.get(0);
        PayrollRecord record = payrollCalculator.processPayroll(foundEmployee, attendance);

        resultArea.setText("--- Payslip Summary ---\n" +
            "Employee Name: " + record.getEmployeeName() + "\n" +
            "Gross Pay: P" + record.getGrossPay() + "\n" +
            "SSS: P" + record.getSssDeduction() + "\n" +
            "PhilHealth: P" + record.getPhilHealthDeduction() + "\n" +
            "Pag-IBIG: P" + record.getPagIbigDeduction() + "\n" +
            "Tax: P" + record.getTaxDeduction() + "\n" +
            "Total Deductions: P" + record.getTotalDeductions() + "\n" +
            "Net Pay: P" + record.getNetPay()
        );
    }
}