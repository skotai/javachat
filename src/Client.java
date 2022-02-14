import java.net.*;
import java.io.*;

public class Client {//客户端
	private static final int PORT=1111;//端口
	public static String user;
	public static Socket socket;
	public Client(String user) {
		this.user=user;

		try{
			socket=new Socket("127.0.0.1",PORT);//建立socket连接
			System.out.println("【"+user+"】Welcome to the Chat Room！");

			Thread tt=new Thread(new Threads(socket,user));//建立客户端线程
			tt.start();//启动线程
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		new Client(user);

	}
}
class Threads implements Runnable {
	public String user;
	private Socket socket;
	private BufferedReader keybord;
	public BufferedReader br;
	private PrintWriter pw;
	private String msg;
	ClientUI gm=new ClientUI();



	public Threads(Socket socket,String user) throws IOException {
	try{
		this.socket=socket;
		this.user=user;
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void run() {
		try{
			br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while((msg=br.readLine())!=null) {
				String message=msg;
				if(message.equals("1008611")) {//匹配字符串 显示好友列表
					gm.listModel1.clear();//接收前清空好友列表
					gm.jcomb.removeAllItems();//清空JCombox
					gm.jcomb.addItem("All");
					message=br.readLine();
					String[] str=message.split(":");//将接收到的所有用户信息分隔开
					for(String ss:str) {
						gm.listModel1.addElement(ss);//将所有用户信息添加到好友列表
						gm.jcomb.addItem(ss);//将所有用户信息添加到JCombox
					}
				}
				else if(message.equals("841163574")) {//私聊
					message=br.readLine();
					System.out.println("Receive："+message);//在服务器端显示私聊消息
					gm.jta2.append(message+"\n");//在我的频道显示私聊信息
				}
				else if(message.equals("10010")) {//显示说话消息
					message=br.readLine();
					System.out.println("Receive："+message);//在服务器端显示说话信息
					gm.jta1.append(message+"\n");//在公共频道显示说话信息
					gm.jta2.append(message+"\n");//在我的频道显示说话信息
				}
				else if(message.equals("10086")) {//显示进入聊天室
					message=br.readLine();
					gm.jta1.append(message+"\n");//在公共频道显示进入聊天室信息
					gm.jta2.append(message+"\n");//在我的频道显示进入聊天室信息
				}
				else if(message.equals("123654")) {//刷新
					gm.listModel1.clear();//将好友列表清空
					gm.jcomb.removeAllItems();//将JCombox 清空
					gm.jcomb.addItem("All");
					message=br.readLine();
					String[] sr=message.split(":");//将接收到的用户信息分隔开
					for(String sst:sr) {
						gm.listModel1.addElement(sst);////将刷新后所有用户信息添加到好友列表
						gm.jcomb.addItem(sst);//将刷新后所有用户信息添加到JCombox
					}
				}
				else if(message.equals("456987")) {//下线
					message=br.readLine();
					gm.jta1.append(message+"\n");//在公共频道显示用户下线信息
					gm.jta2.append(message+"\n");//在我的频道显示用户下线信息
				}
			}
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}

}
