import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeeAttendanceSystem extends JFrame {
    private JTextField employeeIdField;
    private JButton timeInButton;
    private JButton timeOutButton;
    private JTextArea reportTextArea;
    private String attendanceFilePath;
    private String lateEmployeesFilePath;
    private List<Integer> timedInEmployees;
    private List<Integer> lateEmployees;
    private List<Integer> halfDayEmployees;
    private List<Integer> hiredEmployees; // List of hired employee IDs

    public EmployeeAttendanceSystem() {
        super("Employee Attendance System");

        // Create and set up the GUI components
        employeeIdField = new JTextField(20);
        timeInButton = new JButton("Time In");
        timeOutButton = new JButton("Time Out");
        reportTextArea = new JTextArea(20, 50);
        attendanceFilePath = "attendance.txt"; // File path to store the attendance data
        lateEmployeesFilePath = "late_employees.txt";
        timedInEmployees = new ArrayList<>();
        lateEmployees = new ArrayList<>();
        halfDayEmployees = new ArrayList<>();
        hiredEmployees = new ArrayList<>(); // Initialize the list of hired employees
        hiredEmployees.addAll(Arrays.asList(1, 2, 3, 4, 5, 8, 10, 22, 103));
        timeInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                markAttendance(true);
            }
        });

        timeOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                markAttendance(false);
            }
        });

        // Add the components to the JFrame
        setLayout(new FlowLayout());
        add(new JLabel("Employee ID:"));
        add(employeeIdField);
        add(timeInButton);
        add(timeOutButton);
        add(new JScrollPane(reportTextArea));

        // Load initial attendance data and late employees
        try {
            loadAttendanceFromFile();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data.");
        }

        // Update the report text area to display late employees
        updateReport();
    }

    private boolean isEmployeeHired(int employeeId) {
        return hiredEmployees.contains(employeeId);
    }

    private boolean hasEmployeeTimedInToday(int employeeId) throws IOException {
        List<String> attendanceDataList = new ArrayList<>();
        File file = new File(attendanceFilePath);
        if (!file.exists()) {
            file.createNewFile();
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    attendanceDataList.add(line);
                }
            }
        }

        String todayDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        for (String attendanceData : attendanceDataList) {
            String[] data = attendanceData.split(",");
            int dataEmployeeId = Integer.parseInt(data[0]);
            String dataDate = LocalDateTime.parse(data[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            boolean isTimeIn = Boolean.parseBoolean(data[2]);

            if (dataEmployeeId == employeeId && dataDate.equals(todayDate) && isTimeIn) {
                return true; // Employee has already timed in today
            }
        }

        return false; // Employee has not timed in today
    }

    private void markAttendance(boolean isTimeIn) {
        try {
            int employeeId = Integer.parseInt(employeeIdField.getText());

            if (!isEmployeeHired(employeeId)) {
                employeeIdField.setText("");
                JOptionPane.showMessageDialog(this, "Invalid Employee ID.");
                return;
            }

            if (isTimeIn && hasEmployeeTimedInToday(employeeId)) {
                JOptionPane.showMessageDialog(this, "Employee has already timed in today.");
                return;
            }

            LocalTime currentTime = LocalTime.now();
            LocalTime officeStartTime = LocalTime.of(9, 15);
            LocalTime officeEndTime = LocalTime.of(17, 0);
            LocalTime halfDayCutoffTime = LocalTime.of(11, 30);

            if (isTimeIn) {
                if (currentTime.isBefore(officeStartTime)) {
                    timedInEmployees.add(employeeId);
                } else if (currentTime.isAfter(officeStartTime) && currentTime.isBefore(officeEndTime)) {
                    lateEmployees.add(employeeId);
                    if (currentTime.isBefore(halfDayCutoffTime)) {
                        halfDayEmployees.add(employeeId);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot time in after office hours.");
                    return;
                }

            } else {
                if (!timedInEmployees.contains(employeeId)) {
                    JOptionPane.showMessageDialog(this, "Employee has not timed in yet.");
                    return;
                }
                timedInEmployees.remove(Integer.valueOf(employeeId));
            }

            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String attendanceData = String.format("%d,%s,%s,%s\n", employeeId, timeStamp, isTimeIn, getAttendanceType(isTimeIn, employeeId));

            writeAttendanceToFile(attendanceData);
            updateReport();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Employee ID.");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving attendance data.");
        }
    }

    private String getAttendanceType(boolean isTimeIn, int employeeID) {
        if (isTimeIn) {
            if (halfDayEmployees.contains(employeeID)) {
                return "Half Day";
            } else {
                return "Full Day";
            }
        } else {
            return "";
        }
    }

    private void writeAttendanceToFile(String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(attendanceFilePath, true))) {
            writer.write(data);
        }
    }

    private void loadAttendanceFromFile() throws IOException {
        File file = new File(attendanceFilePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int employeeId = Integer.parseInt(data[0]);
                boolean isTimeIn = Boolean.parseBoolean(data[2]);

                if (isTimeIn) {
                    timedInEmployees.add(employeeId);
                }
            }
        }
    }

    private void updateReport() {
        try {
            List<String> attendanceDataList = new ArrayList<>();
            File file = new File(attendanceFilePath);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        attendanceDataList.add(line);
                    }
                }
            }

            StringBuilder report = new StringBuilder("Employee Attendance Report for Today:\n");
            for (String attendanceData : attendanceDataList) {
                String[] data = attendanceData.split(",");
                int employeeId = Integer.parseInt(data[0]);
                String timeStamp = data[1];
                boolean isTimeIn = Boolean.parseBoolean(data[2]);

                String status = "";
                if (lateEmployees.contains(employeeId)) {
                    status = " (Late)";
                } else if (halfDayEmployees.contains(employeeId)) {
                    status = " (Half Day)";
                }

                report.append(String.format("Employee ID: %d, Time: %s, Time In: %s%s\n", employeeId, timeStamp, isTimeIn ? "Yes" : "No", status));
            }
            reportTextArea.setText(report.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching attendance records.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmployeeAttendanceSystem attendanceSystem = new EmployeeAttendanceSystem();
            attendanceSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            attendanceSystem.pack();
            attendanceSystem.setVisible(true);
        });
    }

    @Override
    public void dispose() {
        // Save the late employees data to the file before closing the application
        try {
            updateLateEmployeesFile();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving late employees data.");
        }
        super.dispose();
    }

    private void updateLateEmployeesFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(lateEmployeesFilePath, false))) {
            for (Integer employeeId : lateEmployees) {
                writer.write(String.format("%d,%s\n", employeeId, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            }
        }
    }
}
