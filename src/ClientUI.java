
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Hashtable;

import javax.swing.JOptionPane;

public class chatUI extends Panel implements ActionListener, Runnable {
	Socket socket = null;
	DataInputStream in = null;
	DataOutputStream out = null;
	Thread threadMessage = null;
	TextArea  publictalk, privatetalk = null;
	TextField sendmes = null;
	Button enter, refreshpublic, refreshprivate;

	Button filetransfer; // 传输文件按钮
	File logFile; // 聊天记录文件

	Label not = null;
	String name = null;
	String ip=null;
	Hashtable listTable;
	List listComponent = null;
	Choice privateChatList;
	int width, height;

	public chatUI(String name, Hashtable listTable, int width, int height) {
		setLayout(null);
		setBackground(Color.green);
		this.width = width;
		this.height = height;
		setSize(width, height);
		this.listTable = listTable;
		this.name = name;
		threadMessage = new Thread(this);
		 publictalk = new TextArea(10, 10);
		privatetalk = new TextArea(10, 10);
		enter = new Button("enter");
		refreshpublic = new Button("refresh public");
		refreshprivate = new Button("refresh private");
		not = new Label("double click", Label.CENTER);
		sendmes = new TextField(28);

		filetransfer = new Button("file transfer");

		enter.addActionListener(this);
		sendmes.addActionListener(this);
		refreshpublic.addActionListener(this);
		refreshprivate.addActionListener(this);

		filetransfer.addActionListener(this);

		listComponent = new List();
		listComponent.addActionListener(this);
		privateChatList = new Choice();
		privateChatList.add("All(*)");
		privateChatList.select(0);

		add( publictalk);
		 publictalk.setBounds(10, 10, (width-120), (height-120)/2);
		add(privatetalk);
		privatetalk.setBounds(10 , 10+(height-120)/2, (width-120),(height-120)/2);
		add(listComponent);
		listComponent.setBounds(10 + (width - 120), 10, 100, (height-160));
		add(not);
		not.setBounds(10 + (width - 120), 10 + (height - 160), 110, 40);
		Panel pSouth = new Panel();
		pSouth.add(sendmes);
		pSouth.add(enter);
		pSouth.add(privateChatList);
		pSouth.add(refreshpublic);
		pSouth.add(refreshprivate);
		pSouth.add(filetransfer);
		add(pSouth);
		pSouth.setBounds(10, 20 + (height - 120), width-20, 60);

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
			threadMessage.start();
		} catch (Exception e) {
		}
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == enter || e.getSource() == sendmes) {
			String message = "";
			String people = privateChatList.getSelectedItem();
			people = people.substring(0, people.indexOf("("));
			message = sendmes.getText();
			if (message.length() > 0) {
				try {
					if (people.equals("All")) {
						out.writeUTF("public chat:" + name + ":" + message);
					} else {
						out.writeUTF("private talk:" + name + ":" + message + "#" + people);
						privatetalk.append("\n" + "to " + people + ":" + message);

					}
				} catch (IOException event) {
				}
			}
		} else if (e.getSource() == listComponent) {
			privateChatList.insert(listComponent.getSelectedItem(), 0);
			privateChatList.repaint();
		} else if (e.getSource() == refreshpublic) {
			 publictalk.setText(null);
		} else if (e.getSource() == refreshprivate) {
			privatetalk.setText(null);
		} else if (e.getSource() == filetransfer) {
			String filePath = null;
			String toPeople;
			String people = privateChatList.getSelectedItem();
			toPeople = people.substring(0, people.indexOf("("));

			try {
				if (toPeople.equals("All")) {
					JOptionPane.showMessageDialog(null, "send to who？","message",JOptionPane.INFORMATION_MESSAGE);
				} else {
					filePath = JOptionPane.showInputDialog(null,"input the path(like:c:\\"+"\\"+"mm.txt)","input",JOptionPane.INFORMATION_MESSAGE);
					int ok = JOptionPane.showConfirmDialog(null, "confirm to send this file?", "confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (ok == JOptionPane.YES_OPTION) {
						String mess = "Document waiting for confirmation:" + name + "@to#" + filePath + "$" + toPeople;
						out.writeUTF(mess);
						privatetalk.append("\n" + "send file " + filePath + " to"
								+ toPeople + "  ...");

					}
				}
			} catch (IOException et) {
			}

		}
	}

	public void run() {
		while (true) {
			String s = null;
			try {
				s = in.readUTF();

				//将聊天记录写入文件
				FileWriter fw = new FileWriter("d:\\log.txt", true);
				PrintWriter pw = new PrintWriter(fw);

				if (s.startsWith("chat content:")) {
					String content = s.substring(s.indexOf(":") + 1);
					 publictalk.append("\n" + content);
					pw.println("\n" + content);

				}
				if (s.startsWith("private talk:")) {
					String content = s.substring(s.indexOf(":") + 1);
					privatetalk.append("\n" + content);
					pw.println("\n" + content);

				} else if (s.startsWith("chatter:")) {
					String people = s.substring(s.indexOf(":") + 1, s.indexOf(" gender"));

					String sex = s.substring(s.indexOf("gender")+6);

					listTable.put(people, people + "(" + sex + ")");

					listComponent.add((String) listTable.get(people));
					listComponent.repaint();
					pw.println("\n" + s);

				} else if (s.startsWith("Document waiting for confirmation")) {
					String filePath = s.substring(s.indexOf("$") + 1);
					String fromPeople = s.substring(s.indexOf(":") + 1, s.indexOf("@"));
					String toPeople = s.substring(s.indexOf("#") + 1, s.indexOf("$"));
					String mess = null;
					mess = fromPeople + " send the file from" + filePath + ",accept？";
					int ok = JOptionPane.showConfirmDialog(null, mess, "accept",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					// privatetalk.append("\n"+fromPeople
					// +"  send the file from "+filePath+"  accept？");
					if (ok == JOptionPane.YES_OPTION) {
						privatetalk.append("\n" + "agree the file from" + fromPeople );
						out.writeUTF("file confirm :" + fromPeople + "$" + filePath);
						// 使用本地文件系统接受网络数据并存为新文件
						String storPath = JOptionPane.showInputDialog(null,"please input the path!(like:"+filePath+")","message",JOptionPane.INFORMATION_MESSAGE);
						File file = new File(storPath);
						file.createNewFile();
						RandomAccessFile raf = new RandomAccessFile(file, "rw");
						// 通过Socket连接文件服务器

						Socket server = new Socket(InetAddress.getLocalHost(), 8888);
						// 创建网络接受流接受服务器文件数据
						InputStream netIn = server.getInputStream();
						InputStream in = new DataInputStream(
								new BufferedInputStream(netIn));
						// 创建缓冲区缓冲网络数据
						byte[] buf = new byte[2048];
						int num = in.read(buf);
						while (num != (-1)) {// 是否读完所有数据
							raf.write(buf, 0, num);// 将数据写往文件
							raf.skipBytes(num);// 顺序写文件字节
							num = in.read(buf);// 继续从网络中读取文件
						}
						in.close();
						raf.close();
						out.writeUTF("file complete:" + fromPeople + "$" + filePath);
						privatetalk.append("\n" + "file" + filePath + "success！   saved in " + storPath);
					}
				} else if (s.startsWith("agree")) {
					String toPeople = s.substring(s.indexOf(":") + 1, s.indexOf("$"));
					String filePath = s.substring(s.indexOf("$") + 1);
					privatetalk.append("\n" + "agree，start transfer...");

				} else if (s.startsWith("file complete")) {
					privatetalk.append("\n" + "complete!");

				} else if (s.startsWith("User disconnection:")) {
					String awayPeopleName = s.substring(s.indexOf(":") + 1);
					listComponent.remove((String) listTable.get(awayPeopleName));
					listComponent.repaint();
					 publictalk.append("\n" + (String) listTable.get(awayPeopleName) + "disconnection");
					listTable.remove(awayPeopleName);

				}
				pw.close();
				fw.close();
				Thread.sleep(5);
			} catch (IOException e) {
				listComponent.removeAll();
				listComponent.repaint();
				listTable.clear();
				 publictalk.setText("disconnection from server.");

				break;
			} catch (InterruptedException e) {
			}
		}
	}
}
