package pp2016.team16.server.engine;

/**
 * @ Gerber, Alina, 5961246
 */
import java.io.*;
import java.net.*;
import java.util.*;

import pp2016.team16.shared.*;
import pp2016.team16.server.comm.ServerComm;
import pp2016.team16.server.engine.IServerEngine;
import pp2016.team16.server.map.AlleLevel;


class ServerEngine
{
	MessageObject serverDatenbestand = new MessageObject();
	ServerComm server = new ServerComm();
	public int[][] map ;
	public int levelzaehler;

	ServerEngine() throws InterruptedException, IOException {
		System.out.println("Starte Server");

	}
	public void run(){
		while(server.serverOpen){
			MessageObject m = server.gebeWeiterAnServer();
			this.nachrichtenVerarbeiten(m);
		}
	}
	

	/**
	 * Message-Handeling @ Gerber, Alina , 5961246
	 */
	void nachrichtenVerarbeiten(MessageObject eingehendeNachricht) {
		/*if (eingehendeNachricht instanceof LoginMessage) 
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
				System.out.println("Server hat LoginDaten an den Client gesendet");
			} else {
				answer.success = 0;
				messageSchicken(answer);
				System.out.println("Server konnte LoginDaten nicht finden");
			}

		} else if (eingehendeNachricht instanceof LogoutMessage)
		{
			System.out.println("Server Shutdown");
			break; // Aufforderung zum runterfahren
		} else */if (eingehendeNachricht instanceof ChangeLevelMessage) {
			System.out.println("sERVER HAT lEVEL-nACHRICHT empfangen");
			this.levelzaehler = ((ChangeLevelMessage) eingehendeNachricht).levelzaehler;
			AlleLevel levelObject = new AlleLevel();
			map =levelObject.setzeInhalt(levelzaehler);
			ChangeLevelMessage answer = new ChangeLevelMessage();
			answer.map = levelObject.level;
			server.gebeWeiterAnClient(answer);
		} /*else if (eingehendeNachricht instanceof MoveMessage) {
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
		}*/
	}

}
