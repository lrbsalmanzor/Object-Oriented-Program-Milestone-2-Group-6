package repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.ContractualEmployee;
import model.Employee;
import model.RegularEmployee;

public class EmployeeRepository {
    private String filePath;

    public EmployeeRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip CSV header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 7) {
                    String id = values[0].trim();
                    String lastName = values[1].trim();
                    String firstName = values[2].trim();
                    String status = values[3].trim();
                    String position = values[4].trim();
                    double basicSalary = Double.parseDouble(values[5].trim());
                    double hourlyRate = Double.parseDouble(values[6].trim());

                    if (status.equalsIgnoreCase("Regular")) {
                        employees.add(new RegularEmployee(id, lastName, firstName, status, position, basicSalary, hourlyRate, 1500.0, 1000.0, 1000.0));
                    } else {
                        employees.add(new ContractualEmployee(id, lastName, firstName, status, position, basicSalary, hourlyRate));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading employee dataset: " + e.getMessage());
        }
        return employees;
    }
}