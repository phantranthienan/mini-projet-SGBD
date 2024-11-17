import java.util.HashSet;
import java.util.Set;

public class DistinctOperator implements Operateur {
    private TableDisque tableDisque;
    private int keyColumn; // The column index to find distinct values for
    private Set<Integer> distinctValues;
    private boolean initialized;

    public DistinctOperator(TableDisque tableDisque, int keyColumn) {
        this.tableDisque = tableDisque;
        this.keyColumn = keyColumn;
        this.distinctValues = new HashSet<>();
        this.initialized = false;
    }

    @Override
    public void open() {
        tableDisque.open();
        distinctValues.clear();
        initialized = true;
        System.out.println("Opened DistinctOperator for column " + keyColumn);
    }

    @Override
    public Tuple next() {
        if (!initialized) {
            return null; // Operator not properly initialized
        }

        Tuple tuple;
        while ((tuple = tableDisque.next()) != null) {
            int value = tuple.val[keyColumn];
            if (distinctValues.add(value)) {
                System.out.println("New distinct value found in column " + keyColumn + ": " + value);
                Tuple result = new Tuple(1);
                result.val[0] = value;
                return result;
            } else {
                System.out.println("Duplicate value skipped in column " + keyColumn + ": " + value);
            }
        }

        System.out.println("No more distinct values available for column " + keyColumn);
        return null; // No more distinct values available
    }

    @Override
    public void close() {
        tableDisque.close();
        System.out.println("Closed DistinctOperator for column " + keyColumn);
    }
}
