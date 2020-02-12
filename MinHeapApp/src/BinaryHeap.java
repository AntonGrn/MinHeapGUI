/**
 Inspiration from:
 @author Min Heap in Java https://www.geeksforgeeks.org/min-heap-in-java/
 @author Understand How to Implement a Binary Heap in Java https://www.edureka.co/blog/binary-heap-in-java/
 @author (Book) Data Structures and Algorithm Analysis in Java

 Modified by Anton Göransson

 - The algorithms ignores index 0, making index 1 the root node for a Levelorder structure.
 - Calculates average run time of building a heap.
 - Accomodates for invalid values (-1), to manage heaps with uneven leaves, returning a valid heap.
   Eg:
           A
         /   \
        B     C
      /  \   /  \
     D    E F    -

 */

public class BinaryHeap {

    private int[] inputArray;
    private int[] heap;
    private int validItems[];
    private int heapSize;
    private int nbrOfRepetitions; // for calculating average run time
    private double[] runTimes; // stores run times of each repetition
    public double avgRuntime;

    public BinaryHeap(int[] inputArray) {
        this.inputArray = inputArray;
        nbrOfRepetitions = 10;
        runTimes = new double[nbrOfRepetitions];
    }

    // ================================ BUILD HEAP: O(NLogN) ====================================================

    public int[] buildHeap_NlogN() {
        long start;
        long end;
        for(int r = 0 ; r < nbrOfRepetitions ; r++) {
            setup(inputArray);
            start = System.nanoTime();
            //--------- START -------------
            for (int value : validItems) {
                insert(value); // insert validItems successive into empty heap (create hole & percolate up)
            }
            //--------- STOP -------------
            end = System.nanoTime();
            runTimes[r] = ((double)end - start) / 1000000;
        }
        calculateAverageRuntime();
        return returnHeap();
    }

    private void insert(int value) {
        heap[++heapSize] = value; //Insert value in new "hole", starting at index 1 rather than 0 (ignoring index 0)
        percolateUp(heapSize);
    }

    private void percolateUp(int index) {
        int tmp = heap[index]; //Value to percolate up (newly inserted)
        while (index > 1 && tmp < heap[parentPos(index)]) { //while not at root, AND parent is larger; keep percolating up
            heap[index] = heap[parentPos(index)]; // Move parent down (percolating the "hole" up)
            index = parentPos(index); // Move one step up for next round.
        }
        heap[index] = tmp; // Wherever the hole now is; insert the value
    }

    // ================================= BUILD HEAP: O(N) =======================================================

    public int[] buildHeap_N() {
        long start;
        long end;
        for(int r = 0 ; r < nbrOfRepetitions ; r++) {
            setup(inputArray);
            start = System.nanoTime();
            //--------- START -------------
            heapSize = heap.length;
            //Arbitrary filling up the heap with all valid values
            for (int i = 1; i < heapSize; i++) {
                heap[i] = validItems[i - 1];
            }

            // Only percolate down from parent nodes. Start at lowest sub trees and work up towards root node
            for (int parent = heapSize / 2; parent > 0; parent--) {
                percolateDown(parent);
            }
            //--------- STOP -------------
            end = System.nanoTime();
            runTimes[r] = ((double)end - start) / 1000000;
        }
        calculateAverageRuntime();
        return returnHeap();
    }

    private void percolateDown(int index) {
        if (!isLeaf(index)) {
            int minChildPos = minChildPos(index);
            if (heap[minChildPos] <= heap[index]) {
                int tmp = heap[minChildPos];
                heap[minChildPos] = heap[index];
                heap[index] = tmp;
                // Continue percolate down until: On a leaf, OR children larger than parent
                percolateDown(minChildPos);
            }
        }
    }

    // ===================================== UTILITY METHODS ==========================================================

    private int parentPos(int pos) {
        return pos / 2;
    }

    private int leftChildPos(int pos) {
        return pos * 2;
    }

    private int rightChildPos(int pos) {
        return pos * 2 + 1;
    }

    private boolean hasLeftChild(int pos) {
        try {
            int tmp = heap[leftChildPos(pos)];
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    private boolean hasRightChild(int pos) {
        try {
            int tmp = heap[rightChildPos(pos)];
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    private boolean isLeaf(int pos) {
        return !(hasLeftChild(pos) || hasRightChild(pos));
    }

    private int minChildPos(int pos) {
        int minPos;
        if (hasLeftChild(pos) && hasRightChild(pos)) {
            minPos = (heap[leftChildPos(pos)] < heap[rightChildPos(pos)] ? leftChildPos(pos) : rightChildPos(pos));
        } else if (hasLeftChild(pos)) {
            minPos = leftChildPos(pos);
        } else {
            minPos = rightChildPos(pos);
        }
        return minPos;
    }

    // Build input tree (array) of valid values (exclude value: -1, except for at index 0)
    private void setup(int[] inputArray) {
        // Accommodate for invalid values: -1
        this.inputArray = inputArray;
        int validItems = 0;
        int count = 0;
        for (int i : inputArray) {
            if (i != -1) {
                validItems++;
            }
        }
        this.validItems = new int[validItems];
        heap = new int[validItems + 1]; //to ignore index 0
        for (int i : inputArray) {
            if (i != -1) {
                this.validItems[count++] = i;
            }
        }
        heap[0] = -1;
        heapSize = 0; //Start with empty heap for NLogN Build
    }

    private void calculateAverageRuntime() {
        avgRuntime = 0;
        for(double time : runTimes) {
            avgRuntime += time;
        }
        avgRuntime = avgRuntime / nbrOfRepetitions;
    }

    private int[] returnHeap() { // Return expected heap size (inputSize +1). Fill unused positions with -1
        int reBuild[] = new int[inputArray.length + 1];
        int count = 0;
        for (int value : heap) {
            reBuild[count++] = value;
        }
        for (int i = count; i < reBuild.length; i++) {
            reBuild[i] = -1;
        }
        return reBuild;
    }

}
