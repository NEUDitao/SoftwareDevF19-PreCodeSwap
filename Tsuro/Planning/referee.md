
# Referee

A Referee is an `Interface` implemented by a `TsuroReferee` which is a:
  - `new TileStrategyTsuroReferee(RuleChecker, [List-of Player], Iterator<ITile>)`
    - Which constructs a Referee that uses the set of rules in `RuleChecker` (check out more details at rules.md) to facilitate a game of Tsuro and monitors the game played by the `[List-of Player]`.
    - The players are in order of age, oldest first.
    - The `Iterator<ITile>` represents a strategy for choosing tiles to give to the players.

The Referee should only have one method, `List<Set<Player>> startGame()`, which starts a game of Tsuro with the Players it was constructed with using the rules it was given. The `List<Set<Player>>` is an ordered list of the ranking the player acheived. The last player (or players in the case of a tie) left achieved "first place," and so are in the Set at the first index. We use `Set`s for ties, if there are multiple `Player`s in a `Set`, it means they got eliminated at the same time.

`Referee`s should consult their `RuleChecker` after every move made by a `Player`. `Referee`s should construct their own board and refer to it as the "master" board. Since boards are immutable, `Player`s cannot mess with the `Referee`'s copy.

`Player`s and `InitialPlacement` are defined in player.md.
`Tile`s are specified in tiles.md.


Communication should look like:
```
+-------------+                              +---------+                                          +---------+ +---------+
| RuleChecker |                              | Referee |                                          | Player1 | | PlayerN |
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
       |        | Referee performs move on      |-|                                                    |           |
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
       |        | Referee performs move on      |-|                                                    |           |
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
       |        | Referee performs move on      |-|                                                    |           |
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
       |        | Referee performs move on      |-|                                                    |           |
       |        | own Board if valid, else kick | |                                                    |           |
       |        |-------------------------------| | ------\                                            |           |
       |                                          |-| ... |                                            |           |
       |                                          | |-----|                                            |           |
       |                                          | ----------------------------------------------\    |           |
       |                                          |-| Continue until a player (in this case,      |    |           |
       |                                          | | all PlayerN except Player1) gets eliminated |    |           |
       |                                          | --------------------------------------------\-|    |           |
       |                                          |-| After elimination of a Player, discontiue |      |           |
       |                                          | | communication with them from Ref          |      |           |
       |----------------------------------------\ | |-------------------------------------------|      |           |
       || return List<Set<Player>> as specified |-|                                                    |           |
       ||---------------------------------------| |                                                    |           |
       |                                          |                                                    |           |
```

<!--
object RuleChecker Referee Player1 PlayerN
note left of Referee: Given Players and Rulechecker\n and told to start game
Referee->Player1: makeInitialMove()
Player1->Referee: InitialPlacement object
Referee->RuleChecker: isValidInitialMove()
RuleChecker->Referee: result
note left of Referee: Referee performs move on\n own Board if valid, else kick
Referee->PlayerN: makeInitialMove()
PlayerN->Referee: InitialPlacement object
Referee->RuleChecker: isValidInitialMove()
RuleChecker->Referee: result
note left of Referee: Referee performs move on\n own Board if valid, else kick
Referee->Player1: makeIntermediateMove()
Player1->Referee: Tile object
Referee->RuleChecker: isValidIntermediateMove()
RuleChecker->Referee: result
note left of Referee: Referee performs move on\n own Board if valid, else kick
Referee->PlayerN: makeIntermediateMove()
PlayerN->Referee: Tile object
Referee->RuleChecker: isValidIntermediateMove()
RuleChecker->Referee: result
note left of Referee: Referee performs move on\n own Board if valid, else kick
note right of Referee: ...
note right of Referee: Continue until a player (in this case, \nall PlayerN except Player1) gets eliminated
note right of Referee: After elimination of a Player, discontiue \ncommunication with them from Ref
note left of Referee: return List<Set<Player>> as specified

https://textart.io/sequence

-->

