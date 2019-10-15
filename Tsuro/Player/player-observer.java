import static com.tsuro.board.XBoard.statePatsToBoard;
import static com.tsuro.utils.GsonUtils.getTsuroGson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import com.tsuro.action.IAction;
import com.tsuro.action.IntermediateAction;
import com.tsuro.board.BoardPanel;
import com.tsuro.board.IBoard;
import com.tsuro.board.XBoard;
import com.tsuro.board.statepat.IStatePat;
import com.tsuro.observer.TurnPat;
import com.tsuro.player.PlayerState;
import com.tsuro.rulechecker.HackerIdealRuleChecker;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import lombok.NonNull;


class PlayerObserver implements ObserverInterface<PlayerState> {


  @NonNull
  private final JFrame frame;
  @NonNull
  private final BoardPanel panel;

  public PlayerObserver() {
    this.frame = new JFrame();
    this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    panel = new BoardPanel();
    frame.add(panel);
  }

  @Override
  public void update(PlayerState state) {

    @NonNull final IAction actionPat = state.action;

    Optional<IBoard> newBoard = actionPat
        .doActionIfValid(new HackerIdealRuleChecker(), state.board, state.token, state.hand);

    panel.drawBoard(
        newBoard.orElseThrow(() -> new IllegalStateException("Board has invalid rules")));

    frame.pack();
    frame.setSize(1000, 1000);
    frame.setVisible(true);
  }

}

class RObserver {

  public static void main(String[] args) throws IOException {

    doStuff(System.in);
    /*
    if (args.length != 2) {
      throw new IllegalArgumentException("Proper usage is ./robserver ip-address port");
    }
    String ipAddress = args[0];
    String port = args[1];

    @Cleanup Socket s = new Socket(ipAddress, Integer.parseInt(port));

    doStuff(s.getInputStream());*/
  }

  static void doStuff(InputStream inputStream) {
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