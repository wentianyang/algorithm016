import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * 二叉堆 使用数组实现
 */
public class BinaryHeap {
    // 默认是二叉树，可以通过改变d变成多叉树
    private static final int d = 2;
    private int[] heap;
    private int heapSize;

    public BinaryHeap(int capacity) {
        heapSize = 0;
        heap = new int[capacity + 1];
        Arrays.fill(heap, -1);
    }

    public boolean isEmpty() {
        return heapSize == 0;
    }

    public boolean isFull() {
        return heapSize == heap.length;
    }

    /**
     * 求当前索引的父节点的索引
     * 
     * @param index 当前索引
     * @return 父节点的索引
     */
    private int parent(int index) {
        return (index - 1) / d;
    }

    private int kthChild(int index, int k) {
        return d * index + k;
    }

    /**
     * 堆的插入 1. 放入堆尾 2. 将插入的值从下往上进行调整，只要大于父节点，就和父节点进行交换
     * 
     * @param value
     */
    public void insert(int value) {
        if (isFull()) {
            throw new NoSuchElementException("Heap is full, No space to insert new element");
        }
        // 放入堆尾，然后将heapSize++
        heap[heapSize++] = value;
        heapifyUp(heapSize - 1);
    }

    /**
     * 向上和父节点比较，
     * 
     * @param index 堆尾索引
     */
    private void heapifyUp(int index) {
        int insertValue = heap[index];
        while (index > 0 && insertValue > heap[parent(index)]) {
            heap[index] = heap[parent(index)];
            index = parent(index);
        }
        heap[index] = insertValue;
    }

    /**
     * 堆的删除
     * 
     * @param x
     * @return
     */
    public int delete(int x) {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty, No element to delete");
        }
        int maxElement = heap[x];
        heap[x] = heap[heapSize - 1];
        heapSize--;
        heapifyDown(x);
        return maxElement;
    }

    private void heapifyDown(int index) {
        int child;
        int temp = heap[index];
        while (kthChild(index, 1) < heapSize) {
            child = maxChild(index);
            if (temp >= heap[child]) {
                break;
            }
            heap[index] = heap[child];
            index = child;
        }
        heap[index] = temp;
    }

    public void printHeap() {
        System.out.print("Heap = ");
        for (int i = 0; i < heapSize; i++) {
            System.out.print(heap[i] + " ");
        }
        System.out.println();
    }

    public int findMax() {
        if (isEmpty())
            throw new NoSuchElementException("Heap is empty");
        return heap[0];
    }

    /**
     * 获取左、右节点的最大节点
     * 
     * @param index
     * @return
     */
    private int maxChild(int index) {
        int leftChild = kthChild(index, 1);
        int rightChild = kthChild(index, 2);
        return heap[leftChild] > heap[rightChild] ? leftChild : rightChild;
    }
}