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
	LinkedList<MessageObject> EmpfangeVomServer = new LinkedList<MessageObject>();
	LinkedList<MessageObject> SendeAnServer=new LinkedList<MessageObject>();
	ObjectInputStream OIS=null;
	ObjectOutputStream OOS=null;
	Socket c;
	

	public ClientComm(String host,int port){
		try{
			c = new Socket(host, port);
			
		}catch(IOException e){
			
		}
	}

	public void sendeAnServer(){

		
		try{
			
			MessageObject msg = SendeAnServer.removeFirst();
			OOS = new ObjectOutputStream(c.getOutputStream());
			OOS.writeObject(msg);
			OOS.flush();
			if(msg instanceof LogoutMessage)
				beende();
			
			//OIS=new ObjectInputStream(c.getInputStream());
		}catch(IOException e){
			
		} 
		
	}
	public void empfangeVomServer() throws IOException, ClassNotFoundException{
		OIS=new ObjectInputStream(c.getInputStream());
		MessageObject bmsg = (MessageObject)OIS.readObject();
		EmpfangeVomServer.addLast(bmsg);
		gebeWeiterAnClient();
	}
	
	public void BekommeVonClient(MessageObject cmsg){
		SendeAnServer.addLast(cmsg);
		sendeAnServer();
	}
	
	public MessageObject gebeWeiterAnClient(){
	MessageObject j = new MessageObject();
	j=EmpfangeVomServer.removeFirst();
	return j;
}
	
	public void beende() throws IOException{
		OIS.close();
		OOS.close();
		c.close();
	}
}
