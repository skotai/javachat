import server.ChatServer;
import server.FileServer;
import java.io.IOException;

public class ServerMain {
  public static void main(String args[]) {

    try {
      int port = 8888;
      ChatServer cs = new ChatServer(port);
      FileServer fs = new FileServer(port + 1);
      cs.start();
      fs.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
