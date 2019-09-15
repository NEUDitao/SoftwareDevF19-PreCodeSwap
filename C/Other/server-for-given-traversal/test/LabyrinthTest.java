import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for the Labyrinth interface.
 */
public class LabyrinthTest {

  private Token red;
  private Token blue;
  private Node n1;
  private Node n2;
  private Edge e1;
  private Node n3;
  private Node n4;
  private Edge e3;
  private Edge e2;
  private Edge e4;
  private Token yellow;
  private SimpleGraph l;


  @org.junit.jupiter.api.BeforeEach
  void setUp() {

    yellow = new Token();
    red = new Token();
    blue = new Token();
    n1 = new Node("Matthias", null);
    n2 = new Node("Michael", red);
    e1 = new Edge(n1, n2);
    n3 = new Node("Dustin", yellow);
    n4 = new Node("Dexter", blue);
    e2 = new Edge(n3, n2);
    e3 = new Edge(n2, n4);
    e4 = new Edge(n4, n1);
    l = new SimpleGraph(Arrays.asList(n1, n2, n3, n4), Arrays.asList(e1, e2, e3, e4));

  }

  @org.junit.jupiter.api.AfterEach
  void tearDown() {
  }

  @org.junit.jupiter.api.Test
  void createLabyrinth() {
    var temp = l.createLabyrinth(Arrays.asList(n1, n2, n3, n4));

    // testing if you can reach each other using a labyrinth from createLabyrinth
    assertTrue(temp.canReach(red, n2));
    assertFalse(temp.canReach(red, n4));

  }

  @org.junit.jupiter.api.Test
  void addToken() {

    var green = new Token();
    assertFalse(l.canReach(green, n1));
    l.addToken(green, n1);
    assertTrue(l.canReach(green, n1));
    assertEquals(n1.token, green);

    assertNotEquals(n4.token, green);
    l.addToken(green, n4);
    assertEquals(n4.token, green);

  }

  @org.junit.jupiter.api.Test
  void canReach() {
    var green = new Token();
    assertTrue(l.canReach(red, n2));
    assertTrue(l.canReach(yellow, n1));
    assertFalse(l.canReach(red, n3));
    assertFalse(l.canReach(blue, n3));
    assertFalse(l.canReach(green, n4));
  }


}
