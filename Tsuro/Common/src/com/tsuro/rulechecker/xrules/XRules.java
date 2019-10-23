package com.tsuro.rulechecker.xrules;

import static com.tsuro.board.XBoard.statePatsToBoard;
import static com.tsuro.utils.GsonUtils.getTsuroGson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import com.tsuro.action.IntermediateAction;
import com.tsuro.board.IBoard;
import com.tsuro.board.XBoard;
import com.tsuro.board.statepat.IStatePat;
import com.tsuro.observer.TurnPat;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.rulechecker.TsuroRuleChecker;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Does the testing task of https://ccs.neu.edu/home/matthias/4500-f19/6.html
 */
public class XRules {

  /**
   * Reads input as specified in the form for the experimentation task of
   * https://ccs.neu.edu/home/matthias/4500-f19/5.html in standard in, and produces the results for
   * the testing task of https://ccs.neu.edu/home/matthias/4500-f19/6.html
   */
  public static void main(String[] args) {
    doStuff(System.in);
  }

  /**
   * Reads the given input stream and prints out whether or not the move is cheating.
   */
  static void doStuff(InputStream inputStream) {

    JsonStreamParser jsp = new JsonStreamParser(new InputStreamReader(inputStream));
    Gson g = getTsuroGson();

    JsonElement first = jsp.next();
    List<IStatePat> statePats = XBoard.getStatePats(g, first);
    IBoard b = statePatsToBoard(statePats);

    while (jsp.hasNext()) {
      TurnPat thePat = g.fromJson(jsp.next(), TurnPat.class);

      IRuleChecker daRules = new TsuroRuleChecker();

      Optional<IBoard> daBoard = new IntermediateAction(thePat.actionPat.tile)
          .doActionIfValid(daRules, b, thePat.actionPat.token, Arrays.asList(thePat.t1, thePat.t2));

      if (daBoard.isPresent()) {
        System.out.println("\"legal\"");
      } else {
        System.out.println("\"cheating\"");
      }
    }

  }

}
