package pp2016.team16.client.comm;
/**
 * @author: Ann-Catherine Hartmann, Matrikelnr: 60038514/ Prüfungsnummer: 37658
 **/

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import pp2016.team16.shared.*;

public class ClientComm extends Thread{
	LinkedList<MessageObject> empfangeVomServer = new LinkedList<MessageObject>();//Empfangeschlange
	LinkedList<MessageObject> sendeAnServer=new LinkedList<MessageObject>();//Sendeschlange
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
			oos = new ObjectOutputStream(c.getOutputStream());
			oos.flush();
			ois=new ObjectInputStream(c.getInputStream());
			this.start();
		}catch(IOException e){
			
		}
	}

	
	
	
	public void run(){
		while (clientOpen && c.isClosed()!=true){
			
			schickeIchBinDaNachricht();
			empfangeVomServer();
			
			try {
				sleep(600);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			if (isInterrupted()){
				break;
			}
		}
			//beende();
		
	}
	public void sendeAnServer(){

		
		try{
			MessageObject msg = sendeAnServer.removeFirst();
			System.out.println("Clientest 1");
			System.out.println("Clientest 2");
			oos.writeObject(msg);
			System.out.println("Clientest 3");
			oos.flush();
			System.out.println("Clientest 4");
			if(msg instanceof BeendeMessage){
				System.out.println("BeendeMessage erkannt");
				System.out.println("alles geschlosen");}
			
			
		}catch(IOException e){
			this.interrupt();
			System.out.println("sendeanServer");
		} 
		
	}
	public void empfangeVomServer() {
		
		try {
			
			System.out.println("Leere Nachricht");
			MessageObject bmsg = (MessageObject)ois.readObject();
			empfangeVomServer.addLast(bmsg);
		} catch (IOException e) {
			
			//e.printStackTrace();
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
	
	
	public void beende(){
	
		//clientOpen=false;
		
		try {
			
			clientOpen=false;
			
			c.shutdownInput();
			c.shutdownOutput();
			c.close();
			ois.close();
			oos.close();
			
			
			
			
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();}
		
	}
	
	public void schickeIchBinDaNachricht(){
		IchBinDa i= new IchBinDa();
		System.out.println("Ich bin da");
		bekommeVonClient(i);
	}
}
	

	