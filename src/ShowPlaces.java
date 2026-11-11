/*
 * ShowPlaces.java
 * DLSU Campus Map Finder - term project
 *
 * Menu choice 2: show all the places from A to Z.
 * We sort a COPY of the names, so the place numbers (the positions in
 * PlaceList, used by the graph) do not change.
 */
public class ShowPlaces {

    public static void show(PlaceList places) {
        if (places.size() == 0) {
            System.out.println("There are no places to show.");
            return;
        }

        String[] names = places.toArray();
        Sorter.insertionSort(names);

        System.out.println("Places from A to Z (" + names.length + "):");
        for (int i = 0; i < names.length; i++) {
            System.out.println((i + 1) + ". " + names[i]);
        }
    }
}
