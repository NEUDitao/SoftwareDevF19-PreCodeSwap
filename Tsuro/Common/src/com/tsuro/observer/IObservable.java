package com.tsuro.observer;

public interface IObservable<T> {

  /**
   * Adds the given Observer to this Player.
   */
  void addObserver(IObserver<T> obs);

  /**
   * Removes the given Observer from this Player. Does not do anything if observer is not on the
   * Player.
   */
  void removeObserver(IObserver<T> obs);

}
