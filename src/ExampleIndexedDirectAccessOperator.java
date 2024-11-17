public class ExampleIndexedDirectAccessOperator {
    public static void main(String[] args) {
        String filePath = "D:/Study-Work/Java Avance/mini-projet SGBD/src/table1";

        HashTableIndex hashTableIndex = new HashTableIndex(filePath);
        hashTableIndex.buildIndex(0);

        IndexedDirectAccessOperator operator = new IndexedDirectAccessOperator(filePath, 3, hashTableIndex);
        operator.open();

        System.out.println("Tuples:");
        Tuple t;
        while ((t = operator.next()) != null) {
            System.out.println(java.util.Arrays.toString(t.getVal()));
        }
        System.out.println("Total disk reads: " + operator.getReadsCount());
        operator.close();
    }
}
