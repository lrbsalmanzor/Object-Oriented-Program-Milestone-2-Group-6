import model.AttendanceRecord;
import model.ContractualEmployee;
import model.Employee;
import model.PayrollRecord;
import model.RegularEmployee;
import service.PayrollCalculator;

public class ConsoleTestMain {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        PayrollCalculator calculator = new PayrollCalculator();

        Employee regular = new RegularEmployee(
            "TEST01", "Sample", "Regular", "Regular", "Staff",
            20000.0, 125.0, 1500.0, 1000.0, 1000.0
        );

        Employee contractual = new ContractualEmployee(
            "TEST02", "Sample", "Contractual", "Contractual", "Staff",
            20000.0, 125.0
        );

        // Synthetic hours supplied directly; no CSV files are required.
        AttendanceRecord regularAttendance = new AttendanceRecord(
            "TEST01", "2026-10-01", "08:00", "17:00",
            8.0, 0.0, 0.0
        );

        AttendanceRecord contractualAttendance = new AttendanceRecord(
            "TEST02", "2026-10-01", "08:00", "17:00",
            8.0, 0.0, 0.0
        );

        // Parent-type references must run each subclass's implementation.
        check("Regular gross with configured allowances",
            4500.0, regular.calculateGrossPay(8.0, 0.0, 0.0));

        check("Contractual gross without manual allowances",
            1000.0, contractual.calculateGrossPay(8.0, 0.0, 0.0));

        check("Contractual gross with manual allowances",
            1200.0, contractual.calculateGrossPay(8.0, 0.0, 200.0));

        PayrollRecord regularResult =
            calculator.processPayroll(regular, regularAttendance);

        PayrollRecord contractualResult =
            calculator.processPayroll(contractual, contractualAttendance);

        check("Processor uses RegularEmployee override",
            4500.0, regularResult.getGrossPay());

        check("Processor uses ContractualEmployee override",
            1000.0, contractualResult.getGrossPay());

        checkTotals("Regular", regularResult);
        checkTotals("Contractual", contractualResult);

        // Observations expose current behavior without inventing an OT rule.
        System.out.println("\n--- Behavior requiring requirements review ---");
        System.out.printf(
            "Contractual gross: no OT = %.2f; two OT hours = %.2f%n",
            contractual.calculateGrossPay(8.0, 0.0, 0.0),
            contractual.calculateGrossPay(8.0, 2.0, 0.0)
        );

        AttendanceRecord zeroAttendance = new AttendanceRecord(
            "TEST02", "2026-10-02", "08:00", "08:00",
            0.0, 0.0, 0.0
        );

        PayrollRecord zeroResult =
            calculator.processPayroll(contractual, zeroAttendance);
        check("Zero gross has zero deductions",
            0.0, zeroResult.getTotalDeductions());

        check("Zero gross has zero net pay",
            0.0, zeroResult.getNetPay());
        System.out.printf(
            "Zero hours: gross = %.2f; deductions = %.2f; net = %.2f%n",
            zeroResult.getGrossPay(),
            zeroResult.getTotalDeductions(),
            zeroResult.getNetPay()
        );

        System.out.printf(
            "%nChecks passed: %d | Checks failed: %d%n", passed, failed
        );
        System.out.println(
            "Contribution tables, CP2 parity, CSV loading, and GUI flow "
            + "still require separate verification."
        );

        if (failed > 0) {
            throw new AssertionError("One or more backend checks failed.");
        }
    }

    private static void checkTotals(String label, PayrollRecord record) {
        double sum = record.getSssDeduction()
            + record.getPhilHealthDeduction()
            + record.getPagIbigDeduction()
            + record.getTaxDeduction();

        check(label + " deduction total",
            sum, record.getTotalDeductions());

        check(label + " net pay",
            record.getGrossPay() - sum, record.getNetPay());
    }

    private static void check(String label, double expected, double actual) {
        boolean matches = Double.isFinite(actual)
            && Math.abs(expected - actual) < 0.000001;

        if (matches) {
            passed++;
        } else {
            failed++;
        }

        System.out.printf(
            "%s | %s | expected %.2f | actual %.2f%n",
            matches ? "PASS" : "FAIL", label, expected, actual
        );
    }
}