public class ExampleIndexedNestedLoopJoin {
    public static void main(String[] args) {
        String filePathOuter = "D:/Study-Work/Java Avance/mini-projet SGBD/src/table1";
        String filePathInner = "D:/Study-Work/Java Avance/mini-projet SGBD/src/table2";

        HashTableIndex hashTableIndex = new HashTableIndex(filePathInner);
        hashTableIndex.buildIndex(0); // Build index on the first column (index 0)

        TableDisque outerTable = new TableDisque();
        outerTable.setFilePath(filePathOuter);

        IndexedDirectAccessOperator innerTableOperator = new IndexedDirectAccessOperator(filePathInner,  hashTableIndex);

        IndexedNestedLoopJoin joinOperator = new IndexedNestedLoopJoin(outerTable, innerTableOperator, hashTableIndex, 0, 0);
        joinOperator.open();

        System.out.println("Join Results ****");
        Tuple t;
        while ((t = joinOperator.next()) != null) {
            System.out.println(t);
        }
        joinOperator.close();
    }
}
