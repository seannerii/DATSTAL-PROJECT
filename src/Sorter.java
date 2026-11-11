/*
 * Sorter.java
 * DLSU Campus Map Finder - term project
 *
 * Our own sort, written by hand (no Arrays.sort or Collections.sort).
 * Insertion sort works like sorting playing cards in your hand: take
 * the next name and slide it to the left until it is in the right place.
 */
public class Sorter {

    /*
     * Sort the names from A to Z. Upper and lower case do not matter.
     * The array itself is changed. Nothing is returned.
     */
    public static void insertionSort(String[] names) {
        if (names == null) {
            return;
        }
        for (int i = 1; i < names.length; i++) {
            String current = names[i];
            int j = i - 1;
            // Move every bigger name one step to the right.
            while (j >= 0 && names[j].compareToIgnoreCase(current) > 0) {
                names[j + 1] = names[j];
                j--;
            }
            names[j + 1] = current;
        }
    }
}
