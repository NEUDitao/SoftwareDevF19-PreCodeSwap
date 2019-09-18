import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import cs4500.hw2.json.ColorString;
import cs4500.hw2.json.NodePair;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import labyrinth.ColoredToken;
import labyrinth.Labyrinth;

/**
 * An implementation of Labyrinth that sends valid messages to a Labyrinth server.
 */
public class TCPLabyrinth implements Labyrinth {

  private final List<NodePair> nodePairs;
  private final JsonStreamParser serverIn;
  private final Writer serverOut;
  private final PrintWriter userOut;
  List<JsonElement> outgoingMessageQ = new LinkedList<>();
  Gson gson = new Gson();
  private final JsonElement lab;

  /**
   * Constructor that takes in input/output from server, and writes to user.
   * @param nodePairs
   * @param serverIn
   * @param serverOut
   * @param userOut
   */
  public TCPLabyrinth(List<NodePair> nodePairs, JsonStreamParser serverIn, Writer serverOut,
      PrintWriter userOut) {

    this.nodePairs = nodePairs;
    this.serverIn = serverIn;
    this.serverOut = serverOut;
    this.userOut = userOut;

    Set<String> nodeNames = new HashSet<>();

    for (NodePair n : nodePairs) {
      nodeNames.add(n.from);
      nodeNames.add(n.to);
    }

    JsonArray nodeNamesArray = new JsonArray();
    nodeNames.forEach(nodeNamesArray::add);

    JsonArray nodePairsArray = new JsonArray();
    nodePairs.stream().map(NodePair::toJsonArray).forEach(nodePairsArray::add);

    JsonArray message = new JsonArray();
    message.add("lab");
    message.add(nodeNamesArray);
    message.add(nodePairsArray);

    this.lab = message;

  }

  @Override
  public void addColoredToken(ColoredToken token, String name) {

    JsonArray message = new JsonArray();
    message.add("add");
    message.add(gson.toJsonTree(ColorString.fromColor(token.getColor())));
    message.add(name);
    outgoingMessageQ.add(message);

  }

  @Override
  public boolean reaches(ColoredToken token, String name) {
    JsonArray message = new JsonArray();
    message.add("move");
    message.add(gson.toJsonTree(ColorString.fromColor(token.getColor())));
    message.add(name);
    outgoingMessageQ.add(message);

    outgoingMessageQ.add(0, this.lab);

    boolean returnable = false;

    try {
      serverOut.write(outgoingMessageQ.toString());
      serverOut.flush();

      JsonElement messageFromServer = serverIn.next();

      for (JsonElement e : messageFromServer.getAsJsonArray()) {
        JsonArray tempMessage = new JsonArray();
        if (e.isJsonPrimitive()) {
          tempMessage.add("the response to");
          tempMessage.add(message);
          tempMessage.add("is");
          tempMessage.add(e);
          returnable = e.getAsBoolean();
        } else {
          tempMessage.add("invalid");
          tempMessage.add(e);
        }
        userOut.write(tempMessage.toString());
      }

    } catch (IOException e) {
      userOut.write("sending message failed. Exiting program");
      userOut.flush();
      System.exit(1);
    }

    return returnable;
  }
}
