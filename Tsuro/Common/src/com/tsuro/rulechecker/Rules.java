package com.tsuro.rulechecker;


import java.util.function.Supplier;
import lombok.NonNull;

/**
 * Rules.PP file required for assignment 3
 */
public class Rules {

  /**
   * Makes a new {@link IRuleChecker} given a function that makes {@link IRuleChecker}s
   *
   * Example usage: {@code makeRules(TsuroRuleChecker::new);}
   */
  public static IRuleChecker makeRules(@NonNull Supplier<IRuleChecker> ruleCheckerSupplier) {
    return ruleCheckerSupplier.get();
  }

}
