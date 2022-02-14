import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
class ClientUI extends JFrame implements ActionListener {
	JFrame jf=new JFrame("chat room");

	public Client soc;
	public PrintWriter pw;

	public JPanel jp1=new JPanel();
	public JPanel jp2=new JPanel();
	public JPanel jp3=new JPanel();
	public JPanel jp4=new JPanel();
	public JPanel jp5=new JPanel();
	public JPanel jp6=new JPanel();
	public JPanel jp7=new JPanel();

	public static JTextArea jta1=new JTextArea(12,42);
	public static JTextArea jta2=new JTextArea(12,42);

	public JLabel jl1=new JLabel("To");

	public static JComboBox jcomb=new JComboBox();

	public JCheckBox jcb=new JCheckBox("Private chat");

	public JTextField jtf=new JTextField(36);

	public JButton jb1=new JButton("Send>>");
	public JButton jb2=new JButton("Refresh");

	public static DefaultListModel listModel1;
	public static JList lst1;

	public String na;
	public String se;
	public String message;


	public void getClientUI(String name,String sex) {//显示聊天界面

		jcomb.addItem("All");

		this.na=name;
		this.se=sex;

		jta1.setEditable(false);
		jta2.setEditable(false);

		listModel1= new DefaultListModel();

		lst1 = new JList(listModel1);
		lst1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lst1.setVisibleRowCount(18);
		lst1.setFixedCellHeight(28);
		lst1.setFixedCellWidth(100);

		JScrollPane jsp1=new JScrollPane(jta1);
		JScrollPane jsp2=new JScrollPane(jta2);
		JScrollPane jsp3=new JScrollPane(lst1);

		jsp3.setBorder(new TitledBorder("Friends List"));
		jsp1.setBorder(new TitledBorder("Main chat channel"));
		jsp2.setBorder(new TitledBorder(na+" Channel"));

		jp1.setLayout(new GridLayout(2,1));
		jp1.add(jsp1);
		jp1.add(jsp2);

		jp2.setLayout(new FlowLayout(FlowLayout.LEFT));
		jp2.add(jl1);
		jp2.add(jcomb);
		jp2.add(jcb);

		jp3.setLayout(new FlowLayout(FlowLayout.LEFT));
		jp3.add(jtf);
		jp3.add(jb1);

		jp4.setLayout(new GridLayout(2,1));
		jp4.add(jp2);
		jp4.add(jp3);

		jp5.setLayout(new BorderLayout());
		jp5.add(jp1,BorderLayout.NORTH);
		jp5.add(jp4,BorderLayout.SOUTH);

		jp6.setLayout(new BorderLayout());
		jp6.add(jsp3,BorderLayout.NORTH);
		jp6.add(jb2,BorderLayout.SOUTH);

		jp7.setLayout(new FlowLayout(FlowLayout.LEFT));
		jp7.add(jp5);
		jp7.add(jp6);

		jf.add(jp7);

		jf.setLocation(200,200);
		jf.setSize(700,650);

		jf.setResizable(false);
		jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jf.setVisible(true);

		jb1.addActionListener(this);
		jb2.addActionListener(this);

		jta1.setLineWrap(true);
		jta2.setLineWrap(true);
		jsp1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jsp1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jsp2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jsp2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jsp3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jsp3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jf.pack();
	}
	public void sock() {
		try{
		String user=na+"("+se+")";//将用户信息保存成字符串形式
		soc=new Client(user);//创建客户端对象
		pw=new PrintWriter(soc.socket.getOutputStream());//创建输出流
		pw.println("1008611");//发送好友列表标识
		pw.println(na+":"+se);//发送用户信息
		pw.flush();
		pw.println("10086");//发送进入聊天室标识
		pw.println("【"+na+"】"+"Access to the chat room");//发送进入聊天室信息
		pw.flush();
	}catch(Exception ex){
		ex.printStackTrace();
	}
	}
	public ClientUI() {//设置窗口关闭事件，如果点击窗口右上角叉号关闭，执行下边程序
		jf.addWindowListener( new WindowAdapter(){
								  public void windowClosing(WindowEvent e){
									  try {
										  pw=new PrintWriter(soc.socket.getOutputStream());
										  pw.println("456987");//发送下线标识
										  pw.println(na+":leave");//发送下线信息
										  pw.flush();
										  jf.dispose();//关闭窗口
									  }catch(Exception ex) {
									  }
								  }
							  }
		);
	}
	public void actionPerformed(ActionEvent event) {//事件触发
		jb1.setText("Send>>");
		jb2.setText("Refresh");

		try{
			pw=new PrintWriter(soc.socket.getOutputStream());
			if(event.getActionCommand().equals("Send>>")) {//点击发送触发

				if(!jtf.getText().equals("")) {
					if(jcb.isSelected()) {
						String name1=(String)jcomb.getSelectedItem();
						message="<private talk> "+na+" ("+se+") "+"to "+name1+" said："+jtf.getText();
						pw.println("841163574");//发送私聊标识
						pw.println(na+":"+name1+"1072416535"+message);//发送私聊信息
						pw.flush();
					}
					else{
						pw.println("10010");//发送聊天标识
						pw.println(na+" said："+jtf.getText());//发送聊天信息
						pw.flush();
					}
				}
			}
			else if(event.getActionCommand().equals("Refresh")) {//点击刷新触发
				pw=new PrintWriter(soc.socket.getOutputStream());
				pw.println("123654");//发送刷新标识
				pw.flush();
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}
		jtf.setText("");//清空输入栏信息
		jtf.requestFocus();//输入焦点
	}
}
