import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class DictionaryApplication {

    public static void main(String[] args) {
        try {
            Scanner io = new Scanner(System.in);

            System.out.print("Enter the word -->");

            URL url = new URL("https://api.dictionaryapi.dev/api/v2/entries/en/" + io.next());
            HttpURLConnection dictionaryAppConnection = (HttpURLConnection) url.openConnection();
            dictionaryAppConnection.setRequestMethod("GET");
            int dictionaryAppResponse = dictionaryAppConnection.getResponseCode();
            BufferedReader b = new BufferedReader(new InputStreamReader(dictionaryAppConnection.getInputStream()));
            String line;
            String responseStringJson = "";
            while ((line = b.readLine()) != null)
                responseStringJson += line;
            //The returned JSON is in the form of single length array
            JSONArray jsonArray = new JSONArray(responseStringJson);

            //Getting the only Object form the array
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            JSONArray meaningsArray = jsonObject.getJSONArray("meanings");


            for (int i = 0; i < meaningsArray.length(); i++) {
                String partOfSpeech = (String) meaningsArray.getJSONObject(i).get("partOfSpeech");
                System.out.println("partOfSpeech : " + partOfSpeech);
                JSONArray definitionArray = meaningsArray.getJSONObject(i).getJSONArray("definitions");
                for (int j = 0; j < definitionArray.length(); j++) {
                    System.out.println(definitionArray.getJSONObject(j).get("definition"));
                    JSONArray synonymsArray = definitionArray.getJSONObject(j).optJSONArray("synonyms");
                    if (synonymsArray.length() != 0) {
                        System.out.println("Synonyms: " + synonymsArray);
                    }

                    JSONArray antonymsArray = definitionArray.getJSONObject(j).optJSONArray("antonyms");
                    if (antonymsArray.length() != 0) {
                        System.out.println("Antonyms: " + antonymsArray);
                    }
                }

            }
        } catch (Exception e) {
            System.out.println("Illegal Word Please reenter the word");
            main(new String[]{});
        }

    }
}
