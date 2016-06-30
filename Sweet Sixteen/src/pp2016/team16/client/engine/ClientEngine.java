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

public class ClientEngine extends Thread // entweder extends Thread oder implements
// Runnable sind notwendig um mehrere
// Threads gleichzeitig laufen zu lassen.
// Dies ist notwendig, da Server um Client
// natürlich parallel aktiv sein müssen
{
	// In diesem Objekt speichert der Client interne Daten
	MessageObject clientDatenbestand = new MessageObject();
	Character c;

	ClientEngine() throws InterruptedException {
		System.out.println("Starte Client");

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
			System.out.println(daten.toString());
			if (((LoginAnswerMessage) daten).success == 1) {
				this.c = new Character(((LoginAnswerMessage) daten).CharacterID);
				this.c.position = ((LoginAnswerMessage) daten).startposition;
				this.datenBeimServerAnfragen(new ChangeLevelMessage(1));
			}
		} else if (daten instanceof ChangeLevelMessage) {
			if (((ChangeLevelMessage) daten).login = false) {
				System.out.println("Das neue Level wurde geladen \n");
			} else {
				System.out.println("Das erste Level wurde geladen");
				System.out.println("Der Login wurde abgeschlossen \n");
			}
			c.level = ((ChangeLevelMessage) daten).map;

		} else if (daten instanceof MoveMessage) {
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
		}
	}

	// Methoden für GUI
		void login(String n, String p) throws Exception {
		LoginMessage anfrage = new LoginMessage(n, p);
		this.datenBeimServerAnfragen(anfrage);
	}

	void logout() throws Exception {
		LogoutMessage exitNachricht = new LogoutMessage();
		this.datenBeimServerAnfragen(exitNachricht);
	}

	// Diese Methode wird entweder eine Liste/Array etc. zurückgeben, in dem der
	// von Astern berechnete Weg gespeicher tist
	public WegAnfragen(int x, int y) {

	}

	void bewege(int x, int y) throws Exception {

		MoveMessage move = new MoveMessage();
		move.posAlt = c.getPosition();
		move.posNeu[x] = y;
		System.out.println("Der Character will sich" + " zu Position [" + x
				+ ", " + y + "] bewegen");
		this.datenBeimServerAnfragen(move);
	}

	void changeLevel() throws Exception {
		System.out.println("Der Client fragt ein neues Level an");
		ChangeLevelMessage level = new ChangeLevelMessage();
		level.login = false;
		this.datenBeimServerAnfragen(level);
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
	void cheatBenutzen(int i) throws Exception {
		CheatMessage cheat = new CheatMessage(i);
		this.datenBeimServerAnfragen(cheat);
	}

}
