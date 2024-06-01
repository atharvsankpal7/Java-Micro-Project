import javax.swing.*;
import java.awt.event.*;
import java.awt.GridLayout;
import java.util.ArrayList;

public class LoginPage implements ActionListener
{

    JTextField JT1 = new JTextField(20);
    JPasswordField JT2 = new JPasswordField(15);

    ArrayList<Doctor> doctors = new ArrayList<>();

    void loginPageFrame()
    {
        JFrame J = new JFrame("Login Page");
        J.setSize(400, 200);

        JLabel JL1 = new JLabel("Username: ");
        JLabel JL2 = new JLabel("Password: ");

        J.add(JL1);
        J.add(JT1);
        J.add(JL2);
        J.add(JT2);

        JButton JB = new JButton("Login");
        JB.addActionListener(this);

        J.add(JB);

        J.setLayout(new GridLayout(3, 2));
        J.setVisible(true);
        J.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    String username1 = "samrat";
    String password1 = "password";

    public void actionPerformed(ActionEvent ae)
    {
        String username = JT1.getText();
        String password = new String(JT2.getPassword());

        if (username.equals(username1) && password.equals(password1))
        {
            this.doctors();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void doctors()
    {
        JFrame J = new JFrame("Doctors");
        J.setSize(400, 300);

        Doctor drSamrat = new Doctor("Dr.Samrat");
//        drSamrat.addAppointment("10:00 AM");
//        drSamrat.addAppointment("2:30 PM");

        Doctor drSatyajeet = new Doctor("Dr.Satyajeet");
//        drSatyajeet.addAppointment("11:00 AM");
//        drSatyajeet.addAppointment("3:00 PM");

        doctors.add(drSamrat);
        doctors.add(drSatyajeet);

        for (Doctor doctor : doctors)
        {
            JButton doctorButton = new JButton(doctor.getName());
            doctorButton.addActionListener(e -> bookAppointment(doctor));
            J.add(doctorButton);
        }

        // Create a "Log Out" button
        JButton logOutButton = new JButton("Log Out");
        logOutButton.addActionListener(e ->
        {
            J.dispose(); // Close the Doctors frame

        });

        J.add(logOutButton);

        J.setLayout(new GridLayout(3, 2));
        J.setVisible(true);
        J.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void bookAppointment(Doctor doctor)
    {
        ArrayList<String> appointments = doctor.getAppointments();
        if (!appointments.isEmpty())
        {
            String[] appointmentArray = appointments.toArray(new String[0]);
            String chosenTime = (String) JOptionPane.showInputDialog(null, "Select a time for the appointment:",
                    "Appointment Booking", JOptionPane.QUESTION_MESSAGE, null, appointmentArray, appointmentArray[0]);

            if (chosenTime != null)
            {
                JOptionPane.showMessageDialog(null, "Appointment booked for " + doctor.getName() + " at " + chosenTime,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else
        {
            JOptionPane.showMessageDialog(null, "No appointments found for " + doctor.getName(), "Appointments", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String args[])
    {
        LoginPage LP = new LoginPage();
        LP.loginPageFrame();
    }

    private static class Doctor
    {
        private String name;
        private ArrayList<String>  appointments;

        public Doctor(String name)
        {
            this.name = name;
            this.appointments = new ArrayList<>();
        }

        public String getName()
        {
            return name;
        }

        public ArrayList<String> getAppointments()
        {
            return appointments;
        }

        public void addAppointment(String appointmentTime)
        {
            appointments.add(appointmentTime);
        }
    }
}