package pp2016.team16.server.comm;
import java.io.*;
import java.net.*;
import java.util.LinkedList;

import pp2016.team16.shared.IchBinDa;
import pp2016.team16.shared.LogoutMessage;
import pp2016.team16.shared.MessageObject;


//Gruppe 16 Kommunikation; Ann-Catherine Hartmann


public class ServerComm {
	public ServerSocket serverS;
	public Socket s;
	public boolean serverOpen;
	ObjectOutputStream ost=null;
	ObjectInputStream in=null;
	LinkedList<MessageObject> empfangeVomClient = new LinkedList<MessageObject>();
	LinkedList<MessageObject> sendeAnClient = new LinkedList<MessageObject>();
	
public ServerComm(){
	
}
public ServerComm(int port){
		try {
			serverS = new ServerSocket(port);
		}catch(IOException e){}
		}

		
		public void run() {
			serverOpen=true;
			while (serverOpen){
				try {
				//serverS.setSoTimeout(60000);
				s = serverS.accept();
				verarbeiteNachricht();
			}catch(IOException e){}
			}
		}
		
		public void verarbeiteNachricht(){
			try {
				ost = new ObjectOutputStream(s.getOutputStream());
				ost.flush();
				in = new ObjectInputStream(s.getInputStream());
				MessageObject n;
				n = (MessageObject)in.readObject();
				if (n instanceof IchBinDa);else 
				if (n instanceof LogoutMessage){
					schlieﬂe();
				}
				else{
					empfangeVomClient.addLast(n);
					gebeWeiterAnServer();
				}
			} catch (IOException | ClassNotFoundException e) {
			}
			
		}
		
		public MessageObject gebeWeiterAnServer(){  
			MessageObject r;
			r=empfangeVomClient.removeFirst();
			return r;
		}
		
		public void gebeWeiterAnClient(MessageObject h){
			sendeAnClient.addLast(h);
			sendeAnClient();
		}
		
		public void sendeAnClient(){
			try {
				MessageObject m = sendeAnClient.removeFirst();
				ost=new ObjectOutputStream(s.getOutputStream());
				ost.writeObject(m);
				ost.flush();
			} catch (IOException e) {
				
			
			}
			
			
		}
		public void schlieﬂe() throws IOException{
			serverOpen = false;
			ost.close();
			in.close();
			serverS.close();
			s.close();
			
			
		}
		
		
}

