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
import pp2016.team16.client.gui.Highscore;
import pp2016.team16.client.gui.HindiBones;
import pp2016.team16.shared.Heiltrank;

public class ClientEngine extends Thread// entweder extends Thread oder implements
// Runnable sind notwendig um mehrere
// Threads gleichzeitig laufen zu lassen.
// Dies ist notwendig, da Server um Client
// natï¿½rlich parallel aktiv sein mï¿½ssen
{
	// In diesem Objekt speichert der Client interne Daten
	public ClientComm com;
	//public Highscore highScore;
	public Konstanten konstante = new Konstanten();
	public Map map = new Map();
	public Spieler spieler = new Spieler("img//spieler.png");
	public LinkedList<Monster> monsterListe = new LinkedList<Monster>();
	public LinkedList<String> highscore=new LinkedList<String>();
	public boolean eingeloggt = false;
	public boolean login=false;
	public boolean neuesLevel = false;
	public int itemBenutzen = 4;
	public boolean gespeichert=false;
	
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
					sleep(100);
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
		 }this.interrupt();
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
			while(!monsterListe.isEmpty()){
				monsterListe.remove();
			}
			for(int i=0;i<c.level.length;i++){
				for(int j=0;j<c.level.length;j++){
					int Variable = c.level[i][j];
					switch(Variable){
				case 0: map.karte[i][j] = new Wand(); break;
				case 1: map.karte[i][j] = new Boden(); break;
//				case 3: map.karte[i][j] = new Schluessel(); break;
				case 5: map.karte[i][j] = new Heiltrank(20); break;
				case 6: map.karte[i][j] = new Tuer(false); break;
				case 4: map.karte[i][j] = new Tuer(true); this.spieler.setPos(i, j); break;
				case 2: map.karte[i][j] = new Boden(); this.monsterListe.add(new Monster(i,j,0)); break;
				// Monster, welche erst nach dem Aufheben des Schluessels erscheinen
				case 3: map.karte[i][j] = new Schluessel(); break;
				case 8: map.karte[i][j] = new Boden(); this.monsterListe.add(new Monster(i,j,1)); break;
			}	
					
				}
				
			}
			
			
			
			map.levelzaehler = c.levelzaehler;
			this.neuesLevel = true;
			System.out.println("Neues Level gespeichert");
			

		} else if (daten instanceof SBewegungMessage) {
			System.out.println("Neue Position");
			SBewegungMessage position = (SBewegungMessage) daten;
			this.spieler.setPos(position.neuX, position.neuY);
		}else if (daten instanceof MBewegungMessage){
			try{
				System.out.println("Monster-Bewegung");
			
			int richtung = ((MBewegungMessage) daten).richtung;
			Monster m = monsterListe.get(((MBewegungMessage) daten).monsterNummer);
			System.out.println(((MBewegungMessage) daten).monsterNummer + "  "+ richtung);
			switch (richtung) {
			case 0:
				m.hoch();
				break;
			case 1:
				m.rechts();
				break;
			case 2:
				m.runter();
				break;
			case 3:
				m.links();
				break;}}
			catch(Exception e){
				
			}
			
			
		} else if (daten instanceof ItemStatusMessage){
			// Schluessel aufnehmen
			if(((ItemStatusMessage) daten).art == 1) {
				spieler.nimmSchluessel();
				map.karte[spieler.getXPos()][spieler.getYPos()] = new Boden();
				this.itemBenutzen = 1;

				spieler.nimmSchluessel();
			}

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
		} else if (daten instanceof MAngriffMessage){
			Monster m =monsterListe.get(((MAngriffMessage) daten).monsternummer);
			spieler.changeHealth(-konstante.BOX/8);
			m.angriff =true;
					
		}else if(daten instanceof MStatusMessage){
				MStatusMessage msm = (MStatusMessage) daten;
				System.out.println("Statusmessage kommt an");
				int j=msm.monsternummer;
				boolean mtot=msm.tot;
				boolean heilen =msm.heilen;
				Monster m = monsterListe.get(j);
				if (mtot==true)
					monsterListe.remove(j);
				else if (mtot == false && heilen == false)
					m.changeHealth(-(konstante.BOX/4));
				else if (mtot == false && heilen == true)
					m.changeHealth(konstante.BOX/8);
			
			
		}else if (daten instanceof HighScoreMessage){
			System.out.println("HighScoreNachricht wird erkannt");
			HighScoreMessage hm =(HighScoreMessage) daten;
			String n = hm.zeile;
			System.out.println(n);
			highscore.add(n);
			System.out.println("HighScore wird gelesen in Liste:"+n);
			
		}
		  
		  else if (daten instanceof CheatMessage) {
				int i = ((CheatMessage) daten).i;
				switch (i) {
				case 1:
					spieler.changeHealth(konstante.BOX/4);
					break;
				case 2:
					int zufallszahl;
				    zufallszahl = (int)(Math.random() * monsterListe.size()); 
				    Monster m =monsterListe.get(zufallszahl);
				    m.changeHealth(-konstante.BOX/2);
					break;
				}
			}
	}


	// Methoden fï¿½r GUI
	
	
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

	public void logout(int level) throws Exception {
		LogoutMessage anfrage = new LogoutMessage(level);
		com.bekommeVonClient(anfrage);
	}
	 
	public void beende(int level){
		BeendeMessage bm = new BeendeMessage(level);
		com.bekommeVonClient(bm);
		
	}
	// kann , wenn notwendig , neues x oder y zurï¿½ckgeben...
	public void wegAnfragen(int x, int y) throws InterruptedException {
		System.out.println("Der Spieler mï¿½chte sich bewegen");
		if(!(map.karte[x][y] instanceof Wand)){
		spieler.zielX = x;
		spieler.zielY = y;
		SBewegungMessage anfrage = new SBewegungMessage();
		anfrage.altX = spieler.getXPos();
		anfrage.altY = spieler.getYPos();
		anfrage.neuX = spieler.zielX;
		anfrage.neuY = spieler.zielY;
		com.bekommeVonClient(anfrage);
		sleep(600);}
		else System.out.println("Du versuchst auf eine Wand zu gehen");
	}

	
