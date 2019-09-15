package cs4500.hw2;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import cs4500.hw2.json.NodePair;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import labyrinth.Labyrinth;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

class LabyrinthClientTest {

  static Gson gson = new Gson();

  @org.junit.jupiter.api.Test
  void jsonLab() {
    Function<List<NodePair>, Labyrinth> labyrinthFunction = mock(Function.class);
    when(labyrinthFunction.apply(any())).thenReturn(null);
    LabyrinthClient lc = new LabyrinthClient(null, null, null, labyrinthFunction);

    JsonArray jsa = new JsonArray(3);
    jsa.add("lab");

    NodePair np1 = new NodePair("Matthias", "Michael");
    jsa.add(gson.toJsonTree(np1));

    NodePair np2 = new NodePair("Michael", "Dustin");
    jsa.add(gson.toJsonTree(np2));

    lc.jsonLab(jsa);

    LinkedList<NodePair> expected = new LinkedList<>();
    expected.add(np1);
    expected.add(np2);

    verify(labyrinthFunction).apply(expected);
  }

  @org.junit.jupiter.api.Test
  void jsonAdd() {
  }

  @org.junit.jupiter.api.Test
  void jsonMove() {
  }

  @org.junit.jupiter.api.Test
  void doJSONToken() {
  }

  @org.junit.jupiter.api.Test
  void start() {
  }
}