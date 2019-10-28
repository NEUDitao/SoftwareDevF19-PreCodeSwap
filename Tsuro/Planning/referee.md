
# com.tsuro.referee.Referee

A com.tsuro.referee.Referee is represented by:
  - `new com.tsuro.referee.Referee(RuleChecker, [List-of com.tsuro.player.StrategyPlayer], Iterator<ITile>)`
    - Which constructs a com.tsuro.referee.Referee that uses the set of rules in `RuleChecker` (check out more details at rules.md) to facilitate a game of Tsuro and monitors the game played by the `[List-of com.tsuro.player.StrategyPlayer]`.
    - The players are in order of age, oldest first.
    - The `Iterator<ITile>` represents a strategy for choosing tiles to give to the players.

The com.tsuro.referee.Referee should only have one method, `List<Set<com.tsuro.player.StrategyPlayer>> startGame()`, which starts a game of Tsuro with the Players it was constructed with using the rules it was given. The `List<Set<com.tsuro.player.StrategyPlayer>>` is an ordered list of the ranking the player acheived. The last player (or players in the case of a tie) left achieved "first place," and so are in the Set at the first index. We use `Set`s for ties, if there are multiple `com.tsuro.player.StrategyPlayer`s in a `Set`, it means they got eliminated at the same time.

`com.tsuro.referee.Referee`s should consult their `RuleChecker` after every move made by a `com.tsuro.player.StrategyPlayer`. `com.tsuro.referee.Referee`s should construct their own board and refer to it as the "master" board. Since boards are immutable, `com.tsuro.player.StrategyPlayer`s cannot mess with the `com.tsuro.referee.Referee`'s copy.

`com.tsuro.player.StrategyPlayer`s and `InitialPlacement` are defined in player.md.
`Tile`s are specified in tiles.md.


Communication should look like:
```
+-------------+                              +---------+                                          +---------+ +---------+
| RuleChecker |                              | com.tsuro.referee.Referee |                                          | Player1 | | PlayerN |
+-------------+                              +---------+                                          +---------+ +---------+
       |        --------------------------------\ |                                                    |           |
       |        | Given Players and Rulechecker |-|                                                    |           |
       |        | and told to start game        | |                                                    |           |
       |        |-------------------------------| |                                                    |           |
       |                                          | makeInitialMove()                                  |           |
       |                                          |--------------------------------------------------->|           |
       |                                          |                                                    |           |
       |                                          |                            InitialPlacement object |           |
       |                                          |<---------------------------------------------------|           |
       |                                          |                                                    |           |
       |                     isValidInitialMove() |                                                    |           |
       |<-----------------------------------------|                                                    |           |
       |                                          |                                                    |           |
       | result                                   |                                                    |           |
       |----------------------------------------->|                                                    |           |
       |        --------------------------------\ |                                                    |           |
       |        | com.tsuro.referee.Referee performs move on      |-|                                                    |           |
       |        | own Board if valid, else kick | |                                                    |           |
       |        |-------------------------------| |                                                    |           |
       |                                          | makeInitialMove()                                  |           |
       |                                          |--------------------------------------------------------------->|
       |                                          |                                                    |           |
       |                                          |                                        InitialPlacement object |
       |                                          |<---------------------------------------------------------------|
       |                                          |                                                    |           |
       |                     isValidInitialMove() |                                                    |           |
       |<-----------------------------------------|                                                    |           |
       |                                          |                                                    |           |
       | result                                   |                                                    |           |
       |----------------------------------------->|                                                    |           |
       |        --------------------------------\ |                                                    |           |
       |        | com.tsuro.referee.Referee performs move on      |-|                                                    |           |
       |        | own Board if valid, else kick | |                                                    |           |
       |        |-------------------------------| |                                                    |           |
       |                                          | makeIntermediateMove()                             |           |
       |                                          |--------------------------------------------------->|           |
       |                                          |                                                    |           |
       |                                          |                                        Tile object |           |
       |                                          |<---------------------------------------------------|           |
       |                                          |                                                    |           |
       |                isValidIntermediateMove() |                                                    |           |
       |<-----------------------------------------|                                                    |           |
       |                                          |                                                    |           |
       | result                                   |                                                    |           |
       |----------------------------------------->|                                                    |           |
       |        --------------------------------\ |                                                    |           |
       |        | com.tsuro.referee.Referee performs move on      |-|                                                    |           |
       |        | own Board if valid, else kick | |                                                    |           |
       |        |-------------------------------| |                                                    |           |
       |                                          | makeIntermediateMove()                             |           |
       |                                          |--------------------------------------------------------------->|
       |                                          |                                                    |           |
       |                                          |                                                    Tile object |
       |                                          |<---------------------------------------------------------------|
       |                                          |                                                    |           |
       |                isValidIntermediateMove() |                                                    |           |
       |<-----------------------------------------|                                                    |           |
       |                                          |                                                    |           |
       | result                                   |                                                    |           |
       |----------------------------------------->|                                                    |           |
       |        --------------------------------\ |                                                    |           |
       |        | com.tsuro.referee.Referee performs move on      |-|                                                    |           |
       |        | own Board if valid, else kick | |                                                    |           |
       |        |-------------------------------| | ------\                                            |           |
       |                                          |-| ... |                                            |           |
       |                                          | |-----|                                            |           |
       |                                          | ----------------------------------------------\    |           |
       |                                          |-| Continue until a player (in this case,      |    |           |
       |                                          | | all PlayerN except Player1) gets eliminated |    |           |
       |                                          | --------------------------------------------\-|    |           |
       |                                          |-| After elimination of a com.tsuro.player.StrategyPlayer, discontiue |      |           |
       |                                          | | communication with them from Ref          |      |           |
       |----------------------------------------\ | |-------------------------------------------|      |           |
       || return List<Set<com.tsuro.player.StrategyPlayer>> as specified |-|                                                    |           |
       ||---------------------------------------| |                                                    |           |
       |                                          |                                                    |           |
```

