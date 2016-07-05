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
	public Map map = new Map();
	public Spieler spieler = new Spieler();
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
			AlleLevel levelObject = new AlleLevel();
			map.levelzaehler = 1;
			map.level =levelObject.setzeInhalt(map.levelzaehler);
			Leser l = new Leser(map.level);
			this.spieler = l.sengine.spieler;
			this.monsterListe = l.sengine.monsterListe;
			map.karte = l.getLevel();
			ChangeLevelMessage answer = new ChangeLevelMessage();
			answer.level = map.level;
			answer.levelzaehler = map.levelzaehler;
			server.gebeWeiterAnClient(answer);
			
		} else if (eingehendeNachricht instanceof MoveMessage) {
			System.out.println("Der Spieler m�chte sich bewegen");
			MoveMessage m = (MoveMessage) eingehendeNachricht;
			spieler.setPos(m.altX, m.altY);
			spieler.zielX = m.neuX;
			spieler.zielY = m.neuY;
			int richtung = spieler.geheZumZiel();
			MoveMessage answer = new MoveMessage();
			answer.richtung = richtung;
			server.gebeWeiterAnClient(answer);
			
		} /*else if (eingehendeNachricht instanceof CheatMessage) {
			int i = ((CheatMessage) eingehendeNachricht).i;
			switch (i) {
			case 1:
				System.out.println("Cheat Nummer 1, zB Leben erh�hen");
				break;
			case 2:
				System.out.println("Cheat Nummer 2, zB Monster schw�chen");
				break;
			case 3:
				System.out.println("Cheat Nummer 3, zB Schusszahl erh�hen");
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
  * eine anmeldung oder einen login durchgef�hrt hat;
  * au�erdem sollte wenn es eine neue anmeldung ist 1 bei this.map.levelzaehler gespeichert werden
  * wenn man sich einlogt, dann soll nicht nur gepr�ft werden ob name und passwort zusammen passen sondern 
  * auch noch die levelnr ausgelesen werden ( die speichern wir mit), und an this.map.levelzaehler �bergeben werden
public void logIn(int i, String name, String passwort){
	if (i ==1){
	//neuer	
		FileWriter fw = new FileWriter ("Anmeldung"); //zum Abspeichern als Textdatei
		BufferedWriter bw = new BufferedWriter (fw);
		try{
			bw.write(String name);
			bw.newLine();
			}
		bw.write(ergebnisb);
		System.out.println(ergebnisb);
		bw.close();//Schlie�t die Datei
		}catch(FileNotFoundException e){
			System.err.println("Fehler");
		
		}
	}else if(i==2){
		
	}else{
		
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
 //es wird immer name, passwort und levelnr gespeichert
  * wichtig ist, dass die bereits bestehenden eintragungen entweder �berschrieben oder gel�scht werden
public void speichern( String name, String passwort, int levelNr){
	
}*/
}
