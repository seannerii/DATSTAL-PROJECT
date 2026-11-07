public class Graph {

    // One item in a neighbor list: a place that has a road to us.
    public static class Neighbor {
        public final int place; // number of the neighbor place
        public Neighbor next;   // next neighbor in the list, or null

        Neighbor(int place) {
            this.place = place;
            this.next = null;
        }
    }

    private Neighbor[] heads; // heads[i] is the first neighbor of place i
    private int placeCount;   // how many places are in the graph
    private int roadCount;    // how many roads (a two-way road counts 1)

    // Start with room for 10 places. The array grows when it is full.
    public Graph() {
        heads = new Neighbor[10];
        placeCount = 0;
        roadCount = 0;
    }

    // Add a new place with no roads yet. Returns the new place number.
    public int addPlace() {
        if (placeCount == heads.length) {
            grow();
        }
        heads[placeCount] = null;
        placeCount++;
        return placeCount - 1;
    }

    /*
     * Add a road between place a and place b. It goes both ways.
     * Returns false and changes nothing if a number is not a place,
     * if a and b are the same place, or if the road is already there.
     */
    public boolean addRoad(int a, int b) {
        if (!isPlace(a) || !isPlace(b) || a == b || hasRoad(a, b)) {
            return false;
        }
        append(a, b);
        append(b, a);
        roadCount++;
        return true;
    }

    // True if there is a road between a and b.
    public boolean hasRoad(int a, int b) {
        if (!isPlace(a) || !isPlace(b)) {
            return false;
        }
        for (Neighbor n = heads[a]; n != null; n = n.next) {
            if (n.place == b) {
                return true;
            }
        }
        return false;
    }

    /*
     * The first neighbor of a place, or null if it has no roads.
     * Walk through all neighbors with next, like this:
     *   for (Graph.Neighbor n = g.firstNeighbor(p); n != null;
     *        n = n.next) { ... n.place ... }
     */
    public Neighbor firstNeighbor(int place) {
        if (!isPlace(place)) {
            return null;
        }
        return heads[place];
    }

    // How many places are in the graph.
    public int placeCount() {
        return placeCount;
    }

    // How many roads are in the graph (a two-way road counts once).
    public int roadCount() {
        return roadCount;
    }

    // Print every place and its neighbors, to check the graph is right.
    public void print(PlaceList places) {
        for (int i = 0; i < placeCount; i++) {
            System.out.print(places.get(i) + " -> ");
            for (Neighbor n = heads[i]; n != null; n = n.next) {
                System.out.print(places.get(n.place));
                if (n.next != null) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }

    // True if the number is the number of a place in this graph.
    private boolean isPlace(int number) {
        return number >= 0 && number < placeCount;
    }

    // Add "to" at the END of the neighbor list of "from".
    // The neighbors stay in the order the roads were added.
    private void append(int from, int to) {
        Neighbor node = new Neighbor(to);
        if (heads[from] == null) {
            heads[from] = node;
            return;
        }
        Neighbor last = heads[from];
        while (last.next != null) {
            last = last.next;
        }
        last.next = node;
    }

    // Make the array twice as big and copy the list heads over.
    private void grow() {
        Neighbor[] bigger = new Neighbor[heads.length * 2];
        for (int i = 0; i < placeCount; i++) {
            bigger[i] = heads[i];
        }
        heads = bigger;
    }
}
