import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonStreamParser;
import cs4500.hw2.LabyrinthClient;
import cs4500.hw2.NotARequestException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import labyrinth.ColoredTokenImpl;

/**
 * Main class for facilitating communication between user inputs and the server
 */
public class TCPLabClient {

  static Gson gson = new Gson();

  public static void main(String[] args) throws IOException {

    String address = "LOCALHOST";
    int port = 8000;
    String name = "John Doe";
    if (args.length >= 1) {
      address = args[0];
    }

    if (args.length >= 2) {
      port = Integer.parseInt(args[1]);
    }

    if (args.length >= 3) {
      name = args[2];
    }

    Socket s = new Socket(address, port);

    InputStreamReader in = new InputStreamReader(s.getInputStream());

    OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());

    runClient(in, out, new InputStreamReader(System.in), new OutputStreamWriter(System.out), name);
  }

  /**
   * Main function to run the client for labyrinths
   *
   * @param serverIn  The stream reading from the server
   * @param serverOut The stream outputting to the server
   * @param userIn    The stream reading from the user
   * @param userOut   The stream outputting to the user
   * @param name      The name that the user chooses
   */
  public static void runClient(Reader serverIn, Writer serverOut,
      Reader userIn, Writer userOut, String name)
      throws IOException {

    JsonStreamParser serverScan = new JsonStreamParser(serverIn);
    JsonStreamParser userScan = new JsonStreamParser(userIn);
    PrintWriter printOut = new PrintWriter(userOut);

    // escapes quotes
    serverOut.write(gson.toJsonTree(name).toString());
    serverOut.flush();

    String internalName = serverScan.next().getAsString();
    JsonArray serverCallMe = new JsonArray();
    serverCallMe.add("the server will call me");
    serverCallMe.add(internalName);

    userOut.write(serverCallMe.toString());

    LabyrinthClient labClient = new LabyrinthClient(userIn, userOut, ColoredTokenImpl::new, l -> {
      return new TCPLabyrinth(l, serverScan, serverOut, printOut);
    });

    try {
      while (userScan.hasNext()) {
        JsonElement element = userScan.next();
        try {
          labClient.doJSONToken(element);
        } catch (NotARequestException e) {
          JsonArray errorMessage = new JsonArray();
          errorMessage.add("not a request");
          errorMessage.add(element);
          printOut.println(errorMessage.toString());
        }
      }
    } catch (JsonIOException e) {
      // Gson throws exceptions when there's nothing coming in
    }

  }

}
