
An Administrator is a:
  - `new Administrator()`

  and has one method:

  - `List<Set<com.tsuro.player.StrategyPlayer>> startTournament(List<com.tsuro.player.StrategyPlayer>)`
    - This method will start a tournament of Tsuro using the `List<com.tsuro.player.StrategyPlayer>` given, where the list is sorted by the ages of the Players, **ascending**. The `List<Set<com.tsuro.player.StrategyPlayer>>` it returns is the rankings of the players, where earlier sets represent higher placings, and the sets contain the players who achieved that placing.

**Interpretation**: An administrator starts a tournament of Tsuro, taking players and putting them in different games with referees, and eventually determines the rankings of the `com.tsuro.player.StrategyPlayer`s. The Administrator runs the tournament by creating games with random permutations of players, then ranking players based on performance.

```
+-------+                                       +---------+                   +---------+ +---------+
| Admin |                                       | com.tsuro.referee.Referee |                   | Player1 | | PlayerN |
+-------+                                       +---------+                   +---------+ +---------+
    |                                                |                             |           |
    | Creates com.tsuro.referee.Referee with game                      |                             |           |
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
object Admin com.tsuro.referee.Referee Player1 PlayerN
Admin->com.tsuro.referee.Referee: Creates com.tsuro.referee.Referee with game 
Admin->com.tsuro.referee.Referee: Initializes with Player1 through PlayerN
com.tsuro.referee.Referee->Player1: Take initial turn
com.tsuro.referee.Referee->PlayerN: Take initial turn
com.tsuro.referee.Referee->Player1: Take intermediate turn
com.tsuro.referee.Referee->PlayerN: Take intermediate turn
note right of com.tsuro.referee.Referee: ...
com.tsuro.referee.Referee->Admin: Winners

note right of Admin: Repeat with other player configurations --->