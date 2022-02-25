


import java.awt.*;
import java.net.*;
import java.awt.event.*;
import java.io.*;
import java.util.Hashtable;

public class ServerUI extends Panel implements ActionListener, Runnable {
	TextField nameFile = null;
	TextField ipFile = null;
	String name = null;
	Checkbox male = null, female = null;
	CheckboxGroup group = null;
	Button ec = null, qc = null;
	Button ss=null;
	Socket socket = null;
	DataInputStream in = null;
	DataOutputStream out = null;
	Thread thread = null;
	boolean icc = false;
	Hashtable listTable;

	public ServerUI(Hashtable listTable) {
		this.listTable = listTable;
		nameFile = new TextField(10);
		//-------------
		ipFile=new TextField(20);
		//-------------
		group = new CheckboxGroup();
		male = new Checkbox("male", true, group);
		female = new Checkbox("female", false, group);
		ec = new Button("connect");
		qc = new Button("quit");
		ec.addActionListener(this);
		qc.addActionListener(this);
		thread = new Thread(this);
		add(new Label("name:"));
		add(nameFile);
		add(male);
		add(female);
		add(ec);
		add(qc);
		qc.setEnabled(false);
	}

	public void seticc(boolean b) {
		icc = b;
	}

	public boolean geticc() {
		return icc;
	}

	public String getName() {
		return name;
	}

	public void setName(String s) {
		name = s;
	}

	public void setSocketConnection(Socket socket, DataInputStream in,
			DataOutputStream out) {

		this.socket = socket;
		this.in = in;
		this.out = out;
		try {
			thread.start();
		} catch (Exception e) {
			nameFile.setText("" + e);
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ec) {
			qc.setEnabled(true);
			if (icc == true) {
				nameFile.setText("chatting:" + name);
			} else {
				this.setName(nameFile.getText());
				String sex = group.getSelectedCheckbox().getLabel();
				if (socket != null && name != null) {
					try {
						out.writeUTF("name:" + name + " gender:" + sex);
					} catch (IOException ee) {
						nameFile.setText("S_thread connection" + ee);
					}
				}
			}
		}
		if (e.getSource() == qc) {
			try {
				out.writeUTF("User disconnect:");
			} catch (IOException ee) {
			}
		}
	}

	public void run() {
		String message = null;
		while (true) {
			if (in != null) {
				try {
					message = in.readUTF();
				} catch (IOException e) {
					nameFile.setText("disconnect from server" + e);
				}
			}
			if (message.startsWith("now you can chat:")) {
				icc = true;
				break;
			} else if (message.startsWith("chatter:")) {
				String people = message.substring(message.indexOf(":") + 1);
				listTable.put(people, people);
			} else if (message.startsWith("you can not chat:")) {
				icc = false;
				nameFile.setText("The nickname is already occupied");
			}
		}
	}
}
