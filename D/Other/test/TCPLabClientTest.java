import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

class TCPLabClientTest {

  private Reader read;
  private Writer write;
  private Reader serverReader;
  private Writer serverWriter;
  private final String labFromClient = "[\"lab\", {\"from\" : \"a\", \"to\": \"b\"}, {\"from\" : \"b\", \"to\" : \"c\"}, {\"from\" : \"d\", \"to\": \"b\"}]\n";
  private final String labForServer = "[\"lab\", [\"a\", \"b\", \"c\", \"d\"], [[\"a\", \"b\"], [\"b\", \"c\"], [\"d\", \"b\"]]]\n";

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

  @org.junit.jupiter.api.Test
  @UserInput("")
  @ServerOutput("")
  void runClient() {

  }


}