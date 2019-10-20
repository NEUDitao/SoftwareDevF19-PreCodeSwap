# Players

A Player is an `Interface`. Implementors should create classes to implement the Interface. 

A Player has two methods:
  - `IAction makeInitMove(Collection<Tile>, Token, Board, RuleChecker)`
    - Returns an `IAction` on the given `Board` using a `Tile` from the `Collection<Tile>` playing as the given `Token` adhering to the `RuleChecker`
  - `IAction makeIntermediateMove(Collection<Tile>, Token, Board, RuleChecker)`
    - Returns the `IAction` that this `Player` wants to perform for the given `Token` on the given `Board`, adhering to the `RuleChecker`.

 A Player represents some entity in a game of Tsuro. Its methods are only called by whoever is facilitating the game, and is fed immutable representations of the information it needs to know. 

Internally, `Player`s do not **need** to keep track of their own state, they are fed information as needed. However, implementations may choose to keep track of some state, whether it be a stream of inputs, or any other state related to the `Player`'s strategy.

---
- `Tile`s are specified in tiles.md
- `Token`, `Board`, and `BoardLocation` are specified in board.md
- `RuleChecker` is specified in rules.md
- An example of a game facilitator can be found in 
- `IAction`s are specified in board.md