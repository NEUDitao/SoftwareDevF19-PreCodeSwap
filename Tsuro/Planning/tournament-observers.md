
A `TournamentObserver` implements `IObserver<TournamentState>`.

A `TournamentObserver` is updated every time a game ends, which is when a `Referee` returns from `startGame()`. See [the definition of `startGame()`](referee.md) in `referee.md` for more information on this. 

`TournamentState` holds the information that a TournamentObserver wants to see.
A `TournamentState` holds:

  - a `List<Set<IPlayer>>`, which represents the standings of the most recent game that ended. See [administrator.md](administrator.md) for more information on this. 
  - a `List<Set<IPlayer>>`, which represents the overall standings of a tournament. Details are left up to implementations of tournaments. Can be left empty.


---
### Definitions

- `IObserver<T>` can be found [here](../Common/src/com/tsuro/observer/IObserver.java)
- `IPlayer` can be found [here](../Common/src/player-interface.java)
- A Referee can be found [here](../Admin/referee.java)