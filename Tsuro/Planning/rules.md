# Interface

A RuleChecker is an interface implemented by a TsuroRuleChecker.
  - `new TsuroRuleChecker();`

A RuleChecker should have two methods:
  - `boolean isValidInitialMove(Board, Tile, Token, BoardLocation, Collection<Tile>);`
    - Will return true if placing the Tile as an initial move with the given BoardLocation is valid. 
  - `boolean isValidIntermediateMove(Board, Tile, Token, Collection<Tile>);`
    - Will return true if placing the given Tile on the ReadOnlyBoard to the location adjacent to the Token will violate any rules.

Both need to ensure that the Tile placed was also actually given to the players, which is represented by the `Collection<Tile>`
### Other classes

  - A Board is an interface defined in src/com/tsuro/board/Board.java
  - A Tile is an interface defined in src/com/tsuro/tile/Tile.java
  - A Token is an interface defined in src/com/tsuro/board/Token.java
  - A BoardLocation is a utility class defined in src/com/tsuro/board/BoardLocation.java
  

