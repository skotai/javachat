
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
public class ServerUI extends JFrame {

    public static void main(String[] args) {
        new ServerUI();
    }
    public static JFrame window;
    public JButton start;//サーバ起動ボタン
    public JTextArea show;
    public Server server;
    public JTextField port;
    static List<Socket> clients;//サーバにアクセスしたクライアントを保存する
    public ServerUI() {
        window = new JFrame("Server");
        window.setLayout(null);
        window.setBounds(200, 200, 500, 400);
        window.setResizable(false);

        //ポート
        JLabel label1 = new JLabel("ポート:");
        label1.setBounds(180, 8, 50, 25);
        window.add(label1);
        port = new JTextField();
        port.setBounds(220, 8, 40, 25);
        window.add(port);


        //start  ボタン
        start = new JButton("リンク開始");
        start.setBounds(180, 300, 120, 50);
        window.add(start);

        //
        show = new JTextArea();
        show.setBounds(15, 70, 450, 220);
        show.setEditable(false);
        show.setLineWrap(true);
        show.setWrapStyleWord(true);
        JScrollPane scrollPane1 = new JScrollPane(show);
        scrollPane1.setBounds(15, 70, 450, 220);
        window.add(scrollPane1);


        //「サーバ起動」ボタン押すと、サーバが起動する
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                server = new Server(ServerUI.this);
            }
        });


        window.setVisible(true);
        show.setText("「サーバ起動」を押してサーバを起動できる\n");
    }


}