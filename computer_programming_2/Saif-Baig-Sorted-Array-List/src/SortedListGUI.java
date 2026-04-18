import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SortedListGUI extends JFrame {
    private SortedList sortedList;
    private JTextField inputField;
    private JTextField searchField;
    private JTextArea displayArea;
    private JButton addButton;
    private JButton searchButton;
    private JButton clearButton;
    
    public SortedListGUI() {
        sortedList = new SortedList();
        
        setTitle("Sorted List - Binary Search");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.add(new JLabel("Enter string to add:"), BorderLayout.NORTH);
        inputField = new JTextField();
        addButton = new JButton("Add to List");
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);
        
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.add(new JLabel("Enter string to search:"), BorderLayout.NORTH);
        searchField = new JTextField();
        searchButton = new JButton("Search");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        clearButton = new JButton("Clear All");
        buttonPanel.add(clearButton);
        
        displayArea = new JTextArea(20, 40);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        
        add(topPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
        
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addElement();
            }
        });
        
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addElement();
            }
        });
        
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchElement();
            }
        });
        
        searchField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchElement();
            }
        });
        
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortedList = new SortedList();
                displayArea.setText("");
                inputField.setText("");
                searchField.setText("");
            }
        });
        
        displayArea.append("=== Sorted List Demo with Binary Search ===\n");
        displayArea.append("Enter strings to add them in sorted order\n\n");
    }
    
    private void addElement() {
        String element = inputField.getText().trim();
        if (!element.isEmpty()) {
            boolean added = sortedList.add(element);
            if (added) {
                displayArea.append("ADDED: \"" + element + "\"\n");
                displayArea.append("Current list size: " + sortedList.size() + "\n");
                displayArea.append("Full list: " + sortedList.getAllElements() + "\n\n");
            } else {
                displayArea.append("DUPLICATE: \"" + element + "\" already exists\n\n");
            }
            inputField.setText("");
            inputField.requestFocus();
        }
    }
    
    private void searchElement() {
        String element = searchField.getText().trim();
        if (!element.isEmpty()) {
            String result = sortedList.search(element);
            displayArea.append("SEARCH: \"" + element + "\"\n");
            displayArea.append(result + "\n\n");
            searchField.setText("");
            searchField.requestFocus();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SortedListGUI().setVisible(true);
            }
        });
    }
}