import java.util.LinkedList;
import java.util.Queue;

/**
 *
 *
 * The traversal algorithms assumes a tree in Levelorder as input,
 * and returns tree in: inOrder, preOrder OR postOrder.
 * No algorithm for Levelorder, since that is the base case.
 * Rearranging to LevelOrder is made in Controller class by resetting
 * the structure.
 *
 * Conforming to the rest of the project:
 *  - The algorithms ignores index 0, making index 1 the root node for a Levelorder structure.
 *  - The algorithms ignores node value -1, which indicates excluded value (placed last and handled as excluded by Controller class)
 * */

public class Traversal {
    private int[] tree;
    private int originalSize;
    private Queue<Integer> traversedTree;

    public int[] inOrder(int[] tree) {
        setUp(tree);
        inOrder(1);
        return returnTree();
    }

    private void inOrder(int node) {
        if(isNull(node)) {
            return;
        }
        inOrder(leftChildPos(node));     // L
        traversedTree.offer(tree[node]); // N
        inOrder(rightChildPos(node));    // R
    }

    public int[] preOrder(int[] tree) {
        setUp(tree);
        preOrder(1);
        return returnTree();
    }

    private void preOrder(int node) {
        if(isNull(node)) {
            return;
        }
        traversedTree.offer(tree[node]); // N
        preOrder(leftChildPos(node));    // L
        preOrder(rightChildPos(node));   // R
    }

    public int[] postOrder(int[] tree) {
        setUp(tree);
        postOrder(1);
        return returnTree();
    }

    private void postOrder(int node) {
        if(isNull(node)) {
            return;
        }
        postOrder(leftChildPos(node));   // L
        postOrder(rightChildPos(node));  // R
        traversedTree.offer(tree[node]); // N
    }


    // ===================================== UTILITY METHODS ==========================================================

    private int leftChildPos(int pos) {
        return pos * 2;
    }

    private int rightChildPos(int pos) {
        return pos * 2 + 1;
    }

    private boolean isNull(int pos) {
        try {
            int tmp = tree[pos];
        } catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
        return false;
    }


    /**
     * @param originalTree gets rebuilt to int[] tree, where node values -1 other
     *                    than index 0 is excluded.
     */
    private void setUp(int[] originalTree) {
        traversedTree = new LinkedList<>();
        traversedTree.offer(-1); // Representing index 0, to be ignored
        // Build tree by excluding node values -1 (except on index 0)
        originalSize = originalTree.length;
        Queue<Integer> tmpQ = new LinkedList<>();
        tmpQ.offer(-1); // Representing index 0, to be ignored
        for(int index = 0 ; index < originalTree.length ; index++) {
            if((index != 0) && (originalTree[index] != -1)) {
                tmpQ.offer(originalTree[index]);
            }
        }
        tree = new int[tmpQ.size()];
        for(int index = 0; index < tree.length ; index++) {
            tree[index] = tmpQ.poll();
        }
    }

    private int[] returnTree() {
        //Rebuilding tree according to traversal scheme, and filling up to original size.
        tree = new int[originalSize];
        for(int index = 0 ; index < originalSize ; index++) {
            if (traversedTree.isEmpty()) {
                tree[index] = -1;
            } else {
                tree[index] = traversedTree.poll();
            }
        }
        return tree;
    }

}