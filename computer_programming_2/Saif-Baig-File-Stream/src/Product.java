import java.io.IOException;
import java.io.RandomAccessFile;

public class Product
{
    public static final int NAME_LENGTH = 35;
    public static final int DESCRIPTION_LENGTH = 75;
    public static final int ID_LENGTH = 6;

    public static final int RECORD_SIZE =
            (NAME_LENGTH + DESCRIPTION_LENGTH + ID_LENGTH)
                    * Character.BYTES
                    + Double.BYTES;

    private String name;
    private String description;
    private String id;
    private double cost;

    public Product(String name, String description, String id, double cost)
    {
        this.name = name;
        this.description = description;
        this.id = id;
        this.cost = cost;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getId()
    {
        return id;
    }

    public double getCost()
    {
        return cost;
    }

    public void writeToFile(RandomAccessFile file) throws IOException
    {
        file.writeChars(fixLength(name, NAME_LENGTH));
        file.writeChars(fixLength(description, DESCRIPTION_LENGTH));
        file.writeChars(fixLength(id, ID_LENGTH));
        file.writeDouble(cost);
    }

    public static Product readFromFile(RandomAccessFile file) throws IOException
    {
        String name = readFixedString(file, NAME_LENGTH).trim();
        String description = readFixedString(file, DESCRIPTION_LENGTH).trim();
        String id = readFixedString(file, ID_LENGTH).trim();
        double cost = file.readDouble();

        return new Product(name, description, id, cost);
    }

    private static String readFixedString(
            RandomAccessFile file,
            int length
    ) throws IOException
    {
        StringBuilder buffer = new StringBuilder(length);

        for (int i = 0; i < length; i++)
        {
            buffer.append(file.readChar());
        }

        return buffer.toString();
    }

    private static String fixLength(String value, int length)
    {
        if (value == null)
        {
            value = "";
        }

        StringBuilder buffer = new StringBuilder(value);

        if (buffer.length() > length)
        {
            return buffer.substring(0, length);
        }

        while (buffer.length() < length)
        {
            buffer.append(' ');
        }

        return buffer.toString();
    }

    @Override
    public String toString()
    {
        return "Name: "
                + name
                + "\nDescription: "
                + description
                + "\nID: "
                + id
                + "\nCost: $"
                + String.format("%.2f", cost);
    }
}