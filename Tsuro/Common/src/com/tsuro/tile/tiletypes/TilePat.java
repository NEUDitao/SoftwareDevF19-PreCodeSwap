package com.tsuro.tile.tiletypes;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * Represents a TilePat as a Java Object
 */
@AllArgsConstructor
public class TilePat {

  @NonNull
  public final int index;
  @NonNull
  public final int degree;

}
