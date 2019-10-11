package com.tsuro.rulechecker;


import java.util.function.Supplier;

/**
 * Rules.PP file required for assignment 3
 */
public class Rules {

  /**
   * Makes a new {@link RuleChecker} given a function that makes {@link RuleChecker}s
   *
   * Example usage: {@code makeRules(TsuroRuleChecker::new);}
   */
  public static RuleChecker makeRules(Supplier<RuleChecker> ruleCheckerSupplier) {
    return ruleCheckerSupplier.get();
  }

}
