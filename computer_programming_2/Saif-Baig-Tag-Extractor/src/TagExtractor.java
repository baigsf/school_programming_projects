import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.*;

public class TagExtractor extends JFrame {
    private JLabel fileLabel;
    private JTextArea resultTextArea;
    private JButton selectFileButton;
    private JButton selectStopWordsButton;
    private JButton saveButton;
    private String currentFileName;
    private String stopWordsFileName;
    private Map<String, Integer> tagMap;
    private Set<String> stopWordsSet;

    public TagExtractor() {
        setTitle("Tag/Keyword Extractor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLayout(new BorderLayout(10, 10));

        tagMap = new HashMap<>();
        stopWordsSet = new HashSet<>();
        currentFileName = "";
        stopWordsFileName = "";

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        fileLabel = new JLabel("No file selected");
        fileLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(fileLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        selectFileButton = new JButton("Select Text File");
        selectStopWordsButton = new JButton("Select Stop Words File");
        saveButton = new JButton("Save Tags to File");
        saveButton.setEnabled(false);

        buttonPanel.add(selectFileButton);
        buttonPanel.add(selectStopWordsButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        add(scrollPane, BorderLayout.CENTER);

        selectFileButton.addActionListener(e -> selectTextFile());
        selectStopWordsButton.addActionListener(e -> selectStopWordsFile());
        saveButton.addActionListener(e -> saveTagsToFile());

        setLocationRelativeTo(null);
    }

    private void selectTextFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Select Text File to Extract Tags From");

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            currentFileName = selectedFile.getName();
            fileLabel.setText("Processing: " + currentFileName);

            if (stopWordsSet.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a stop words file first!");
                return;
            }

            processFile(selectedFile);
        }
    }

    private void selectStopWordsFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Select Stop Words File");

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            stopWordsFileName = selectedFile.getName();
            loadStopWords(selectedFile);
            JOptionPane.showMessageDialog(this, "Stop words loaded: " + stopWordsSet.size() + " words");
        }
    }

    private void loadStopWords(File file) {
        stopWordsSet.clear();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim().toLowerCase();
                if (!word.isEmpty()) {
                    stopWordsSet.add(word);
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error loading stop words file: " + e.getMessage());
        }
    }

    private void processFile(File file) {
        tagMap.clear();
        
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String word = scanner.next();
                word = word.toLowerCase();
                word = word.replaceAll("[^a-z]", "");
                
                if (!word.isEmpty() && !stopWordsSet.contains(word)) {
                    tagMap.put(word, tagMap.getOrDefault(word, 0) + 1);
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error processing file: " + e.getMessage());
            return;
        }

        displayTags();
        saveButton.setEnabled(!tagMap.isEmpty());
    }

    private void displayTags() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tags extracted from: ").append(currentFileName).append("\n");
        sb.append("Stop words file: ").append(stopWordsFileName).append("\n");
        sb.append("Total unique tags: ").append(tagMap.size()).append("\n");
        sb.append("=".repeat(50)).append("\n\n");

        TreeMap<String, Integer> sortedMap = new TreeMap<>(tagMap);
        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
            sb.append(String.format("%-20s : %d\n", entry.getKey(), entry.getValue()));
        }

        resultTextArea.setText(sb.toString());
    }

    private void saveTagsToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Tags to File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            try (PrintWriter writer = new PrintWriter(selectedFile)) {
                TreeMap<String, Integer> sortedMap = new TreeMap<>(tagMap);
                for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
                    writer.println(entry.getKey() + " : " + entry.getValue());
                }
                JOptionPane.showMessageDialog(this, "Tags saved successfully!");
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TagExtractor extractor = new TagExtractor();
            extractor.setVisible(true);
        });
    }
}