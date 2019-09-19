package Other;

import java.util.Scanner;
import src.JsonSort;

// Used tutorial seen here: https://www.baeldung.com/a-guide-to-java-sockets
public class TcpServer {

  protected ServerSocket serverSocket;
  protected Socket clientSocket;
  protected PrintStream out;
  protected Scanner in;

  public void connect() throws IOException {

    serverSocket = new ServerSocket(8000);
    clientSocket = serverSocket.accept();
    out = new PrintStream(clientSocket.getOutputStream());
    in = new Scanner(new InputStreamReader(clientSocket.getInputStream()));
  }

  public void stop() throws IOException {
    in.close();
    out.close();
    clientSocket.close();
    serverSocket.close();
  }

  public void read() {
    StringBuilder strBuilder = new StringBuilder();
    while (!serverSocket.isClosed()) {
      if (in.hasNextLine()) {
        strBuilder.append(in.nextLine());
      } else {
        try {
          serverSocket.close();
        } catch (IOException e) {
          System.exit(1);
        }
      }
    }
    System.setIn(new ByteArrayInputStream(strBuilder.toString().getBytes()));
    System.setOut(this.out);
    JsonSort.main(new String[]{"-up"});
  }

  public static void main(String[] argv) {
    try {
      TcpServer server = new TcpServer();
      server.connect();
      server.read();
      server.stop();
    } catch (IOException e) {
      System.exit(1);
    }
  }
}
