import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class DictionaryApp {
    private static String dictionaryURL_String = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    private static Scanner io = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            System.out.print("Enter the word that you want to search --> ");
            dictionaryURL_String += io.next();

            URL url = new URL(dictionaryURL_String);

            HttpURLConnection dictionaryAppConnection = (HttpURLConnection) url.openConnection();
            dictionaryAppConnection.setRequestMethod("GET");

            int dictionaryResponseCode = dictionaryAppConnection.getResponseCode();

            System.out.println(dictionaryResponseCode);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dictionaryAppConnection.getInputStream()));
            String line;
            StringBuilder dictionaryResponse = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                dictionaryResponse.append(line);
            }
            System.out.println(dictionaryResponse);
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
