import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TCPLabClient {


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

  private static void runClient(InputStreamReader serverIn, OutputStreamWriter serverOut,
      InputStreamReader userIn, OutputStreamWriter userOut, String name)
      throws IOException {

    try (JsonReader serverScan = new JsonReader(serverIn);
    JsonReader userScan = new JsonReader(userIn)) {
      serverOut.write(name);

      String internalName = serverScan.nextString();
      JsonArray serverCallMe = new JsonArray();
      serverCallMe.add("the server will call me");
      serverCallMe.add(internalName);

      userOut.write(serverCallMe.toString());

      // actually use TCPLabyrinth
    }

  }

}
