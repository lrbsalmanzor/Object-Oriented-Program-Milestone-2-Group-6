package model;

public class AttendanceRecord {
    private String employeeId;
    private String date;
    private String timeIn;
    private String timeOut;
    private double hoursWorked;
    private double overtimeHours;
    private double tardinessMinutes;

    public AttendanceRecord(String employeeId, String date, String timeIn, String timeOut, double hoursWorked, double overtimeHours, double tardinessMinutes) {
        this.employeeId = employeeId;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.hoursWorked = hoursWorked;
        this.overtimeHours = overtimeHours;
        this.tardinessMinutes = tardinessMinutes;
    }

    public String getEmployeeId() { return employeeId; }
    public String getDate() { return date; }
    public double getHoursWorked() { return hoursWorked; }
    public double getOvertimeHours() { return overtimeHours; }
    public double getTardinessMinutes() { return tardinessMinutes; }
}