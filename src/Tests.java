/*
 * Tests.java
 * DLSU Campus Map Finder - term project
 *
 * Small tests for our own data structures and algorithms.
 * Run this file from the project folder. Every line should say PASS.
 * We add new tests here every week.
 */
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class Tests {

    private static int passed = 0;
    private static int failed = 0;

    // Text that MapLoader printed during the last quietLoad call.
    private static String lastOutput = "";

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

    public static void main(String[] args) throws IOException {
        System.out.println("--- PlaceList ---");
        testPlaceList();
        System.out.println("--- IntQueue ---");
        testIntQueue();
        System.out.println("--- Graph ---");
        testGraph();
        System.out.println("--- MapLoader ---");
        testMapLoader();
        System.out.println("--- map.txt ---");
        testMapFile();
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

    private static void testGraph() {
        Graph g = new Graph();
        check("new graph has 0 places", g.placeCount() == 0);
        check("new graph has 0 roads", g.roadCount() == 0);

        int a = g.addPlace();
        int b = g.addPlace();
        int c = g.addPlace();
        check("place numbers start at 0 and count up",
                a == 0 && b == 1 && c == 2);
        check("graph has 3 places", g.placeCount() == 3);
        check("a place with no roads has no neighbor",
                g.firstNeighbor(a) == null);

        check("addRoad is true for a new road", g.addRoad(a, b));
        check("road goes from a to b", g.hasRoad(a, b));
        check("road also goes from b to a", g.hasRoad(b, a));
        check("no road between a and c", !g.hasRoad(a, c));
        check("graph has 1 road", g.roadCount() == 1);

        check("the same road again is refused", !g.addRoad(a, b));
        check("the same road reversed is refused", !g.addRoad(b, a));
        check("road count stays 1", g.roadCount() == 1);
        check("a road from a place to itself is refused", !g.addRoad(a, a));
        check("a road to a place that does not exist is refused",
                !g.addRoad(a, 7));
        check("a negative place number is refused", !g.addRoad(-1, b));
        check("hasRoad is false for a bad number", !g.hasRoad(0, 99));

        g.addRoad(a, c);
        Graph.Neighbor first = g.firstNeighbor(a);
        check("neighbors stay in the order the roads were added",
                first.place == b && first.next.place == c
                        && first.next.next == null);
        check("firstNeighbor of a bad number is null",
                g.firstNeighbor(9) == null);

        Graph big = new Graph();
        for (int i = 0; i < 25; i++) {
            big.addPlace();
        }
        for (int i = 0; i < 24; i++) {
            big.addRoad(i, i + 1);
        }
        check("graph grows past 10 places", big.placeCount() == 25);
        check("24 roads in a chain", big.roadCount() == 24);
        check("road at the end of a big graph",
                big.hasRoad(23, 24) && big.hasRoad(24, 23));
        check("no road between far places", !big.hasRoad(0, 24));
    }

    private static void testMapLoader() throws IOException {
        PlaceList places = new PlaceList();
        Graph graph = new Graph();
        String file = writeTemp(
                "Library Cafeteria\nCafeteria Gym\nLibrary Gym\n");
        check("a good file loads", quietLoad(file, places, graph));
        check("a good file prints no warning", lastOutput.isEmpty());
        check("3 places are loaded", places.size() == 3);
        check("3 roads are loaded", graph.roadCount() == 3);
        check("places are numbered in the order they appear",
                places.get(0).equals("Library")
                        && places.get(1).equals("Cafeteria")
                        && places.get(2).equals("Gym"));
        check("roads go both ways",
                graph.hasRoad(0, 1) && graph.hasRoad(1, 0));

        places = new PlaceList();
        graph = new Graph();
        file = writeTemp("Library Cafeteria\nlibrary GYM\n");
        quietLoad(file, places, graph);
        check("same name in a different case is the same place",
                places.size() == 3);
        check("the first spelling is kept", places.get(2).equals("GYM"));

        places = new PlaceList();
        graph = new Graph();
        file = writeTemp("# my map\n\n  Library \t Cafeteria  \n\n"
                + "#another note\r\nGym Library\r\n");
        check("comments, blank lines and extra spaces are fine",
                quietLoad(file, places, graph));
        check("no warning for comments and blank lines",
                lastOutput.isEmpty());
        check("2 roads from the messy file", graph.roadCount() == 2);
        check("3 places from the messy file", places.size() == 3);

        places = new PlaceList();
        graph = new Graph();
        file = writeTemp("OnlyOne\nLibrary Cafeteria\nA B C\n"
                + "Gym Gym\nCafeteria Library\n");
        check("a file with some bad lines still loads",
                quietLoad(file, places, graph));
        check("warning for a line with 1 name",
                lastOutput.contains("line 1 skipped"));
        check("warning for a line with 3 names",
                lastOutput.contains("line 3 skipped"));
        check("warning for a road from a place to itself",
                lastOutput.contains("line 4 skipped"));
        check("warning for a repeated road",
                lastOutput.contains("line 5 skipped"));
        check("only the good line is used",
                places.size() == 2 && graph.roadCount() == 1);

        places = new PlaceList();
        graph = new Graph();
        file = writeTemp("\uFEFFLibrary Cafeteria\n");
        quietLoad(file, places, graph);
        check("an invisible mark at the file start is ignored",
                places.linearSearch("Library") == 0);

        places = new PlaceList();
        graph = new Graph();
        check("a missing file gives false",
                !quietLoad("no-such-file-12345.txt", places, graph));
        check("a missing file prints a 'not found' message",
                lastOutput.contains("was not found"));

        places = new PlaceList();
        graph = new Graph();
        check("an empty file gives false",
                !quietLoad(writeTemp(""), places, graph));
        check("an empty file prints a message",
                lastOutput.contains("no roads"));

        places = new PlaceList();
        graph = new Graph();
        check("a file with only bad lines gives false",
                !quietLoad(writeTemp("Hello\nA B C\n"), places, graph));

        places = new PlaceList();
        graph = new Graph();
        check("a null file name gives false",
                !quietLoad(null, places, graph));
        check("a null file name prints a message",
                lastOutput.contains("no file name"));

        places = new PlaceList();
        places.add("Old");
        graph = new Graph();
        file = writeTemp("Library Cafeteria\n");
        check("a list that is not empty is refused",
                !quietLoad(file, places, graph));
        check("the refusal prints a message", lastOutput.contains("empty"));
    }

    // Check our real map file. Run the tests from the project folder.
    private static void testMapFile() {
        if (!new File("map.txt").exists()) {
            System.out.println("SKIP  map.txt not found here"
                    + " (run the tests from the project folder)");
            return;
        }
        PlaceList places = new PlaceList();
        Graph graph = new Graph();
        check("map.txt loads", quietLoad("map.txt", places, graph));
        check("map.txt has no bad lines", lastOutput.isEmpty());
        check("map.txt has at least 10 places", places.size() >= 10);
        check("map.txt has at least 15 roads", graph.roadCount() >= 15);
    }

    // Load a map but keep the printed text in lastOutput, so the tests
    // can check the messages without filling the screen.
    private static boolean quietLoad(String file, PlaceList places,
                                     Graph graph) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
        boolean ok;
        try {
            ok = MapLoader.load(file, places, graph);
        } finally {
            System.setOut(original);
        }
        lastOutput = buffer.toString();
        return ok;
    }

    // Write text into a new temporary file. Returns the file path.
    private static String writeTemp(String text) throws IOException {
        File file = File.createTempFile("mapTest", ".txt");
        file.deleteOnExit();
        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8)) {
            writer.write(text);
        }
        return file.getPath();
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
