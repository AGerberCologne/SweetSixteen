package pp2016.team16.client.engine;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;

import clientengine.Character;
import pp2016.team16.shared.*;

class ClientEngine extends Thread // entweder extends Thread oder implements
									// Runnable sind notwendig um mehrere
									// Threads gleichzeitig laufen zu lassen.
									// Dies ist notwendig, da Server um Client
									// natürlich parallel aktiv sein müssen
{
	MessageObject clientDatenbestand = new MessageObject(); // In diesem Objekt
															// speichert der
															// Client seine
															// internen Daten

	Socket s;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	Character c;

	ClientEngine() throws InterruptedException {
		System.out.println("Starte Client");

	}

	public void run() // Run Methode wird in der Main durch den .start() Befehl
						// aufgerufen.
	{
		System.out.println("");// Für die Formatierung
		try {
			// Beim ersten Start downloaded der Client die Spielerdaten vom
			// Server
			String name = JOptionPane.showInputDialog("Username");
			String passwort = JOptionPane.showInputDialog("Passwort");
			this.login(name, passwort);
			this.sleep(2000);
			this.move(4, 5);
			this.sleep(2000);
			this.changeLevel();
			this.sleep(2000);
			this.cheatBenutzen(2);
			this.sleep(2000);
			this.logout();
		} catch (Exception e) // Exceptionbehandlung
		{
			System.out.println("Client Exception: " + e);
		}
	}

	void beenden() throws Exception // Server Nachricht zum runterfahren senden
	{
		LogoutMessage exitNachricht = new LogoutMessage();
		this.datenBeimServerAnfragen(exitNachricht);

	}

	void datenBeimServerAnfragen(MessageObject anfrage) throws Exception // Den
																			// Server
																			// um
																			// seinen
																			// aktuellen
																			// Datenbestand
																			// bitten
	{
		this.connectionInitialisation();
		oos.writeObject(anfrage); // Nachricht in den Stream schreiben
		oos.flush(); // und abschicken
		this.datenVomServerErhalten();

	}

	void datenVomServerErhalten() throws Exception {
		System.out.println("Client wartet auf Daten vom Server");
		MessageObject daten = (MessageObject) ois.readObject();
		this.nachrichtVerarbeiten(daten);
	}

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

	void login(String n, String p) throws Exception {
		LoginMessage anfrage = new LoginMessage(n, p);
		this.datenBeimServerAnfragen(anfrage);
	}

	void logout() throws Exception {
		System.out
				.println("Client möchte sich ausloggen und den Server herunterfahren");
		this.beenden();
		this.connectionShutdown();
	}

	void move(int x, int y) throws Exception {

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

	void connectionInitialisation() throws UnknownHostException, IOException {
		s = new Socket("localhost", 1234); // Socket aufbauen

		oos = new ObjectOutputStream(s.getOutputStream()); // Streams
															// vorbereiten
		ois = new ObjectInputStream(s.getInputStream());
	}

	void connectionShutdown() throws IOException {
		oos.close(); // Streams schließen
		ois.close();
		s.close(); // Socket schließen
	}
}
