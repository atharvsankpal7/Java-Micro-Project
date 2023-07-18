import org.json.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class WeatherApp extends JFrame {
    private static final String API_KEY = "e67c1f157951a09e4c1d6269d7028c50";
    private JTextField searchLocation;
    private JButton locationSearch;
    private JLabel locationDisplay;
    private JLabel temperature;
    private JLabel winds;
    private JLabel humidity;
    private JLabel climate;

    public WeatherApp() {
        setTitle("Weather App");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        searchLocation = new JTextField(15);
        locationSearch = new JButton("Search");
        locationDisplay = new JLabel();
        temperature = new JLabel();
        winds = new JLabel();
        humidity = new JLabel();
        climate = new JLabel();

        locationSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateWeather();
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(WeatherApp.this, "Please Enter a Valid Location");
                }
            }
        });

        searchLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateWeather();
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(WeatherApp.this, "Please Enter a Valid Location");
                }
            }
        });

        add(new JLabel("Enter Location: "));
        add(searchLocation);
        add(locationSearch);
        add(locationDisplay);
        add(temperature);
        add(winds);
        add(humidity);
        add(climate);

        locationDisplay.setVisible(false);
        temperature.setVisible(false);
        winds.setVisible(false);
        humidity.setVisible(false);
        climate.setVisible(false);
    }

    private void updateWeather() throws IOException {
        String location = searchLocation.getText();
        String apiUrl = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", location, API_KEY);

        try (InputStream inputStream = new URL(apiUrl).openStream(); Scanner scanner = new Scanner(inputStream)) {
            StringBuilder responseBuilder = new StringBuilder();
            while (scanner.hasNext()) {
                responseBuilder.append(scanner.nextLine());
            }

            String response = responseBuilder.toString();
            JSONObject jsonObject = new JSONObject(response);

            String name = jsonObject.getString("name");
            double tempKelvin = jsonObject.getJSONObject("main").getDouble("temp");
            double windSpeed = jsonObject.getJSONObject("wind").getDouble("speed");
            int humidityValue = jsonObject.getJSONObject("main").getInt("humidity");
            String weatherMain = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");

            int tempCelsius = (int) (tempKelvin - 273.15);

            locationDisplay.setText("Location: " + name);
            temperature.setText("Temperature: " + tempCelsius + "°C");
            winds.setText("Winds: " + windSpeed + "m/s");
            humidity.setText("Humidity: " + humidityValue + "%");
            climate.setText("Climate: " + weatherMain);

            locationDisplay.setVisible(true);
            temperature.setVisible(true);
            winds.setVisible(true);
            humidity.setVisible(true);
            climate.setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WeatherApp().setVisible(true);
            }
        });
    }
}
