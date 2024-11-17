public class ExampleAvgOperator {
    public static void main(String[] args) {
        String filePath = "D:/Study-Work/Java Avance/mini-projet SGBD/src/table1";
        TableDisque tableDisque = new TableDisque();
        tableDisque.setFilePath(filePath);

        System.out.println("Calculating average:");
        AvgOperator avgOperator = new AvgOperator(tableDisque, 1);
        avgOperator.open();
        Tuple avgResult = avgOperator.next();
        if (avgResult != null) {
            System.out.println("Average: " + avgResult);
        } else {
            System.out.println("No data to calculate average.");
        }
        avgOperator.close();

        // Using DistinctOperator
        System.out.println("\nFetching distinct tuples:");
        DistinctOperator distinctOperator = new DistinctOperator(tableDisque, 1);
        distinctOperator.open();
        Tuple distinctTuple;
        while ((distinctTuple = distinctOperator.next()) != null) {
            System.out.println("Distinct Tuple: " + distinctTuple);
        }
        distinctOperator.close();
    }
}

