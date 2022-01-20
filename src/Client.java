import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread {

    ClientUI ui;
    Socket client;
    BufferedReader reader;
    PrintWriter writer;

    public Client(ClientUI ui) {
        this.ui = ui;
    }
    public void sup(ClientUI ui){
        try {
            String ip = ui.ip.getText(); //ipを取得する
            int port = Integer.parseInt(ui.port.getText()); //ポートを取得する
            client = new Socket(ip, port);//サーバにアクセスするIPとポートをセット
            println("接続成功\n");
            println("ポート:"+port);
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            writer = new PrintWriter(client.getOutputStream(), true);
            String name = ui.name.getText();
            sendMsg("Welcome:" + name);
        } catch (NumberFormatException nu) {
            println("エラー：正しいIPとポートを入力してください");
            nu.printStackTrace();
        } catch (IOException e) {
            println("エラー：正しいIPとポートを入力してください");
            println(e.toString());
            e.printStackTrace();
        }
        this.start();
    }
    public void init(){
        sup(ui);
    }
    public void run() {
        String msg = "";
        while (true) {
            try {
                //メッセージの読み込み
                msg = reader.readLine();
            } catch (IOException e) {
                println("サーバから切り離した");

                break;
            }
            if (msg != null && msg.trim() != "") {
                println(msg);
            }
        }
    }
    //入力したメッセージを見れるように
    public void sendMsg(String msg) {
        try {
            writer.println(msg);
        } catch (Exception e) {
            println(e.toString());
        }
    }
    //UIに文字列を表示できるメソッド
    public void println(String s) {
        if (s != null) {
            this.ui.show.setText(this.ui.show.getText() + s + "\n");
            System.out.println(s + "\n");
        }
    }

}