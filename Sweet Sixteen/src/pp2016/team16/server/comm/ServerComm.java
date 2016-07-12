package pp2016.team16.server.comm;
import java.io.*;
import java.net.*;
import java.util.LinkedList;

import pp2016.team16.shared.BeendeMessage;
import pp2016.team16.shared.IchBinDa;
import pp2016.team16.shared.LogoutMessage;
import pp2016.team16.shared.MessageObject;
/**
 * @author: Ann-Catherine Hartmann, Matrikelnr: 60038514/ Prüfungsnummer: 37658
 **/
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
			try {
				s = serverS.accept();
				ost=new ObjectOutputStream(s.getOutputStream());
				ost.flush();
				in = new ObjectInputStream(s.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			while (serverOpen){
				System.out.println("Server Run");
				//serverS.setSoTimeout(60000);
				
				verarbeiteNachricht();
				try {
					sleep(600);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Server Run 2");
			}
			System.out.println("Server wird gesclossen");
			this.schliesse();
		}
		
		public void verarbeiteNachricht(){
			try {
				
				MessageObject n;
				System.out.println("InputStream");
				n = (MessageObject)in.readObject();
				System.out.println("Message gelesen");
				if (n instanceof IchBinDa){
					System.out.println("IchBinDa");
				}
				else if (n instanceof BeendeMessage){
					System.out.println("ost wird geschlossen");
					//serverOpen = false;
					schliesse();
				}
				else{
					empfangeVomClient.addLast(n);
					System.out.println("Test 5");
				}
			} catch (IOException | ClassNotFoundException e) {
				//e.printStackTrace();
				//serverOpen = false;
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
				ost.writeObject(m);
				ost.flush();	
			} catch (IOException e) {
				serverOpen = false;
			
			}
			
			
		}
		public void schliesse(){
			try {
			//ost.close();
			//in.close();
			serverS.close();
			s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
			}
			
		}
		
		
}

