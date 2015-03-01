package ClientPackage;

import javax.swing.*;  
import java.awt.Font;
import java.awt.event.*; 
import java.io.*; 
import java.net.*; 
import java.util.ArrayList; 
import java.util.Iterator; 


/** 
 * create the GUI of the client side (Unique for each login user)
 * All of them can have a group chat
 * users can retrieve from chat and can join again
 * @author Avinash,Rahath
 *
 */
@SuppressWarnings("rawtypes")
public class Client extends WindowAdapter implements ActionListener{ 
	JFrame frame; 
	JList list;
	JList list1; 
	JTextArea tf; 
	JTextArea fileuploadstatus;
	DefaultListModel model; 
	DefaultListModel model1,model2; 
	JButton button; 
	JButton lout; 
	JButton openButton;
	JScrollPane scrollpane; 
	JScrollPane scrollpane1;
	JLabel label,label2,label3,label4; 
	Socket s,s1,s2,s3; 
	DataInputStream din; 
	DataOutputStream dout; 
	DataOutputStream dlout; 
	DataOutputStream dout1; 
	DataInputStream din1; 
	DataInputStream fileInput; 
	DataOutputStream fileOutput;
	String name; 
	@SuppressWarnings("unchecked")
	/**
	 * GUI created (constructor)
	 * @param name Username provided while creating Client object
	 * @throws IOException
	 */
	Client(String name) throws IOException{ 
		frame = new JFrame("Chat Window"); 
		frame.setLocation(375,100); 
		tf = new JTextArea(); 
		fileuploadstatus = new JTextArea(); 
		model=new DefaultListModel(); 
		model1=new DefaultListModel(); 
		model2=new DefaultListModel();
		label=new JLabel("Message");
		label2=new JLabel("Onilne Users"); 
		label3=new JLabel("Chatting Messages"); 
		label4=new JLabel(name+"'s chat window"); 
		label4.setFont(new Font("Serif", Font.ITALIC, 30));
		fileuploadstatus.setFont(new Font("Serif", Font.BOLD, 12));
		list=new JList(model); 
		list1=new JList(model1); 
		button=new JButton("Send"); 
		lout=new JButton("Log Out"); 
		scrollpane=new JScrollPane(list); 
		scrollpane1=new JScrollPane(list1);
		openButton = new JButton("Browse");
		//from here 
		scrollpane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){ 
			public void adjustmentValueChanged(AdjustmentEvent e) { 
				e.getAdjustable().setValue(e.getAdjustable().getMaximum()); 
			} 
		}); 
		scrollpane1.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){ 
			public void adjustmentValueChanged(AdjustmentEvent e){ 
				e.getAdjustable().setValue(e.getAdjustable().getMaximum()); 
			} 
		});
		JPanel panel = new JPanel(); 
		button.addActionListener(this); 
		lout.addActionListener(this); 
		openButton.addActionListener(this);
		panel.add(tf);
		panel.add(button);
		panel.add(scrollpane); 
		panel.add(label);
		panel.add(label2);
		panel.add(label3);
		panel.add(label4);
		panel.add(lout); 
		panel.add(scrollpane1);
		panel.add(openButton);
		panel.add(fileuploadstatus);
		scrollpane.setBounds(10,70,250,300); 
		scrollpane1.setBounds(350,70,150,300); 
		label.setBounds(10,370,80,30); 
		label2.setBounds(350,50,150,20); 
		label3.setBounds(10,50,250,20); 
		label4.setBounds(100,15,280,30);
		tf.setBounds(10,400,380,70);
		fileuploadstatus.setBounds(10,480,380,60);
		button.setBounds(410,400,90,30); 
		lout.setBounds(410,440,90,30); 
		openButton.setBounds(410,480,90,30);
		frame.add(panel); 
		panel.setLayout(null); 
		frame.setSize(550,580); 
		frame.setResizable(false); 
		frame.setVisible(true); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.name=name; 
		frame.addWindowListener(this); 
		
		//creates a socket object
		s = new Socket("localhost",1004); 
		s1 = new Socket("localhost",1004); 
		s2 = new Socket("localhost",1004);
		s3 = new Socket("localhost",1004);
		
		//create input stream for a particular socket 
		din=new DataInputStream(s.getInputStream()); 
		//create output stream 
		dout=new DataOutputStream(s.getOutputStream()); 
		//sending a message for login 
		dout.writeUTF(name+" has Logged in"); 
		dlout=new DataOutputStream(s1.getOutputStream()); 
		dout1=new DataOutputStream(s2.getOutputStream()); 
		din1=new DataInputStream(s2.getInputStream()); 
		
		fileInput = new DataInputStream(s3.getInputStream());
		
		// creating a thread for maintaining the list of user name 
		UserNamesThreadClass getUserNmaes = new UserNamesThreadClass(dout1,model1,name,din1); 
		Thread t1=new Thread(getUserNmaes); 
		t1.start(); 
		
		
		//creating a thread for receiving a messages 
		ReceiveMessageThreadClass recvMessage = new ReceiveMessageThreadClass(din,model); 
		Thread t=new Thread(recvMessage); 
		t.start(); 
		
		//creating a thread for receiving a messages 
		ReceiveFileNamesThreadClass recvfilename = new ReceiveFileNamesThreadClass(fileInput,model2); 
		Thread t2=new Thread(recvfilename); 
		t2.start();
	} 
	@SuppressWarnings("static-access")
	/**
	 * @param e an Action event on Login page
	 * @return void 
	 */
	public void actionPerformed(ActionEvent e){ 
		// sending the messages 
		if(e.getSource()==button){
			String str=""; 
			str=tf.getText(); 
			tf.setText(""); 
			str=name+": > "+str; 
			try{ 
				dout.writeUTF(str); 
				dout.flush(); 
			}catch(IOException ae){
				System.out.println(ae);
			} 
		} 
		// client logout 
		if (e.getSource()==lout){ 
			frame.dispose(); 
			//new Welcome(); 
			JOptionPane.showMessageDialog(null,"Return Back to Welcome page"); 
			try{ 
				//sending the message for logout 
				dout.writeUTF(name+" has Logged out"); 
				dlout.writeUTF(name); 
				dlout.flush(); 
				Thread.currentThread().sleep(1000); 
				System.exit(1); 
			}
			catch(Exception oe){} 
		}
		if (e.getSource() == openButton){
			JFileChooser chooser = new JFileChooser();
	        int option = chooser.showOpenDialog(frame);
	        if (option == JFileChooser.APPROVE_OPTION) {
	          fileuploadstatus.setText("Successfully uploaded : " + ((chooser.getSelectedFile()!=null)?
	                            chooser.getSelectedFile().getName():"nothing")+"\n Look for it in Chat Application folder of Project");
	          File f = chooser.getSelectedFile();
	          try{
	        	  byte[] mybytearray = new byte[(int) f.length()];
	        	  BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
	        	  bis.read(mybytearray, 0, mybytearray.length);
	        	  DataOutputStream os = new DataOutputStream(s3.getOutputStream());
	        	  os.writeUTF(f.getName());
	        	  os.write(mybytearray, 0, mybytearray.length);
	        	  os.flush();
	        	  bis.close();
	        	  s3.close();
	          }catch(IOException e1){}
	        }
	        else {
	        	fileuploadstatus.setText("You canceled.");
	        }
		}
		
	} 
	@SuppressWarnings("static-access")
	/**
	 * @param w an Action event on Client GUI
	 * @return void 
	 */
	public void windowClosing(WindowEvent w){ 
		try{ 
			dlout.writeUTF(name); 
			dlout.flush(); 
			Thread.currentThread().sleep(1000); 
			System.exit(1); 
		}
		catch(Exception oe){} 
	} 
} 


