# Players

A com.tsuro.player.IPlayer is an `Interface` and has the following methods
  - `Action makeInitMove(Collection<Tile>, Token, Board, RuleChecker)`
    - Returns an `Action` on the given `Board` using a `Tile` from the `Collection<Tile>` playing as the given `Token` adhering to the `RuleChecker`
  - `Action makeIntermediateMove(Collection<Tile>, Token, Board, RuleChecker)`
    - Returns the `Action` that this `com.tsuro.player.StrategyPlayer` wants to perform for the given `Token` on the given `Board`, adhering to the `RuleChecker`.
  - `com.tsuro.player.IPlayer addObserver(Observer<PlayerState>)`
    - adds an Observer on the com.tsuro.player.StrategyPlayer that expects a PlayerState. Returns the new com.tsuro.player.StrategyPlayer w/ the Observer. **DOES NOT MODIFY ORIGINAL**
  - `com.tsuro.player.IPlayer removeObserver(Observer<PlayerState>)`
    - removes the Observer given (if it exists). Returns the new com.tsuro.player.StrategyPlayer w/o the Observer. **DOES NOT MODIFY ORIGINAL**

 An `com.tsuro.player.IPlayer` represents some entity in a game of Tsuro. Its methods are only called by whoever is facilitating the game, and is fed immutable representations of the information it needs to know. 

Internally, `com.tsuro.player.IPlayer`s do not **need** to keep track of their own state, they are fed information as needed. However, implementations may choose to keep track of some state, whether it be a stream of inputs, or any other state related to the `com.tsuro.player.IPlayer`'s strategy.

An `com.tsuro.player.IPlayer` should notify all observers after each call to `makeInitMove` and `makeIntermediateMove`.

---

A PlayerState is a [POJO](https://en.wikipedia.org/wiki/Plain_old_Java_object) which contains:
  - `IAction action`
  - `Collection<ITile> hand`
  - `Token token`
  - `Board board`

  and represents the most recent `action` a `com.tsuro.player.StrategyPlayer` with the given `token` took with the `hand` they were dealt on the `board`


---
- `Tile`s are specified in tiles.md
- `Token`, `Board`, and `BoardLocation` are specified in board.md
- `RuleChecker` is specified in rules.md
- An example of a game facilitator can be found in 
- `Action`s are specified in board.md