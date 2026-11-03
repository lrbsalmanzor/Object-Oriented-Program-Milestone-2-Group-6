package com.motorph.payroll;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class TaxCalculator {

    private static final BigDecimal ZERO =
            new BigDecimal("0.00");

    private static final BigDecimal BRACKET_1 =
            new BigDecimal("20832.00");

    private static final BigDecimal BRACKET_2 =
            new BigDecimal("33333.00");

    private static final BigDecimal BRACKET_3 =
            new BigDecimal("66667.00");

    private static final BigDecimal BRACKET_4 =
            new BigDecimal("166667.00");

    private static final BigDecimal BRACKET_5 =
            new BigDecimal("666667.00");

    private static final BigDecimal BASE_1 =
            new BigDecimal("20833.00");

    private static final BigDecimal BASE_2 =
            new BigDecimal("33333.00");

    private static final BigDecimal BASE_3 =
            new BigDecimal("66667.00");

    private static final BigDecimal BASE_4 =
            new BigDecimal("166667.00");

    private static final BigDecimal BASE_5 =
            new BigDecimal("666667.00");

    private static final BigDecimal FIXED_2 =
            new BigDecimal("2500.00");

    private static final BigDecimal FIXED_3 =
            new BigDecimal("10833.00");

    private static final BigDecimal FIXED_4 =
            new BigDecimal("40833.33");

    private static final BigDecimal FIXED_5 =
            new BigDecimal("200833.33");

    public double computeTax(double taxableIncome) {

        if (!Double.isFinite(taxableIncome)
                || taxableIncome < 0) {

            throw new IllegalArgumentException(
                    "Taxable income must be finite and non-negative."
            );
        }

        BigDecimal income =
                BigDecimal.valueOf(taxableIncome);

        BigDecimal tax;

        if (income.compareTo(BRACKET_1) <= 0) {

            tax = ZERO;

        } else if (income.compareTo(BRACKET_2) < 0) {

            tax = income
                    .subtract(BASE_1)
                    .multiply(new BigDecimal("0.20"));

        } else if (income.compareTo(BRACKET_3) < 0) {

            tax = FIXED_2.add(
                    income.subtract(BASE_2)
                            .multiply(new BigDecimal("0.25"))
            );

        } else if (income.compareTo(BRACKET_4) < 0) {

            tax = FIXED_3.add(
                    income.subtract(BASE_3)
                            .multiply(new BigDecimal("0.30"))
            );

        } else if (income.compareTo(BRACKET_5) < 0) {

            tax = FIXED_4.add(
                    income.subtract(BASE_4)
                            .multiply(new BigDecimal("0.32"))
            );

        } else {

            tax = FIXED_5.add(
                    income.subtract(BASE_5)
                            .multiply(new BigDecimal("0.35"))
            );
        }

        return tax.setScale(
                2,
                RoundingMode.HALF_UP
        ).doubleValue();
    }
}