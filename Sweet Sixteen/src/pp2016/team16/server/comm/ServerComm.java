package pp2016.team16.server.comm;
import java.io.*;
import java.net.*;
import java.util.LinkedList;

import pp2016.team16.shared.IchBinDa;
import pp2016.team16.shared.LogoutMessage;
import pp2016.team16.shared.MessageObject;


//Gruppe 16 Kommunikation; Ann-Catherine Hartmann


public class ServerComm {
	
	public ServerSocket ServerS;
	public Socket S;
	boolean ServerOpen;
	ObjectOutputStream OST=null;
	ObjectInputStream IN=null;
	LinkedList<MessageObject> EmpfangeVomClient = new LinkedList<MessageObject>();
	LinkedList<MessageObject> SendeAnClient = new LinkedList<MessageObject>();
	
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
				//OST = new ObjectOutputStream(S.getOutputStream());
				IN = new ObjectInputStream(S.getInputStream());
				MessageObject n = new MessageObject();
				n = (MessageObject)IN.readObject();
				if (n instanceof IchBinDa);
				else if (n instanceof LogoutMessage){
					Schlieﬂe();
				}
				else{
					EmpfangeVomClient.addLast(n);
					GebeWeiterAnServer();
				}
			} catch (IOException | ClassNotFoundException e) {
			}
			
		}
		
		public MessageObject GebeWeiterAnServer(){  
			MessageObject r= new MessageObject();
			r=EmpfangeVomClient.removeFirst();
			return r;
		}
		
		
		public void SendeAnClient(){
			try {
				MessageObject m = SendeAnClient.removeFirst();
				OST=new ObjectOutputStream(S.getOutputStream());
				OST.writeObject(m);
				OST.flush();
			} catch (IOException e) {
				
			
			}
			
			
		}
		public void Schlieﬂe() throws IOException{
			ServerOpen = false;
			OST.close();
			IN.close();
			ServerS.close();
			S.close();
			
			
		}
		
		
}
