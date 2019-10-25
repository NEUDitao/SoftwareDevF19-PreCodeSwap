
A `RefereeObserver` implements `IObserver<RefereeState>`.

A `RefereeObserver` is updated every turn, which is whenever an `IPlayer` attempts an `IAction`.

`RefereeState` holds the information that a RefereeObserver wants to see.
A RefereeState holds:

  - an `IBoard`, which represents the board prior to the turn
  - a `Token`, which represents the `Token` placing an `ITile` this turn
  - a `List<ITile>`, which represents the hand that an `IPlayer` had when they made the turn
  - a `IAction`, which represents the information about the turn made by the `IPlayer`
  - a `List<Token>`, which represents the players that have been eliminated in the current round
  - a `List<Set<Token>>`, which represents the order of which players were eliminated in previous rounds. See [the definition of `startGame()`](referee.md) in `referee.md` for more information on this. 
  
---

### Definitions

- `IObserver<T>` can be found [here](../Common/src/com/tsuro/observer/IObserver.java)
- `IBoard` can be found [here](../Common/src/com/tsuro/board/IBoard.java)
- `Token` can be found [here](../Common/src/com/tsuro/board/Token.java)
- `ITile` can be found [here](../Common/src/com/tsuro/tile/ITile.java)
- `IAction` can be found [here](../Common/src/com/tsuro/action/IAction.java)
- `IPlayer` can be found [here](../Common/src/player-interface.java)
- A Referee can be found [here](../Admin/referee.java)