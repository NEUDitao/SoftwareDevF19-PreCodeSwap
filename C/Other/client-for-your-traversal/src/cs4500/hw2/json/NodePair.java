package cs4500.hw2.json;

import com.google.gson.JsonArray;
import java.util.Objects;

/**
 * Represents a pair of nodes.
 */
public class NodePair {

  public final String from;
  public final String to;


  public NodePair(String from, String to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NodePair nodePair = (NodePair) o;
    return Objects.equals(from, nodePair.from) &&
        Objects.equals(to, nodePair.to);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to);
  }

  /**
   * Turns this nodePair into a JsonArray of two elements, the first being from second being two
   *
   * @return array of two elements
   */
  public JsonArray toJsonArray() {
    JsonArray out = new JsonArray();
    out.add(this.from);
    out.add(this.to);
    return out;
  }
}
