package com.motorph.payroll;

public final class ContributionCalculator {

    private final SSSCalculator sssCalculator;
    private final PhilHealthCalculator philHealthCalculator;
    private final PagIBIGCalculator pagIBIGCalculator;

    public ContributionCalculator() {

        this(
                new SSSCalculator(),
                new PhilHealthCalculator(),
                new PagIBIGCalculator()
        );
    }

    public ContributionCalculator(
            SSSCalculator sss,
            PhilHealthCalculator philHealth,
            PagIBIGCalculator pagIBIG) {

        this.sssCalculator =
                java.util.Objects.requireNonNull(sss);

        this.philHealthCalculator =
                java.util.Objects.requireNonNull(philHealth);

        this.pagIBIGCalculator =
                java.util.Objects.requireNonNull(pagIBIG);
    }

    public ContributionBreakdown computeAll(double grossPay) {

        return new ContributionBreakdown(
                sssCalculator.computeSSS(grossPay),
                philHealthCalculator.computePhilHealth(grossPay),
                pagIBIGCalculator.computePagIBIG(grossPay)
        );
    }

    public record ContributionBreakdown(
            double sss,
            double philHealth,
            double pagIBIG) {

        public double getTotal() {
            return sss + philHealth + pagIBIG;
        }
    }
}