// class is used to maintaining the list of user name 
@SuppressWarnings("rawtypes")
class UserNamesThreadClass implements Runnable{ 
	DataOutputStream dout1; 
	DefaultListModel model1; 
	DataInputStream din1; 
	String name,lname; 
	ArrayList alname=new ArrayList(); 
	ObjectInputStream obj;
	int i=0; 
	/**
	 * 
	 * @param dout1  output stream object of socket to write user name 
	 * @param model1 List Object to store user names 
	 * @param name String User name
	 * @param din1 Input stream object to read all user names from server
	 */
	UserNamesThreadClass(DataOutputStream dout1,DefaultListModel model1,String name,DataInputStream din1){ 
		this.dout1=dout1; 
		this.model1=model1; 
		this.name=name;
		this.din1=din1; 
	} 
	@SuppressWarnings("unchecked")
	public void run(){ 
		try{ 
			dout1.writeUTF(name); 
			// write the user name in output stream 
			while(true){ 
				obj=new ObjectInputStream(din1); 
				//read the list of user names 
				alname = (ArrayList)obj.readObject(); 
				if(i>0) 
					model1.clear(); 
				Iterator i1 = alname.iterator(); 
				System.out.println(alname); 
				while(i1.hasNext()){ 
					lname=(String)i1.next(); 
					i++; 
					//add the user names in list box 
					model1.addElement(lname); 
				} 
			} 
		}catch(Exception oe){} 
	} 
} 


//class is used to receive messages
@SuppressWarnings("rawtypes")
class ReceiveMessageThreadClass implements Runnable{ 
	DataInputStream din; 
	DefaultListModel model;
	/**
	 * constructor to receive messages
	 * @param din DataInputStream object to read messages sent by server 
	 * @param model ListObject to fill messages
	 */
	ReceiveMessageThreadClass(DataInputStream din, DefaultListModel model){ 
		this.din=din; 
		this.model=model; 
	} 
	@SuppressWarnings("unchecked")
	public void run(){ 
		String str1=""; 
		while(true){ 
			try{ 
				str1 = din.readUTF(); 		// read the message  
				model.addElement(str1); 	// add the message in list box
			}catch(Exception e){} 
		} 
	} 
}



//class is used to update files uploaded to server
@SuppressWarnings("rawtypes")
class ReceiveFileNamesThreadClass implements Runnable{ 
	DataInputStream din; 
	DefaultListModel model;
	/**
	 * constructor to receive messages
	 * @param din DataInputStream object to read messages sent by server 
	 * @param model ListObject to fill messages
	 */
	ReceiveFileNamesThreadClass(DataInputStream din, DefaultListModel model){ 
		this.din=din; 
		this.model=model; 
	} 
	@SuppressWarnings("unchecked")
	public void run(){ 
		String str1=""; 
		while(true){ 
			try{ 
				str1 = din.readUTF(); 		// read the message  
				System.out.println("hiiii");
				model.addElement(str1); 	// add the message in list box
			}catch(Exception e){} 
		} 
	} 
}
		

