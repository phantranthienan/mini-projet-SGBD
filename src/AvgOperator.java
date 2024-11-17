public class AvgOperator implements Operateur {
    private TableDisque tableDisque;
    private int keyColumn; // The column index to calculate the average for
    private int sum; // Sum of the values in the selected column
    private int count; // Number of tuples processed
    private boolean calculated;

    public AvgOperator(TableDisque tableDisque, int keyColumn) {
        this.tableDisque = tableDisque;
        this.keyColumn = keyColumn;
        this.sum = 0;
        this.count = 0;
        this.calculated = false;
    }

    @Override
    public void open() {
        tableDisque.open();
        sum = 0;
        count = 0;
        calculated = false;
        System.out.println("Opened AvgOperator for column " + keyColumn);
    }

    @Override
    public Tuple next() {
        if (calculated) {
            return null;
        }

        Tuple tuple;
        // Iterate through all tuples to calculate the sum and count for the selected column
        while ((tuple = tableDisque.next()) != null) {
            int value = tuple.val[keyColumn];
            sum += value;
            count++;
            System.out.println("Processing value " + value + " from column " + keyColumn);
        }

        // Print final sum and count for debugging
        System.out.println("Final sum for column " + keyColumn + ": " + sum);
        System.out.println("Total number of elements in column " + keyColumn + ": " + count);

        calculated = true;

        // If there are tuples, calculate the average and return it as a tuple
        if (count > 0) {
            double average = (double) sum / count; // Calculate the average as a double
            Tuple result = new Tuple(1);
            result.val[0] = (int) average; // Store the integer part in the tuple (adjust if needed)
            System.out.printf("Calculated average for column %d: %.2f%n", keyColumn, average);
            return result;
        } else {
            System.out.println("No data available to calculate the average for column " + keyColumn);
            return null;
        }
    }

    @Override
    public void close() {
        tableDisque.close(); // Close the underlying table
        System.out.println("Closed AvgOperator for column " + keyColumn);
    }
}
