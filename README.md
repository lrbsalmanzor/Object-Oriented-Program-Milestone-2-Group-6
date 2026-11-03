# MotorPH Payroll: Backend (MO-IT110 Object-Oriented Programming)

Backend of the object-oriented MotorPH Payroll System. It follows the Milestone 1 design and the Milestone 2 plan. Weeks 7 to 8 are backend only, no GUI, tested from the console. GUI integration starts in Week 9.

## What the backend does
For each employee, it takes an attendance record and produces one immutable 'PayrollRecord':
1. **Gross pay** = regular pay + approved overtime (`Employee.computeGrossPay()`).
2. **Deductions** = SSS + PhilHealth + Pag-IBIG (employee shares) + BIR withholding tax.
3. **Net pay** = gross pay - deductions.

`PayrollCalculator` handles one employee or a whole batch. `ExecutivePayrollProcessor` changes the workflow for executives: gross, then executive adjustment, then deductions, then net.

## OOP Concepts Used
| Concept | Where |
|---|---|
| **Encapsulation** | Private fields and guarded setters. `setHoursWorked` accepts only 0-24, and pay-rate setters reject values of 0 or less. `PayrollRecord` has final fields and no setters. |
| **Abstraction** | `Employee` is abstract. It declares `computeGrossPay()` and `computeOvertimePay()` and leaves the pay rules to subclasses. |
| **Inheritance** | `RegularEmployee` and `ContractualEmployee` extend `Employee`. `ExecutivePayrollProcessor` extends `PayrollCalculator`. |
| **Polymorphism** | Employees are handled through `Employee[]`, so the right `computeGrossPay()` runs at runtime. A `PayrollCalculator` reference can hold an `ExecutivePayrollProcessor`. |

## Design Decisions 
1. **Overtime is counted once.** `computeGrossPay()` is the only place that adds overtime pay. `PayrollCalculator` never adds it again. Only **approved** overtime hours are paid.
2. **Compensation fields per type.** `RegularEmployee` uses `basicSalary`. `ContractualEmployee` uses `hourlyRate`. The unused field stays 0.
3. **`PayrollRecord` owns the final values.** `PayrollCalculator` totals are intermediate only.
4. **Batch matching.** `processPayrollBatch(Employee[], AttendanceRecord[])` matches each employee to a log by employee ID. A missing or duplicate log stops the whole batch before any pay is computed.
5. **Executive adjustment** is added to gross pay before deductions, and a new record is built once. The baseline record is never modified.

## Statutory Rates and Tax
| Item | Rule | Source |
|---|---|---|
| SSS | Monthly Salary Credit = salary rounded to the nearest 500, limited to 5,000-35,000. Employee share = 5%. | SSS Circular No. 2024-006 (in force for 2026), via taxcalculator.com.ph |
| PhilHealth | 5% premium on salary limited to 10,000-100,000, split 50/50. Employee share = 2.5%. | PhilHealth 2026 table, via GreatDay HR |
| Pag-IBIG | 1% if salary is 1,500 or below, otherwise 2%, on a maximum fund salary of 10,000 (so at most 200). | HDMF Circular No. 460, via taxcalculator.com.ph |
| Withholding tax | Monthly table effective January 1, 2023 onwards, on gross pay minus the three employee shares. | BIR Withholding Tax page |

The SSS, PhilHealth and Pag-IBIG figures come from third-party sites that cite the official circulars. Rates are named
constants in each calculator, so a change is made in one place.

## Build and Run
Needs a JDK (Java 11 or newer). Check with `javac -version`.
```
./build-and-test.sh
```
Or
```
mkdir out
javac -d out $(find src teammate-stubs tests -name '*.java')
java -cp out motorph.ConsoleTests
```
On Windows without `sh`, use an IDE.
