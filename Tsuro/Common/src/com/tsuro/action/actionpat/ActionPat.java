package com.tsuro.action.actionpat;

import com.tsuro.board.IToken;
import com.tsuro.tile.ITile;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * Represents an ActionPat which has a IToken and the Tile that it places.
 */
@AllArgsConstructor
public class ActionPat {

  @NonNull
  public final IToken token;
  @NonNull
  public final ITile tile;

}
