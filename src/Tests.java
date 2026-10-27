/*
 * Tests.java
 * DLSU Campus Map Finder - term project
 *
 * Small tests for our own data structures and algorithms.
 * Run this file. Every line should say PASS.
 * We add new tests here every week.
 */
public class Tests {

    private static int passed = 0;
    private static int failed = 0;

    // Print PASS or FAIL for one check, and count it.
    private static void check(String label, boolean ok) {
        if (ok) {
            passed++;
            System.out.println("PASS  " + label);
        } else {
            failed++;
            System.out.println("FAIL  " + label);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- PlaceList ---");
        testPlaceList();
        System.out.println("--- IntQueue ---");
        testIntQueue();
        System.out.println();
        System.out.println(passed + " passed, " + failed + " failed");
    }

    private static void testPlaceList() {
        PlaceList list = new PlaceList();
        check("new list is empty", list.size() == 0);

        list.add("Library");
        list.add("Cafeteria");
        list.add("Gym");
        check("size is 3 after 3 adds", list.size() == 3);
        check("get(0) is Library", list.get(0).equals("Library"));
        check("get(2) is Gym", list.get(2).equals("Gym"));
        check("get(3) is null", list.get(3) == null);
        check("get(-1) is null", list.get(-1) == null);

        check("search finds Cafeteria at 1",
                list.linearSearch("Cafeteria") == 1);
        check("search ignores upper/lower case",
                list.linearSearch("gYm") == 2);
        check("search does not find Pool", list.linearSearch("Pool") == -1);
        check("search with null gives -1", list.linearSearch(null) == -1);

        String[] copy = list.toArray();
        check("copy has 3 names", copy.length == 3);
        copy[0] = "Changed";
        check("changing the copy does not change the list",
                list.get(0).equals("Library"));

        PlaceList big = new PlaceList();
        for (int i = 0; i < 25; i++) {
            big.add("Place" + i);
        }
        check("list grows past 10 names", big.size() == 25);
        check("first name is kept after growing",
                big.get(0).equals("Place0"));
        check("last name is correct after growing",
                big.get(24).equals("Place24"));
        check("search works after growing",
                big.linearSearch("place24") == 24);
    }

    private static void testIntQueue() {
        IntQueue q = new IntQueue();
        check("new queue is empty", q.isEmpty());
        check("new queue has size 0", q.size() == 0);

        q.enqueue(10);
        q.enqueue(20);
        q.enqueue(30);
        check("size is 3 after 3 enqueues", q.size() == 3);
        check("queue is not empty now", !q.isEmpty());
        check("first out is 10", q.dequeue() == 10);
        check("second out is 20", q.dequeue() == 20);
        check("third out is 30", q.dequeue() == 30);
        check("queue is empty again", q.isEmpty());

        // Wrap around: use 8 slots, free 6, then add 6 more.
        // The new numbers go past the end and back to index 0.
        IntQueue wrap = new IntQueue();
        for (int i = 1; i <= 8; i++) {
            wrap.enqueue(i);
        }
        for (int i = 1; i <= 6; i++) {
            wrap.dequeue();
        }
        for (int i = 9; i <= 14; i++) {
            wrap.enqueue(i);
        }
        check("order is kept when the queue wraps around",
                outInOrder(wrap, 7, 14));

        // Grow while the numbers are wrapped around the array.
        IntQueue grow = new IntQueue();
        for (int i = 1; i <= 10; i++) {
            grow.enqueue(i);
        }
        for (int i = 1; i <= 3; i++) {
            grow.dequeue();
        }
        for (int i = 11; i <= 14; i++) {
            grow.enqueue(i);
        }
        check("order is kept when a wrapped queue grows",
                outInOrder(grow, 4, 14));

        IntQueue many = new IntQueue();
        for (int i = 1; i <= 1000; i++) {
            many.enqueue(i);
        }
        check("1000 numbers come out in order", outInOrder(many, 1, 1000));

        boolean threw = false;
        try {
            new IntQueue().dequeue();
        } catch (IllegalStateException e) {
            threw = true;
        }
        check("dequeue on an empty queue gives an error", threw);
    }

    // Take every number out. True if they are low, low+1, ... high
    // and the queue is empty at the end.
    private static boolean outInOrder(IntQueue q, int low, int high) {
        for (int expected = low; expected <= high; expected++) {
            if (q.isEmpty() || q.dequeue() != expected) {
                return false;
            }
        }
        return q.isEmpty();
    }
}
