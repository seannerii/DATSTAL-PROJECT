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