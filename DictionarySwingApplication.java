import org.json.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class DictionarySwingApplication extends JFrame {
    private JTextField inputField;
    private JPanel resultPanel;

    DictionarySwingApplication() {
        try {
            // Set Nimbus look and feel (Dark theme)
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("Dictionary Application");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1500, 1000);

        inputField = new JTextField(10);
        inputField.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        JButton searchButton = new JButton("Search");

        // When search button is clicked
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchDictionary();
            }
        });
        // When enter is pressed
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchDictionary();
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 128, 128));
        JLabel enterLabel = new JLabel("Enter the word -->");
        enterLabel.setForeground(Color.WHITE);
        enterLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        topPanel.add(enterLabel);
        topPanel.add(inputField);
        topPanel.add(searchButton);

        resultPanel = new JPanel();
        resultPanel.setBackground(new Color(0, 128, 128));
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBorder(new EtchedBorder(1, Color.BLACK, Color.CYAN));

        JScrollPane scrollPane = new JScrollPane(resultPanel);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

    }

    private void searchDictionary() {
        resultPanel.removeAll(); // Clear the previous results
        try {
            String word = inputField.getText();
            URL url = new URL("https://api.dictionaryapi.dev/api/v2/entries/en/" + word);
            HttpURLConnection dictionaryAppConnection = (HttpURLConnection) url.openConnection();
            dictionaryAppConnection.setRequestMethod("GET");

            BufferedReader b = new BufferedReader(new InputStreamReader(dictionaryAppConnection.getInputStream()));
            String line;
            String responseStringJson = "";
            // Converting the byte stream into string
            while ((line = b.readLine()) != null) responseStringJson += line;

            // The returned JSON is in the form of a single-length array
            JSONArray jsonArray = new JSONArray(responseStringJson);
            // Getting the only Object form the array
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            JSONArray meaningsArray = jsonObject.getJSONArray("meanings");
            for (int i = 0; i < meaningsArray.length(); i++) {
                JSONObject meaningObject = meaningsArray.getJSONObject(i);
                String partOfSpeech = meaningObject.getString("partOfSpeech");

                JLabel partOfSpeechPanelLabel = new JLabel("Part of Speech : " + partOfSpeech);
                partOfSpeechPanelLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
                // Create a new panel for each part of speech
                JPanel partOfSpeechPanel = new JPanel();
                partOfSpeechPanel.setBackground(new Color(0, 128, 128));
                partOfSpeechPanel.setBorder(BorderFactory.createTitledBorder(""));
                partOfSpeechPanel.setLayout(new BoxLayout(partOfSpeechPanel, BoxLayout.Y_AXIS));
                partOfSpeechPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                partOfSpeechPanel.add(partOfSpeechPanelLabel);
                JSONArray definitionArray = meaningObject.getJSONArray("definitions");
                for (int j = 0; j < definitionArray.length(); j++) {
                    JSONObject definitionObject = definitionArray.getJSONObject(j);
                    String definition = definitionObject.getString("definition");

                    // Create a section for definitions
                    JLabel definitionLabel = new JLabel("Definition: " + definition);
                    definitionLabel.setForeground(Color.WHITE);
                    definitionLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                    partOfSpeechPanel.add(definitionLabel);

                    JSONArray synonymsArray = definitionObject.optJSONArray("synonyms");
                    if (synonymsArray != null && synonymsArray.length() != 0) {
                        // Create a section for synonyms
                        JLabel synonymsLabel = new JLabel("Synonyms: " + synonymsArray);
                        synonymsLabel.setForeground(Color.WHITE);
                        synonymsLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                        partOfSpeechPanel.add(synonymsLabel);
                    }

                    JSONArray antonymsArray = definitionObject.optJSONArray("antonyms");
                    if (antonymsArray != null && antonymsArray.length() != 0) {
                        // Create a section for antonyms
                        JLabel antonymsLabel = new JLabel("Antonyms: " + antonymsArray);
                        antonymsLabel.setForeground(Color.WHITE);
                        antonymsLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                        partOfSpeechPanel.add(antonymsLabel);
                    }
                }

                // Add the part of speech panel to the main result panel
                resultPanel.add(partOfSpeechPanel);
            }

            repaint();
            revalidate();
        } catch (Exception e) {
            resultPanel.removeAll();
            JLabel errorMessageLabel = new JLabel("Invalid Word. Please Reenter the word");
            errorMessageLabel.setForeground(Color.WHITE);
            errorMessageLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
            resultPanel.add(errorMessageLabel);
            inputField.setText("");
            resultPanel.revalidate();
            resultPanel.repaint();
        }
    }


    public static void main(String[] args) {
        DictionarySwingApplication app = new DictionarySwingApplication();
        app.setVisible(true);
    }
}
