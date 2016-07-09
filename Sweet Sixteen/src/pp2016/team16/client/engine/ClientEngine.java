package pp2016.team16.client.engine;

/**
 * @ Gerber, Alina, 5961246
 */
import java.io.*;
import java.net.*;
import java.util.*;

import pp2016.team16.server.map.Leser;
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
	public Konstanten konstante = new Konstanten();
	public Map map = new Map();
	public Spieler spieler = new Spieler("img//spieler.png",this);
	public LinkedList<Monster> monsterListe;
	public boolean eingeloggt = false;
	public boolean login=false;
	public boolean neuesLevel = false;
	public int itemBenutzen = 4;
	
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
					sleep(600);
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
	
	// Message-Handling
	void nachrichtVerarbeiten(MessageObject daten) throws Exception {
		  if (daten instanceof LoginAnswerMessage) {
			  LoginAnswerMessage l = (LoginAnswerMessage) daten;
			  map.level = l.map;
			  map.levelzaehler = l.levelzaehler;
			  spieler.setName(l.name);
			  spieler.setPasswort(l.passwort);
			  this.eingeloggt= l.eingeloggt;
			  System.out.println("Login wurde empfangen");
			  this.login = true;
			
		} else  if (daten instanceof ChangeLevelMessage) {
			ChangeLevelMessage c = (ChangeLevelMessage) daten;
			map.breite= konstante.WIDTH;
			map.hoehe = konstante.HEIGHT;
			Leser l = new Leser(c.level, this);
			this.spieler = l.cengine.spieler;
			this.monsterListe = l.cengine.monsterListe;
			map.karte = l.getLevel();
			map.levelzaehler = c.levelzaehler;
			this.neuesLevel = true;
			System.out.println("Neues Level gespeichert");
			

		} else if (daten instanceof SBewegungMessage) {
			System.out.println("Neue Position");
			SBewegungMessage position = (SBewegungMessage) daten;
			this.spieler.setPos(position.neuX, position.neuY);
			
		} else if (daten instanceof ItemStatusMessage){
			// Schluessel aufnehmen
			if(((ItemStatusMessage) daten).art == 1) {
				spieler.nimmSchluessel();
				map.karte[spieler.getXPos()][spieler.getYPos()] = new Boden();
				this.itemBenutzen = 1;

			}
			// Heiltrank aufnehmen
			else if (((ItemStatusMessage) daten).art == 0) {
				spieler.nimmHeiltrank((Heiltrank) map.karte[spieler.getXPos()][spieler.getYPos()]);		
				map.karte[spieler.getXPos()][spieler.getYPos()] = new Boden();
				this.itemBenutzen = 0;
			}
			// Schluessel benutzen
			if (((ItemStatusMessage) daten).art == 2) {
				if (!((Tuer) map.karte[spieler.getXPos()][spieler.getYPos()]).istOffen() && spieler.hatSchluessel()) {
					((Tuer) map.karte[spieler.getXPos()][spieler.getYPos()]).setOffen();
					// Nach dem Oeffnen der Tuer ist der Schluessel wieder weg
					spieler.entferneSchluessel();
					if(map.levelzaehler <= konstante.MAXLEVEL){
						this.itemBenutzen = 2;
					}
					else this.itemBenutzen = 3;
				}
			}
		}
		  
		  /*else if (daten instanceof CheatMessage) {
			int i = ((CheatMessage) daten).i;
			System.out.println("Der Cheat wurde angenommen & zwar Nr. " + i
					+ "\n");
		}*/
	}


	// Methoden für GUI
	
	
	public boolean login(int i, String n, String p) throws InterruptedException  { 
		LoginMessage anfrage = new LoginMessage(i, n, p);
		com.bekommeVonClient(anfrage);
		while(login == false){
			System.out.println("Warte auf Login Antwort");
			sleep(600);
		}
		this.login = false;
		return eingeloggt;
	}

	void logout() throws Exception {
		LogoutMessage anfrage = new LogoutMessage();
	}

	// kann , wenn notwendig , neues x oder y zurückgeben...
	public void wegAnfragen(int x, int y) throws InterruptedException {
		System.out.println("Der Spieler möchte sich bewegen");
		spieler.zielX = x;
		spieler.zielY = y;
		SBewegungMessage anfrage = new SBewegungMessage();
		anfrage.altX = spieler.getXPos();
		anfrage.altY = spieler.getYPos();
		anfrage.neuX = spieler.zielX;
		anfrage.neuY = spieler.zielY;
		com.bekommeVonClient(anfrage);
		sleep(10000);
	}

	
// evtl besser kein reurn, sondern abruf über map.karte
	public Spielelement[][] changeLevel() throws Exception{
		System.out.println("Der Client fragt ein neues Level an");
		ChangeLevelMessage anfrage = new ChangeLevelMessage();
		this.map.levelzaehler = 1;
		anfrage.levelzaehler = this.map.levelzaehler;
		com.bekommeVonClient(anfrage);
		while(this.neuesLevel == false){
			System.out.println("Neues Level wurde noch nicht geladen");
			sleep(600);
		}
		this.neuesLevel = false;
		System.out.println("Endlich geschafft");
		return map.karte;
	}
// 0, 1 = aufnehmen, 2 = neues level, 3 = spiel beenden
	public int benutzeItem() throws InterruptedException {
		itemBenutzen= 4;
		ItemStatusMessage  anfrage = new ItemStatusMessage();
		com.bekommeVonClient(anfrage);
		while(itemBenutzen == 4){
			sleep(600);
		}
		return itemBenutzen;
	}



	
	/*void cheatBenutzen(int i) throws Exception {
		CheatMessage cheat = new CheatMessage(i);
		this.datenBeimServerAnfragen(cheat);
	}
*/
}
