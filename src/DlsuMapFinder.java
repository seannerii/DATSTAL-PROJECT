
 //Week 2 version: load the map file and print the graph, so we can check that the map was read correctly.
 // The menu comes in Week 3.

public class DlsuMapFinder {

    public static void main(String[] args) {
        String fileName = "map.txt";
        if (args.length > 0) {
            fileName = args[0];
        }

        PlaceList places = new PlaceList();
        Graph graph = new Graph();

        if (!MapLoader.load(fileName, places, graph)) {
            return;
        }

        System.out.println("Loaded " + places.size() + " places and "
                + graph.roadCount() + " roads from " + fileName);
        System.out.println();
        graph.print(places);
    }
}
