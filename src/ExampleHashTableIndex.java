
public class ExampleHashTableIndex {

    public static void main(String[] args) {
        String filePath = "D:/Study-Work/Java Avance/mini-projet SGBD/src/table1";
        HashTableIndex indexer = new HashTableIndex(filePath);

        indexer.buildIndex(0);
        indexer.displayIndex();

        indexer.lookup(4);
    }
}
