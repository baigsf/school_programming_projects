import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class RandProductMaker extends JFrame
{
    private static final String FILE_NAME = "products.dat";

    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField idField;
    private JTextField costField;
    private JTextField recordCountField;

    private int recordCount;

    public RandProductMaker()
    {
        setTitle("Random Product Maker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        recordCount = getExistingRecordCount();

        createGui();
        updateRecordCountField();
    }

    private void createGui()
    {
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        nameField = new JTextField();
        descriptionField = new JTextField();
        idField = new JTextField();
        costField = new JTextField();
        recordCountField = new JTextField();
        recordCountField.setEditable(false);

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);

        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Cost:"));
        inputPanel.add(costField);

        inputPanel.add(new JLabel("Record #:"));
        inputPanel.add(recordCountField);

        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add");
        JButton clearButton = new JButton("Clear");
        JButton quitButton = new JButton("Quit");

        addButton.addActionListener(e -> addProduct());
        clearButton.addActionListener(e -> clearFields());
        quitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private int getExistingRecordCount()
    {
        File file = new File(FILE_NAME);

        if (!file.exists())
        {
            return 0;
        }

        return (int) (file.length() / Product.RECORD_SIZE);
    }

    private void updateRecordCountField()
    {
        recordCountField.setText(String.valueOf(recordCount + 1));
    }

    private void addProduct()
    {
        String name = nameField.getText().trim();
        String description = descriptionField.getText().trim();
        String id = idField.getText().trim();
        String costText = costField.getText().trim();

        if (name.isEmpty()
                || description.isEmpty()
                || id.isEmpty()
                || costText.isEmpty())
        {
            JOptionPane.showMessageDialog(
                    this,
                    "All fields are required."
            );
            return;
        }

        if (name.length() > Product.NAME_LENGTH)
        {
            JOptionPane.showMessageDialog(
                    this,
                    "Name must be " + Product.NAME_LENGTH
                            + " characters or less."
            );
            return;
        }

        if (description.length() > Product.DESCRIPTION_LENGTH)
        {
            JOptionPane.showMessageDialog(
                    this,
                    "Description must be " + Product.DESCRIPTION_LENGTH
                            + " characters or less."
            );
            return;
        }

        if (id.length() > Product.ID_LENGTH)
        {
            JOptionPane.showMessageDialog(
                    this,
                    "ID must be " + Product.ID_LENGTH
                            + " characters or less."
            );
            return;
        }

        double cost;

        try
        {
            cost = Double.parseDouble(costText);

            if (cost < 0)
            {
                JOptionPane.showMessageDialog(
                        this,
                        "Cost must be 0 or greater."
                );
                return;
            }
        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(
                    this,
                    "Cost must be a valid number."
            );
            return;
        }

        Product product = new Product(name, description, id, cost);

        try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw"))
        {
            file.seek(file.length());
            product.writeToFile(file);

            recordCount++;
            clearFields();
            updateRecordCountField();

            JOptionPane.showMessageDialog(
                    this,
                    "Product saved."
            );
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(
                    this,
                    "Error writing file: " + ex.getMessage()
            );
        }
    }

    private void clearFields()
    {
        nameField.setText("");
        descriptionField.setText("");
        idField.setText("");
        costField.setText("");
        nameField.requestFocus();
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new RandProductMaker().setVisible(true));
    }
}