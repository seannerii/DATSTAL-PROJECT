# DLSU Campus Map Finder

Term project (DATSTAL). A text-only Java program. It loads a campus map
from a text file, shows the places from A to Z, and finds the path with
the fewest stops between two places using breadth-first search (BFS).

## How to compile and run

Open a terminal in the project folder (the folder that has `src` and
`map.txt`). Then:

    javac -d out src/*.java
    java -cp out DlsuMapFinder

To run our tests (every line should say PASS):

    java -cp out Tests
    java -cp out SorterTest
    java -cp out PathFinderTest
    java -cp out MenuTest

Run everything from the project folder, so the program can find
`map.txt`. In IntelliJ IDEA: open the folder, then right-click
`DlsuMapFinder` and choose Run.

## How to use the program

The program shows a menu and keeps asking until you choose 4.

    ==== DLSU Campus Map Finder ====
    1. Load the map file
    2. Show all places (A to Z)
    3. Find a path between 2 places
    4. Quit

1. **Load the map file.** Type the file name, or press Enter to use
   `map.txt`. If the file is missing, the program says so and keeps the
   old map (if there is one).
2. **Show all places (A to Z).** Prints every place name in A to Z
   order.
3. **Find a path.** Type a start place and a destination place. Upper
   and lower case do not matter. The program prints the path with the
   fewest stops and the number of stops:

        Start place: Gokongwei
        Destination place: Faculty
        Path: Gokongwei -> Hermitage -> EcoCar -> StMiguel -> BrBloemen
        -> StJoseph -> Faculty
        Number of stops: 6

   (The real program prints the path on one line.) A stop is one road we
   walk, so the start place is not counted. If a place name is wrong, or
   there is no path, the program prints a message and goes back to the
   menu.
4. **Quit.**

## The map file

`map.txt` is a text file. Each line has two place names with a space
between them. It means there is a road between those two places.
Roads go both ways.

    LaSalle HenrySy

- A place name has no spaces (for example `HenrySy`).
- Upper and lower case do not matter (`lasalle` is the same as `LaSalle`).
- Blank lines and lines that start with # are ignored.
- A bad line is skipped and the program prints a warning.

We made the map from the DLSU campus map. Each place is a building on
that map. A road means two buildings that are next to each other with
a direct walk between them.

`Retreat`, `Preschool`, `Kanlungan` and `Carpentry` are across the
streets, on the far right of the campus map. They are only connected to
each other, so a path from the main campus to them shows the "no path"
message.

### Place names

| Name in map.txt | Building (number on the DLSU campus map)         |
|-----------------|--------------------------------------------------|
| LaSalle         | St. La Salle Hall (1)                            |
| Conservatory    | Marilen Gaerlan Conservatory (2)                 |
| BrJohn          | Br. John Hall (3)                                |
| BrConnon        | Br. Gabriel Connon Hall (4)                      |
| Yuchengco       | E. Yuchengco Hall (5)                            |
| Chapel          | PGP Chapel (6)                                   |
| Faculty         | Faculty Bldg, former University Library (7)      |
| BrWilliam       | Br. William Hall (8)                             |
| StJoseph        | St. Joseph Hall (9)                              |
| HenrySy         | Henry Sy Hall (10)                               |
| BrBloemen       | Br. Bloemen Hall (11)                            |
| Velasco         | Velasco Hall (12)                                |
| StMiguel        | St. Miguel Hall (13)                             |
| StMutien        | St. Mutien Marie Hall (14)                       |
| EcoCar          | Eco Car / Solar Car Shelter (15)                 |
| Hermitage       | Le' Hermitage (16)                               |
| Gokongwei       | Gokongwei Hall (17)                              |
| STRC            | STRC Bldg (18)                                   |
| Maintenance     | Inventory Mngt. Off / ROTC / Maintenance Shop (19) |
| Razon           | Enrique Razon Sports Center (20)                 |
| BrAndrew        | Br. Andrew Gonzalez Hall (21)                    |
| Retreat         | Retreat House (22)                               |
| Preschool       | Preschool (23)                                   |
| Kanlungan       | Bahay Kanlungan / Legal Aide Clinic (24)         |
| Carpentry       | Carpentry / Painting Workshop / IMO Ext. (25)    |

## Who wrote each part

| Part                              | File                    | Written by |
|-----------------------------------|-------------------------|------------|
| Array list of places              | src/PlaceList.java      | Ramin      |
| Linear search                     | src/PlaceList.java      | Ramin      |
| Sort (A to Z)                     | src/Sorter.java         | Ramin      |
| Show places (menu choice 2)       | src/ShowPlaces.java     | Ramin      |
| Queue                             | src/IntQueue.java       | Valencia   |
| BFS                               | src/PathFinder.java     | Valencia   |
| Print the path or "no path"       | src/PathFinder.java     | Valencia   |
| Graph                             | src/Graph.java          | Neri       |
| Map file reader                   | src/MapLoader.java      | Neri       |
| Main program, menu, error messages| src/DlsuMapFinder.java  | Neri       |
| Map file                          | map.txt                 | All        |
| Tests: lists, queue, graph, loader| src/Tests.java          | Neri       |
| Tests: sort and show places       | src/SorterTest.java     | Ramin      |
| Tests: BFS                        | src/PathFinderTest.java | Valencia   |
| Tests: menu                       | src/MenuTest.java       | Neri       |

## Help sources

- Youtube
- DLSU campus map (picture of the De La Salle University campus map):
  building names and which buildings are next to each other.
- DATSTAL ppts
