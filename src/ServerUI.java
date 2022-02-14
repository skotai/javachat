import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
class UI extends Frame implements ActionListener {
	JFrame jf=new JFrame("Chat Login");

	JPanel jp1=new JPanel();
	JPanel jp2=new JPanel();
	JPanel jp3=new JPanel();
	JPanel jp4=new JPanel();

	
	JLabel jl1=new JLabel("Name：");
	JLabel jl2=new JLabel("IP Address：");
	JLabel jl3=new JLabel("port：");

	JRadioButton jrb1=new JRadioButton("male");
	JRadioButton jrb2=new JRadioButton("female");
	//JRadioButton jrb3=new JRadioButton("secret");

	public JTextField jtf1=new JTextField(10);
	public JTextField jtf2=new JTextField(10);
	public JTextField jtf3=new JTextField(10);

	JButton jb1=new JButton("Connect");
	JButton jb2=new JButton("Disconnection");

	TitledBorder tb=new TitledBorder("");

	ButtonGroup gb=new ButtonGroup();

	public void init() {//显示登录界面

		jb1.addActionListener(this);
		jb2.addActionListener(this);

		jp1.add(jl1);
		jp1.add(jtf1);
		jp1.add(jrb1);
		jp1.add(jrb2);
		//jp1.add(jrb3);

		jp2.add(jl2);
		jp2.add(jtf2);
		jp2.add(jl3);
		jp2.add(jtf3);


		jp3.add(jb1);
		jp3.add(jb2);

		jp4.setLayout(new GridLayout(3,1));
		jp4.add(jp1);
		jp4.add(jp2);
		jp4.add(jp3);


		jf.add(jp4);


		jtf2.setText("localhost");
		jtf3.setText("1111");

		gb.add(jrb1);
		gb.add(jrb2);
		//gb.add(jrb3);

		jf.setLocation(200, 200);
		jf.setSize(350, 200);
		jf.setResizable(false);
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf.setVisible(true);
	}



	public void actionPerformed(ActionEvent event) {//事件触发
		jb1.setText("Connect");
		jb2.setText("Disconnect");
		String s1=null;

		if(event.getActionCommand().equals("Disconnect")) {
			System.exit(0);
		}
		if(event.getActionCommand().equals("Connect")) {
			if(jtf1.getText().equals("")) {
				JOptionPane.showMessageDialog(null,"Please input your name");
			}
			//else if(!jrb1.isSelected()&&!jrb2.isSelected()&&!jrb3.isSelected())
			else if(!jrb1.isSelected()&&!jrb2.isSelected()) {
				JOptionPane.showMessageDialog(null,"Please select a gender");
			}
			else {
				jf.setVisible(false);
				if(jrb1.isSelected()) {
					s1="boy";
				}
				else if(jrb2.isSelected()) {
					s1="girl";
				}
				ClientUI gmu=new ClientUI();
				gmu.getClientUI(jtf1.getText(),s1);
				gmu.sock();
			}
		}
	}

}
public class ServerUI {
	public static void main(String[] args)
	{
		new UI().init();
	}
}
