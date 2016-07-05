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
	public Spielelement[][] karte = new Spielelement[21][21];
	

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
			this.karte = l.getLevel();
			ChangeLevelMessage answer = new ChangeLevelMessage();
			//ChangeLevelMessage answer = new ChangeLevelMessage(spieler,monsterListe,karte);
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
		} /*else if (eingehendeNachricht instanceof CheatMessage) {
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
		bw.close();//Schließt die Datei
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
public void speichern(){
	
}*/
}
