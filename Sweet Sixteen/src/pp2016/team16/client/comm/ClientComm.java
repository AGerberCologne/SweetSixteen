package pp2016.team16.client.comm;
//Gruppe 16 Kommunikation; Ann-Catherine Hartmann

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import pp2016.team16.client.engine.IClientComm;
import pp2016.team16.shared.LogoutMessage;
import pp2016.team16.shared.MessageObject;

public class ClientComm implements IClientComm {
	LinkedList<MessageObject> empfangeVomServer = new LinkedList<MessageObject>();
	LinkedList<MessageObject> sendeAnServer=new LinkedList<MessageObject>();
	ObjectInputStream ois=null;
	ObjectOutputStream oos=null;
	Socket c;
	public boolean clientOpen;

	public ClientComm(String host,int port){
		try{
			c = new Socket(host, port);
			clientOpen=true;
		}catch(IOException e){
			
		}
	}

	
	public ClientComm(){
		
	}

	public void sendeAnServer(){

		
		try{
			
			MessageObject msg = sendeAnServer.removeFirst();
			oos = new ObjectOutputStream(c.getOutputStream());
			oos.writeObject(msg);
			oos.flush();
			ois=new ObjectInputStream(c.getInputStream());
			//empfangeVomServer();
			if(msg instanceof LogoutMessage)
				beende();
			
			
		}catch(IOException e){
			
		} 
		
	}
	public void empfangeVomServer() throws IOException, ClassNotFoundException{
		oos=new ObjectOutputStream(c.getOutputStream());
		oos.flush();
		ois=new ObjectInputStream(c.getInputStream());
		MessageObject bmsg = (MessageObject)ois.readObject();
		empfangeVomServer.addLast(bmsg);
	}
	
	public void bekommeVonClient(MessageObject cmsg){
		sendeAnServer.addLast(cmsg);
		sendeAnServer();
	}
	
	public MessageObject gebeWeiterAnClient(){
	MessageObject j;
	if (empfangeVomServer.isEmpty()!=false){
	j=empfangeVomServer.removeFirst();
	return j;}
	else {
		return null;
	}	
}
	
	public void beende() throws IOException{
		clientOpen=false;
		ois.close();
		oos.close();
		c.close();
		
	}
}
	

	