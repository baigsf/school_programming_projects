import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.*;

public class DataStreams extends JFrame {
    private JTextField searchField;
    private JTextArea originalTextArea;
    private JTextArea filteredTextArea;
    private JButton loadButton;
    private JButton searchButton;
    private JButton quitButton;
    private Path currentFilePath;

    public DataStreams() {
        setTitle("Java Data Stream Processing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search String:"));
        searchField = new JTextField(30);
        searchPanel.add(searchField);
        topPanel.add(searchPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        loadButton = new JButton("Load File");
        searchButton = new JButton("Search");
        quitButton = new JButton("Quit");

        searchButton.setEnabled(false);

        buttonPanel.add(loadButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(quitButton);
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        JPanel textAreaPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        textAreaPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        originalTextArea = new JTextArea();
        originalTextArea.setEditable(false);
        originalTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane originalScrollPane = new JScrollPane(originalTextArea);
        originalScrollPane.setBorder(BorderFactory.createTitledBorder("Original File"));

        filteredTextArea = new JTextArea();
        filteredTextArea.setEditable(false);
        filteredTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane filteredScrollPane = new JScrollPane(filteredTextArea);
        filteredScrollPane.setBorder(BorderFactory.createTitledBorder("Filtered Results"));

        textAreaPanel.add(originalScrollPane);
        textAreaPanel.add(filteredScrollPane);

        add(textAreaPanel, BorderLayout.CENTER);

        loadButton.addActionListener(e -> loadFile());
        searchButton.addActionListener(e -> searchFile());
        quitButton.addActionListener(e -> System.exit(0));
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "text", "log");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            currentFilePath = selectedFile.toPath();

            try {
                String content = new String(Files.readAllBytes(currentFilePath));
                originalTextArea.setText(content);
                filteredTextArea.setText("");
                searchButton.setEnabled(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchFile() {
        if (currentFilePath == null) {
            JOptionPane.showMessageDialog(this, "Please load a file first.",
                    "No File Loaded", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String searchString = searchField.getText().trim();

        if (searchString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search string.",
                    "Empty Search", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String searchLower = searchString.toLowerCase();
            
            try (Stream<String> lines = Files.lines(currentFilePath)) {
                List<String> matchingLines = lines
                    .filter(line -> line.toLowerCase().contains(searchLower))
                    .collect(Collectors.toList());

                StringBuilder result = new StringBuilder();
                for (String line : matchingLines) {
                    result.append(line).append("\n");
                }

                filteredTextArea.setText(result.toString());
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error processing file: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new DataStreams().setVisible(true);
        });
    }
}