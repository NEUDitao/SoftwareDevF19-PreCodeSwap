# Interface

A RuleChecker is an interface implemented by a TsuroRuleChecker.
  - `new TsuroRuleChecker();`

A RuleChecker should have two methods:
  - `boolean isValidInitialMove(ReadOnlyBoard, Tile, BoardLocation);`
    - Will return true if placing the Tile as an initial move with the given BoardLocation is valid. 
  - `boolean isValidIntermediateMove(ReadOnlyBoard, Tile, Token);`
    - Will return true if placing the given Tile on the ReadOnlyBoard to the location adjacent to the Token will violate any rules.

### Other classes

  - A ReadOnlyBoard is an interface defined in src/com/tsuro/board/ReadOnlyBoard.java
  - A Tile is an interface defined in src/com/tsuro/tile/Tile.java
  - A Token is an interface defined in src/com/tsuro/board/Token.java
  - A BoardLocation is a utility class defined in src/com/tsuro/board/BoardLocation.java
  

