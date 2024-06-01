
//AI generated (CLI to GUI)

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAppSwing extends JFrame implements ActionListener {
    private static final String API_KEY = "2abc2b77f9a6cee0b011dc62f122440f";
    private JLabel cityLabel, weatherLabel, tempLabel, maxTempLabel, minTempLabel,
            seaLevelLabel, visibilityLabel, windSpeedLabel, longitudeLabel, latitudeLabel;
    private JTextField cityTextField;
    private JButton searchButton;

    public WeatherAppSwing() {
        setTitle("Weather App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(10, 2, 10, 10));
        setPreferredSize(new Dimension(400, 400));

        cityLabel = new JLabel("City Name:");
        cityTextField = new JTextField();
        weatherLabel = new JLabel("Weather:");
        tempLabel = new JLabel("Temperature:");
        maxTempLabel = new JLabel("Max Temperature:");
        minTempLabel = new JLabel("Min Temperature:");
        seaLevelLabel = new JLabel("Sea Level:");
        visibilityLabel = new JLabel("Visibility:");
        windSpeedLabel = new JLabel("Wind Speed:");
        longitudeLabel = new JLabel("Longitude:");
        latitudeLabel = new JLabel("Latitude:");

        searchButton = new JButton("Search");
        searchButton.addActionListener(this);

        add(cityLabel);
        add(cityTextField);
        add(searchButton);
        add(new JLabel());
        add(weatherLabel);
        add(new JLabel());
        add(tempLabel);
        add(new JLabel());
        add(maxTempLabel);
        add(new JLabel());
        add(minTempLabel);
        add(new JLabel());
        add(seaLevelLabel);
        add(new JLabel());
        add(visibilityLabel);
        add(new JLabel());
        add(windSpeedLabel);
        add(new JLabel());
        add(longitudeLabel);
        add(new JLabel());
        add(latitudeLabel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            try {
                fetchWeatherData(cityTextField.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Either wrong city name was entered or a network connection error occurred.");
            }
        }
    }

    private void fetchWeatherData(String cityName) throws Exception {
        String apiCallString = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", cityName, API_KEY);
        URL url = new URL(apiCallString);
        HttpURLConnection weatherAppConnection = (HttpURLConnection) url.openConnection();
        weatherAppConnection.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(weatherAppConnection.getInputStream()));
        String line;
        String weatherAppResponse = "";
        while ((line = br.readLine()) != null) {
            weatherAppResponse += line;
        }

        JSONObject weatherAppResponseJSON = new JSONObject(weatherAppResponse);

        String weather = (String) weatherAppResponseJSON.getJSONArray("weather").getJSONObject(0).get("main");
        BigDecimal temperature = ((BigDecimal) weatherAppResponseJSON.getJSONObject("main").get("temp")).subtract(BigDecimal.valueOf(273.15));
        BigDecimal maxTemperature = ((BigDecimal) weatherAppResponseJSON.getJSONObject("main").get("temp_max")).subtract(BigDecimal.valueOf(273.15));
        BigDecimal minTemperature = ((BigDecimal) weatherAppResponseJSON.getJSONObject("main").get("temp_min")).subtract(BigDecimal.valueOf(273.15));
        Integer seaLevel = (Integer) weatherAppResponseJSON.getJSONObject("main").get("sea_level");
        Integer visibility = (Integer) weatherAppResponseJSON.get("visibility");
        BigDecimal windSpeed = (BigDecimal) weatherAppResponseJSON.getJSONObject("wind").get("speed");
        BigDecimal longitude = (BigDecimal) weatherAppResponseJSON.getJSONObject("coord").get("lon");
        BigDecimal latitude = (BigDecimal) weatherAppResponseJSON.getJSONObject("coord").get("lat");
        windSpeed = windSpeed.multiply(BigDecimal.valueOf(3.6));

        weatherLabel.setText("Weather: " + weather);
        tempLabel.setText("Temperature: " + temperature + " °C");
        maxTempLabel.setText("Max Temperature: " + maxTemperature + " °C");
        minTempLabel.setText("Min Temperature: " + minTemperature + " °C");
        seaLevelLabel.setText("Sea Level: " + seaLevel);
        visibilityLabel.setText("Visibility: " + visibility + " feet");
        windSpeedLabel.setText("Wind Speed: " + windSpeed + " Km/h");
        longitudeLabel.setText("Longitude: " + longitude);
        latitudeLabel.setText("Latitude: " + latitude);
    }
}
