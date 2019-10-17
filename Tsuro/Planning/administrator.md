
An Administrator is a:
  - `new Administrator()`

  and has one method:

  - `List<Set<Player>> startTournament(List<Player>)`
    - This method will start a tournament of Tsuro using the `List<Player>` given, where the list is sorted by the ages of the Players, **ascending**. The `List<Set<Player>>` it returns is the rankings of the players, where earlier sets represent higher placings, and the sets contain the players who achieved that placing.

**Interpretation**: An administrator starts a tournament of Tsuro, taking players and putting them in different games with referees, and eventually determines the rankings of the `Player`s.

```
+-------+                                       +---------+                   +---------+ +---------+
| Admin |                                       | Referee |                   | Player1 | | PlayerN |
+-------+                                       +---------+                   +---------+ +---------+
    |                                                |                             |           |
    | Creates Referee with game                      |                             |           |
    |----------------------------------------------->|                             |           |
    |                                                |                             |           |
    | Initializes with Player1 through PlayerN       |                             |           |
    |----------------------------------------------->|                             |           |
    |                                                |                             |           |
    |                                                | Take initial turn           |           |
    |                                                |---------------------------->|           |
    |                                                |                             |           |
    |                                                | Take initial turn           |           |
    |                                                |---------------------------------------->|
    |                                                |                             |           |
    |                                                | Take intermediate turn      |           |
    |                                                |---------------------------->|           |
    |                                                |                             |           |
    |                                                | Take intermediate turn      |           |
    |                                                |---------------------------------------->|
    |                                                | ------\                     |           |
    |                                                |-| ... |                     |           |
    |                                                | |-----|                     |           |
    |                                                |                             |           |
    |                                        Winners |                             |           |
    |<-----------------------------------------------|                             |           |
    | ------------------------------------------\    |                             |           |
    |-| Repeat with other player configurations |    |                             |           |
    | |-----------------------------------------|    |                             |           |
    |                                                |                             |           |
    
```

<!---
object Admin Referee Player1 PlayerN
Admin->Referee: Creates Referee with game 
Admin->Referee: Initializes with Player1 through PlayerN
Referee->Player1: Take initial turn
Referee->PlayerN: Take initial turn
Referee->Player1: Take intermediate turn
Referee->PlayerN: Take intermediate turn
note right of Referee: ...
Referee->Admin: Winners

note right of Admin: Repeat with other player configurations --->