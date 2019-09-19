import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

class TCPLabClientTest {

  private Reader read;
  private Writer write;
  private Reader serverReader;
  private Writer serverWriter;
  private final String labFromClient = "[\"lab\", {\"from\" : \"a\", \"to\": \"b\"}, {\"from\" : \"b\", \"to\" : \"c\"}, {\"from\" : \"d\", \"to\": \"b\"}]\n";
  private final String labForServer = "[\"lab\", [\"a\", \"b\", \"c\", \"d\"], [[\"a\", \"b\"], [\"b\", \"c\"], [\"d\", \"b\"]]]\n";


  private final String nameForServer = "\"John Doe\"";
  private final String serverExpectedPrint = "[\"the server will call me\",\"sJohnDoe\"]";

  private final String fromServerNameReturn = "\"sJohnDoe\"";

  @Retention(RetentionPolicy.RUNTIME)
  @interface UserInput {

    public String value() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  @interface ServerOutput {

    public String value() default "";
  }


  @BeforeEach
  void setUp(TestInfo info) {

    UserInput userInput = null;
    ServerOutput serverOutput = null;

    if (info.getTestMethod().isPresent()) {

      Method m = info.getTestMethod().get();
      userInput = m.getAnnotation(UserInput.class);
      serverOutput = m.getAnnotation(ServerOutput.class);
    }

    read = new StringReader(userInput == null ? "" : userInput.value());
    write = new StringWriter();

    serverReader = new StringReader(userInput == null ? "" : serverOutput.value());
    serverWriter = new StringWriter();
  }

  /**
   * Turns a string containing json into a list of distinct json elements
   */
  static List<JsonElement> jsonElementCreator(String jsons) {

    JsonStreamParser jsp = new JsonStreamParser(jsons);

    ArrayList<JsonElement> returnable = new ArrayList<>();

    jsp.forEachRemaining(returnable::add);

    return returnable;
  }

  /**
   * Takes two strings containing possibly multiple json elements and determines whether they
   * contain the same elements in the same order
   */
  static void assertJsonEquals(String expected, String actual) {
    List<JsonElement> expectedList = jsonElementCreator(expected);
    List<JsonElement> actualList = jsonElementCreator(actual);

    assertEquals(expectedList, actualList);
  }

  /**
   * Given expected output to the server and output to the user, run a standard suite of tests
   */
  void runTests(String expectedServerIn, String expectedUserOut) throws IOException {

    TCPLabClient.runClient(serverReader, serverWriter, read, write, "John Doe");

    assertJsonEquals(expectedServerIn, serverWriter.toString());
    assertJsonEquals(expectedUserOut, write.toString());
  }

  @org.junit.jupiter.api.Test
  @UserInput("")
  @ServerOutput(fromServerNameReturn)
  void runClient() throws IOException {

    runTests(nameForServer, serverExpectedPrint);

  }

  private final String clientAdd1 = "[\"add\", \"red\", \"a\"][\"add\",\"black\",\"b\"][\"add\",\"green\",\"f\"]";
  private final String clientMove1 = "[\"move\",\"red\",\"b\"]";

  private final String clientQuery1 = clientAdd1 + clientMove1;
  private final String serverResponse1 = "[[\"add\",\"green\",\"f\"], true]";
  private final String expectedServerQuery1 =
      "[" + labForServer + "," + "[\"add\", \"red\", \"a\"],"
          + "[\"add\",\"black\",\"b\"],[\"add\",\"green\",\"f\"],[\"move\",\"red\",\"b\"]" + "]";
  private final String expectedUserResponse1 = "[\"invalid\", [\"add\",\"green\", \"f\"]]\n"
      + "[\"the response to\", [\"move\", \"red\", \"b\"], \"is\", true]";

  @org.junit.jupiter.api.Test
  @UserInput(labFromClient + clientQuery1)
  @ServerOutput(fromServerNameReturn + serverResponse1)
  void testTrueMove() throws IOException {
    runTests(nameForServer + expectedServerQuery1, serverExpectedPrint + expectedUserResponse1);
  }

  @org.junit.jupiter.api.Test
  @UserInput(labFromClient + clientAdd1)
  @ServerOutput(fromServerNameReturn)
  void testNoMoveyNoSendy() throws IOException {
    runTests(nameForServer, serverExpectedPrint);
  }


  private final String clientQuery2 = "[\"move\", \"black\", \"d\"]";
  private final String serverResponse2 = "[false]";
  private final String expectedServerQuery2 =
      nameForServer + "[" + labForServer + "," + clientQuery2 + "]";
  private final String theAnswerIs2 = "[\"the response to\", [\"move\", \"black\", \"d\"], \"is\", false]";
  private final String expectedUserResponse2 =
      serverExpectedPrint + theAnswerIs2;

  @org.junit.jupiter.api.Test
  @UserInput(labFromClient + clientQuery2)
  @ServerOutput(fromServerNameReturn + serverResponse2)
  void testFalseMove() throws IOException {
    runTests(expectedServerQuery2, expectedUserResponse2);
  }

  /**
   * Wrapper to make "not a request" arrays
   */
  static String notARequestExpected(String inner) {
    return "[\"not a request\", " + inner + "]";
  }

  private final String clientQueryBreak1 = "[\"lab\", {\"from\" : \"f\", \"to\" : \"g\"}]";
  private final String clientQueryBreak2 = "\"a\"";
  private final String clientQueryBreak3 = " 4 ";
  private final String clientQueryBreak4 = "[\"ab\", {\"from\" : \"f\", \"to\" : \"g\"}]";
  private final String clientQueryBreak5 = "[\"move\", \"Dr. Racket\", \"d\"]";
  private final String clientQueryBreak6 = "[\"add\", \"pink\", \"a\"]";

  private final List<String> clientQueryBreakList =
      Arrays.asList(clientQueryBreak1, clientQueryBreak2, clientQueryBreak3, clientQueryBreak4,
          clientQueryBreak5, clientQueryBreak6);

  private final String clientQueryBreak =
      clientQueryBreak1 + clientQueryBreak2 + clientQueryBreak3 + clientQueryBreak4 +
          clientQueryBreak5 + clientQueryBreak6;

  @org.junit.jupiter.api.Test
  @UserInput(clientQuery2 + labFromClient + clientQueryBreak + clientQuery2)
  @ServerOutput(fromServerNameReturn + serverResponse2)
  void testClientBreak() throws IOException {
    String expectedPrint = clientQueryBreakList.stream().map(TCPLabClientTest::notARequestExpected)
        .reduce("", String::concat);

    runTests(expectedServerQuery2,
        serverExpectedPrint + notARequestExpected(clientQuery2) + expectedPrint + theAnswerIs2);
  }

}