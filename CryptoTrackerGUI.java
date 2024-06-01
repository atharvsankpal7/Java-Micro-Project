import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class CryptoTrackerGUI extends JFrame {
    private static final String API_URL = "https://api.coingecko.com/api/v3";

    private JTextArea outputTextArea;

    public CryptoTrackerGUI() {
        super("CryptoTracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        JLabel label = new JLabel("Enter a cryptocurrency symbol: ");
        JTextField inputField = new JTextField(100);
        JButton submitButton = new JButton("Submit");

        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputTextArea);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String symbol = inputField.getText();

                try {
                    if (!symbol.isEmpty()) {
                        String cryptoData = fetchData(symbol);
                        displayCryptoData(symbol, cryptoData);
                    } else {
                        displayError("Please enter a cryptocurrency symbol.");
                    }
                } catch (IOException ex) {
                    displayError("An error occurred: " + ex.getMessage());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Invalid Input");
                }
            }
        });

        inputPanel.add(label);
        inputPanel.add(inputField);
        inputPanel.add(submitButton);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private void displayError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private String fetchData(String symbol) throws IOException {
        String apiUrl = API_URL + "/simple/price?ids=" + symbol
                + "&vs_currencies=usd&include_market_cap=true&include_24hr_vol=true";
        System.out.println(apiUrl);
        try (Scanner scanner = new Scanner(new URL(apiUrl).openStream())) {
            scanner.useDelimiter("\\A");
            return scanner.next();
        }

    }

    private void displayCryptoData(String symbol, String data) throws Exception {
        String price = data.split("\"usd\":")[1].split(",")[0];
        String marketCap = data.split("\"usd_market_cap\":")[1].split(",")[0];
        String volume = data.split("\"usd_24h_vol\":")[1].split("}")[0];

        outputTextArea.setText(""); // Clear previous output
        outputTextArea.append("Crypto: " + symbol + "\n");
        outputTextArea.append("Price: $" + price + "\n");
        outputTextArea.append("Market Cap: $" + marketCap + "\n");
        outputTextArea.append("Volume (24h): $" + volume + "\n");
    }

    public static void main(String[] args) {
        new CryptoTrackerGUI();
    }
}