<!--
object RuleChecker com.tsuro.referee.Referee Player1 PlayerN
note left of com.tsuro.referee.Referee: Given Players and Rulechecker\n and told to start game
com.tsuro.referee.Referee->Player1: makeInitialMove()
Player1->com.tsuro.referee.Referee: InitialPlacement object
com.tsuro.referee.Referee->RuleChecker: isValidInitialMove()
RuleChecker->com.tsuro.referee.Referee: result
note left of com.tsuro.referee.Referee: com.tsuro.referee.Referee performs move on\n own Board if valid, else kick
com.tsuro.referee.Referee->PlayerN: makeInitialMove()
PlayerN->com.tsuro.referee.Referee: InitialPlacement object
com.tsuro.referee.Referee->RuleChecker: isValidInitialMove()
RuleChecker->com.tsuro.referee.Referee: result
note left of com.tsuro.referee.Referee: com.tsuro.referee.Referee performs move on\n own Board if valid, else kick
com.tsuro.referee.Referee->Player1: makeIntermediateMove()
Player1->com.tsuro.referee.Referee: Tile object
com.tsuro.referee.Referee->RuleChecker: isValidIntermediateMove()
RuleChecker->com.tsuro.referee.Referee: result
note left of com.tsuro.referee.Referee: com.tsuro.referee.Referee performs move on\n own Board if valid, else kick
com.tsuro.referee.Referee->PlayerN: makeIntermediateMove()
PlayerN->com.tsuro.referee.Referee: Tile object
com.tsuro.referee.Referee->RuleChecker: isValidIntermediateMove()
RuleChecker->com.tsuro.referee.Referee: result
note left of com.tsuro.referee.Referee: com.tsuro.referee.Referee performs move on\n own Board if valid, else kick
note right of com.tsuro.referee.Referee: ...
note right of com.tsuro.referee.Referee: Continue until a player (in this case, \nall PlayerN except Player1) gets eliminated
note right of com.tsuro.referee.Referee: After elimination of a com.tsuro.player.StrategyPlayer, discontiue \ncommunication with them from Ref
note left of com.tsuro.referee.Referee: return List<Set<com.tsuro.player.StrategyPlayer>> as specified

https://textart.io/sequence

-->

