/*
 * SorterTest.java
 * DLSU Campus Map Finder - term project
 *
 * Tests for Sorter and ShowPlaces. Run this file from the project
 * folder. Every line should say PASS.
 */
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

public class SorterTest {

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
        System.out.println("--- Sorter ---");
        testSorter();
        System.out.println("--- ShowPlaces ---");
        testShowPlaces();
        System.out.println("--- map.txt ---");
        testMapFile();
        System.out.println();
        System.out.println(passed + " passed, " + failed + " failed");
    }

    private static void testSorter() {
        String[] names = {"Gym", "cafeteria", "Library", "Bar"};
        Sorter.insertionSort(names);
        check("names are sorted A to Z, ignoring case",
                same(names, "Bar", "cafeteria", "Gym", "Library"));

        String[] sorted = {"A", "B", "C"};
        Sorter.insertionSort(sorted);
        check("a sorted array stays the same", same(sorted, "A", "B", "C"));

        String[] reversed = {"D", "C", "B", "A"};
        Sorter.insertionSort(reversed);
        check("a reversed array gets sorted",
                same(reversed, "A", "B", "C", "D"));

        String[] twice = {"b", "a", "b", "a"};
        Sorter.insertionSort(twice);
        check("the same name twice is fine", same(twice, "a", "a", "b", "b"));

        String[] one = {"Solo"};
        Sorter.insertionSort(one);
        check("one name stays the same", same(one, "Solo"));

        String[] none = new String[0];
        Sorter.insertionSort(none);
        check("an empty array does not crash", none.length == 0);

        Sorter.insertionSort(null);
        check("null does not crash", true);

        String[] many = new String[200];
        for (int i = 0; i < many.length; i++) {
            many[i] = "Place" + (i * 37 % 200);
        }
        Sorter.insertionSort(many);
        check("200 scrambled names get sorted", isSorted(many));
    }

    private static void testShowPlaces() {
        PlaceList list = new PlaceList();
        list.add("Library");
        list.add("cafeteria");
        list.add("Gym");

        String text = capture(list);
        check("it prints the title with the number of places",
                text.contains("Places from A to Z (3):"));
        check("1st line is cafeteria", text.contains("1. cafeteria"));
        check("2nd line is Gym", text.contains("2. Gym"));
        check("3rd line is Library", text.contains("3. Library"));
        check("the original order is not changed",
                list.get(0).equals("Library")
                        && list.get(1).equals("cafeteria")
                        && list.get(2).equals("Gym"));

        check("an empty list prints a message",
                capture(new PlaceList()).contains("no places"));
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
        PrintStream original = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        boolean loaded;
        try {
            loaded = MapLoader.load("map.txt", places, graph);
        } finally {
            System.setOut(original);
        }
        check("map.txt loads", loaded);

        String[] names = places.toArray();
        Sorter.insertionSort(names);
        check("all places in map.txt are sorted A to Z", isSorted(names));
        check("no place is lost by the sort",
                names.length == places.size());
    }

    // Run ShowPlaces.show and return the text it printed.
    private static String capture(PlaceList list) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
        try {
            ShowPlaces.show(list);
        } finally {
            System.setOut(original);
        }
        return buffer.toString();
    }

    // True if the array has exactly these names, in this order.
    private static boolean same(String[] actual, String... expected) {
        if (actual.length != expected.length) {
            return false;
        }
        for (int i = 0; i < actual.length; i++) {
            if (!actual[i].equals(expected[i])) {
                return false;
            }
        }
        return true;
    }

    // True if every name is not bigger than the next one (A to Z).
    private static boolean isSorted(String[] names) {
        for (int i = 0; i + 1 < names.length; i++) {
            if (names[i].compareToIgnoreCase(names[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }
}
