/*
 * PathFinderTest.java
 * DLSU Campus Map Finder - term project
 *
 * Tests for PathFinder (BFS). Run this file from the project folder.
 * Every line should say PASS.
 */
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

public class PathFinderTest {

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
        System.out.println("--- findPath ---");
        testFindPath();
        System.out.println("--- showPath ---");
        testShowPath();
        System.out.println("--- all pairs ---");
        testAllPairs();
        System.out.println("--- map.txt ---");
        testMapFile();
        System.out.println();
        System.out.println(passed + " passed, " + failed + " failed");
    }

    private static void testFindPath() {
        // A chain: 0 - 1 - 2 - 3
        Graph chain = makeGraph(4, new int[][]{{0, 1}, {1, 2}, {2, 3}});
        check("path along a chain",
                same(PathFinder.findPath(chain, 0, 3), 0, 1, 2, 3));
        check("path along a chain, the other way",
                same(PathFinder.findPath(chain, 3, 0), 3, 2, 1, 0));
        check("start and goal are the same place",
                same(PathFinder.findPath(chain, 2, 2), 2));
        check("start and goal next to each other",
                same(PathFinder.findPath(chain, 1, 2), 1, 2));

        // A long way (0-1-2-3) and a shortcut (0-4-3).
        Graph shortcut = makeGraph(5, new int[][]{
                {0, 1}, {1, 2}, {2, 3}, {0, 4}, {4, 3}});
        check("BFS takes the shortcut with fewer stops",
                same(PathFinder.findPath(shortcut, 0, 3), 0, 4, 3));

        // A square: 0 - 1 - 2 - 3 - 0
        Graph square = makeGraph(4, new int[][]{
                {0, 1}, {1, 2}, {2, 3}, {3, 0}});
        int[] across = PathFinder.findPath(square, 0, 2);
        check("a path in a loop has 2 stops",
                across != null && across.length == 3);

        // Two separate groups: 0 - 1 and 2 - 3
        Graph split = makeGraph(4, new int[][]{{0, 1}, {2, 3}});
        check("no path between two separate groups",
                PathFinder.findPath(split, 0, 3) == null);
        check("a path inside one group still works",
                same(PathFinder.findPath(split, 2, 3), 2, 3));

        check("a negative place number gives null",
                PathFinder.findPath(chain, -1, 2) == null);
        check("a place number that is too big gives null",
                PathFinder.findPath(chain, 0, 9) == null);

        Graph big = new Graph();
        for (int i = 0; i < 30; i++) {
            big.addPlace();
        }
        for (int i = 0; i < 29; i++) {
            big.addRoad(i, i + 1);
        }
        int[] longPath = PathFinder.findPath(big, 0, 29);
        check("a chain of 30 places has a path of 30 places",
                longPath != null && longPath.length == 30);
    }

    private static void testShowPath() {
        PlaceList places = new PlaceList();
        places.add("Library");   // 0
        places.add("Cafeteria"); // 1
        places.add("Gym");       // 2
        places.add("Pool");      // 3
        places.add("Court");     // 4
        Graph graph = makeGraph(5, new int[][]{{0, 1}, {1, 2}, {3, 4}});

        String text = capture(places, graph, 0, 2);
        check("it prints the path with arrows",
                text.contains("Path: Library -> Cafeteria -> Gym"));
        check("it prints the number of stops",
                text.contains("Number of stops: 2"));

        text = capture(places, graph, 0, 1);
        check("1 road is 1 stop", text.contains("Number of stops: 1"));

        text = capture(places, graph, 1, 1);
        check("the same place is 0 stops",
                text.contains("Path: Cafeteria")
                        && text.contains("Number of stops: 0"));

        text = capture(places, graph, 0, 3);
        check("it prints a message when there is no path",
                text.contains("There is no path between Library and Pool."));
        check("it does not print a path when there is none",
                !text.contains("Path:"));
    }

    // For every pair of places, compare BFS with a second method
    // (Floyd-Warshall) that finds all shortest distances.
    private static void testAllPairs() {
        Graph graph = makeGraph(12, new int[][]{
                {0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 0},
                {1, 6}, {6, 7}, {7, 3}, {2, 8}, {8, 9}, {9, 10},
                {0, 10}});
        // Place 11 has no roads on purpose.
        compareWithFloyd(graph, "12 places: every pair is a shortest path");
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
        compareWithFloyd(graph, "map.txt: every pair is a shortest path");
    }

    private static void compareWithFloyd(Graph graph, String label) {
        int n = graph.placeCount();
        int far = 1000000;
        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    dist[i][j] = 0;
                } else if (graph.hasRoad(i, j)) {
                    dist[i][j] = 1;
                } else {
                    dist[i][j] = far;
                }
            }
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }}
            }
        }

        boolean allOk = true;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int[] path = PathFinder.findPath(graph, i, j);
                if (dist[i][j] >= far) {
                    if (path != null) {
                        allOk = false;
                    }
                } else if (path == null
                        || path.length - 1 != dist[i][j]
                        || path[0] != i
                        || path[path.length - 1] != j
                        || !hasAllRoads(graph, path)) {
                    allOk = false;
                }
            }
        }
        check(label, allOk);
    }

    // True if there is a road between every two places next to each
    // other on the path.
    private static boolean hasAllRoads(Graph graph, int[] path) {
        for (int i = 0; i + 1 < path.length; i++) {
            if (!graph.hasRoad(path[i], path[i + 1])) {
                return false;
            }
        }
        return true;
    }

    // Make a graph with some places and roads. Each road is {a, b}.
    private static Graph makeGraph(int placeCount, int[][] roads) {
        Graph graph = new Graph();
        for (int i = 0; i < placeCount; i++) {
            graph.addPlace();
        }
        for (int i = 0; i < roads.length; i++) {
            graph.addRoad(roads[i][0], roads[i][1]);
        }
        return graph;
    }

    // Run PathFinder.showPath and return the text it printed.
    private static String capture(PlaceList places, Graph graph,
                                  int start, int goal) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
        try {
            PathFinder.showPath(places, graph, start, goal);
        } finally {
            System.setOut(original);
        }
        return buffer.toString();
    }

    // True if the path has exactly these place numbers, in this order.
    private static boolean same(int[] path, int... expected) {
        if (path == null || path.length != expected.length) {
            return false;
        }
        for (int i = 0; i < path.length; i++) {
            if (path[i] != expected[i]) {
                return false;
            }
        }
        return true;
    }
}