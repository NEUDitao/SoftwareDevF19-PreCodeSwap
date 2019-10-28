package com.tsuro.utils;

/**
 * A function that takes in parameters of type A, B, C, D, and returns a type of F.
 */
public interface QuadFunc<A, B, C, D, F> {

  /**
   * Method to implement for the {@link QuintFunc}
   */
  F apply(A a, B b, C c, D d);

}
