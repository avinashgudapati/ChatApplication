package ServerPackage;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Listens for clients, Adds them to group chat, broadcasts messages to all users 
 * @author Avinash,Rahath
 *
 */
@SuppressWarnings("rawtypes")
public class Server {
	private ServerSocket ss; 
	private Socket s,s1,s2,s3;  
	private ArrayList al=new ArrayList();
	private ArrayList al1=new ArrayList();
	private ArrayList al2=new ArrayList();
	private ArrayList al3=new ArrayList();
	private ArrayList alname=new ArrayList();

	@SuppressWarnings("unchecked")
	Server() throws IOException{ 
		ss = new ServerSocket(1004); 		// create server socket 
	 	while(true){ 
	 		s=ss.accept(); 					//accept the client socket 
	 		s1=ss.accept();
	 		s2=ss.accept();
	 		s3=ss.accept();
	 		al.add(s); 						// add the client socket in array list 
	 		al1.add(s1); 
	 		al2.add(s2);
	 		al3.add(s3);
	 		System.out.println("Client is Connected"); 
	 		
	 		//new thread for maintaining the list of user name
	 		UserListThreadClass userlistmaintenance = new UserListThreadClass(s2,al2,alname);  
	 		Thread t2=new Thread(userlistmaintenance); 
	 		t2.start(); 
	 		
	 		//new thread for receive and sending the messages
	 		MessageThreadClass messagetransactions = new MessageThreadClass(s,al); 
	 		Thread t=new Thread(messagetransactions); 
	 		t.start();
	 		
	 		//new thread for update the list of user name
	 		UpdateUserListThreadClass updation = new UpdateUserListThreadClass(s1,al1,s,s2); 
	 		Thread t1=new Thread(updation); 
	 		t1.start(); 
	 		
	 		//new thread for receiving and sending files
	 		FileThreadClass FileTransfer = new FileThreadClass(s3,al3); 
	 		Thread t3=new Thread(FileTransfer); 
	 		t3.start();
	 	} 
	 }
	 public static void main(String[] args){ 
		try{ 
			 new Server(); 
		}
		catch (IOException e){}
	 }
}


//class is used to update the list of user name 
@SuppressWarnings("rawtypes")
class UpdateUserListThreadClass implements Runnable{ 
	Socket s1,s,s2; 
	static ArrayList al1; 
	DataInputStream ddin; 
	String sname; 
	@SuppressWarnings("static-access")
	UpdateUserListThreadClass(Socket s1,ArrayList al1,Socket s,Socket s2){ 
		this.s1=s1; 
		this.al1=al1; 
		this.s=s; 
		this.s2=s2; 
	} 
	public void run(){ 
		try{ 
			ddin = new DataInputStream(s1.getInputStream()); 
			while(true){ 
				sname = ddin.readUTF(); 
				System.out.println("Exit :"+sname); 
				UserListThreadClass.alname.remove(sname);//remove the logout user name from array list 
				UserListThreadClass.every(); 
				al1.remove(s1); 
				MessageThreadClass.al.remove(s);
				UserListThreadClass.al2.remove(s2);
				if(al1.isEmpty()) 
					System.exit(0); //all client has been logout 
			} 
		}
		catch(Exception ie){} 
	} 
} 


// class is used to maintain the list of all online users 
@SuppressWarnings("rawtypes")
class UserListThreadClass implements Runnable{ 
	Socket s2; 
	static ArrayList al2; 
	static ArrayList alname; 
	static DataInputStream din1; 
	static DataOutputStream dout1; 
	
	@SuppressWarnings("static-access")
	/**
	 * default constructor
	 * @param s2 Socket to get input stream from client
	 * @param al2   
	 * @param alname
	 */
	UserListThreadClass(Socket s2,ArrayList al2,ArrayList alname){ 
		this.s2=s2; 
		this.al2=al2; 
		this.alname=alname; 
	} 
	@SuppressWarnings("unchecked")
	public void run(){ 
		try{ 
			din1= new DataInputStream(s2.getInputStream());
			// store the user name in array list 
			alname.add(din1.readUTF()); 
			every(); 
		}
		catch(Exception oe){
			System.out.println("Main expression"+oe);
		} 
	} 
	
	// send the list of user name to all client 
	static void every() throws Exception{ 
		Iterator i1=al2.iterator(); 
		Socket st1; 
		while(i1.hasNext()){ 
			st1=(Socket)i1.next(); 
			dout1 = new DataOutputStream(st1.getOutputStream()); 
			ObjectOutputStream obj = new ObjectOutputStream(dout1); 
			//write the list of users in stream of all clients 
			obj.writeObject(alname); 
			dout1.flush(); 
			obj.flush(); 
		} 
	} 
} 


//class is used to receive the message and send it to all clients 
@SuppressWarnings("rawtypes")
class MessageThreadClass implements Runnable{ 
	Socket s; 
	static ArrayList al; 
	DataInputStream din;
	DataOutputStream dout; 
	@SuppressWarnings("static-access")
	MessageThreadClass(Socket s, ArrayList al){ 
		this.s=s; 
		this.al=al; 
	} 
	public void run(){ 
		String str;  
		try{ 
			din=new DataInputStream(s.getInputStream()); 
		}
		catch(Exception e){} 
		while(true){ 
			try{ 
				str=din.readUTF(); //read the message 
				distribute(str); 
			}catch (IOException e){} 
		} 
	} 
	
	/**
	 * Send message to all clients
	 * @param str Message string from each client
	 * @throws IOException
	 */
	public void distribute(String str)throws IOException{ 
		Iterator i=al.iterator();
		Socket st; 
		while(i.hasNext()){ 
			st=(Socket)i.next(); 
			dout=new DataOutputStream(st.getOutputStream()); 
			dout.writeUTF(str); 
			dout.flush(); 
		} 
	} 
}
	
	//class is used to receive the message and send it to all clients 
	@SuppressWarnings("rawtypes")
	class FileThreadClass implements Runnable{ 
		Socket s; 
		static ArrayList al; 
		DataOutputStream dout; 
		@SuppressWarnings("static-access")
		FileThreadClass(Socket s, ArrayList al){ 
			this.s=s; 
			this.al=al; 
		} 
		public void run(){  
			try{ 
				while(true){ 
					byte[] mybytearray = new byte[1024];
					DataInputStream is = new DataInputStream(s.getInputStream());
					String s1 = is.readUTF();
					FileOutputStream fos = new FileOutputStream(s1);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					int bytesRead = is.read(mybytearray, 0, mybytearray.length);
					bos.write(mybytearray, 0, bytesRead);
					bos.close();
					s.close(); 
					distributeFiles(s1);
				}
			}catch(Exception e){}  
		} 
		/**
		 * Send Files to all clients
		 * @param str Message string from each client
		 * @throws IOException
		 **/
		public void distributeFiles(String str)throws IOException{ 
			Iterator i=al.iterator();
			Socket st; 
			while(i.hasNext()){ 
				st=(Socket)i.next(); 
				dout=new DataOutputStream(st.getOutputStream()); 
				System.out.println(str);
				dout.writeUTF(str); 
				dout.flush(); 
			} 
		} 
	}
