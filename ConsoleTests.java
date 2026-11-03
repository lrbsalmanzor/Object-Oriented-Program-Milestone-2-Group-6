package motorph;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsoleTests {

    private static final double EPS = 0.005;
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws IOException {
        Path workDir = args.length > 0
                ? Paths.get(args[0])
                : Files.createTempDirectory("motorph-tests");
        Files.createDirectories(workDir);

        testAttendanceRecord();
        testContractualEmployee();
        testPolymorphism();
        testStatutoryCalculators();
        testTaxCalculator();
        testSinglePayrollRuns();
        testExecutivePayroll();
        testBatchPayroll();
        testAttendanceRepository(workDir);
        testPayrollSummary();

        System.out.println();
        System.out.println("Passed: " + passed + "   Failed: " + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void testAttendanceRecord() {
        section("AttendanceRecord (Task 5)");
        AttendanceRecord r = new AttendanceRecord("E001", 8, 2, 1, 5);
        check("valid record keeps its values", r.getEmployeeID().equals("E001") && r.getHoursWorked() == 8
                && r.getOvertimeHours() == 2 && r.getApprovedOvertimeHours() == 1 && r.getTardiness() == 5);
        check("4-argument constructor approves no overtime",
                new AttendanceRecord("E001", 8, 2, 0).getApprovedOvertimeHours() == 0);

        r.setHoursWorked(0);
        check("setHoursWorked accepts 0", r.getHoursWorked() == 0);
        r.setHoursWorked(24);
        check("setHoursWorked accepts 24", r.getHoursWorked() == 24);
        expectIAE("setHoursWorked(25) throws", () -> r.setHoursWorked(25));
        expectIAE("setHoursWorked(-1) throws", () -> r.setHoursWorked(-1));
        expectIAE("setHoursWorked(NaN) throws", () -> r.setHoursWorked(Double.NaN));
        check("failed setter leaves the old value", r.getHoursWorked() == 24);

        expectIAE("constructor rejects 25 hours", () -> new AttendanceRecord("E001", 25, 0, 0));
        expectIAE("blank employee ID rejected", () -> new AttendanceRecord("  ", 8, 0, 0));
        expectIAE("comma in employee ID rejected", () -> new AttendanceRecord("E,1", 8, 0, 0));
        expectIAE("negative tardiness rejected", () -> new AttendanceRecord("E001", 8, 0, -1));
        expectIAE("approved overtime above logged overtime rejected",
                () -> new AttendanceRecord("E001", 8, 2, 3, 0));
        expectIAE("setApprovedOvertimeHours above logged overtime rejected",
                () -> r.setApprovedOvertimeHours(5));
    }

    private static void testContractualEmployee() {
        section("ContractualEmployee (Task 4)");
        ContractualEmployee c = new ContractualEmployee("E003", "Juan Dela Cruz", "Ops", 100);
        check("uses hourlyRate", c.getHourlyRate() == 100);
        check("basicSalary is unused (0)", c.getBasicSalary() == 0);

        c.setAttendanceRecord(new AttendanceRecord("E003", 8, 2, 2, 0));
        approx("overtime pay = 2 approved h x 100 x 1.25", c.computeOvertimePay(), 250.00);
        approx("gross = 8 x 100 + overtime", c.computeGrossPay(), 1050.00);
        check("overtime counted exactly once",
                Math.abs(c.computeGrossPay() - (8 * 100 + c.computeOvertimePay())) < EPS);

        c.setAttendanceRecord(new AttendanceRecord("E003", 8, 2, 0, 0));
        approx("unapproved overtime earns nothing", c.computeOvertimePay(), 0.00);
        approx("gross without approved overtime", c.computeGrossPay(), 800.00);

        c.setAttendanceRecord(new AttendanceRecord("E003", 0, 0, 0, 0));
        approx("zero hours gives zero gross", c.computeGrossPay(), 0.00);

        ContractualEmployee noLog = new ContractualEmployee("E004", "No Log", "Ops", 100);
        expectThrows("no attendance record -> IllegalStateException", IllegalStateException.class,
                noLog::computeGrossPay);

        expectIAE("hourlyRate of 0 rejected", () -> c.setHourlyRate(0));
        expectIAE("negative hourlyRate rejected", () -> c.setHourlyRate(-5));
        expectIAE("constructor rejects hourlyRate 0", () -> new ContractualEmployee("E005", "X", "Ops", 0));
        check("rejected rate leaves old rate", c.getHourlyRate() == 100);
    }

    private static void testPolymorphism() {
        section("Abstraction and polymorphism");
        Employee[] staff = {
                new RegularEmployee("E001", "Ana", "Sales", 35000, 0),
                new ContractualEmployee("E003", "Juan", "Ops", 100)
        };
        staff[0].setAttendanceRecord(new AttendanceRecord("E001", 8, 0, 0));
        staff[1].setAttendanceRecord(new AttendanceRecord("E003", 8, 2, 2, 0));

        approx("Employee[] dispatch -> RegularEmployee.computeGrossPay()", staff[0].computeGrossPay(), 35000.00);
        approx("Employee[] dispatch -> ContractualEmployee.computeGrossPay()", staff[1].computeGrossPay(), 1050.00);
        check("Employee is abstract", java.lang.reflect.Modifier.isAbstract(Employee.class.getModifiers()));

        PayrollCalculator calc = new ExecutivePayrollProcessor(5000);
        check("ExecutivePayrollProcessor can be used as a PayrollCalculator", calc instanceof PayrollCalculator);
    }

    private static void testStatutoryCalculators() {
        section("SSS / PhilHealth / Pag-IBIG calculators (Task 26)");
        SSSCalculator sss = new SSSCalculator();
        approx("SSS 15,000 -> MSC 15,000 -> 750", sss.computeContribution(15000), 750.00);
        approx("SSS 28,500 (table example) -> 1,425", sss.computeContribution(28500), 1425.00);
        approx("SSS 28,700 -> MSC 28,500 -> 1,425", sss.computeContribution(28700), 1425.00);
        approx("SSS 35,000 -> 1,750", sss.computeContribution(35000), 1750.00);
        approx("SSS 60,000 capped at MSC 35,000 -> 1,750", sss.computeContribution(60000), 1750.00);
        approx("SSS 4,000 floored at MSC 5,000 -> 250", sss.computeContribution(4000), 250.00);
        approx("SSS 5,249.99 -> MSC 5,000 -> 250", sss.computeContribution(5249.99), 250.00);
        approx("SSS 5,250 -> MSC 5,500 -> 275", sss.computeContribution(5250), 275.00);
        expectIAE("SSS negative salary rejected", () -> sss.computeContribution(-1));

        PhilHealthCalculator ph = new PhilHealthCalculator();
        approx("PhilHealth 10,000 -> 250", ph.computeContribution(10000), 250.00);
        approx("PhilHealth 35,000 (table example) -> 875", ph.computeContribution(35000), 875.00);
        approx("PhilHealth 8,000 uses 10,000 floor -> 250", ph.computeContribution(8000), 250.00);
        approx("PhilHealth 100,000 -> 2,500", ph.computeContribution(100000), 2500.00);
        approx("PhilHealth 120,000 capped -> 2,500", ph.computeContribution(120000), 2500.00);
        expectIAE("PhilHealth negative salary rejected", () -> ph.computeContribution(-1));

        PagIBIGCalculator pi = new PagIBIGCalculator();
        approx("Pag-IBIG 1,400 -> 1% = 14 (table example)", pi.computeContribution(1400), 14.00);
        approx("Pag-IBIG 1,500 -> 1% = 15", pi.computeContribution(1500), 15.00);
        approx("Pag-IBIG 25,000 capped -> 200 (table example)", pi.computeContribution(25000), 200.00);
        approx("Pag-IBIG 10,000 -> 200", pi.computeContribution(10000), 200.00);
        approx("Pag-IBIG 110,000 -> 200", pi.computeContribution(110000), 200.00);
        expectIAE("Pag-IBIG negative salary rejected", () -> pi.computeContribution(-1));

        ContributionCalculator all = new ContributionCalculator();
        approx("ContributionCalculator 35,000 -> 1,750 + 875 + 200", all.computeAll(35000), 2825.00);
        approx("ContributionCalculator 60,000 -> 1,750 + 1,500 + 200", all.computeAll(60000), 3450.00);
    }

    private static void testTaxCalculator() {
        section("TaxCalculator - BIR monthly table 2023+ (Task 26)");
        TaxCalculator t = new TaxCalculator();
        approx("tax at 0", t.computeWithholdingTax(0), 0.00);
        approx("tax at 13,675 (below 20,833)", t.computeWithholdingTax(13675), 0.00);
        approx("tax at 20,833 (top of exempt band)", t.computeWithholdingTax(20833), 0.00);
        approx("tax at 32,175 -> 15% over 20,833", t.computeWithholdingTax(32175), 1701.30);
        approx("tax at 33,333 -> 1,875.00", t.computeWithholdingTax(33333), 1875.00);
        approx("tax at 56,550 -> 1,875 + 20% over 33,333", t.computeWithholdingTax(56550), 6518.40);
        approx("tax at 66,667 -> 8,541.80", t.computeWithholdingTax(66667), 8541.80);
        approx("tax at 100,000 -> 8,541.80 + 25% over 66,667", t.computeWithholdingTax(100000), 16875.05);
        approx("tax at 166,667 -> 33,541.80", t.computeWithholdingTax(166667), 33541.80);
        approx("tax at 666,667 -> 183,541.80", t.computeWithholdingTax(666667), 183541.80);
        approx("tax at 700,000 -> 183,541.80 + 35% over 666,667", t.computeWithholdingTax(700000), 195208.35);
        expectIAE("tax rejects negative income", () -> t.computeWithholdingTax(-1));
    }

    private static void testSinglePayrollRuns() {
        section("PayrollCalculator single run");
        PayrollCalculator calc = new PayrollCalculator();

        PayrollRecord a = calc.processPayrollRun(
                new RegularEmployee("E001", "Ana", "Sales", 15000, 0), log("E001", 8, 0, 0));
        approxRecord("15,000 salary", a, 15000.00, 1325.00, 13675.00);

        PayrollRecord b = calc.processPayrollRun(
                new RegularEmployee("E002", "Ben", "Sales", 35000, 0), log("E002", 8, 0, 0));
        approxRecord("35,000 salary", b, 35000.00, 4526.30, 30473.70);

        PayrollRecord c = calc.processPayrollRun(
                new RegularEmployee("E006", "Cy", "Sales", 60000, 0), log("E006", 8, 0, 0));
        approxRecord("60,000 salary", c, 60000.00, 9968.40, 50031.60);

        check("intermediate getGrossPay matches last run", Math.abs(calc.getGrossPay() - 60000.00) < EPS);
        check("intermediate getNetPay matches last run", Math.abs(calc.getNetPay() - 50031.60) < EPS);

        PayrollRecord d = calc.processPayrollRun(
                new ContractualEmployee("E003", "Juan", "Ops", 100), log("E003", 8, 2, 2));
        approxRecord("contractual 8h + 2h approved overtime", d, 1050.00, 510.50, 539.50);

        expectIAE("null employee rejected", () -> calc.processPayrollRun(null, log("E001", 8, 0, 0)));
        expectIAE("null attendance rejected", () -> calc.processPayrollRun(
                new RegularEmployee("E001", "Ana", "Sales", 15000, 0), null));
        expectIAE("attendance for a different employee rejected", () -> calc.processPayrollRun(
                new RegularEmployee("E001", "Ana", "Sales", 15000, 0), log("E999", 8, 0, 0)));
    }

    private static void testExecutivePayroll() {
        section("ExecutivePayrollProcessor (Task 15)");
        Employee exec = new RegularEmployee("E002", "Ben", "Sales", 35000, 0);
        AttendanceRecord log = log("E002", 8, 0, 0);

        PayrollRecord baseline = new PayrollCalculator().processPayrollRun(exec, log);
        PayrollRecord executive = new ExecutivePayrollProcessor(5000).processPayrollRun(exec, log);

        approxRecord("executive: gross 35,000 + 5,000 adjustment", executive, 40000.00, 5568.40, 34431.60);
        check("adjustment raises gross before deductions", executive.getGrossPay() > baseline.getGrossPay());
        check("deductions are recomputed on the adjusted gross", executive.getDeductions() > baseline.getDeductions());
        approxRecord("baseline record is untouched", baseline, 35000.00, 4526.30, 30473.70);

        PayrollCalculator asParent = new ExecutivePayrollProcessor(5000);
        approxRecord("parent-type reference runs the executive workflow",
                asParent.processPayrollRun(exec, log), 40000.00, 5568.40, 34431.60);

        PayrollRecord zeroAdj = new ExecutivePayrollProcessor(0).processPayrollRun(exec, log);
        approxRecord("zero adjustment equals the baseline run", zeroAdj, 35000.00, 4526.30, 30473.70);

        expectIAE("negative adjustment rejected", () -> new ExecutivePayrollProcessor(-1));
    }

    private static void testBatchPayroll() {
        section("processPayrollBatch (Task 14)");
        Employee[] employees = {
                new RegularEmployee("E001", "Ana", "Sales", 15000, 0),
                new RegularEmployee("E002", "Ben", "Sales", 35000, 0),
                new ContractualEmployee("E003", "Juan", "Ops", 100)
        };
        // logs deliberately in a different order than the employees
        AttendanceRecord[] logs = {
                log("E003", 8, 2, 2), log("E001", 8, 0, 0), log("E002", 8, 0, 0)
        };

        PayrollCalculator calc = new PayrollCalculator();
        PayrollRecord[] out = calc.processPayrollBatch(employees, logs);
        check("one record per employee", out.length == 3);
        check("results keep the employee order",
                out[0].getEmployeeID().equals("E001") && out[1].getEmployeeID().equals("E002")
                        && out[2].getEmployeeID().equals("E003"));
        approxRecord("batch E001 matched to its own log", out[0], 15000.00, 1325.00, 13675.00);
        approxRecord("batch E002 matched to its own log", out[1], 35000.00, 4526.30, 30473.70);
        approxRecord("batch E003 (contractual) matched to its own log", out[2], 1050.00, 510.50, 539.50);

        PayrollRecord[] execOut = new ExecutivePayrollProcessor(5000).processPayrollBatch(employees, logs);
        approx("executive batch uses the overridden run (E002 net)", execOut[1].getNetPay(), 34431.60);

        expectIAE("missing log for an employee stops the batch",
                () -> calc.processPayrollBatch(employees, new AttendanceRecord[]{log("E001", 8, 0, 0), log("E002", 8, 0, 0)}));
        expectIAE("duplicate log stops the batch",
                () -> calc.processPayrollBatch(employees, new AttendanceRecord[]{log("E001", 8, 0, 0), log("E001", 8, 0, 0),
                        log("E002", 8, 0, 0), log("E003", 8, 0, 0)}));
        expectIAE("null employees array rejected", () -> calc.processPayrollBatch(null, logs));
        expectIAE("null logs array rejected", () -> calc.processPayrollBatch(employees, null));
        check("empty batch returns an empty array",
                calc.processPayrollBatch(new Employee[0], new AttendanceRecord[0]).length == 0);
        check("extra logs with no matching employee are ignored",
                calc.processPayrollBatch(new Employee[]{employees[0]}, logs).length == 1);
    }

    private static void testAttendanceRepository(Path workDir) throws IOException {
        section("AttendanceRepository (Task 17)");
        Path csv = workDir.resolve("attendance-test.csv");
        Files.deleteIfExists(csv);

        AttendanceRepository repo = new AttendanceRepository(csv.toString());
        check("missing file: findByEmployeeId returns null", repo.findByEmployeeId("E001") == null);
        check("missing file: findAll returns an empty list", repo.findAll().isEmpty());
        check("missing file: nothing was created by reading", !Files.exists(csv));

        repo.save(new AttendanceRecord("E001", 8, 2, 1, 5));
        repo.save(new AttendanceRecord("E002", 7.5, 0, 0, 0));
        check("saved record can be found", repo.findByEmployeeId("E001") != null);
        check("unknown ID returns null", repo.findByEmployeeId("E999") == null);
        check("null ID returns null", repo.findByEmployeeId(null) == null);

        AttendanceRepository reopened = new AttendanceRepository(csv.toString());
        AttendanceRecord loaded = reopened.findByEmployeeId("E001");
        check("data survives a restart (new repository instance)",
                loaded != null && loaded.getHoursWorked() == 8 && loaded.getOvertimeHours() == 2
                        && loaded.getApprovedOvertimeHours() == 1 && loaded.getTardiness() == 5);

        repo.save(new AttendanceRecord("E001", 6, 0, 0, 0));
        check("save with the same ID replaces the record, not duplicates it", repo.findAll().size() == 2);
        check("replaced values are stored", repo.findByEmployeeId("E001").getHoursWorked() == 6);

        List<String> lines = new ArrayList<>(Files.readAllLines(csv, StandardCharsets.UTF_8));
        lines.add("BAD LINE");
        lines.add("E010,abc,0,0,0");
        lines.add("E011,30,0,0,0");
        lines.add("E012,8,0,0,0");
        Files.write(csv, lines, StandardCharsets.UTF_8);
        AttendanceRepository dirty = new AttendanceRepository(csv.toString());
        check("malformed lines are skipped, good lines still load",
                dirty.findAll().size() == 3 && dirty.findByEmployeeId("E012") != null);
        check("skipped line count is reported", dirty.getSkippedLineCount() == 3);

        expectIAE("null record rejected", () -> repo.save(null));
        expectIAE("blank file path rejected", () -> new AttendanceRepository(" "));

        Files.deleteIfExists(csv);
    }

    private static void testPayrollSummary() {
        section("PayrollSummary (Task 19)");
        List<PayrollRecord> records = Arrays.asList(
                new PayrollRecord("E001", 15000.00, 1325.00, 13675.00),
                new PayrollRecord("E002", 35000.00, 4526.30, 30473.70),
                new PayrollRecord("E003", 1050.00, 510.50, 539.50));
        Map<String, String> departments = new HashMap<>();
        departments.put("E001", "Sales");
        departments.put("E002", "Sales");
        departments.put("E003", "Ops");

        PayrollSummary s = new PayrollSummary(records, departments);
        check("total employees", s.totalEmployees() == 3);
        approx("total gross", s.totalGross(), 51050.00);
        approx("total deductions", s.totalDeductions(), 6361.80);
        approx("total company net pay", s.totalCompany(), 44688.20);
        approx("average net pay", s.averageNetPay(), 14896.07);
        approx("Sales net total", s.totalByDepartment("Sales"), 44148.70);
        approx("department name ignores case and spaces", s.totalByDepartment("  ops "), 539.50);
        approx("unknown department totals 0", s.totalByDepartment("Finance"), 0.00);
        expectIAE("null department rejected", () -> s.totalByDepartment(null));

        List<String> csv = s.toCsvLines();
        check("CSV header is Metric,Value", csv.get(0).equals("Metric,Value"));
        check("CSV rows are exactly as expected", csv.equals(Arrays.asList(
                "Metric,Value", "Total Employees,3", "Total Gross Pay,51050.00",
                "Total Deductions,6361.80", "Total Net Pay,44688.20", "Average Net Pay,14896.07")));

        PayrollSummary empty = new PayrollSummary(new ArrayList<PayrollRecord>(), new HashMap<String, String>());
        approx("empty summary: average net pay is 0", empty.averageNetPay(), 0.00);
        check("empty summary: no employees", empty.totalEmployees() == 0);

        List<PayrollRecord> mutable = new ArrayList<>(records);
        PayrollSummary copy = new PayrollSummary(mutable, departments);
        mutable.clear();
        check("summary keeps its own copy of the records", copy.totalEmployees() == 3);

        expectIAE("null records rejected", () -> new PayrollSummary(null, departments));
        expectIAE("null department map rejected", () -> new PayrollSummary(records, null));
    }

    private static AttendanceRecord log(String id, double hours, double overtime, double approved) {
        return new AttendanceRecord(id, hours, overtime, approved, 0);
    }

    private static void section(String title) {
        System.out.println();
        System.out.println("== " + title);
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  PASS  " + name);
        } else {
            failed++;
            System.out.println("  FAIL  " + name);
        }
    }

    private static void approx(String name, double actual, double expected) {
        boolean ok = Math.abs(actual - expected) < EPS;
        if (ok) {
            passed++;
            System.out.println("  PASS  " + name + " = " + actual);
        } else {
            failed++;
            System.out.println("  FAIL  " + name + " expected " + expected + " but was " + actual);
        }
    }

    private static void approxRecord(String name, PayrollRecord r, double gross, double deductions, double net) {
        boolean ok = Math.abs(r.getGrossPay() - gross) < EPS
                && Math.abs(r.getDeductions() - deductions) < EPS
                && Math.abs(r.getNetPay() - net) < EPS;
        if (ok) {
            passed++;
            System.out.println("  PASS  " + name + " -> gross " + r.getGrossPay() + ", deductions "
                    + r.getDeductions() + ", net " + r.getNetPay());
        } else {
            failed++;
            System.out.println("  FAIL  " + name + " expected (" + gross + ", " + deductions + ", " + net
                    + ") but was " + r);
        }
    }

    private static void expectIAE(String name, Runnable action) {
        expectThrows(name, IllegalArgumentException.class, action);
    }

    private static void expectThrows(String name, Class<? extends Throwable> type, Runnable action) {
        try {
            action.run();
            failed++;
            System.out.println("  FAIL  " + name + " (nothing was thrown)");
        } catch (Throwable t) {
            if (type.isInstance(t)) {
                passed++;
                System.out.println("  PASS  " + name);
            } else {
                failed++;
                System.out.println("  FAIL  " + name + " (threw " + t.getClass().getSimpleName() + ")");
            }
        }
    }
}
