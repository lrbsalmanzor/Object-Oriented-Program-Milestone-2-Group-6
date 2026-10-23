package repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.AttendanceRecord;

public class AttendanceRepository {
    private String filePath;

    public AttendanceRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<AttendanceRecord> getAttendanceByEmployeeId(String employeeId) {
        List<AttendanceRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 7 && values[0].trim().equals(employeeId)) {
                    records.add(new AttendanceRecord(
                        values[0].trim(),
                        values[1].trim(),
                        values[2].trim(),
                        values[3].trim(),
                        Double.parseDouble(values[4].trim()),
                        Double.parseDouble(values[5].trim()),
                        Double.parseDouble(values[6].trim())
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading attendance dataset: " + e.getMessage());
        }
        return records;
    }
}