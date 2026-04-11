import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class RandProductSearch extends JFrame
{
    private static final String FILE_NAME = "products.dat";

    private JTextField searchField;
    private JTextArea resultsArea;

    public RandProductSearch()
    {
        setTitle("Random Product Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        createGui();
    }

    private void createGui()
    {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        searchField = new JTextField();

        JButton searchButton = new JButton("Search");
        JButton quitButton = new JButton("Quit");

        searchButton.addActionListener(e -> searchProducts());
        quitButton.addActionListener(e -> System.exit(0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(searchButton);
        buttonPanel.add(quitButton);

        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        resultsArea = new JTextArea();
        resultsArea.setEditable(false);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultsArea), BorderLayout.CENTER);
    }

    private void searchProducts()
    {
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.isEmpty())
        {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter part of a product name."
            );
            return;
        }

        File dataFile = new File(FILE_NAME);

        if (!dataFile.exists())
        {
            JOptionPane.showMessageDialog(
                    this,
                    "Data file not found. Run RandProductMaker first."
            );
            return;
        }

        StringBuilder output = new StringBuilder();
        int matches = 0;

        try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "r"))
        {
            long totalRecords = file.length() / Product.RECORD_SIZE;

            for (int i = 0; i < totalRecords; i++)
            {
                Product product = Product.readFromFile(file);

                if (product.getName().toLowerCase().contains(searchText))
                {
                    matches++;
                    output.append("Match ")
                            .append(matches)
                            .append("\n")
                            .append(product)
                            .append("\n\n");
                }
            }

            if (matches == 0)
            {
                resultsArea.setText("No matches found.");
            }
            else
            {
                resultsArea.setText(output.toString());
            }
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(
                    this,
                    "Error reading file: " + ex.getMessage()
            );
        }
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(
                () -> new RandProductSearch().setVisible(true)
        );
    }
}