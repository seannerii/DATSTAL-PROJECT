/*
 * MapLoader.java
 * DLSU Campus Map Finder - term project
 *
 * Reads the map text file. Each line has two place names, like this:
 *     Library Cafeteria
 * It means there is a road between them (both ways).
 * Place names have no spaces. Blank lines and lines that start with #
 * are ignored. Other bad lines are skipped, with a warning.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MapLoader {

    /*
     * Load the map file. Give it an EMPTY PlaceList and an EMPTY Graph.
     * Returns true if the map was loaded.
     * Returns false and prints a message if it could not be loaded.
     * After false, do not use the place list and graph (make new ones).
     */
    public static boolean load(String fileName, PlaceList places,
                               Graph graph) {
        if (places.size() != 0 || graph.placeCount() != 0) {
            System.out.println("Error: the place list and graph must be "
                    + "empty before loading.");
            return false;
        }
        if (fileName == null || fileName.trim().isEmpty()) {
            System.out.println("Error: no file name was given.");
            return false;
        }
        String name = fileName.trim();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(name),
                        StandardCharsets.UTF_8))) {
            readLines(reader, places, graph);
        } catch (FileNotFoundException e) {
            System.out.println("Error: file '" + name + "' was not found.");
            System.out.println("Looked here: "
                    + new File(name).getAbsolutePath());
            return false;
        } catch (IOException e) {
            System.out.println("Error: could not read file '" + name + "'.");
            return false;
        }

        if (places.size() == 0) {
            System.out.println("Error: no roads were found in '" + name
                    + "'.");
            return false;
        }
        return true;
    }

    // Read the file line by line and build the place list and graph.
    private static void readLines(BufferedReader reader, PlaceList places,
                                  Graph graph) throws IOException {
        int lineNumber = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            lineNumber++;
            // Some editors put an invisible mark at the start of a file.
            if (lineNumber == 1 && line.startsWith("\uFEFF")) {
                line = line.substring(1);
            }
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            String[] words = line.split("\\s+");
            if (words.length != 2) {
                warn(lineNumber, "needs exactly 2 place names", line);
                continue;
            }
            if (words[0].equalsIgnoreCase(words[1])) {
                warn(lineNumber, "a road needs 2 different places", line);
                continue;
            }

            int a = findOrAddPlace(words[0], places, graph);
            int b = findOrAddPlace(words[1], places, graph);
            if (!graph.addRoad(a, b)) {
                warn(lineNumber, "this road is already in the map", line);
            }
        }
    }

    // Find a place by name (linear search). If it is new, add it to the
    // place list and to the graph. Returns the place number.
    private static int findOrAddPlace(String name, PlaceList places,
                                      Graph graph) {
        int number = places.linearSearch(name);
        if (number == -1) {
            places.add(name);
            number = graph.addPlace();
        }
        return number;
    }

    private static void warn(int lineNumber, String reason, String line) {
        System.out.println("Warning: line " + lineNumber + " skipped ("
                + reason + "): " + line);
    }
}
