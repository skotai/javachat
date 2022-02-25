import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ClientChat  implements Runnable {

	Socket socket = null;
	DataInputStream in = null;
	DataOutputStream out = null;
	ServerUI namep = null;
	chatUI chatp = null;
	Hashtable listTable;
	JLabel not;
	JPanel north, center;
	Thread thread;

	public  ClientChat() {
		int width = 800;
		int height = 800;
		listTable = new Hashtable();
		JFrame frame;

		frame=new JFrame("chat room");
		Container contentPane=frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		//System.out.println("ttt");
		//setLayout(new BorderLayout());
		namep = new ServerUI(listTable);
		int h = namep.getSize().height;
		chatp = new chatUI("", listTable, width, height - (h + 5));
		chatp.setVisible(false);
		not = new JLabel("connect to server...");
		not.setForeground(Color.red);
		north = new JPanel(new FlowLayout(FlowLayout.LEFT));
		center = new JPanel();
		north.add(namep);
		north.add(not);
		center.add(chatp);
		frame.add(north, BorderLayout.NORTH);
		frame.add(center, BorderLayout.CENTER);

		frame.setSize(1000,1000);
		frame.setVisible(true);
	}

	public void start() {
		if (socket != null && in != null && out != null) {
			try {
				socket.close();
				in.close();
				out.close();
				chatp.setVisible(false);

			} catch (Exception ee) {
			}
		}
		try {
			socket = new Socket("localhost", 6666);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ee) {
			not.setText("fail to connect");
		}
		if (socket != null) {
			InetAddress address = socket.getInetAddress();
			try{
				not.setText("connect to " + address);

				namep.setSocketConnection(socket, in, out);
				north.validate();
			}catch(NullPointerException e){
//				System.out.println("error");
			}
		}
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stop() {
		try {
			socket.close();
			thread = null;
		} catch (IOException e) {
			//this.showStatus(e.toString());
		}
	}

	public void run() {
		try{
			while (thread != null) {
				if (namep.geticc() == true) {
					chatp.setVisible(true);
					chatp.setName(namep.getName());
					chatp.setSocketConnection(socket, in, out);
					//not.setText(" ");
					center.validate();
					break;
				}
				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}
			}
		}catch(NullPointerException ne){

		}
	}

	public static void main(String[] args) throws NullPointerException{
		ClientChat cc=new ClientChat();

		cc.start();

	}


}
