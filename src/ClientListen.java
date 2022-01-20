
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientListen extends Thread{

    BufferedReader reader;
    PrintWriter writer;
    ServerUI ui;
    Socket socket;

    public ClientListen(ServerUI ui, Socket socket) {
        this.ui = ui;
        this.socket=socket;
        this.start();
    }
    @Override
    public void run() {
        String msg;
        while (true) {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
                msg = reader.readLine();
                send_messag(msg+"");
            } catch (IOException e) {
                println(e.toString());
                break;
            }
        }
    }

    //すべてのクライアントにメッセージを送る。
    public synchronized void send_messag(String msg) {
        try {
            for (int i = 0; i < ui.clients.size(); i++) {
                Socket client = ui.clients.get(i);
                writer = new PrintWriter(client.getOutputStream(), true);
                writer.println(msg);
            }

        } catch (Exception e) {
            println(e.toString());
        }
    }

    public void println(String s) {
        if (s != null) {
            this.ui.show.setText(this.ui.show.getText() + s + "\n");
            System.out.println(s + "\n");
        }
    }
}