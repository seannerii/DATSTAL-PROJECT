/*
 * IntQueue.java
 * DLSU Campus Map Finder - term project
 *
 * A queue of int numbers, written by hand. First in, first out.
 * It is a circular array: when the end of the array is reached,
 * new numbers go back to the start (index 0) if there is room.
 * BFS (Week 3) uses this queue to hold place numbers.
 */
public class IntQueue {

    private int[] items; // holds the numbers
    private int front;   // position of the oldest number
    private int size;    // how many numbers are in the queue now

    // Start with room for 10 numbers. The array grows when it is full.
    public IntQueue() {
        items = new int[10];
        front = 0;
        size = 0;
    }

    // Put a number at the back of the line.
    public void enqueue(int value) {
        if (size == items.length) {
            grow();
        }
        int back = (front + size) % items.length;
        items[back] = value;
        size++;
    }

    // Take the number at the front of the line and return it.
    // Always check isEmpty() first. An empty queue throws an error.
    public int dequeue() {
        if (size == 0) {
            throw new IllegalStateException("Queue is empty");
        }
        int value = items[front];
        front = (front + 1) % items.length;
        size--;
        return value;
    }

    // True when there is nothing in the queue.
    public boolean isEmpty() {
        return size == 0;
    }

    // How many numbers are in the queue.
    public int size() {
        return size;
    }

    // Make the array twice as big. The numbers are copied in order,
    // so the oldest number is at position 0 again.
    private void grow() {
        int[] bigger = new int[items.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = items[(front + i) % items.length];
        }
        items = bigger;
        front = 0;
    }
}
