import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class HTMLSearch extends JFrame implements ActionListener {

    private JTextField urlField;
    private JTextField searchTermField;
    private JTextArea resultArea;
    private JButton searchButton;
    private JPanel inputPanel;
    private JScrollPane scrollPane;
    private JFrame mainFrame;

    public HTMLSearch() {
        mainFrame = new JFrame();

        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        JLabel urlLabel = new JLabel("Enter URL:");
        urlField = new JTextField();
        JLabel searchTermLabel = new JLabel("Enter Search Term:");
        searchTermField = new JTextField();

        searchButton = new JButton("Search");
        searchButton.addActionListener(this);

        inputPanel.add(urlLabel);
        inputPanel.add(urlField);
        inputPanel.add(searchTermLabel);
        inputPanel.add(searchTermField);
        inputPanel.add(new JLabel());
        inputPanel.add(searchButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        scrollPane = new JScrollPane(resultArea);
        mainFrame.setTitle("HTML Search");
        mainFrame.setSize(600, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        mainFrame.add(inputPanel, BorderLayout.NORTH);
        mainFrame.add(scrollPane, BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String url = urlField.getText();
        String searchTerm = searchTermField.getText().toLowerCase();
        resultArea.setText("");

        Set<String> ulink = new HashSet<>();

        try {
            URL urlObj = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlObj.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.toLowerCase();
                if (line.contains("<a") && line.contains(searchTerm)) {
                    int n = line.indexOf("href=\"");
                    if (n != -1) {
                        int startIndex = n + 6;
                        int endIndex = line.indexOf("\"", startIndex);
                        if (endIndex > startIndex) {
                            String link = line.substring(startIndex, endIndex);
                            if (!link.contains("#") && ulink.add(link)) {
                                resultArea.append(link + "\n");
                            }
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException ex) {
            resultArea.setText("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        HTMLSearch frame = new HTMLSearch();

    }
}