// evtl besser kein reurn, sondern abruf ï¿½ber map.karte
	public Spielelement[][] changeLevel() throws Exception{
		System.out.println("Der Client fragt ein neues Level an");
		ChangeLevelMessage anfrage = new ChangeLevelMessage();
		//this.map.levelzaehler = 1;
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

	public void angriffSpieler(){
		SAngriffMessage sam =new SAngriffMessage();
		System.out.println("SAngriffM");
		com.bekommeVonClient(sam);
		}

	
	// 1 = leben erhöhen, 2 = bei einem zufälligen Monster wird die hälfte der Leben weg genommen
		// es können aber pro level nur 2 mal cheats benutzt werden
		void cheatBenutzen(int i) throws Exception {
			CheatMessage cheat = new CheatMessage(i);
			com.bekommeVonClient(cheat);
		}

			
	public void angriffMonster(){
		MBewegungMessage mbm = new MBewegungMessage();
		com.bekommeVonClient(mbm);
		
	}
	public void speichereLevel( int level){
		SpeicherMessage s = new SpeicherMessage(level);
		System.out.println("Versuche zu speichern");
		com.bekommeVonClient(s);
	}
	public void schickeHighScore(){
		HighScoreAnfrageMessage hsam = new HighScoreAnfrageMessage();
		System.out.println("HighScore wird bei server angefragt");
		com.bekommeVonClient(hsam);
		try {
			sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void fuegeZuHighScorehinzu(String name, int zeit){
		SetzeHighScoreMessage shsm = new SetzeHighScoreMessage(name,zeit);
		com.bekommeVonClient(shsm);
	}
}
