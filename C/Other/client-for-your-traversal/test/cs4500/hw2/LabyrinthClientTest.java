package cs4500.hw2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import cs4500.hw2.json.NodePair;
import java.awt.Color;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import labyrinth.ColoredToken;
import labyrinth.Labyrinth;

class LabyrinthClientTest {

  static Gson gson = new Gson();
  private Function<List<NodePair>, Labyrinth> labyrinthFunction;
  private Function<Color, ColoredToken> tokenFunction;
  private LabyrinthClient labClient;
  private StringWriter output;


  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    output = new StringWriter();
    labyrinthFunction = mock(Function.class);
    tokenFunction = mock(Function.class);
    labClient = spy(new LabyrinthClient(null, output, tokenFunction, labyrinthFunction));
  }

  @org.junit.jupiter.api.Test
  void jsonLab() {
    when(labyrinthFunction.apply(any())).thenReturn(null);

    JsonArray jsa = new JsonArray(3);
    jsa.add("lab");

    NodePair np1 = new NodePair("Matthias", "Michael");
    jsa.add(gson.toJsonTree(np1));

    NodePair np2 = new NodePair("Michael", "Dustin");
    jsa.add(gson.toJsonTree(np2));

    labClient.jsonLab(jsa);

    LinkedList<NodePair> expected = new LinkedList<>();
    expected.add(np1);
    expected.add(np2);

    verify(labyrinthFunction).apply(expected);
  }

  @org.junit.jupiter.api.Test
  void jsonAdd() {
    ColoredToken ct = mock(ColoredToken.class);
    Labyrinth lab = mock(Labyrinth.class);
    when(labyrinthFunction.apply(any())).thenReturn(lab);
    when(tokenFunction.apply(any())).thenReturn(ct);

    JsonArray ja = new JsonArray(1);
    ja.add("lab");

    labClient.jsonLab(ja);

    ja = new JsonArray(3);
    ja.add("add");
    ja.add("red");
    ja.add("Matthias");
    labClient.jsonAdd(ja);

    verify(tokenFunction).apply(Color.RED);
    verify(lab).addColoredToken(ct, "Matthias");
  }

  @org.junit.jupiter.api.Test
  void jsonMove() throws IOException {

    ColoredToken ct = mock(ColoredToken.class);
    Labyrinth lab = mock(Labyrinth.class);

    when(labyrinthFunction.apply(any())).thenReturn(lab);
    when(tokenFunction.apply(any())).thenReturn(ct);
    // mock the client to return true
    when(lab.reaches(any(), any())).thenReturn(true);

    JsonArray ja = new JsonArray(1);
    ja.add("lab");

    labClient.jsonLab(ja);

    assertEquals(output.toString(), "");

    ja = new JsonArray(3);
    ja.add("move");
    ja.add("red");
    ja.add("Matthias");
    labClient.jsonMove(ja);

    assertEquals(output.toString(), "true");


    verify(lab).reaches(ct, "Matthias");

  }

  @org.junit.jupiter.api.Test
  void doJSONToken() throws IOException {

    ColoredToken ct = mock(ColoredToken.class);
    Labyrinth lab = mock(Labyrinth.class);

    when(labyrinthFunction.apply(any())).thenReturn(lab);
    when(tokenFunction.apply(any())).thenReturn(ct);

    JsonArray ja = new JsonArray(3);
    ja.add("move");
    ja.add("red");
    ja.add("Matthias");
    labClient.doJSONToken(ja);

    verify(labClient, never()).jsonMove(any());

    JsonArray jb = new JsonArray(1);
    jb.add("lab");

    labClient.doJSONToken(jb);

    verify(labClient).jsonLab(jb);

    labClient.doJSONToken(ja);

    verify(labClient).jsonMove(any());



  }

}