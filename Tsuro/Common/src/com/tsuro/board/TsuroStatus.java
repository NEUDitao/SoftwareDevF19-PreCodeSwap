package com.tsuro.board;

/**
 * Represents a property of the last move that was taken on a {@link Board}.
 */
public enum TsuroStatus {
  INIT_TILE_NOT_ON_EDGE_BOARD,
  INIT_TILE_TOUCHING_ANY,
  INIT_TOKEN_SUICIDE,
  CONTAINS_LOOP,
  INTERMEDIATE_TOKEN_SUICIDE // :'(. Don't commit suicide. If you need help, reach out. SWD ain't that bad
}
