# DLSU Campus Map Finder

Term project (DATSTAL). A text-only Java program. It loads a campus map
from a text file, shows the places from A to Z, and finds the path with
the fewest stops between two places using breadth-first search (BFS).

## How to compile and run

Open a terminal in the project folder (the folder that has `src` and
`map.txt`). Then:

    javac -d out src/*.java
    java -cp out DlsuMapFinder

To run our tests:

    java -cp out Tests

Run the program from the project folder, so it can find `map.txt`.
In IntelliJ IDEA: open the folder, then right-click `DlsuMapFinder` and
choose Run.

## Who wrote each part

Replace Name 1, Name 2, Name 3 with our real names. Fix the file names
when each part is built.

| Part                      | File                 | Written by |
|---------------------------|----------------------|------------|
| Array list of places      | src/PlaceList.java   | Name 1     |
| Sort (A to Z)             | (Week 3)             | Name 1     |
| Queue                     | src/IntQueue.java    | Name 2     |
| BFS                       | (Week 3)             | Name 2     |
| Graph                     | (Week 2)             | Neri       |
| Map file reader           | (Week 2)             | Neri       |
| Menu and error messages   | (Week 3)             | All        |
| Tests                     | src/Tests.java       | All        |

## Help sources

- Youtube
- Datstal PPTS
