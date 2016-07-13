package pp2016.team16.server.comm;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

import pp2016.team16.shared.BeendeMessage;
import pp2016.team16.shared.IchBinDa;
import pp2016.team16.shared.MessageObject;



/**
 * Handelt die Kommunikation zwischen Server und Client auf der Serverseite
 * 
 * @author: Ann-Catherine Hartmann 6038514
 **/
public class ServerComm extends Thread {
	public ServerSocket serverS;
	public Socket s;
	int port = 10000;
	public boolean serverOpen;
	ObjectOutputStream ost = null;
	ObjectInputStream in = null;
	/**
	 * @param Empfangeschlange
	 */
	LinkedList<MessageObject> empfangeVomClient = new LinkedList<MessageObject>();
	/**
	 * @param Sendeschlange
	 */
	LinkedList<MessageObject> sendeAnClient = new LinkedList<MessageObject>();
	
	/**
	 * Hier wird das Serversocket initialisiert und setzt den Boolean serverOpen auf true,
	 * damit run() die Nachrichten vom Client kontinuierlich aus dem InputStream liest 
	 * über die Methode verarbeiteNachricht(). Zudem wird run() mit Hilfe von this.start() 
	 * aufgerufen.
	 * 
	 * @author Ann-Catherine Hartmann 6038514
	 */
	public ServerComm() {
		try {
			serverS = new ServerSocket(port);
			serverOpen = true;
			this.start();
		} catch (IOException e) {
		}
	}
	/**
	 * Die Methode run() initialisiert das Socket s, indem es sich mit dem Serversocket verbindet,
	 * den Objectoutput- und Inputstream und flush bei seiner Initailisierung auch den Objectoutput-
	 * stream. Während der Boolean serverOpen true ist, wird periodisch die Methode verarbeiteNachricht()
	 * aufgerufen, um die vom Client gesendeten Objekte an den Server weitergeben zu können. Wenn 
	 * serverOpen vom der ServerEngine auf false gesetzt wird, wird die Methode schliessen() aufge-
	 * rufen.
	 * 
	 * @author Ann-Catherine Hartmann 6038514
	 */
	public void run() {
		try {
			s = serverS.accept();
			ost = new ObjectOutputStream(s.getOutputStream());
			ost.flush();
			in = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			 e.printStackTrace();
		}
		while (serverOpen) {
			verarbeiteNachricht();
			try {
				sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		this.schliesse();
	}
	/**
	 * Diese Methode liest das Objekt aus dem InputStream und prüft danach, ob es sich um eine
	 * IchBinDa-Nachricht handelt. Wenn dies der Fall ist, wird nichts weiter gemacht außer einer
	 * Ausgabe von "Ich bin da", damit man sieht, dass die Methode funktioniert. Wenn dies nicht 
	 * der Fall ist, dass wird das Objekt in die Empfangeschlange eingefügt. 
	 * 
	 * @author Ann-Catherine Hartmann 6038514
	 */
	public void verarbeiteNachricht() {
		try {

			MessageObject n;
			n = (MessageObject) in.readObject();
			if (n instanceof IchBinDa) {
				System.out.println("IchBinDa");
			} else if (n instanceof BeendeMessage) {
				schliesse();
			} else {
				empfangeVomClient.addLast(n);
			}
		} catch (IOException | ClassNotFoundException e) {
			 e.printStackTrace();
			
		}

	}
	/**
	 * Diese Methode gibt das erste Element der Empfangeschlange zurück, wenn die Empfangeschlange
	 * nicht leer ist, ansonsten wird null zurückgegeben
	 * 
	 * @return die erste Message der Empfangeschlange oder null, wenn es kein Element in
	 * der LinkedList gibt 
	 * @author Ann-Catherine Hartmann 6038514
	 */
	public MessageObject gebeWeiterAnServer() {
		if (empfangeVomClient.isEmpty() == false) {
			MessageObject anfrage;
			anfrage = empfangeVomClient.removeFirst();
			return anfrage;
		} else
			return null;
	}
	/**
	 * Die Methode fügt die übergebene Nachricht an das Ende der Sendeschlange und ruft dann
	 * die Methode sendeAnClient() auf. 
	 * 
	 * @param servermessage ist die Message, die der Server an den Client schicken will
	 * @author Ann-Catherine Hartmann 6038514
	 */
	public void gebeWeiterAnClient(MessageObject servermessage) {
		sendeAnClient.addLast(servermessage);
		sendeAnClient();
	}
	/**
	 * Schreibt das erste Element der Sendeschlange in den ObjectOutputStream und flusht diesen.
	 * Sollte dies nicht mehr funktionieren, weil der Client geschlossen wird, wird serverOpen auf 
	 * false gesetzt, damit sich alles schliesst
	 * 
	 * @author Ann-Catherine Hartmann 6038514
	 */
	public void sendeAnClient() {
		try {
			MessageObject m = sendeAnClient.removeFirst();
			ost.writeObject(m);
			ost.flush();
		} catch (IOException e) {
			serverOpen = false;

		}

	}
	/**
	 * Diese Methode schließt den Objectinput-, Objectoutputstream, das Serversocket und das
	 * Socket.
	 * 
	 * @author Ann-Catherine Hartmann 6038514
	 */
	public void schliesse() {
		try {
			in.close();
			ost.close();
			serverS.close();
			s.close();
		} catch (IOException e) {
			
		}

	}

}
