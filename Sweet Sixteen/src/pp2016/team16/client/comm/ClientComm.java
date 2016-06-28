package pp2016.team16.client.comm;
//Gruppe 16 Kommunikation; Ann-Catherine Hartmann

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class ClientComm {
	LinkedList<Message> ClientList = new LinkedList<Message>();
	InputStream IS;
	OutputStream OS;
	ObjectInputStream OIS=null;
	ObjectOutputStream OOS=null;
	Socket c;
	
	public ClientComm(String host,int port){
		try{
			c = new Socket(host, port);
			System.out.println("Verbindung zum Client steht");
			
		}catch(IOException e){
			
		}
	}
	public void SendeAnServer(Message msg){
		
		try{
			OOS = new ObjectOutputStream(c.getOutputStream());
			System.out.println("ObjectStream steht");
			OOS.writeObject(msg);
			OOS.flush();
			System.out.println("Client sendet an Server");
			Message bmsg = new Message();
			OIS=new ObjectInputStream(c.getInputStream());
			System.out.println("ObjectInputStream steht");
			bmsg=(Message)OIS.readObject();
			//ClientList.addLast(bmsg);
			System.out.println("Client empfängt Antwort von Server"+ getMessage(bmsg));
		}catch(IOException e){
			
		} catch (ClassNotFoundException e) {
			
		}
		
	}
/*public Message GebeWeiterAnClient(){
	Message j = new Message();
	j=ClientList.removeFirst();
	return j;
}*/
}