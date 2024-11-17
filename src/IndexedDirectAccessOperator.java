import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class IndexedDirectAccessOperator implements Operateur {
    private String filePath;
    private int blockSize;
    private int memorySize;
    private int tupleSize;
    private int key;
    private Tuple[][] cache;
    private Queue<Integer> memoryQueue;
    private int reads;
    private int[] blockMap;
    private static final int HEADER_SIZE = 2;
    private boolean isOpen;
    private RandomAccessFile file;
    private HashTableIndex hashTableIndex;
    private List<Long> positions;
    private int currentPositionIndex;

    public IndexedDirectAccessOperator(String filePath, HashTableIndex hashTableIndex) {
        this(filePath, 4, 3, 4, -1 , hashTableIndex);
    }

    public IndexedDirectAccessOperator(String filePath, int key, HashTableIndex hashTableIndex) {
        this(filePath, 4, 3, 4, key, hashTableIndex);
    }

    public IndexedDirectAccessOperator(String filePath, int blockSize, int memorySize, int tupleSize, int key, HashTableIndex hashTableIndex) {
        this.filePath = filePath;
        this.blockSize = blockSize;
        this.memorySize = memorySize;
        this.tupleSize = tupleSize;
        this.key = key;
        this.cache = new Tuple[memorySize][blockSize];
        this.memoryQueue = new LinkedList<>();
        this.blockMap = new int[memorySize];
        this.reads = 0;
        this.isOpen = false;
        this.hashTableIndex = hashTableIndex;
        this.currentPositionIndex = 0;

        for (int i = 0; i < memorySize; i++) {
            blockMap[i] = -1;
        }

        setPositionsFromIndex(key);
    }

    @Override
    public void open() {
        try {
            file = new RandomAccessFile(filePath, "r");
            isOpen = true;
            currentPositionIndex = 0;
//            System.out.println("Operator opened.");
        } catch (IOException e) {
            System.out.println("Error opening file.");
            e.printStackTrace();
        }
    }

    public void setPositionsFromIndex(int key) {
        positions = hashTableIndex.getIndex().get(key);
        currentPositionIndex = 0;
        if (positions == null || positions.isEmpty()) {
            System.out.println("Key " + key + " not found in index.");
        } else {
            System.out.println("Positions for key " + key + ": " + positions);
        }
    }

    @Override
    public Tuple next() {
        if (!isOpen || positions == null || currentPositionIndex >= positions.size()) {
            return null; // End of positions or operator not open
        }

        long position = positions.get(currentPositionIndex++);
//        System.out.println("Fetching tuple at position: " + position);

        try {
            int blockIndex = (int) ((position - HEADER_SIZE) / (blockSize * tupleSize));

            if (blockMap[blockIndex % memorySize] != blockIndex) {
//                System.out.println("Block " + blockIndex + " not in cache. Loading...");
                loadBlock(file, blockIndex, tupleSize);
                reads++;
            }
//            } else {
//                System.out.println("Block " + blockIndex + " found in cache.");
//            }

            int offsetWithinBlock = (int) (((position - HEADER_SIZE) % (blockSize * tupleSize)) / tupleSize);
            Tuple tuple = cache[blockIndex % memorySize][offsetWithinBlock];

            return tuple;
        } catch (IOException e) {
            System.out.println("Error reading tuple.");
            e.printStackTrace();
            return null;
        }
    }

    private void loadBlock(RandomAccessFile file, int blockIndex, int tupleSize) throws IOException {
        long startPosition = HEADER_SIZE + blockIndex * blockSize * tupleSize;
        file.seek(startPosition);

        int memoryPosition = blockIndex % memorySize;
        for (int i = 0; i < blockSize; i++) {
            Tuple tuple = new Tuple(tupleSize);
            for (int j = 0; j < tupleSize; j++) {
                tuple.val[j] = file.read();
            }
            cache[memoryPosition][i] = tuple;
        }

        if (memoryQueue.size() >= memorySize) {
            int evictedBlock = memoryQueue.poll();
            blockMap[evictedBlock % memorySize] = -1;
//            System.out.println("Evicted block " + evictedBlock + " from cache.");
        }

        memoryQueue.add(blockIndex);
        blockMap[memoryPosition] = blockIndex;
    }

    @Override
    public void close() {
        try {
            if (file != null) {
                file.close();
            }
            isOpen = false;
//            System.out.println("Operator closed.");
        } catch (IOException e) {
            System.out.println("Error closing file.");
            e.printStackTrace();
        }
    }

    public int getReadsCount() {
        return reads;
    }
}
