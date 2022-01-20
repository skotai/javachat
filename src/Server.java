
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
public class Server extends Thread{

    ServerUI ui;
    ServerSocket ss;
    BufferedReader reader;
    PrintWriter writer;

    public Server(ServerUI ui) {
        this.ui = ui;
        this.start();
    }

    @Override
    public void run() {
        try {
            int port = Integer.parseInt(ui.port.getText()); //入力したポートを取得する
            ss = new ServerSocket(port);
            ui.clients=new ArrayList<>();
            println("サーバ起動した、ポート:"+port);
            while (true) {
                Socket client = ss.accept();
                ui.clients.add(client);
                new ClientListen(ui, client);
            }
        } catch (IOException e) {
            println(e.toString());
            e.printStackTrace();
        }

    }

    public void println(String s) {
        if (s != null) {
            this.ui.show.setText(this.ui.show.getText() + s + "\n");
            System.out.println(s + "\n");
        }
    }


}