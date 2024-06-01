import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherApp {
    private static final String API_KEY = "2abc2b77f9a6cee0b011dc62f122440f";

    static Scanner io = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        try {
            WeatherAppSwing();
        } catch (Exception e) {

            System.out.println("Either Wrong City name was entered or the network connection error has occurred");
            main(new String[] { "" });
        }
    }

    static void WeatherAppSwing() throws Exception {
        System.out.println("Enter the name of the city --> ");
        String cityName = io.next();
        String apiCallString = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", cityName,
                API_KEY);

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
        BigDecimal temperature = ((BigDecimal) weatherAppResponseJSON.getJSONObject("main").get("temp"))
                .subtract(BigDecimal.valueOf(273.15));
        BigDecimal maxTemperature = ((BigDecimal) weatherAppResponseJSON.getJSONObject("main").get("temp_max"))
                .subtract(BigDecimal.valueOf(273.15));
        BigDecimal minTemprature = ((BigDecimal) weatherAppResponseJSON.getJSONObject("main").get("temp_min"))
                .subtract(BigDecimal.valueOf(273.15));
        Integer seaLevel = (Integer) weatherAppResponseJSON.getJSONObject("main").get("sea_level");
        Integer visibility = (Integer) weatherAppResponseJSON.get("visibility");
        BigDecimal windSpeed = (BigDecimal) weatherAppResponseJSON.getJSONObject("wind").get("speed");
        BigDecimal longitude = (BigDecimal) weatherAppResponseJSON.getJSONObject("coord").get("lon");
        BigDecimal latitude = (BigDecimal) weatherAppResponseJSON.getJSONObject("coord").get("lat");
        windSpeed = windSpeed.multiply(BigDecimal.valueOf(3.6));
        System.out.println("weather : " + weather);
        System.out.println("temp : " + temperature + " °C");
        System.out.println("max temp : " + maxTemperature + " °C");
        System.out.println("min temp : " + minTemprature + " °C");
        System.out.println("Sea Level : " + seaLevel);
        System.out.println("visibility : " + visibility + " feets");
        System.out.println("Wind Speed : " + windSpeed + " Km/S");
        System.out.println("longitude : " + longitude);
        System.out.println("latitude : " + latitude);

    }
}