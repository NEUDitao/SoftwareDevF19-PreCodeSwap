import cs4500.hw2.StrJParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;


public class TCPStrJParse {

  public final static int portNumber = 8000;

  public static void main(String argv[]) throws IOException {

    try( ServerSocket sSocket = new ServerSocket(portNumber)){

      try(Socket socket = sSocket.accept()){

        InputStream stream = socket.getInputStream();

        InputStreamReader reader = new InputStreamReader(stream);

        //char[] chars = new char[1000];

        //reader.read(chars);

        String json = StrJParser.testableMain(reader, true);

        OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());

        writer.write(json);
        System.out.println(json);
      }

    }

  }
}
