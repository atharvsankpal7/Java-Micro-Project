import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import org.json.*;

public class CatApp extends JFrame {
    private static final String CAT_API = "https://api.thecatapi.com/v1/images/search";
    private JButton changeBtn;
    private JLabel catImg;
    private String imageUrl;

    public CatApp() {
        setTitle("Random Cat Image");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        changeBtn = new JButton("Change Image");
        catImg = new JLabel();

        add(catImg);
        add(changeBtn);

        apiCall();

        catImg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openLinkInBackground(imageUrl);
            }
        });

        changeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeBtn.setEnabled(false);
                apiCall();
            }
        });
    }

    private void apiCall() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(CAT_API);
                    Scanner scanner = new Scanner(url.openStream());
                    StringBuilder responseBuilder = new StringBuilder();

                    while (scanner.hasNext()) {
                        responseBuilder.append(scanner.nextLine());
                    }

                    scanner.close();
                    String response = responseBuilder.toString();
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    imageUrl = jsonObject.getString("url");
                    ImageIcon imageIcon = new ImageIcon(new URL(imageUrl));
                    catImg.setIcon(imageIcon);

                    changeBtn.setEnabled(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void openLinkInBackground(String url) {
        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CatApp().setVisible(true);
            }
        });
    }
}
