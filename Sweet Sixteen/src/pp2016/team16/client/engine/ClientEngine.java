package pp2016.team16.client.engine;

/**
 * @ Gerber, Alina, 5961246
 */
import java.io.*;
import java.net.*;
import java.util.*;

import pp2016.team16.shared.*;
import pp2016.team16.shared.Map;
import pp2016.team16.client.comm.ClientComm;
import pp2016.team16.client.gui.HindiBones;

public class ClientEngine extends Thread// entweder extends Thread oder implements
// Runnable sind notwendig um mehrere
// Threads gleichzeitig laufen zu lassen.
// Dies ist notwendig, da Server um Client
// natürlich parallel aktiv sein müssen
{
	// In diesem Objekt speichert der Client interne Daten
	ClientComm com;
	public Map map = new Map();
	public Spieler spieler = new Spieler();
	public LinkedList<Monster> monsterListe;
	public boolean eingeloggt = false;
	public boolean neuesLevel = false;

	 public ClientEngine()  {
		System.out.println("Starte Client");
		com = new ClientComm();
		this.start();

	}
	
	 public void run(){
		 while(com.clientOpen){
			 MessageObject m = com.gebeWeiterAnClient();
			 if(m == null){
				 System.out.println("Test 2");
				 try {
					sleep(6000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 else{ 
				 try {
					 this.nachrichtVerarbeiten(m);
				 } catch (Exception e) {
					 // TODO Auto-generated catch block
					 e.printStackTrace();
				 }
			 }
		 }
	 }
	/*
	 * public void run() // Run Methode wird in der Main durch den .start()
	 * Befehl // aufgerufen. { System.out.println("");// Für die Formatierung
	 * try { // Beim ersten Start downloaded der Client die Spielerdaten vom //
	 * Server String name = JOptionPane.showInputDialog("Username"); String
	 * passwort = JOptionPane.showInputDialog("Passwort"); this.login(name,
	 * passwort); this.sleep(2000); this.move(4, 5); this.sleep(2000);
	 * this.changeLevel(); this.sleep(2000); this.cheatBenutzen(2);
	 * this.sleep(2000); this.logout(); } catch (Exception e) //
	 * Exceptionbehandlung { System.out.println("Client Exception: " + e); } }
	 */
	// Message-Handling
	void nachrichtVerarbeiten(MessageObject daten) throws Exception {
		  if (daten instanceof LoginAnswerMessage) {
			  LoginAnswerMessage l = (LoginAnswerMessage) daten;
			  map.level = l.map;
			  map.levelzaehler = l.levelzaehler;
			  spieler.setName(l.name);
			  spieler.setPasswort(l.passwort);
			  this.eingeloggt= l.eingeloggt;
			
		} else  if (daten instanceof ChangeLevelMessage) {
			map.karte = ((ChangeLevelMessage) daten).map;
			map.levelzaehler = ((ChangeLevelMessage) daten).levelzaehler;
			this.spieler =((ChangeLevelMessage) daten).spieler;
			this.monsterListe = ((ChangeLevelMessage) daten).monsterListe;
			this.neuesLevel = true;
			System.out.println("Neues Level gespeichert");

		} else if (daten instanceof MoveMessage) {
			int richtung = ((MoveMessage) daten).richtung;
			switch(richtung){
				case 1:
				case 2:
				case 3:
				case 0:
			}
			
			
		} /*else if (daten instanceof CheatMessage) {
			int i = ((CheatMessage) daten).i;
			System.out.println("Der Cheat wurde angenommen & zwar Nr. " + i
					+ "\n");
		}*/
	}


	// Methoden für GUI
	
	/*public void übergebe(){
		if( f instanceof Spieler){
			this.spieler = (Spieler) f;
		}
		else this.monster = (Monster) f;
	}*/
	
	public boolean login( int i, String n, String p) throws InterruptedException  { 
		LoginMessage anfrage = new LoginMessage(i, n, p);
		com.bekommeVonClient(anfrage);
		return eingeloggt;
	}

	void logout() throws Exception {
		LogoutMessage anfrage = new LogoutMessage();
	}

	// Diese Methode wird entweder eine Liste/Array etc. zurückgeben, in dem der
	// von Astern berechnete Weg gespeicher tist
	void wegAnfragen(int x, int y) throws InterruptedException {
		spieler.zielX = x;
		spieler.zielY = y;
		MoveMessage anfrage = new MoveMessage();
		anfrage.altX = spieler.getXPos();
		anfrage.altY = spieler.getYPos();
		anfrage.neuX = spieler.zielX;
		anfrage.neuY = spieler.zielY;
		com.bekommeVonClient(anfrage);
		wait();
	}

	

	public Spielelement[][] changeLevel() throws Exception{
		System.out.println("Der Client fragt ein neues Level an");
		ChangeLevelMessage anfrage = new ChangeLevelMessage();
		anfrage.levelzaehler = this.map.levelzaehler;
		com.bekommeVonClient(anfrage);
		while(this.neuesLevel == false){
		//	System.out.println("Neues Level wurde noch nicht geladen");
		}
		this.neuesLevel = false;
		System.out.println("Endlch geschafft");
		return map.karte;
	}

	void benutzeItem() {

	}



	
	/*void cheatBenutzen(int i) throws Exception {
		CheatMessage cheat = new CheatMessage(i);
		this.datenBeimServerAnfragen(cheat);
	}
*/
}
