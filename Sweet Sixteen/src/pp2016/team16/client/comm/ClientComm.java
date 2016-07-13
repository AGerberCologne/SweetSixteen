package pp2016.team16.client.comm;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import pp2016.team16.shared.*;

/**
 * Handelt die Kommunikation zwischen der Server- und Clientengine auf der Clientseite
 * 
 * @author: Ann-Catherine Hartmann, Matrikelnr: 60038514
 **/


public class ClientComm extends Thread {
	/**
	 * @param Empfangeschlange
	 */
	LinkedList<MessageObject> empfangeVomServer = new LinkedList<MessageObject>();
	/**
	 * @param Sendeschlange
	 */
	LinkedList<MessageObject> sendeAnServer = new LinkedList<MessageObject>();
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	Socket client;
	/**
	 * @param host unseres Clientsockets. Dieser ist leider nur über diesen String änderbar aufgrund
	 * unserer Objektstruktur
	 */
	public String host = "localhost";
	/**
	 * @param Portnummer unseres Servers, ist leider nicht veränderbar über
	 *  main-Methode aufgrund unserer Objektstruktur;
	 */
	public int port = 10000;
	public boolean clientOpen;

	/**
	 * Der Konstruktor initialisiert das Socket, setzt den Booleanwert
	 * clientOpen auf true, damit danach die run-Methode immer Nachrichten vom
	 * Server empfängt und die Ichbinda-Nachrichten verschickt, initialisiert
	 * den ObjectInput-und ObjectOutputStream und flusht den OutputStream.
	 * Zuletzt wird die run-Methde mit start() aufgerufen
	 * 
	 * @author Ann-Catherine Hartmann 6038514
	 */
	public ClientComm() {
		try {
			client = new Socket(host, port);
			clientOpen = true;
			oos = new ObjectOutputStream(client.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(client.getInputStream());
			this.start();
		} catch (IOException e) {

		}
	}

	/**
	 * solange clientOpen true ist und das Socket nicht geschlossen ist, schickt
	 * die Methode eine IchBinDaNachricht periodisch und fragt an, ob etwas vom
	 * Server geschickt wurde
	 * 
	 * @author Ann-Catherine Hartmann 6038514
	 */

	public void run() {
		while (clientOpen && client.isClosed() != true) {

			schickeIchBinDaNachricht();
			empfangeVomServer();

			try {
				sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (isInterrupted()) {
				break;
			}
		}

	}
	/**
	 * Das erste Element der Sendeschlange wird entfernt und in den Output
	 * 
	 * @author Ann-Catherine Hartmann 6038514
	 */
	public void sendeAnServer() {

		try {
			MessageObject msg = sendeAnServer.removeFirst();
			oos.writeObject(msg);
			oos.flush();

		} catch (IOException e) {
			this.interrupt();
		}

	}
	/**
	 * Die Methode empfängt das nächste Objekt aus dem Inputstream und fügt dies als
	 * letztes Element in die Empfangeschlange ein.
	 * 
	 * @author Ann-Catherine Hartmann 6038514
	 */
	public void empfangeVomServer() {

		try {
			
			MessageObject antwort = (MessageObject) ois.readObject();
			empfangeVomServer.addLast(antwort);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {

		}

	}
	/**
	 * hier wird das Objekt übergeben, das an den Server geschickt werden soll. Dieses 
	 * wird als Letztes in die Sendeschlange eingefügt und die Methode sendeAnServer wird
	 * aufgerufen.
	 * 
	 * @author Ann-Catherine Hartmann 6038514
	 */
	public void bekommeVonClient(MessageObject anfrage) {
		sendeAnServer.addLast(anfrage);
		sendeAnServer();
		
	}
	/**
	 * wenn die Empfangeschlange nicht leer ist, wird das erste Element der Liste 
	 * zurückgegeben, ansonsten wird null zurückgegeben 
	 * 
	 * @return das erste Nachrichtenobjekt der Empfangeschlange oder null, wenn 
	 * Empfangeschlange leer ist
	 * 
	 * 
	 * @author Ann-Catherine Hartmann 6038514
	 *
	 */
	public MessageObject gebeWeiterAnClient() {
		MessageObject antwort;
		if (empfangeVomServer.isEmpty() == false) {
			antwort = empfangeVomServer.removeFirst();
			return antwort;
		} else {
			return null;
		}
	}

	/**
	 * Setzt zuerst den Boolean clientOpen auf false, damit die run()-Methode aus meiner 
	 * Klasse und aus der der Cientengine beendet wird. Zudem wird der Input-, Output- , 
	 * Objectinput-, Objectputstream und das Socket geschlossen
	 * 
	 * @author Ann-Catherine Hartmann 6038514  
	 *
	 */

	public void beende() {
		try {

			clientOpen = false;

			client.shutdownInput();
			client.shutdownOutput();
			client.close();
			ois.close();
			oos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * schickt eine Nachricht des Typs IchBinDa an den Server
	 * 
	 * @author Ann-Catherine Hartmann 6038514
	 */
	public void schickeIchBinDaNachricht() {
		IchBinDa ibd = new IchBinDa();
		bekommeVonClient(ibd);
	}
}
	