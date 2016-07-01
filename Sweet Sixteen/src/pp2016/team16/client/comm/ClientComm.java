package pp2016.team16.client.comm;
//Gruppe 16 Kommunikation; Ann-Catherine Hartmann

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import pp2016.team16.shared.LogoutMessage;

import pp2016.team16.shared.MessageObject;

public class ClientComm {
	LinkedList<MessageObject> empfangeVomServer = new LinkedList<MessageObject>();
	LinkedList<MessageObject> sendeAnServer=new LinkedList<MessageObject>();
	ObjectInputStream ois=null;
	ObjectOutputStream oos=null;
	Socket c;
	

	public ClientComm(String host,int port){
		try{
			c = new Socket(host, port);
			
		}catch(IOException e){
			
		}
	}

	public void sendeAnServer(){

		
		try{
			
			MessageObject msg = sendeAnServer.removeFirst();
			oos = new ObjectOutputStream(c.getOutputStream());
			oos.writeObject(msg);
			oos.flush();
			if(msg instanceof LogoutMessage)
				beende();
			
			//OIS=new ObjectInputStream(c.getInputStream());
		}catch(IOException e){
			
		} 
		
	}
	public void empfangeVomServer() throws IOException, ClassNotFoundException{
		ois=new ObjectInputStream(c.getInputStream());
		MessageObject bmsg = (MessageObject)ois.readObject();
		empfangeVomServer.addLast(bmsg);
		gebeWeiterAnClient();
	}
	
	public void bekommeVonClient(MessageObject cmsg){
		sendeAnServer.addLast(cmsg);
		sendeAnServer();
	}
	
	public MessageObject gebeWeiterAnClient(){
	MessageObject j = new MessageObject();
	j=empfangeVomServer.removeFirst();
	return j;
}
	
	public void beende() throws IOException{
		ois.close();
		oos.close();
		c.close();
	}
}
