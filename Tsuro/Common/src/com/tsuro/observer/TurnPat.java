package com.tsuro.observer;

import com.tsuro.action.actionpat.ActionPat;
import com.tsuro.tile.ITile;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class TurnPat {

  @NonNull
  public final ActionPat actionPat;
  @NonNull
  public final ITile t1;
  @NonNull
  public final ITile t2;

}
