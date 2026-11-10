/*
 * PathFinder.java
 * DLSU Campus Map Finder - term project
 *
 * Breadth-first search (BFS), written by hand. It finds the path with
 * the fewest stops between two places.
 *
 * How BFS works:
 *   1. Put the start place in a queue and mark it as visited.
 *   2. Take the first place out of the queue.
 *   3. Put all its neighbors that are not visited yet in the queue,
 *      mark them as visited, and remember where we came from.
 *   4. Repeat from step 2 until we reach the goal or the queue is empty.
 * The queue makes us visit the places 1 stop away first, then 2 stops
 * away, and so on. So the first time we reach the goal, we got there
 * with the fewest stops.
 */
public class PathFinder {

    /*
     * Find the shortest path from start to goal. Both are place numbers.
     * Returns the place numbers on the path, in order, including start
     * and goal. Returns null if there is no path, or if a number is not
     * a place in the graph.
     */
    public static int[] findPath(Graph graph, int start, int goal) {
        int count = graph.placeCount();
        if (start < 0 || start >= count || goal < 0 || goal >= count) {
            return null;
        }

        boolean[] visited = new boolean[count];
        int[] cameFrom = new int[count]; // the place before this one
        for (int i = 0; i < count; i++) {
            cameFrom[i] = -1;
        }

        IntQueue queue = new IntQueue();
        visited[start] = true;
        queue.enqueue(start);

        while (!queue.isEmpty() && !visited[goal]) {
            int current = queue.dequeue();
            for (Graph.Neighbor n = graph.firstNeighbor(current);
                 n != null; n = n.next) {
                if (!visited[n.place]) {
                    visited[n.place] = true;
                    cameFrom[n.place] = current;
                    queue.enqueue(n.place);
                }
            }
        }

        if (!visited[goal]) {
            return null;
        }
        return buildPath(cameFrom, start, goal);
    }

    /*
     * Find the shortest path and print it, or print that there is no
     * path. Example:
     *   Path: Library -> Cafeteria -> Gym
     *   Number of stops: 2
     */
    public static void showPath(PlaceList places, Graph graph,
                                int start, int goal) {
        int[] path = findPath(graph, start, goal);
        if (path == null) {
            System.out.println("There is no path between "
                    + places.get(start) + " and " + places.get(goal) + ".");
            return;
        }

        System.out.print("Path: ");
        for (int i = 0; i < path.length; i++) {
            System.out.print(places.get(path[i]));
            if (i < path.length - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();

        // A stop is one road we walk. Library -> Cafeteria -> Gym is
        // 2 stops. The start place is not counted.
        int stops = path.length - 1;
        System.out.println("Number of stops: " + stops);
    }

    // Walk back from the goal to the start using cameFrom, and put the
    // places in a new array in the right order (start first).
    private static int[] buildPath(int[] cameFrom, int start, int goal) {
        int length = 1;
        for (int p = goal; p != start; p = cameFrom[p]) {
            length++;
        }

        int[] path = new int[length];
        int place = goal;
        for (int i = length - 1; i >= 0; i--) {
            path[i] = place;
            place = cameFrom[place];
        }
        return path;
    }
}