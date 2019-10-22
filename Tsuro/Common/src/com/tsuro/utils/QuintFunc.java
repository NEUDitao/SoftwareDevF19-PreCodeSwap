package com.tsuro.utils;

/**
 * A function that takes in parameters of type A, B, C, D, E, and returns a type of F.
 */
public interface QuintFunc<A, B, C, D, E, F> {

    /**
     * Method to implement for the {@link QuintFunc}
     */
    F apply(A a, B b, C c, D d, E e);

}
