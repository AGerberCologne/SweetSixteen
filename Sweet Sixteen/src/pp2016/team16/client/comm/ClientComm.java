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
		int z=1;
		while (clientOpen){
			if (isInterrupted()){
				System.out.println("FEHLER");
				break;
			}
			empfangeVomServer();
			if (z==10){
			schickeIchBinDaNachricht();
			z=1;
			}
			z++;
			try {
				sleep(600);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void sendeAnServer(){

		
		try{
			
			MessageObject msg = sendeAnServer.removeFirst();
			System.out.println("Clientest 1");
			oos = new ObjectOutputStream(c.getOutputStream());
			System.out.println("Clientest 2");
			oos.writeObject(msg);
			System.out.println("Clientest 3");
			oos.flush();
			System.out.println("Clientest 4");
			//ois=new ObjectInputStream(c.getInputStream());
			//empfangeVomServer();
			if(msg instanceof LogoutMessage)
				beende();
			
			
		}catch(IOException e){
			System.out.println("sendeanServer");
		} 
		
	}
	public void empfangeVomServer() {
		
		try {
			ois=new ObjectInputStream(c.getInputStream());
			MessageObject bmsg = (MessageObject)ois.readObject();
			empfangeVomServer.addLast(bmsg);
		} catch (IOException e) {
			System.out.println("Test 7");
		} catch (ClassNotFoundException e) {
			
		}
		
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
	
	public void schickeIchBinDaNachricht(){
		IchBinDa i= new IchBinDa();
		bekommeVonClient(i);
	}
}
	

	