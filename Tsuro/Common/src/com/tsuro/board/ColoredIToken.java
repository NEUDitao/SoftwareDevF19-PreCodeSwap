package com.tsuro.board;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;


/**
 * Represents a {@link IToken} with a Color.
 */
@AllArgsConstructor
@EqualsAndHashCode
public class ColoredIToken implements IToken {

  @NonNull
  public final ColorString color;

}
