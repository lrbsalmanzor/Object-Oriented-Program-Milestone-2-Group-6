package ui;

import javax.swing.*;

public class EmployeeApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MotorPH Payroll System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Login", new LoginPanel(frame));
            tabbedPane.addTab("Employee Records", new RecordsPanel());
            tabbedPane.addTab("Payroll Processing", new PayrollPanel());

            frame.add(tabbedPane);
            frame.setVisible(true);
        });
    }
}