package pp2016.team16.client.engine;

/**
 * @ Gerber, Alina, 5961246
 */
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;

import pp2016.team16.shared.*;
import pp2016.team16.shared.Character;
import pp2016.team16.client.comm.ClientComm;

public class ClientEngine extends Thread// entweder extends Thread oder implements
// Runnable sind notwendig um mehrere
// Threads gleichzeitig laufen zu lassen.
// Dies ist notwendig, da Server um Client
// natürlich parallel aktiv sein müssen
{
	// In diesem Objekt speichert der Client interne Daten
	ClientComm client = new ClientComm();
	MessageObject clientDatenbestand = new MessageObject();
	public int [][] map;
	public int levelzaehler;
	public int posX;
	public int posY;
	Character c;

	 public ClientEngine()  {
		System.out.println("Starte Client");

	}
	
	public void run(){
		while(client.clientOpen){
			MessageObject m = client.gebeWeiterAnClient();
			try {
				this.nachrichtVerarbeiten(m);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		/*if (daten instanceof LoginAnswerMessage) {
			System.out.println(daten.toString());
			if (((LoginAnswerMessage) daten).success == 1) {
				this.c = new Character(((LoginAnswerMessage) daten).CharacterID);
				this.c.position = ((LoginAnswerMessage) daten).startposition;
				
			}
		} else */ if (daten instanceof ChangeLevelMessage) {
			map = ((ChangeLevelMessage) daten).map;
			levelzaehler = ((ChangeLevelMessage) daten).levelzaehler;
			System.out.println("Neues Level gespeichert");

		} /*else if (daten instanceof MoveMessage) {
			if (((MoveMessage) daten).validerZug == true) {
				c.position = ((MoveMessage) daten).posNeu;
				System.out
						.println("Der Spieler hat einen validen Zug gemacht \n");
			} else {
				c.position = ((MoveMessage) daten).posAlt;
				System.out.println("Das ist kein valider Zug \n");
			}
		} else if (daten instanceof CheatMessage) {
			int i = ((CheatMessage) daten).i;
			System.out.println("Der Cheat wurde angenommen & zwar Nr. " + i
					+ "\n");
		}*/
	}

/*
	// Methoden für GUI
		void login(String n, String p)  {
		LoginMessage anfrage = new LoginMessage(n, p);
		
	}

	void logout() throws Exception {
		LogoutMessage exitNachricht = new LogoutMessage();
		(exitNachricht);
	}

	// Diese Methode wird entweder eine Liste/Array etc. zurückgeben, in dem der
	// von Astern berechnete Weg gespeicher tist
	public WegAnfragen(int x, int y) {

	}

	void bewege(int x, int y) throws Exception {

		MoveMessage move = new MoveMessage();
		move.posAlt = 
		move.posNeu[x] = y;
		System.out.println("Der Character will sich" + " zu Position [" + x
				+ ", " + y + "] bewegen");
		SendeAnServer(move);
	} */

	public int[][] changeLevel() throws Exception{
		System.out.println("Der Client fragt ein neues Level an");
		ChangeLevelMessage level = new ChangeLevelMessage();
		level.levelzaehler = this.levelzaehler;
		client.bekommeVonClient(level);
		MessageObject answer = client.gebeWeiterAnClient();
		this.nachrichtVerarbeiten(answer);
		return map;
	}

	void benutzeItem() {

	}



	/*
	 * void lebenändern(int i){
	 * System.out.println("Der Client möchte das Leben ändern");
	 * CharacterChangeMessage change = new CharacterChangeMessage();
	 * change.änderungshöhe = i;
	 * 
	 * }
	 */
	/*void cheatBenutzen(int i) throws Exception {
		CheatMessage cheat = new CheatMessage(i);
		this.datenBeimServerAnfragen(cheat);
	}
*/
}
