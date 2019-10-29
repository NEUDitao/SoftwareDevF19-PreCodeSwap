package com.tsuro.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.experimental.UtilityClass;

/**
 * A utility class with functions to run functions asynchrously.
 */
@UtilityClass
public class TimeoutUtils {

  final int ONE_SECOND = 1000;

  /**
   * Runs the given callable. If it takes longer than one second, terminate the thread and throw an error.
   *
   * @return the result of c, or throws an exception if the callable takes longer than a second
   */
  public static <T> T doFunctionForTime(Callable<T> c)
          throws InterruptedException, ExecutionException, TimeoutException {
    ExecutorService timeOutCatch = Executors.newSingleThreadExecutor();
    Future<T> future = timeOutCatch.submit(c);
    try {
      return future.get(ONE_SECOND, TimeUnit.MILLISECONDS);
    } finally {
      future.cancel(true);
      timeOutCatch.shutdown();
    }
  }

  /**
   * Runs the given runnable. If it takes longer than a second, terminate the thread, and throw an error.
   */
  public static void doFunctionForTime(Runnable r)
          throws InterruptedException, ExecutionException, TimeoutException {
    ExecutorService timeOutCatch = Executors.newSingleThreadExecutor();
    Future<?> future = timeOutCatch.submit(r);
    try {
      future.get(ONE_SECOND, TimeUnit.MILLISECONDS);
    } finally {
      future.cancel(true);
      timeOutCatch.shutdown();
    }
  }

}
