package pp2016.team16.server.engine;

/**
 * @ Gerber, Alina, 5961246
 */
import java.io.*;
import java.net.*;
import java.util.*;

import pp2016.team16.client.gui.HindiBones;
import pp2016.team16.shared.*;
import pp2016.team16.shared.Map;
import pp2016.team16.server.comm.ServerComm;
import pp2016.team16.server.engine.IServerEngine;
import pp2016.team16.server.map.AlleLevel;
import pp2016.team16.server.map.Leser;


public class ServerEngine extends Thread
{ 
	MessageObject serverDatenbestand = new MessageObject();
	ServerComm server;
	Konstanten konstante =new Konstanten();
	public Map map = new Map();
	public Spieler spieler = new Spieler("img//spieler.png",this);
	public LinkedList<Monster> monsterListe;
	public boolean eingeloggt;
	

	public ServerEngine() {
		this.start();
		System.out.println("Starte Server");
		
	}
	public void run(){
		server = new ServerComm();
		while(server.serverOpen){
			MessageObject m = server.gebeWeiterAnServer();
			if(m == null){
				System.out.println("Test1");
				try {
					sleep(6000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else this.nachrichtenVerarbeiten(m);
		}
	}
	

	/**
	 * Message-Handling @ Gerber, Alina , 5961246
	 */
	void nachrichtenVerarbeiten(MessageObject eingehendeNachricht) {
		if (eingehendeNachricht instanceof LoginMessage) 
		{  LoginMessage l = (LoginMessage) eingehendeNachricht;
		  // this.logIn(l.artVonAnmeldung, l.name, l.passwort);
			LoginAnswerMessage answer =new LoginAnswerMessage();
			if(this.eingeloggt){
				spieler.setName(l.name);
				spieler.setPasswort(l.passwort);
				answer.eingeloggt= eingeloggt;
				answer.name = l.name;
				answer.passwort = l.passwort;
				answer.levelzaehler = map.levelzaehler;
				
				
			}
		   
		}/* else if (eingehendeNachricht instanceof LogoutMessage)
		{
			System.out.println("Server Shutdown");
			break; // Aufforderung zum runterfahren
			
		} else */if (eingehendeNachricht instanceof ChangeLevelMessage) {
			System.out.println("Server hat Level-Anfrage erhalten");
			map.levelzaehler = ((ChangeLevelMessage) eingehendeNachricht).levelzaehler;
			map.breite = konstante.WIDTH;
			map.hoehe = konstante.HEIGHT;
			AlleLevel levelObject = new AlleLevel(map.hoehe, map.breite);
			map.level =levelObject.setzeInhalt(map.levelzaehler);
			Leser l = new Leser(map.level, this);
			this.spieler = l.sengine.spieler;
			this.monsterListe = l.sengine.monsterListe;
			map.karte = l.getLevel();
			ChangeLevelMessage answer = new ChangeLevelMessage();
			answer.level = map.level;
			answer.levelzaehler = map.levelzaehler;
			server.gebeWeiterAnClient(answer);
			
		} else if (eingehendeNachricht instanceof MoveMessage) {
			System.out.println("Der Spieler möchte sich bewegen");
			MoveMessage m = (MoveMessage) eingehendeNachricht;
			spieler.setPos(m.altX, m.altY);
			spieler.zielX = m.neuX;
			spieler.zielY = m.neuY;
			int richtung = spieler.geheZumZiel();
			MoveMessage answer = new MoveMessage();
			answer.richtung = richtung;
			server.gebeWeiterAnClient(answer);
			
		} else if (eingehendeNachricht instanceof ItemUseMessage){
			ItemUseMessage answer = new ItemUseMessage();
			// Schluessel aufnehmen
			if (map.karte[spieler.getXPos()][spieler.getYPos()] instanceof Schluessel) {
				spieler.nimmSchluessel();
				map.karte[spieler.getXPos()][spieler.getYPos()] = new Boden();
				answer.art = 1;
				
			}
			// Heiltrank aufnehmen
			else if (map.karte[spieler.getXPos()][spieler.getYPos()] instanceof Heiltrank) {
				spieler.nimmHeiltrank((Heiltrank) map.karte[spieler.getXPos()][spieler.getYPos()]);		
				map.karte[spieler.getXPos()][spieler.getYPos()] = new Boden();
				answer.art = 0;
			}
			// Schluessel benutzen
			if (map.karte[spieler.getXPos()][spieler.getYPos()] instanceof Tuer) {
				if (!((Tuer) map.karte[spieler.getXPos()][spieler.getYPos()]).istOffen() && spieler.hatSchluessel()) {
					((Tuer) map.karte[spieler.getXPos()][spieler.getYPos()]).setOffen();
					// Nach dem Oeffnen der Tuer ist der Schluessel wieder weg
					spieler.entferneSchluessel();
					answer.art =2;
				}
			}
			server.gebeWeiterAnClient(answer);
			
		}/*else if (eingehendeNachricht instanceof CheatMessage) {
			int i = ((CheatMessage) eingehendeNachricht).i;
			switch (i) {
			case 1:
				System.out.println("Cheat Nummer 1, zB Leben erhöhen");
				break;
			case 2:
				System.out.println("Cheat Nummer 2, zB Monster schwächen");
				break;
			case 3:
				System.out.println("Cheat Nummer 3, zB Schusszahl erhöhen");
			}
			CheatMessage answer = new CheatMessage(i);
			nachrichtSchicken(answer);
		} else // unbekannter message Type
		{
			new Exception(
					"Server hat eine Nachricht erhalten, die nicht verarbeitet werden kann");
		}*/
	}
	
/*Ann-Catherine Hartmann,37658
 * 
 //Die Methode sollte im boolean eingeloggt true speichern, falls man erfolgreich 
  * eine anmeldung oder einen login durchgeführt hat;
  * außerdem sollte wenn es eine neue anmeldung ist 1 bei this.map.levelzaehler gespeichert werden
  * wenn man sich einlogt, dann soll nicht nur geprüft werden ob name und passwort zusammen passen sondern 
  * auch noch die levelnr ausgelesen werden ( die speichern wir mit), und an this.map.levelzaehler übergeben werden*/
public void logIn(int i, String name, String passwort){
	String abgleich = name + " "+ passwort;
	if (i ==1){ //Neuanmeldung	
		String initiallevel = "Level 1";
		FileWriter fw;
		try {
			fw = new FileWriter ("Spielerdaten",true);
			BufferedWriter bw = new BufferedWriter (fw);
			bw.newLine();
			bw.write(abgleich);
			bw.newLine();
			bw.write (initiallevel);
			bw.close();//Schließt die Datei
			fw.close();
			eingeloggt = true;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	}else if(i==2){
		
		try {
			FileReader fr;
			fr=new FileReader("Spielerdaten");
			BufferedReader br = new BufferedReader(fr);
			String zeile;
			do {
				zeile =br.readLine();
				if (zeile == abgleich){
					eingeloggt = true;
				}
			}while(zeile!=null);
			
			br.close();
			fr.close();
			
		} catch ( IOException e) {
		}
		
	}
}

//Ann-Catherine Hartmann,37658
public void leseHighScore(){
	
}
//Ann-Catherine Hartmann,37658
public void setHighScore(){
	
}
//Ann-Catherine Hartmann,37658
public void abmelden(){
	
	
	
}
//Ann-Catherine Hartmann,37658
 /*es wird immer name, passwort und levelnr gespeichert
  wichtig ist, dass die bereits bestehenden eintragungen entweder überschrieben oder gelöscht werden*/
public void speichern( String name, String passwort, int levelNr){
	
}
}
