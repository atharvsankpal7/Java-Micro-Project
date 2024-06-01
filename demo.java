import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import java.util.ArrayList;

public class demo {
    public static JFrame frame;
    private static int seatAvailability = 100;
    private static JLabel availabilityLabel;
    private static JLabel reservationLabel;
    private static double ticketPriceKids = 100;
    private static double ticketPriceAdults = 200;
    private static double ticketPriceElders = 150;
    private static JLabel totalPriceLabel;
    private static int currentReservationCount = 0;
    private static JRadioButton kidsRadioButton;
    private static JRadioButton adultsRadioButton;
    private static JRadioButton eldersRadioButton;
    private static JButton bookButton;
    private static JButton payButton;
    private static double totalPrice;

    // New JLabel for customer name and reservation details
    private static JLabel customerDetailsLabel;

    // New variables to store customer name and total seats booked
    private static String customerName = "";
    private static int totalSeatsBooked = 0;

    // ArrayList to store customer names
    private static ArrayList<String> customerList = new ArrayList<>();

    public static void main(String[] args) {
        frame = new JFrame("Event Booking System");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        availabilityLabel = new JLabel("Available Seats: " + seatAvailability);
        panel.add(availabilityLabel);

        reservationLabel = new JLabel("Reserved Seats: 0");
        panel.add(reservationLabel);

        kidsRadioButton = new JRadioButton("Kids (Rs" + ticketPriceKids + ")");
        adultsRadioButton = new JRadioButton("Adults (Rs" + ticketPriceAdults + ")");
        eldersRadioButton = new JRadioButton("Elders (Rs" + ticketPriceElders + ")");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(kidsRadioButton);
        buttonGroup.add(adultsRadioButton);
        buttonGroup.add(eldersRadioButton);

        JPanel radioButtonsPanel = new JPanel();
        radioButtonsPanel.setLayout(new FlowLayout());
        radioButtonsPanel.add(kidsRadioButton);
        radioButtonsPanel.add(adultsRadioButton);
        radioButtonsPanel.add(eldersRadioButton);
        panel.add(radioButtonsPanel);

        bookButton = new JButton("Book Seat");
        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bookSeat();
            }
        });
        bookButton.setBackground(new Color(135, 206, 235)); // Sky blue color
        panel.add(bookButton);

        JPanel payButtonPanel = new JPanel();
        payButtonPanel.setLayout(new FlowLayout());
        payButton = new JButton("Pay Now");
        payButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                payNow();
            }
        });
        payButton.setBackground(new Color(255, 105, 180));
        payButtonPanel.add(payButton);
        panel.add(payButtonPanel);

        customerDetailsLabel = new JLabel("Customer Details: ");
        panel.add(customerDetailsLabel);

        totalPriceLabel = new JLabel("Total Price: Rs 0.00");
        panel.add(totalPriceLabel);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void bookSeat() {
        if (seatAvailability > 0 && currentReservationCount <= seatAvailability) {
            if (!kidsRadioButton.isSelected() && !adultsRadioButton.isSelected() && !eldersRadioButton.isSelected()) {
                JOptionPane.showMessageDialog(null, "please select a ticket type !!", "Reservation Unsuccessful...!!!", JOptionPane.WARNING_MESSAGE);
                return;//stop the booking process if no RadioButton is selected
            }
            seatAvailability--;
            currentReservationCount++;
            int reservedSeats = 100 - seatAvailability;
            availabilityLabel.setText("Available Seats: " + seatAvailability);
            reservationLabel.setText("Reserved Seats: " + reservedSeats);

            if (kidsRadioButton.isSelected()) {
                totalPrice += ticketPriceKids;
            } else if (adultsRadioButton.isSelected()) {
                totalPrice += ticketPriceAdults;
            } else {
                totalPrice += ticketPriceElders;
            }

            DecimalFormat df = new DecimalFormat("0.00");
            totalPriceLabel.setText("Total Price: Rs" + df.format(totalPrice));
            System.out.println("Seat booked!");

            updateCustomerDetailsLabel();
        } else {
            System.out.println("No seats available!");
        }
    }

    private static void payNow() {
        if (currentReservationCount == 0) {
            JOptionPane.showMessageDialog(null, "Please reserve seat first", "Payment Unsuccessful...!!!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (seatAvailability < 100) {

            // Prompt the user to enter their name
            customerName = JOptionPane.showInputDialog(null, "Enter your name:");

            if (customerName != null && !customerName.trim().isEmpty()) { // Check if the customer entered a valid name
                int reservedSeats = 100 - seatAvailability;

                System.out.println("Payment successful! Total amount paid: Rs" + totalPrice);
                System.out.println("Registration successfully completed!");

                // Add the customer name to the list
                customerList.add(customerName);

                // Display the bill along with the customer's name and number of seats booked
                String billText = "Customer Name: " + customerName + "\n" +
                        "Reserved Seats: " + currentReservationCount + "\n" +
                        "Total Price: Rs " + totalPrice;

                JOptionPane.showMessageDialog(null, billText, "Payment Successful", JOptionPane.INFORMATION_MESSAGE);

                // Reset total price and update the display
                totalPrice = 0;
                totalPriceLabel.setText("Total Price: Rs" + (totalPrice));

                // Reset the customer name and reservation count
                customerName = "";

                totalSeatsBooked += currentReservationCount;
                currentReservationCount = 0;
                updateCustomerDetailsLabel();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid customer name!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No seat Available");
        }
    }

    // Helper method to update the customer details label
    private static void updateCustomerDetailsLabel() {
        // Clear the frame from all customer labels before adding new ones
        frame.getContentPane().removeAll();

        // Add the customer details label back to the panel
        frame.getContentPane().add(customerDetailsLabel);

        // Add customer name labels below the customer details label
        for (String name : customerList) {
            JLabel customerLabel = new JLabel(name + " - Booked " + currentReservationCount + " seats");
            frame.getContentPane().add(customerLabel);
        }

        // Revalidate and repaint the frame to update the changes
        frame.revalidate();
        frame.repaint();
    }
}
