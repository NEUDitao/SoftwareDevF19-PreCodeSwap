package com.tsuro.observer;

import static com.tsuro.utils.TimeoutUtils.doFunctionForTime;

import java.util.ArrayList;
import java.util.List;

public abstract class AObserverable<T> implements IObservable<T> {

  private List<IObserver<T>> observers = new ArrayList<>();

  @Override
  public void addObserver(IObserver<T> obs) {
    this.observers.add(obs);
  }

  @Override
  public void removeObserver(IObserver<T> obs) {
    this.observers.remove(obs);
  }

  /**
   * Notifies all observer on this Player with the given State.
   */
  protected void notifyObservers(T state) {
    for (IObserver<T> obs : this.observers) {
      try {
        doFunctionForTime(() -> obs.update(state));
      } catch (Exception e) {
        // they're behaving badly, so they should be terminated
        this.removeObserver(obs);
      }

    }
  }


}
