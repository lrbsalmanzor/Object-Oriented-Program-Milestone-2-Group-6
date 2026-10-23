package repository;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import model.PayrollRecord;

public class PayrollRepository {
    private String filePath;

    public PayrollRepository(String filePath) {
        this.filePath = filePath;
    }

    public void savePayrollRecord(PayrollRecord record) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
            pw.println(record.getEmployeeId() + "," +
                       record.getEmployeeName() + "," +
                       record.getGrossPay() + "," +
                       record.getSssDeduction() + "," +
                       record.getPhilHealthDeduction() + "," +
                       record.getPagIbigDeduction() + "," +
                       record.getTaxDeduction() + "," +
                       record.getNetPay());
        } catch (IOException e) {
            System.err.println("Error saving payroll record: " + e.getMessage());
        }
    }
}