package pp2016.team16.server.comm;
import java.io.*;
import java.net.*;
import java.util.LinkedList;

import pp2016.team16.client.comm.Message;
//Gruppe 16 Kommunikation; Ann-Catherine Hartmann


public class ServerComm {
	
	public ServerSocket ServerS;
	public Socket S;
	boolean ServerOpen;
	ObjectOutputStream OST=null;
	ObjectInputStream IN=null;
	OutputStreamWriter OSW=null;
	InputStreamReader ISR=null;
	LinkedList<Message> ServerList = new LinkedList<Message>();
	
public ServerComm(int port){
		
		while (true){
			try {
			ServerS = new ServerSocket(port);
			ServerS.setSoTimeout(60000);
			S = ServerS.accept();
			System.out.println("Starte Server");
			run();
			
		}catch(IOException e){}
		}
}

		
		public void run() {
			this.ServerOpen = true;
		
			while (this.ServerOpen) {
				verarbeiteNachricht();
			}
		}
		
		public void verarbeiteNachricht(){
			try {
				OST = new ObjectOutputStream(S.getOutputStream());
				Message n = new Message();
				System.out.println("neue Nachricht wird erzeugt");
				IN = new ObjectInputStream(S.getInputStream());
				System.out.println("Server empfängt vom Client und versucht zu empfangen");
				System.out.println("Server versucht Nachricht vom Client zu verarbeiten");
				n = (Message)IN.readObject();
				ServerList.add(n);
				System.out.println("Der Server hat die Nachricht bekommen und schickt Nachricht zurück");
				Message j=new Message("Der Server reagiert auf den Client");
				OST.writeObject(j);
				OST.flush();
				System.out.println("Server hat eine Nachricht zurückgeschickt");
				//GebeWeiterAnServer(); Dies teste ich noch nicht, da ich noch keinen Client und Server habe
				//SendeAnClient(j);
			} catch (IOException | ClassNotFoundException e) {
			}
			
		}
		
		/*public Message GebeWeiterAnServer(){  
			Message r= new Message();
			r=ServerList.removeFirst();
			return r;
		}
		public void SendeAnClient(Message m){
			try {
				OST=new ObjectOutputStream(S.getOutputStream());
				OST.writeObject(m);
				OST.flush();
				System.out.println("Server hat eine Nachricht zurückgeschickt");
			} catch (IOException e) {
				
			}
			
			
		}*/ 
		
		
}
