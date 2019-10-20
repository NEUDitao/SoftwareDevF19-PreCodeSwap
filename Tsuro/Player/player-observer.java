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
import com.tsuro.observer.IObserver;
import com.tsuro.observer.TurnPat;
import com.tsuro.player.HandPanel;
import com.tsuro.player.PlayerState;
import com.tsuro.rulechecker.HackerIdealRuleChecker;
import com.tsuro.utils.RenderUtils;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import lombok.Cleanup;
import lombok.NonNull;

/**
 * A Graphical {@link IObserver} that watches a Player and renders the move they perform.
 */
class PlayerObserver implements IObserver<PlayerState> {


  @NonNull
  private final JFrame frame;
  @NonNull
  private final BoardPanel panel;
  @NonNull
  private final HandPanel handPanel;
  @NonNull
  private final JPanel overallPanel;

  /**
   * Instantiates defaults for a {@link JFrame} and {@link BoardPanel} for rendering the state of
   * the game that this Player is acting on
   */
  public PlayerObserver() {
    this.frame = new JFrame();
    this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    overallPanel = new JPanel(new GridBagLayout());
    frame.add(overallPanel);

    GridBagConstraints boardConstraints = RenderUtils.createDefaultGBC(0, 0);
    boardConstraints.weightx = 2;
    panel = new BoardPanel();
    overallPanel.add(panel, boardConstraints);

    GridBagConstraints handConstraints = RenderUtils.createDefaultGBC(1, 0);
    handPanel = new HandPanel();
    overallPanel.add(handPanel, handConstraints);
  }

  @Override
  public void update(PlayerState state) {

    @NonNull final IAction actionPat = state.action;

    Optional<IBoard> newBoard = actionPat
        .doActionIfValid(new HackerIdealRuleChecker(), state.board, state.token, state.hand);

    IBoard theNewBoard = newBoard
        .orElseThrow(() -> new IllegalStateException("Board has invalid rules"));

    List<Point> highlightedPoints = getDifferences(state.board, theNewBoard);

    panel.drawBoard(theNewBoard, highlightedPoints);

    handPanel.paintHand(state.hand);

    frame.pack();
    frame.setSize(1500, 1000);
    frame.setVisible(true);
  }

  /**
   * Returns a List of the Points that have differences between the old and new board.
   */
  private List<Point> getDifferences(IBoard oldBoard, IBoard theNewBoard) {

    List<Point> highlightedPoints = new ArrayList<>();

    for (int x = 0; x < theNewBoard.getSize().width; x++) {
      for (int y = 0; y < theNewBoard.getSize().height; y++) {
        if (!oldBoard.getTileAt(x, y).equals(theNewBoard.getTileAt(x, y))) {
          highlightedPoints.add(new Point(x, y));
        }
      }
    }

    return highlightedPoints;
  }

}

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

    doStuff(s.getInputStream());
  }

  /**
   * Reads the given input stream and creates a player observer based off the move.
   */
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