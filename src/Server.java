

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

public class Server {
	public static void main(String args[]) {
		ServerSocket server = null;
		Socket you = null;
		Hashtable peopleList;
		peopleList = new Hashtable();

		while (true) {
			try {
				server = new ServerSocket(6666);
			} catch (IOException e1) {
				System.out.println("listening");
			}
			try {
				you = server.accept();
				InetAddress address = you.getInetAddress();
				System.out.println("User IP:" + address);

			} catch (IOException e) {
			}
			if (you != null) {
				S_thread peopleThread = new S_thread(you, peopleList);
				peopleThread.start();
			} else {
				continue;
			}
		}
	}
}

class S_thread extends Thread {
	String name = null, sex = null;
	Socket socket = null;
	File file = null;
	DataOutputStream out = null;
	DataInputStream in = null;
	Hashtable peopleList = null;

	S_thread(Socket t, Hashtable list) {
		peopleList = list;
		socket = t;
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
		}
	}

	public void run() {

		while (true) {
			String s = null;
			try {
				s = in.readUTF();

				if (s.startsWith("name:")) {
					name = s.substring(s.indexOf(":") + 1, s.indexOf(" gender"));
					sex = s.substring(s.lastIndexOf(":") + 1);

					boolean boo = peopleList.containsKey(name);
					if (boo == false) {
						peopleList.put(name, this);
						out.writeUTF("now you can chat:");
						Enumeration enum2 = peopleList.elements();
						while (enum2.hasMoreElements()) {
							S_thread th = (S_thread) enum2
									.nextElement();
							th.out.writeUTF("chatter:" + name + " gender" + sex);

							if (th != this) {
								out.writeUTF("chatter:" + th.name + " gender" + th.sex);
							}
						}

					} else {
						out.writeUTF("you can not chat:");
					}

				} else if (s.startsWith("public chat:")) {
					String message = s.substring(s.indexOf(":") + 1);
					Enumeration enum2 = peopleList.elements();
					while (enum2.hasMoreElements()) {
						((S_thread) enum2.nextElement()).out
								.writeUTF("chat content:" + message);
					}

				} else if (s.startsWith("Document waiting for confirmation:")) {

					// JOptionPane.showMessageDialog(null, s);
					String filePath = s.substring(s.indexOf("#") + 1, s
							.indexOf("$"));
					String fromPeople = s.substring(s.indexOf(":") + 1, s
							.indexOf("@"));
					// String toPeople = s.substring(s.indexOf("#") +
					// 1,s.indexOf("$"));

					String toPeople = s.substring(s.indexOf("$") + 1);
					S_thread toThread = (S_thread) peopleList
							.get(toPeople);
					// if (toThread != null) {
					//

					toThread.out.writeUTF("Document waiting for confirmation:" + fromPeople + "@"
							+ toPeople + "$" + filePath);
					// } else {
					// out.writeUTF("Document waiting for confirmation:" + toPeople + "already disconnect");
					// }

				} else if (s.startsWith("User disconnect:")) {
					Enumeration enum2 = peopleList.elements();
					while (enum2.hasMoreElements()) {
						try {
							S_thread th = (S_thread) enum2
									.nextElement();
							if (th != this && th.isAlive()) {
								th.out.writeUTF("User disconnection:" + name);
							}
						} catch (IOException eee) {
						}
					}
					peopleList.remove(name);
					socket.close();
					System.out.println(name + "User disconnection");
					break;
				} else if (s.startsWith("private talk:")) {
					String pt = s
							.substring(s.indexOf(":") + 1, s.indexOf("#"));
					// String pt=悄悄话1+"  天哪";
					String toPeople = s.substring(s.indexOf("#") + 1);

					S_thread toThread = (S_thread) peopleList
							.get(toPeople);
					if (toThread != null) {
						toThread.out.writeUTF("private talk:" + pt);
					} else {
						out.writeUTF("private talk:" + toPeople + "already disconnect");
					}

				} else if (s.startsWith("file confirm ")) {
					String filePath = s.substring(s.indexOf("$") + 1);
					String fromPeople = s.substring(s.indexOf(":") + 1, s.indexOf("$"));
					// 向用户二进行传输文件

					S_thread fromThread = (S_thread) peopleList
							.get(fromPeople);
					String toPeople = s.substring(s.indexOf("$") + 1);
					fromThread.out.writeUTF("agree:" + "$" + filePath);

					// 创建文件流用来读取文件中的数据

					File file = new File(filePath);

					FileInputStream fos = new FileInputStream(file);

					// 创建网络服务器接受客户请求
					//
					ServerSocket ss = new ServerSocket(8888);

					Socket client = ss.accept();

					// 创建网络输出流并提供数据包装器

					OutputStream netOut = client.getOutputStream();

					OutputStream doc = new DataOutputStream(
							new BufferedOutputStream(netOut));

					// 创建文件读取缓冲区

					byte[] buf = new byte[2048];

					int num = fos.read(buf);

					while (num != (-1)) {// 是否读完文件

						doc.write(buf, 0, num);// 把文件数据写出网络缓冲区

						doc.flush();// 刷新缓冲区把数据写往客户端

						num = fos.read(buf);// 继续从文件中读取数据

					}

					fos.close();
					doc.close();

				} else if (s.startsWith("file complete")) {
					String fromPeople = s.substring(s.indexOf(":") + 1, s.indexOf("$"));
					String filePath = s.substring(s.indexOf("$") + 1);
					S_thread fromThread = (S_thread) peopleList.get(fromPeople);
					fromThread.out.writeUTF("file complete:" + "$" + filePath);

				}

			} catch (IOException ee) {
				Enumeration enum2 = peopleList.elements();
				while (enum2.hasMoreElements()) {
					try {
						S_thread th = (S_thread) enum2.nextElement();
						if (th != this && th.isAlive()) {
							th.out.writeUTF("User disconnection:" + name);
						}
					} catch (IOException eee) {
					}
				}
				peopleList.remove(name);
				try {
					socket.close();
				} catch (IOException eee) {
				}

				System.out.println(name + "User disconnection");
				break;
			}

		}

	}
}
