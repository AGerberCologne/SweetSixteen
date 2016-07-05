package pp2016.team16.client.comm;
//Gruppe 16 Kommunikation; Ann-Catherine Hartmann

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import pp2016.team16.shared.IchBinDa;
import pp2016.team16.shared.LogoutMessage;
import pp2016.team16.shared.MessageObject;

public class ClientComm extends Thread{
	LinkedList<MessageObject> empfangeVomServer = new LinkedList<MessageObject>();
	LinkedList<MessageObject> sendeAnServer=new LinkedList<MessageObject>();
	ObjectInputStream ois=null;
	ObjectOutputStream oos=null;
	Socket c;
	public String host = "localhost";
	public int port = 10000;
	public boolean clientOpen;

	public ClientComm(){
		try{
			c = new Socket(host, port);
			clientOpen=true;
			this.start();
		}catch(IOException e){
			
		}
	}

	
	
	
	public void run(){
		while (clientOpen){
			if (isInterrupted()){
				System.out.println("FEHLER");
				break;
			}
			//IchBinDa i =new IchBinDa();
			//bekommeVonClient(i);
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				empfangeVomServer();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
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
	if (empfangeVomServer.isEmpty()== false){
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
	

	