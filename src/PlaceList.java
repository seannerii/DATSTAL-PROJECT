/*
 * PlaceList.java
 * DLSU Campus Map Finder - term project
 *
 * A growable array that stores the names of the places.
 * The position of a name in this list is also its place number.
 * The graph (Week 2) uses these numbers, so a name never moves.
 */
public class PlaceList {

    private String[] names; // holds the place names
    private int size;       // how many names are stored now

    // Start with room for 10 names. The array grows when it is full.
    public PlaceList() {
        names = new String[10];
        size = 0;
    }

    // Add a name at the end of the list.
    public void add(String name) {
        if (size == names.length) {
            grow();
        }
        names[size] = name;
        size++;
    }

    // Get the name at a position. Returns null if the position is bad.
    public String get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return names[index];
    }

    // How many names are in the list.
    public int size() {
        return size;
    }

    /*
     * Linear search: check the names one by one, from the start.
     * Upper and lower case do not matter ("library" finds "Library").
     * Returns the position, or -1 if the name is not in the list.
     */
    public int linearSearch(String name) {
        if (name == null) {
            return -1;
        }
        for (int i = 0; i < size; i++) {
            if (names[i].equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    /*
     * Return a copy of the names in a new array of the exact size.
     * Sort the copy, not the list, so the place numbers do not change.
     */
    public String[] toArray() {
        String[] copy = new String[size];
        for (int i = 0; i < size; i++) {
            copy[i] = names[i];
        }
        return copy;
    }

    // Make the array twice as big and copy the names over.
    private void grow() {
        String[] bigger = new String[names.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = names[i];
        }
        names = bigger;
    }
}
