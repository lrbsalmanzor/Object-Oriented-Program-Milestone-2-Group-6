package model;

public class Employee {
    private String employeeId;
    private String lastName;
    private String firstName;
    private String status;
    private String position;
    private double basicSalary;
    private double hourlyRate;

    // Constructor
    public Employee(String employeeId, String lastName, String firstName, String status, String position, double basicSalary, double hourlyRate) {
        this.employeeId = employeeId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.status = status;
        this.position = position;
        this.basicSalary = basicSalary;
        this.hourlyRate = hourlyRate;
    }

    // Getters and Setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    // Shared base gross pay computation method
    public double calculateGrossPay(double hoursWorked, double overtimeHours, double allowances) {
        return (hoursWorked * hourlyRate) + allowances;
    }
}