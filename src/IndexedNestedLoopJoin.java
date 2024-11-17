import java.util.List;

public class IndexedNestedLoopJoin implements Operateur {
    private Operateur op1; // Outer table
    private IndexedDirectAccessOperator op2; // Inner table with index access
    private int col1; // Join column for the outer table
    private int col2; // Join column for the inner table
    private Tuple t1; // Current tuple from the outer table
    private boolean newOuterTuple; // Flag to indicate fetching new outer tuple
    private HashTableIndex hashTableIndex; // Index for the inner table

    public IndexedNestedLoopJoin(Operateur op1, IndexedDirectAccessOperator op2, HashTableIndex hashTableIndex, int col1, int col2) {
        this.op1 = op1;
        this.op2 = op2;
        this.col1 = col1;
        this.col2 = col2;
        this.hashTableIndex = hashTableIndex;
        this.newOuterTuple = true;
    }

    @Override
    public void open() {
        op1.open();
        op2.open();
        t1 = null;
        System.out.println("IndexedNestedLoopJoin opened.");
    }

    @Override
    public Tuple next() {
        while (true) {
            if (newOuterTuple) {
                t1 = op1.next();
                if (t1 == null) {
                    return null; // End of outer table
                }
                int key = t1.val[col1];
                op2.setPositionsFromIndex(key);
                newOuterTuple = false; // Start searching for matches in the inner table
            }

            Tuple t2;
            while ((t2 = op2.next()) != null) {
                if (t1.val[col1] == t2.val[col2]) {
                    Tuple result = new Tuple(t1.val.length + t2.val.length);
                    for (int i = 0; i < t1.val.length; i++) {
                        result.val[i] = t1.val[i];
                    }
                    for (int i = 0; i < t2.val.length; i++) {
                        result.val[i + t2.val.length] = t2.val[i];
                    }
                    return result;
                }
            }
            newOuterTuple = true; // Move to the next outer tuple
        }
    }

    @Override
    public void close() {
        op1.close();
        op2.close();
        System.out.println("IndexedNestedLoopJoin closed.");
    }
}
