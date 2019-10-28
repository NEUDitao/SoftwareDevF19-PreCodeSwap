package com.tsuro.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeoutUtils {

  public static <T> T doFunctionForTime(Callable<T> c)
      throws InterruptedException, ExecutionException, TimeoutException {
    ExecutorService timeOutCatch = Executors.newSingleThreadExecutor();
    Future<T> future = timeOutCatch.submit(c);
    try {
      return future.get(1000, TimeUnit.MILLISECONDS);
    } finally {
      future.cancel(true);
    }
  }

  public static void doFunctionForTime(Runnable r)
      throws InterruptedException, ExecutionException, TimeoutException {
    ExecutorService timeOutCatch = Executors.newSingleThreadExecutor();
    Future<?> future = timeOutCatch.submit(r);
    try {
      future.get(1000, TimeUnit.MILLISECONDS);
    } finally {
      future.cancel(true);
    }
  }

}
