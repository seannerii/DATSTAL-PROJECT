# Meeting Notes

Rule: we meet or message each other once in a while every week via Messenger and write down what we decided.

## Week 1 - Meeting 1

- Date: 10/27/26
- What we decided:
  - We use Java and one GitHub repo. Each of us makes our own commits.
  - Ramin: array list of places, sort, linear search.
  - Valencia: queue and BFS.
  - Neri: graph, map file reader, menu and main, README.
  - We meet every once or twice in a week via Messenger
- What we finished in Week 1:
  - One-page plan.
  - PlaceList.java (Ramin) and IntQueue.java (Valencia).
  - Tests.java, README and meeting notes (Neri).
- Who does what before the next meeting:
  - Ramin: check map.txt against the real campus. Start the sort.
  - Valencia: test the map loader with bad files. Read Graph.java.
  - Neri: Graph.java, MapLoader.java, and the Week 2 main program.
  - All: Peer Evaluation 1 (end of Week 2).
- Problems or questions for the instructor:
  -NONE 


## Week 2 - Meeting 1

- Date: 11/7/2026
- What we decided:
  - We made map.txt from the DLSU campus map. Each building is a
    place, and a road is two buildings that are next to each other.
  - Retreat, Preschool, Kanlungan and Carpentry are across the
    streets, so they only connect to each other. We use them to show
    the "no path" message.
  - Place names have no spaces (for example HenrySy), because each
    line of the map file is two names with a space between them.
  - For now, "number of stops" means the roads we walk, so
    Library -> Cafeteria -> Gym is 2 stops. We will change it if the
    instructor says something different.
  - Each of us edits only our own files.
- What we finished in Week 2:
  - Graph.java and MapLoader.java: reads map.txt and builds the
    graph (Neri).
  - map.txt: 25 places and 29 roads, made from the DLSU campus map.
  - Sorter.java and ShowPlaces.java, with tests (Ramin).
  - PathFinder.java (BFS and printing the path), with tests (Valencia).
  - DlsuMapFinder loads map.txt and prints the graph.

- Who does what before the next meeting:
  - Neri: the menu, main, and the error messages.
  - Ramin and Valencia: start your report paragraphs.
  - All: Peer Evaluation 1 (end of Week 2).
- Problems or questions for the instructor:
  none