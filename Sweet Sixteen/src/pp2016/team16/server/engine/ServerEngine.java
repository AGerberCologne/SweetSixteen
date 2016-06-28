package pp2016.team16.server.engine;

/**
 * @ Gerber, Alina, 5961246
 */
import java.io.*;
import java.net.*;
import java.util.*;

import pp2016.team16.shared.*;

class ServerEngine extends Thread // entweder extends Thread oder implements
									// Runnable sind notwendig um mehrere
									// Threads gleichzeitig laufen zu lassen.
									// Dies ist notwendig, da Server um Client
									// natürlich parallel aktiv sein müssen
{
	MessageObject serverDatenbestand = new MessageObject(); // In diesem Objekt
															// speichert der
															// Sercer seine
															// internen Daten

	ServerEngine() throws InterruptedException, IOException {
		System.out.println("Starte Server");

	}

	/**
	 * Message-Handeling @ Gerber, Alina , 5961246
	 */
	void nachrichtenVerarbeiten(MessageObject eingehendeNachricht) {
		if (eingehendeNachricht instanceof LoginMessage) // Daten übernehmen,
															// dass heißt im
															// internen Objekt
															// speichern
		{
			System.out.println("Der Client möchte sich im Server einloggen");
			this.serverDatenbestand.ueberschreibe(eingehendeNachricht);
			LoginAnswerMessage answer = new LoginAnswerMessage();
			// Testdaten, diese müssten eigentlich vom Server geladen werden
			answer.CharacterID = 3;
			answer.startposition[3] = 4;
			if (true) {
				answer.success = 1;
				messageSchicken(answer);
				System.out
						.println("Server hat LoginDaten an den Client gesendet");
			} else {
				answer.success = 0;
				messageSchicken(answer);
				System.out.println("Server konnte LoginDaten nicht finden");
			}

		} else if (eingehendeNachricht instanceof LogoutMessage) // Der Server
																	// bekommt
																	// die
																	// aufforderung
																	// zum
																	// beenden.
		{
			System.out.println("Server Shutdown");
			break; // Aufforderung zum runterfahren
		} else if (eingehendeNachricht instanceof ChangeLevelMessage) {
			System.out.println("Der Server soll neue Leveldaten laden");
			int[][] level = new int[16][16];
			ChangeLevelMessage answer = new ChangeLevelMessage();
			answer.ueberschreibe(eingehendeNachricht);
			answer.map = level;
			nachrichtSchicken(answer);
		} else if (eingehendeNachricht instanceof MoveMessage) {
			System.out.println("Der Spieler möchte sich bewegen");
			MoveMessage answer = new MoveMessage();
			answer.ueberschreibe((MoveMessage) eingehendeNachricht);
			if (true) {
				answer.validerZug = true;
				nachrichtSchicken(answer);
			} else {
				answer.validerZug = false;
				nachrichtSchicken(answer);
			}
		} else if (eingehendeNachricht instanceof CheatMessage) {
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
		}
	}

}
