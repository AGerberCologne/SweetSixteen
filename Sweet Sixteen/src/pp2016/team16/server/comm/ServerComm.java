package pp2016.team16.server.comm;
import java.io.*;
import java.net.*;
import java.util.LinkedList;

import pp2016.team16.shared.IchBinDa;
import pp2016.team16.shared.LogoutMessage;
import pp2016.team16.shared.MessageObject;


//Gruppe 16 Kommunikation; Ann-Catherine Hartmann


public class ServerComm extends Thread {
	public ServerSocket serverS;
	public Socket s;
	int port = 10000;
	public boolean serverOpen;
	ObjectOutputStream ost=null;
	ObjectInputStream in=null;
	LinkedList<MessageObject> empfangeVomClient = new LinkedList<MessageObject>();
	LinkedList<MessageObject> sendeAnClient = new LinkedList<MessageObject>();
	

public ServerComm(){
		try {
			serverS = new ServerSocket(port);
			serverOpen=true;
			this.start();
		}catch(IOException e){}
		}

		
		public void run() {
			
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
				System.out.println("InputStream");
				n = (MessageObject)in.readObject();
				System.out.println("Message gelesen");
				if (n instanceof IchBinDa){
					System.out.println("IchBinDa");
				}
				else if (n instanceof LogoutMessage){
					System.out.println("ost wird geschlossen");
					schliesse();
				}
				else{
					empfangeVomClient.addLast(n);
					System.out.println("Test 5");
				}
			} catch (IOException | ClassNotFoundException e) {
				System.out.println("Test 7");
			}
			
		}
		
		public MessageObject gebeWeiterAnServer(){  
			if (empfangeVomClient.isEmpty()==false){
				MessageObject r;
				r=empfangeVomClient.removeFirst();
			return r;
		}else
			return null;
		}
		
		public void gebeWeiterAnClient(MessageObject h){
			sendeAnClient.addLast(h);
			sendeAnClient();
		}
		
		public void sendeAnClient(){
			try {
				
				MessageObject m = sendeAnClient.removeFirst();
				ost = new ObjectOutputStream(s.getOutputStream());
				ost.writeObject(m);
				ost.flush();	
			} catch (IOException e) {
				System.out.println("Test 9");
			
			}
			
			
		}
		public void schliesse() throws IOException{
			serverOpen = false;
			ost.close();
			in.close();
			serverS.close();
			s.close();
			
			
		}
		
		
}

