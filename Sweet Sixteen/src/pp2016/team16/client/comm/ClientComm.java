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
	public boolean clientOpen;

	public ClientComm(String host,int port){
		try{
			c = new Socket(host, port);
			clientOpen=true;
		}catch(IOException e){
			
		}
	}

	public void sendeAnServer() throws ClassNotFoundException{

		
		try{
			
			MessageObject msg = SendeAnServer.removeFirst();
			OOS = new ObjectOutputStream(c.getOutputStream());
			OOS.writeObject(msg);
			OOS.flush();
			OIS=new ObjectInputStream(c.getInputStream());
			empfangeVomServer();
			if(msg instanceof LogoutMessage)
				beende();
			
			
		}catch(IOException e){
			
		} 
		
	}
	public void empfangeVomServer() throws IOException, ClassNotFoundException{
		OOS=new ObjectOutputStream(c.getOutputStream());
		OOS.flush();
		OIS=new ObjectInputStream(c.getInputStream());
		MessageObject bmsg = (MessageObject)OIS.readObject();
		EmpfangeVomServer.addLast(bmsg);
	}
	
	public void BekommeVonClient(MessageObject cmsg) throws ClassNotFoundException{
		SendeAnServer.addLast(cmsg);
		System.out.println("enque");
		sendeAnServer();
	}
	
	public MessageObject gebeWeiterAnClient(){
	MessageObject j;
	if (EmpfangeVomServer.isEmpty()!=false){
	j=EmpfangeVomServer.removeFirst();
	return j;}
	else {
		return null;
	}	
}
	
	public void beende() throws IOException{
		clientOpen=false;
		OIS.close();
		OOS.close();
		c.close();
		
	}
}
	

	