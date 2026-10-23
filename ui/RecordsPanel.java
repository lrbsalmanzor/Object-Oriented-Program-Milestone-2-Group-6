package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.Employee;
import repository.EmployeeRepository;

public class RecordsPanel extends JPanel {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private EmployeeRepository employeeRepository;

    public RecordsPanel() {
        setLayout(new BorderLayout());
        employeeRepository = new EmployeeRepository("employees.csv");

        String[] columns = {"ID", "Last Name", "First Name", "Status", "Position", "Basic Salary"};
        tableModel = new DefaultTableModel(columns, 0);
        employeeTable = new JTable(tableModel);

        loadEmployeeData();

        add(new JScrollPane(employeeTable), BorderLayout.CENTER);
    }

    private void loadEmployeeData() {
        List<Employee> employees = employeeRepository.getAllEmployees();
        for (Employee emp : employees) {
            Object[] row = {
                emp.getEmployeeId(),
                emp.getLastName(),
                emp.getFirstName(),
                emp.getStatus(),
                emp.getPosition(),
                emp.getBasicSalary()
            };
            tableModel.addRow(row);
        }
    }
}