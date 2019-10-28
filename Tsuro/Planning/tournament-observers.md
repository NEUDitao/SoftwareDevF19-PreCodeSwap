
A `TournamentObserver` implements `IObserver<TournamentState>`.

A `TournamentObserver` is updated every time a game ends, which is when a `com.tsuro.referee.Referee` returns from `startGame()`. See [the definition of `startGame()`](referee.md) in `referee.md` for more information on this. 

`TournamentState` holds the information that a TournamentObserver wants to see.
A `TournamentState` holds:

  - a `List<Set<com.tsuro.player.IPlayer>>`, which represents the standings of the most recent game that ended. See [administrator.md](administrator.md) for more information on this. 
  - a `List<Set<com.tsuro.player.IPlayer>>`, which represents the overall standings of a tournament. Details are left up to implementations of tournaments. Can be left empty.


---
### Definitions

- `IObserver<T>` can be found [here](../Common/src/com/tsuro/observer/IObserver.java)
- `com.tsuro.player.IPlayer` can be found [here](../Common/src/com.tsuro.player.IPlayer.java)
- A com.tsuro.referee.Referee can be found [here](../Admin/Referee.java)