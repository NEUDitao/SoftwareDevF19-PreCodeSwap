package cs4500.hw2;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import cs4500.hw2.json.ColorString;
import cs4500.hw2.json.NodePair;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import labyrinth.ColoredToken;
import labyrinth.ColoredTokenImpl;
import labyrinth.Labyrinth;
import labyrinth.LabyrinthImpl;

/**
 * Generic handler of user inputs for labyrinth
 */
public class LabyrinthClient {

  private final Writer writer;
  private final Function<Color, ColoredToken> tokenFactory;
  private final Function<List<NodePair>, Labyrinth> labyrinthFactory;
  private final Reader reader;
  private Labyrinth currentLabyrinth = null;
  private Gson gson;

  public static void main(String[] args) throws IOException {
    try (Reader input = new InputStreamReader(System.in);
        Writer output = new OutputStreamWriter(System.out)) {
      LabyrinthClient client = new LabyrinthClient(input, output, ColoredTokenImpl::new,
          LabyrinthImpl::new);
      client.start();
    }
  }

  /**
   * Constructs a LabyrinthClient
   *
   * @param reader           Input from the user
   * @param writer           Output to the user
   * @param tokenFactory     Function that creates tokens given a color
   * @param labyrinthFactory Function that creates a labyrinth from a list of Nodepairs
   */
  public LabyrinthClient(Reader reader, Writer writer, Function<Color, ColoredToken> tokenFactory,
      Function<List<NodePair>, Labyrinth> labyrinthFactory) {
    this.reader = reader;
    this.writer = writer;
    this.tokenFactory = tokenFactory;
    this.labyrinthFactory = labyrinthFactory;
    gson = new Gson();
  }

  /**
   * Process a lab request
   */
  void jsonLab(JsonArray element) {
    Iterator<JsonElement> it = element.iterator();
    it.next(); // "lab"
    List<NodePair> nodePairs = new LinkedList<>();
    it.forEachRemaining(je -> nodePairs.add(gson.fromJson(je, NodePair.class)));
    currentLabyrinth = labyrinthFactory.apply(nodePairs);
  }

  /**
   * Process an add request
   */
  void jsonAdd(JsonArray array) {

    ColorString cst = gson.fromJson(array.get(1), ColorString.class);
    if (cst == null) {
      throw new NotARequestException("Color not valid");
    }
    String name = array.get(2).getAsString();
    currentLabyrinth.addColoredToken(tokenFactory.apply(cst.color), name);

  }

  /**
   * Process a move request
   */
  void jsonMove(JsonArray array) throws IOException {

    ColorString cst = gson.fromJson(array.get(1), ColorString.class);
    if (cst == null) {
      throw new NotARequestException("Color not valid");
    }
    String name = array.get(2).getAsString();
    boolean result = currentLabyrinth.reaches(tokenFactory.apply(cst.color), name);
    // writer.write(gson.toJson(result).toString());

  }

  /**
   * Process a generic request
   *
   * @param element A well-formed JSON request to be processed
   */
  public void doJSONToken(JsonElement element) throws IOException {
    if (element.isJsonArray()) {
      JsonArray array = element.getAsJsonArray();
      switch (array.get(0).getAsString()) {
        case "lab":
          if (currentLabyrinth != null) {
            throw new NotARequestException("Second labyrinth");
          } else {
            jsonLab(array);
          }
          break;
        case "add":
          if (currentLabyrinth != null) {
            jsonAdd(array);
          } else {
            throw new NotARequestException("No labyrinth yet");
          }
          break;
        case "move":
          if (currentLabyrinth != null) {
            jsonMove(array);
          } else {
            throw new NotARequestException("No labyrinth yet");
          }
          break;
        default:
          throw new NotARequestException("Given Json Array did not start with correct shtuff");
      }
    } else {
      throw new NotARequestException("Given Json was not an array");
    }
  }

  private void start() throws IOException { // unused
    JsonStreamParser jsp = new JsonStreamParser(this.reader);

    while (jsp.hasNext()) {
      doJSONToken(jsp.next());
    }
  }
}
