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
		int z=1;
		while (clientOpen){
			if (isInterrupted()){
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
			System.out.println("Clientest 2");
			oos.writeObject(msg);
			System.out.println("Clientest 3");
			oos.flush();
			System.out.println("Clientest 4");
			if(msg instanceof BeendeMessage)
				beende();
			
			
		}catch(IOException e){
			System.out.println("sendeanServer");
		} 
		
	}
	public void empfangeVomServer() {
		
		try {
			
			System.out.println("Leere Nachricht");
			MessageObject bmsg = (MessageObject)ois.readObject();
			empfangeVomServer.addLast(bmsg);
		} catch (IOException e) {
			System.out.println("Test 7");
			e.printStackTrace();
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
		this.interrupt();
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
	

	