import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class HashTableIndex {
    private HashMap<Integer, List<Long>> index;
    private String filePath;

    public HashTableIndex(String filePath) {
        this.filePath = filePath;
        this.index = new HashMap<>();
    }

    public void buildIndex(int keyColumn) {
        try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
            int taille = file.read(); // Number of tuples
            int tupleSize = file.read(); // Size of each tuple
            long position = file.getFilePointer(); // Start position after the header

            for (int i = 0; i < taille; i++) {
                file.seek(position);
                Tuple tuple = new Tuple(tupleSize);
                for (int j = 0; j < tupleSize; j++) {
                    tuple.val[j] = file.read();
                }

                System.out.println("Read tuple: " + java.util.Arrays.toString(tuple.val) + " at position: " + position);

                int key = tuple.val[keyColumn];
                index.putIfAbsent(key, new ArrayList<>());
                index.get(key).add(position);

                position = file.getFilePointer();
            }
        } catch (IOException e) {
            System.out.println("Error reading the file or building the index.");
            e.printStackTrace();
        }
    }

    public void displayIndex() {
        for (Integer key : index.keySet()) {
            System.out.println("Key: " + key + " -> Positions: " + index.get(key));
        }
    }

    public void lookup(int key) {
        if (index.containsKey(key)) {
            System.out.println("Key " + key + " found at positions: " + index.get(key));
        } else {
            System.out.println("Key " + key + " not found in the index.");
        }
    }

    public HashMap<Integer, List<Long>> getIndex() {
        return index;
    }
}
