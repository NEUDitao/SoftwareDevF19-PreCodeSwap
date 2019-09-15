package cs4500.hw2.json;

import java.util.Objects;

public class NodePair {
  public final String from;
  public final String to;
  public NodePair(String from, String to){
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
}
