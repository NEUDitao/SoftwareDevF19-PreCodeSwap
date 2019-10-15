package com.tsuro.observer;

import com.tsuro.action.actionpat.ActionPat;
import com.tsuro.tile.ITile;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * Represents a https://ccs.neu.edu/home/matthias/4500-f19/5.html#%28tech._turn._pat%29 as a Java
 * Object.
 */
@AllArgsConstructor
public class TurnPat {

  @NonNull
  public final ActionPat actionPat;
  @NonNull
  public final ITile t1;
  @NonNull
  public final ITile t2;

}
