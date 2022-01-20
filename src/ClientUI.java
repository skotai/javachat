
import javax.swing.*;
import java.awt.event.*;

public class ClientUI extends JFrame {



    public     JButton start;
    public     JTextField send,name,ip,port;
    public static JFrame window;
    public     JTextArea show;
    public     Client socket;

    public ClientUI() {
        window = new JFrame("Client");
        window.setLayout(null);
        window.setBounds(200, 200, 500, 400);
        window.setResizable(false);

        //IP
        JLabel label = new JLabel("IP:");
        label.setBounds(30, 15, 50, 25);
        window.add(label);

        ip = new JTextField();
        ip.setBounds(55, 15, 60, 25);
        window.add(ip);

        //ポート
        JLabel label1 = new JLabel("ポート:");
        label1.setBounds(125, 15, 50, 25);
        window.add(label1);

        port = new JTextField();
        port.setBounds(170, 15, 40, 25);
        window.add(port);

        //名前
        JLabel names = new JLabel("名前:");
        names.setBounds(220, 15, 55, 25);
        window.add(names);

       name = new JTextField();
        name.setBounds(265, 15, 60, 25);
        window.add(name);

        //リンク
        start = new JButton("リンク開始");
        start.setBounds(355, 15, 95, 30);
        window.add(start);


        //メッセージを表示する場所
        show = new JTextArea();
        show.setBounds(15, 70, 450, 220);
        show.setEditable(false);
        show.setLineWrap(true);
        show.setWrapStyleWord(true);
        JScrollPane scrollPane1 = new JScrollPane(show);
        scrollPane1.setBounds(15, 70, 450, 220);
        window.add(scrollPane1);



        //メッセージ発送
        JLabel message = new JLabel("メッセージ:");
        message.setBounds(10, 315, 75, 30);
        window.add(message);

        send = new JTextField();
        send.setBounds(80, 320, 360, 20);
        send.setText(null);
        window.add(send);




        //「リンク開始」ボタンを押すと、クライアントを起動
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                socket = new Client(ClientUI.this);
                socket.init();
            }
        });

        //Enter押すと、メッセージ発送できる
        KeyListener key_Listener = new KeyListener()
        {
            public void keyTyped(KeyEvent e) {}
            public void keyReleased(KeyEvent e){}
            public void keyPressed(KeyEvent e){
                if(e.getKeyChar() == KeyEvent.VK_ENTER )
                {
                    String Name = name.getText();
                    socket.sendMsg(Name + "：" + send.getText());
                    send.setText("");
                }
            }
        };
        send.addKeyListener(key_Listener);


        window.setVisible(true);

    }

    public static void main(String[] args) {
        new ClientUI();
    }
}