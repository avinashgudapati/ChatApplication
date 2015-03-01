package ClientPackage;
 
import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*; 
import java.io.*; 


/**
 *  Login class which takes a user name and passed it to client class 
 * @author Avinash, Rahath
 *
 */
public class Login implements ActionListener{ 
	private JFrame frame1; 
	private JTextField tf; 
	private JButton button; 
	private JLabel heading; 
	private JLabel label; 
	
	
	public static void main(String[] args){ 
		new Login(); 
	}
	
	
	/**
	 * Creates a Login Frame
	 * User need to input name
	 * @param null
	 * @return void
	*/
	public Login(){ 
		frame1 = new JFrame("Login Page"); 
		tf=new JTextField(); 
		button=new JButton("Login"); 
		heading=new JLabel("Chat Server");
		heading.setFont(new Font("Impact", Font.BOLD,40)); 
		label=new JLabel("Enter you Login Name"); 
		label.setFont(new Font("Serif", Font.PLAIN, 24)); 
		JPanel panel = new JPanel(); 
		button.addActionListener(this); 
		panel.add(heading);
		panel.add(tf);
		panel.add(label); 
		panel.add(button); 
		heading.setBounds(30,20,280,80); 
		label.setBounds(20,100,250,60); 
		tf.setBounds(50,150,150,30); 
		tf.setBorder(BorderFactory.createLineBorder(Color.black));
		button.setBounds(70,200,90,30); 
		frame1.add(panel); 
		panel.setLayout(null); 
		frame1.setSize(300, 300); 
		frame1.setVisible(true); 
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	} 
	
	
	/** 
	 * Pass the user name to MyClient class
	 * @param e an Action event on Login page
	 * @return void 
	 */
	@SuppressWarnings("unused")
	public void actionPerformed(ActionEvent e){ 
		String name=""; 
		try{ 
			name=tf.getText(); 
			frame1.dispose(); 
			Client createClientFrame = new Client(name);
		}catch (IOException te){} 
	} 
}

