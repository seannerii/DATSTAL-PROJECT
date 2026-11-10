import java.util.Scanner;

public class DlsuMapFinder {

    private static final String DEFAULT_FILE = "map.txt";

    private static PlaceList places; // the place names
    private static Graph graph;      // the roads between the places
    private static boolean mapLoaded; // true after a map was loaded

    public static void main(String[] args) {
        places = new PlaceList();
        graph = new Graph();
        mapLoaded = false;

        Scanner input = new Scanner(System.in);
        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine(input);
            if (choice == null) {
                break; // there is no more input, so we stop
            }
            switch (choice.trim()) {
                case "1":
                    loadMap(input);
                    break;
                case "2":
                    showPlaces();
                    break;
                case "3":
                    findPath(input);
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("Please enter 1, 2, 3 or 4.");
            }
        }
        System.out.println("Goodbye!");
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("==== DLSU Campus Map Finder ====");
        System.out.println("1. Load the map file");
        System.out.println("2. Show all places (A to Z)");
        System.out.println("3. Find a path between 2 places");
        System.out.println("4. Quit");
        System.out.print("Enter your choice: ");
    }

    // Choice 1: ask for a file name and load the map. We load into NEW
    // objects, so if the file is bad we still have the old map.
    private static void loadMap(Scanner input) {
        System.out.print("Map file name (press Enter for "
                + DEFAULT_FILE + "): ");
        String name = readLine(input);
        if (name == null) {
            return;
        }
        name = name.trim();
        if (name.isEmpty()) {
            name = DEFAULT_FILE;
        }

        PlaceList newPlaces = new PlaceList();
        Graph newGraph = new Graph();
        if (MapLoader.load(name, newPlaces, newGraph)) {
            places = newPlaces;
            graph = newGraph;
            mapLoaded = true;
            System.out.println("Map loaded: "
                    + countOf(places.size(), "place") + " and "
                    + countOf(graph.roadCount(), "road") + ".");
        } else if (mapLoaded) {
            System.out.println("The map was not loaded. "
                    + "The old map is still in use.");
        } else {
            System.out.println("The map was not loaded.");
        }
    }

    // Choice 2: show all places from A to Z.
    private static void showPlaces() {
        if (!requireMap()) {
            return;
        }
        ShowPlaces.show(places);
    }

    // Choice 3: ask for two places, then find the shortest path.
    private static void findPath(Scanner input) {
        if (!requireMap()) {
            return;
        }
        int start = askForPlace(input, "Start place: ");
        if (start == -1) {
            return;
        }
        int goal = askForPlace(input, "Destination place: ");
        if (goal == -1) {
            return;
        }
        PathFinder.showPath(places, graph, start, goal);
    }

    // Ask for a place name and find it with linear search.
    // Returns the place number, or -1 if there is no such place.
    // A message is printed when the name is wrong.
    private static int askForPlace(Scanner input, String question) {
        System.out.print(question);
        String text = readLine(input);
        if (text == null) {
            return -1;
        }
        text = text.trim();
        if (text.isEmpty()) {
            System.out.println("Error: no place name was typed.");
            return -1;
        }
        int number = places.linearSearch(text);
        if (number == -1) {
            System.out.println("Error: '" + text
                    + "' is not a place on the map.");
            System.out.println("Choose 2 to see all the place names.");
        }
        return number;
    }

    // True if a map is loaded. If not, print a message.
    private static boolean requireMap() {
        if (!mapLoaded) {
            System.out.println("No map is loaded yet. "
                    + "Choose 1 to load the map first.");
            return false;
        }
        return true;
    }

    // Read one line. Returns null if there is no more input.
    private static String readLine(Scanner input) {
        if (!input.hasNextLine()) {
            return null;
        }
        return input.nextLine();
    }

    // "1 place" or "25 places".
    private static String countOf(int number, String word) {
        if (number == 1) {
            return number + " " + word;
        }
        return number + " " + word + "s";
    }
}
