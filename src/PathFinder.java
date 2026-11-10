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