package service;

import java.util.List;
import model.PayrollRecord;

public class PayrollSummary {
    public double calculateTotalDisbursement(List<PayrollRecord> records) {
        double total = 0.0;
        for (PayrollRecord record : records) {
            total += record.getNetPay();
        }
        return total;
    }
}