package com.tsuro.observer.robserver;

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
import com.tsuro.player.PlayerState;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import lombok.Cleanup;

/**
 * A class for running a graphical {@link PlayerObserver}.
 */
class RObserver {

  /**
   * Takes an ip address and port to read Json input specified by the experimentation task at
   * https://ccs.neu.edu/home/matthias/4500-f19/5.html#%28tech._turn._pat%29
   *
   * @throws IOException If the Socket fails to connect to the IP and port.
   */
  public static void main(String[] args) throws IOException {

    if (args.length != 2) {
      throw new IllegalArgumentException("Proper usage is ./robserver ip-address port");
    }
    String ipAddress = args[0];
    String port = args[1];

    @Cleanup Socket s = new Socket(ipAddress, Integer.parseInt(port));

    doStuff(s.getOutputStream(), s.getInputStream());
  }

  /**
   * Reads the given input stream and creates a player observer based off the move.
   */
  static void doStuff(OutputStream outputStream, InputStream inputStream) {
    PrintWriter printWriter = new PrintWriter(outputStream);
    printWriter.println("\"Eddiey and Notorious Daniel\"");
    printWriter.flush();

    JsonStreamParser jsp = new JsonStreamParser(new InputStreamReader(inputStream));
    Gson g = getTsuroGson();

    JsonElement first = jsp.next();
    List<IStatePat> statePats = XBoard.getStatePats(g, first);
    IBoard b = statePatsToBoard(statePats);

    TurnPat thePat = g.fromJson(jsp.next(), TurnPat.class);

    PlayerObserver observer = new PlayerObserver();
    observer.update(new PlayerState(new IntermediateAction(thePat.actionPat.tile),
        Arrays.asList(thePat.t1, thePat.t2), thePat.actionPat.token, b));
  }

}
