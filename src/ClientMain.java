import client.ClientView;
import client.Client;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;

public class ClientMain {
  public static void main(String args[]) {
    ClientView cv = new ClientView();
    Client c = new Client();
    cv.setClientThread(c);
    c.setClientViewThread(cv);
    cv.start();
    c.start();
  }
}
