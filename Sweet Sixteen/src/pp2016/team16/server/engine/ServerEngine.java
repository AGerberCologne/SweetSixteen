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


public class ServerEngine extends Thread
{ 
	MessageObject serverDatenbestand = new MessageObject();
	public ServerComm server;
	public Konstanten konstante =new Konstanten();
	public Map map = new Map();
	public Spieler spieler = new Spieler("img//spieler.png");
	public LinkedList<Monster> monsterListe = new LinkedList<Monster>();
	public boolean eingeloggt;
	public Astern astern;
	public String spielername;
	public String spielerpasswort;
	public int monsternr;



	public ServerEngine() {
		this.start();
		System.out.println("Starte Server");

	}
	public ServerEngine(String n){

	}
	public void run(){
		server = new ServerComm();
		while(server.serverOpen){
			try {
				this.monsterBewegung();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			MessageObject n = server.gebeWeiterAnServer();
			if(n == null){
				System.out.println("Test1");
				try {
					sleep(600);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
				try {
					this.nachrichtenVerarbeiten(n);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}


	/**
	 * Message-Handling @ Gerber, Alina , 5961246
	 *
	 */
	void nachrichtenVerarbeiten(MessageObject eingehendeNachricht) throws InterruptedException {
		if (eingehendeNachricht instanceof LoginMessage) 
		{  LoginMessage l = (LoginMessage) eingehendeNachricht;
		this.logIn(l.artVonAnmeldung, l.name, l.passwort);
		LoginAnswerMessage answer =new LoginAnswerMessage();
		if(this.eingeloggt){
			spieler.setName(l.name);
			spieler.setPasswort(l.passwort);
			answer.eingeloggt= eingeloggt;
			answer.name = l.name;
			answer.passwort = l.passwort;
			answer.levelzaehler = map.levelzaehler;
			System.out.println("Login If");
		}
		server.gebeWeiterAnClient(answer);
		} else if (eingehendeNachricht instanceof LogoutMessage)
		{
			System.out.println("Server Shutdown");
			
		} else if (eingehendeNachricht instanceof ChangeLevelMessage) {
			System.out.println("Server hat Level-Anfrage erhalten");
			map.levelzaehler = ((ChangeLevelMessage) eingehendeNachricht).levelzaehler;
			map.breite = konstante.WIDTH;
			map.hoehe = konstante.HEIGHT;
			AlleLevel levelObject = new AlleLevel(map.hoehe, map.breite);
			map.level =levelObject.setzeInhalt(map.levelzaehler);
			for(int i=0;i<map.level.length;i++){
				for(int j=0;j<map.level.length;j++){
					int Variable = map.level[i][j];
					switch(Variable){
					case 0: map.karte[i][j] = new Wand(); break;
					case 1: map.karte[i][j] = new Boden(); break;
					//				case 3: map.karte[i][j] = new Schluessel(); break;
					case 5: map.karte[i][j] = new Heiltrank(20); break;
					case 6: map.karte[i][j] = new Tuer(false); break;
					case 4: map.karte[i][j] = new Tuer(true); this.spieler.setPos(i, j); break;
					case 2: map.karte[i][j] = new Boden(); this.monsterListe.add(new Monster(i,j,0)); break;
					// Monster, welche erst nach dem Aufheben des Schluessels erscheinen
					case 3: map.karte[i][j] = new Boden(); this.monsterListe.add(new Monster(i,j,2)); break;
					case 8: map.karte[i][j] = new Boden(); this.monsterListe.add(new Monster(i,j,1)); break;
					}}}	
			ChangeLevelMessage answer = new ChangeLevelMessage();
			answer.level = map.level;
			answer.levelzaehler = map.levelzaehler;
			server.gebeWeiterAnClient(answer);

		} else if (eingehendeNachricht instanceof SBewegungMessage) {
			System.out.println("Der Spieler m�chte sich bewegen");
			SBewegungMessage m = (SBewegungMessage) eingehendeNachricht;
			spieler.setPos(m.altX, m.altY);
			spieler.zielX = m.neuX;
			spieler.zielY = m.neuY;
			this.berechneWeg();
			System.out.println("abgeschlossen");
			try {
				this.spielermacheSchritt();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (eingehendeNachricht instanceof ItemStatusMessage){
			ItemStatusMessage answer = new ItemStatusMessage();
			// Schluessel aufnehmen
			if (map.karte[spieler.getXPos()][spieler.getYPos()] instanceof Schluessel) {
				spieler.nimmSchluessel();
				map.karte[spieler.getXPos()][spieler.getYPos()] = new Boden();
				answer.art = 1;

			}
			// Heiltrank aufnehmen
			else if (map.karte[spieler.getXPos()][spieler.getYPos()] instanceof Heiltrank) {
				spieler.nimmHeiltrank((Heiltrank) map.karte[spieler.getXPos()][spieler.getYPos()]);		
				map.karte[spieler.getXPos()][spieler.getYPos()] = new Boden();
				answer.art = 0;
			}
			// Schluessel benutzen
			if (map.karte[spieler.getXPos()][spieler.getYPos()] instanceof Tuer) {
				if (!((Tuer) map.karte[spieler.getXPos()][spieler.getYPos()]).istOffen() && spieler.hatSchluessel()) {
					((Tuer) map.karte[spieler.getXPos()][spieler.getYPos()]).setOffen();
					// Nach dem Oeffnen der Tuer ist der Schluessel wieder weg
					spieler.entferneSchluessel();
					answer.art =2;
				}
			}
			server.gebeWeiterAnClient(answer);

		} else if (eingehendeNachricht instanceof SpeicherMessage){
			int l = ((SpeicherMessage)eingehendeNachricht).level;
			System.out.println("wird gespeichert");
			this.speichern(l);
			System.out.println("hat gespeichert");
			boolean b=true;
			SpeicherAntwort antwort = new SpeicherAntwort(b);
			server.gebeWeiterAnClient(antwort);
			
		}else if (eingehendeNachricht instanceof SAngriffMessage){
			Monster m = angriffsMonster();
			m.changeHealth(konstante.BOX/24);
			MStatusMessage msm = new MStatusMessage(monsternr);
			server.gebeWeiterAnClient(msm);
			
		}else if (eingehendeNachricht instanceof MBewegungMessage){
			monsterBewegung();
		}
		
		/*else if (eingehendeNachricht instanceof CheatMessage) {
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


	// Spieler Datenbank Verwaltung


	/*Ann-Catherine Hartmann,37658
	 * 
 //Die Methode sollte im boolean eingeloggt true speichern, falls man erfolgreich 
	 * eine anmeldung oder einen login durchgef�hrt hat;
	 * au�erdem sollte wenn es eine neue anmeldung ist 1 bei this.map.levelzaehler gespeichert werden
	 * wenn man sich einlogt, dann soll nicht nur gepr�ft werden ob name und passwort zusammen passen sondern 
	 * auch noch die levelnr ausgelesen werden ( die speichern wir mit), und an this.map.levelzaehler �bergeben werden*/
	public void logIn(int i, String name, String passwort){
		String abgleich = name + " "+ passwort;
		boolean namegibtesschon = false;
		if (i ==1){ //Neuanmeldung	
			try {
				FileReader fr;
				fr=new FileReader("Spielerdaten");
				BufferedReader br = new BufferedReader(fr);
				String zeile=br.readLine();
				while((zeile = br.readLine()) != null){
					if (zeile.equals(abgleich)){
						eingeloggt = false;
						namegibtesschon=true;
						System.out.println("Name und Passwort gibt es schon");
						break;
					}else if (zeile.startsWith(name+"")){
						eingeloggt = false;
						namegibtesschon=true;
						System.out.println("Name ist vergeben");
						break;
					}
				}

				br.close();
				fr.close();

			}catch( IOException e){}

			if(namegibtesschon==false){
				String initiallevel = "Level 1";
				FileWriter fw;
				try {
					fw = new FileWriter ("Spielerdaten",true);
					BufferedWriter bw = new BufferedWriter (fw);
					bw.newLine();
					bw.write(abgleich);
					bw.newLine();
					bw.write (initiallevel);
					bw.newLine();
					bw.newLine();
					bw.newLine();
					bw.close();//Schlie�t die Datei
					fw.close();
					eingeloggt = true;
					System.out.println("Eingeloggt true");
				} catch (IOException e1) {
					System.out.println("Fehler gefunden");
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}}else if (i==2){
				try {
					spielername = name;
					spielerpasswort = passwort;
					FileReader fr;
					fr=new FileReader("Spielerdaten");
					BufferedReader br = new BufferedReader(fr);
					String zeile;
					while((zeile=br.readLine())!=null){
						System.out.println("Zeile wird gelesen");
						if (zeile.equals(abgleich)){
							eingeloggt = true;
							String level =br.readLine();
							char c = level.charAt(6);
							this.map.levelzaehler =(int) c-48;
							break;
						}
					}
					br.close();
					fr.close();

				} catch ( IOException e) {
				}

			}
	}

	//Ann-Catherine Hartmann,37658
	public void leseHighScore(){

	}
	//Ann-Catherine Hartmann,37658; Methode funktioniert noch nicht
	public void setHighScore(int zeit, String name){
		String z = "Zeit: "+String.valueOf(zeit)+"   Name des Spielers: "+ name;
		try {
			File original =new File( "HighScore");
			File kopie = new File("HighScore2");
			FileReader fr =new FileReader(original);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(kopie);
			BufferedWriter bw = new BufferedWriter(fw);
			StringBuffer sb = new StringBuffer();
			String zeile;
			while((zeile=br.readLine())!=null){
				if(zeile.equals("")==false){
					int i = 6;
					char k = zeile.charAt(i);
					i++;
					String h=String.valueOf(k);
					while(((int)(k=zeile.charAt(i)))!=32){
						h=h+k;
						i++;
					}
					Integer l =Integer.valueOf(h);
					if (l>zeit){
						bw.write(z);
						bw.newLine();
						bw.newLine();
					}}
				bw.write(zeile);
				bw.newLine();
			}
			bw.write(sb.toString());
			bw.flush();
			fw.close();
			bw.close();
			br.close();
			fr.close();
			original.delete();
			kopie.renameTo(original);

		} catch ( IOException e) {
		}



	}
	//Ann-Catherine Hartmann,37658
	public void abmelden(int levelNr){
		this.speichern(levelNr);

	}
	//Ann-Catherine Hartmann,37658
	/*es wird immer name, passwort und levelnr gespeichert
  wichtig ist, dass die bereits bestehenden eintragungen entweder �berschrieben oder gel�scht werden*/
	public void speichern(int levelNr){
		String abgleich = spielername + " "+ spielerpasswort;
		String neuesLevel = "Level " +String.valueOf(levelNr);
		try {
			File original =new File( "Spielerdaten");
			File kopie = new File("Spielerdaten2");
			FileReader fr =new FileReader("Spielerdaten");
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter("Spielerdaten2");
			BufferedWriter bw = new BufferedWriter(fw);
			StringBuffer sb = new StringBuffer();
			String zeile;
			while((zeile=br.readLine())!=null){
				if (zeile.equals(abgleich)){
					bw.write(zeile);
					System.out.println("schreibe 1");
					bw.newLine();
					bw.write(neuesLevel);
					System.out.println("schreibe neues Level");
					bw.newLine();
					System.out.println("schreibe 2");
					br.readLine();
					zeile=br.readLine();
					System.out.println("Lese");
				}
				bw.write(zeile);
				System.out.println("schreibe 3");
				bw.newLine();
			}
			bw.write(sb.toString());
			System.out.println("schreibe 4");
			bw.flush();
			fw.close();
			bw.close();
			br.close();
			fr.close();
			original.delete();
			kopie.renameTo(original);
			System.out.println("Speichern");

		} catch ( IOException e) {
		}

	}
	// Monster Methoden

	public void monsterBewegung() throws InterruptedException{
	for (int i = 0; i < monsterListe.size(); i++) {
		Monster m = monsterListe.get(i);
		boolean event = spieler.hatSchluessel();
		// Da hier alle Monster aufgerufen werden, wird an dieser
		// Stelle auch ein Angriffsbefehl fuer die Monster
		// abgegeben, falls der Spieler in der Naehe ist.
		// Ansonsten soll das Monster laufen
		if(m.aktuellenZustandBestimmen(spieler.getXPos(), spieler.getYPos())==1){
			this.ruhe(m);
			if(m.nachricht){MBewegungMessage answer = new MBewegungMessage();
			answer.richtung = m.dir;
			answer.monsterNummer =i;
			server.gebeWeiterAnClient(answer);
			sleep(600);
			}

		}else if(m.aktuellenZustandBestimmen(spieler.getXPos(), spieler.getYPos())==3){
			this.fluechten(m);
			if(m.nachricht){MBewegungMessage answer = new MBewegungMessage();
			answer.richtung = m.dir;
			answer.monsterNummer =i;
			server.gebeWeiterAnClient(answer);
			sleep(600);
			}
		}
		else {
			this.jagen(m);
			if(m.nachricht){MBewegungMessage answer = new MBewegungMessage();
			answer.richtung = m.dir;
			answer.monsterNummer =i;
			server.gebeWeiterAnClient(answer);
			sleep(600);
			}
			if(this.attackiereSpieler(event, m)){}
			int box = this.konstante.BOX;

			/*	double p = m.cooldownProzent();
			g.setColor(Color.RED);
			g.drawImage(feuerball, (int)(((1-p) * m.getXPos() + (p) * s.getXPos())*box) + box/2,
					(int)(((1-p) * m.getYPos() + (p) * s.getYPos())*box) + box/2, 8, 8, null);
	 */}
	}
}
public boolean attackiereSpieler(boolean hatSchluessel, Monster m) {
		// Ist der Spieler im Radius des Monsters?
		boolean spielerImRadius = (Math
				.sqrt(Math.pow(spieler.getXPos() - m.getXPos(), 2) + Math.pow(spieler.getYPos() - m.getYPos(), 2)) < 2);

		// Kann das Monster angreifen?
		boolean kannAngreifen = false;
		if (m.getTyp() == 0)
			kannAngreifen = ((System.currentTimeMillis() - m.lastAttack) >= m.cooldownAttack);
		if (m.getTyp() == 1)
			kannAngreifen = (hatSchluessel && ((System.currentTimeMillis() - m.lastAttack) >= m.cooldownAttack));
		if (m.getTyp() == 2)
			kannAngreifen = ((System.currentTimeMillis() - m.lastAttack) >= m.cooldownAttack);
		if (spielerImRadius && kannAngreifen) {
			m.lastAttack = System.currentTimeMillis();
			spieler.changeHealth(-m.getSchaden());
		}
		return spielerImRadius;
	}

	public void monsterChangeHealth(Monster m,int change) {
		if (m.getHealth() <= 0) {
			if(m.getTyp()==0 || m.getTyp()==1){
			map.karte[m.getXPos()][m.getYPos()] = new Heiltrank(30);
				monsterListe.remove(m);
			}
			if(m.getTyp() == 2){
			map.karte[m.getXPos()][m.getYPos()] = new Schluessel();
				monsterListe.remove();

			}

		}
	}
	
	boolean zulaessig(Monster m) {
		if (m.dir == 0 && m.getYPos() - 1 > 0) {
			return !(map.karte[m.getXPos()][m.getYPos() - 1] instanceof Wand)
					&& !(map.karte[m.getXPos()][m.getYPos() - 1] instanceof Tuer)
					&& !(map.karte[m.getXPos()][m.getYPos() - 1] instanceof Schluessel);
		} else if (m.dir == 1 && m.getXPos() + 1 < map.karte.length) {
			return !(map.karte[m.getXPos() + 1][m.getYPos()] instanceof Wand)
					&& !(map.karte[m.getXPos() + 1][m.getYPos()] instanceof Tuer)
					&& !(map.karte[m.getXPos() + 1][m.getYPos()] instanceof Schluessel);
		} else if (m.dir == 2 && m.getYPos() + 1 < map.karte.length) {
			return !(map.karte[m.getXPos()][m.getYPos() + 1] instanceof Wand)
					&& !(map.karte[m.getXPos()][m.getYPos() + 1] instanceof Tuer)
					&& !(map.karte[m.getXPos()][m.getYPos() + 1] instanceof Schluessel);
		} else if (m.dir == 3 && m.getXPos()-1 > 0) {
			return !(map.karte[m.getXPos() - 1][m.getYPos()] instanceof Wand)
					&& !(map.karte[m.getXPos() - 1][m.getYPos()] instanceof Tuer)
					&& !(map.karte[m.getXPos() - 1][m.getYPos()] instanceof Schluessel);
		} else
			return false;
	}


	/* Team16: Sweet sixteen
	  * Goekdag, Enes, 5615399
	  * 
	  * */
	public void jagen(Monster m) {                                                                      // Spieler JAGEN (Angriffszustand)
		m.astern = new Astern(m.getYPos(), m.getXPos(), spieler.getXPos(),spieler.getYPos() , map.karte);
		Wegpunkt test = astern.starten();
		System.out.println("Monster:"+m.getXPos()+","+m.getYPos());
		System.out.println("Spieler:"+spieler.getXPos()+","+spieler.getYPos());
		if (test.x<m.getXPos()&&test.y==m.getYPos()) {
			m.dir=3;
			m.move(zulaessig(m));
		}
    	if (test.x>m.getXPos()&&test.y==m.getYPos()) {
    		m.dir=1;
			m.move(zulaessig(m));
		}
		if (test.x==m.getXPos()&&test.y<m.getYPos()) {
			m.dir=0;
			m.move(zulaessig(m));
		}
		if (test.x==m.getXPos()&&test.y>m.getYPos()) {
			m.dir=02;
			m.move(zulaessig(m));
		}
		
	}

	/* Team16: Sweet sixteen
	   * Goekdag, Enes, 5615399
	   * 
	   * */
	public void fluechten(Monster m) {                                                                 // von Spieler FLUECHTEN (Defensivzustand)

		System.out.println("Monster:"+m.getXPos()+","+m.getYPos());
		System.out.println("Spieler:"+spieler.getXPos()+","+spieler.getYPos());

		// Daten speichern zum einfacheren Zugriff
		int spielerX = spieler.getXPos();
		int spielerY = spieler.getYPos();
		int monsterX = m.getXPos();
		int monsterY = m.getYPos();
		// Monster nimmt erstbesten Fluchtweg (Es ist ja auch in Panik)
		if(map.karte[monsterX][monsterY+1] instanceof Boden && spielerY<=monsterY) {
			m.dir = 2; 
			m.move(zulaessig(m));
		}
		if(map.karte[monsterX+1][monsterY] instanceof Boden && spielerX<=monsterX) {
			m.dir = 1; 
			m.move(zulaessig(m));
		}
		if(map.karte[monsterX-1][monsterY] instanceof Boden && spielerX>=monsterX) {
			m.dir = 3; 
			m.move(zulaessig(m));
		}
		if(map.karte[monsterX][monsterY-1] instanceof Boden && spielerY>=monsterY) {
			m.dir = 0; 
			m.move(zulaessig(m));
		}

	}
	public void ruhe(Monster m){                                                             
		//Heilt im "Ruhe" Zustand
		if (m.getHealth()< m.getMaxHealth()) {
			m.setHealth(m.getHealth()+1);	
		}
		m.move(this.zulaessig(m));
	}



	// Spieler Methoden

	/*
	 * Enes
	 * */
	public void berechneWeg() {                                                                     // Spieler JAGEN (Angriffszustand)
		astern= new Astern (spieler.getYPos(), spieler.getXPos(), spieler.zielX, spieler.zielY, map.karte);
		Wegpunkt test = astern.starten();
		System.out.println("Spieler:"+spieler.getXPos()+","+spieler.getYPos());
		System.out.println("SZiel:"+spieler.zielX+","+spieler.zielY);

	}
	/*
	 * Alina
	 * */
	public void spielermacheSchritt() throws InterruptedException{
		while(!astern.positionX.isEmpty()){
			System.out.println("Neue Position wird verschickt");
			int x = astern.positionX.removeFirst();
			int y= astern.positionY.removeFirst();
			spieler.setPos(x,y);
			SBewegungMessage answer = new SBewegungMessage();
			answer.neuX = x;
			answer.neuY = y;
			server.gebeWeiterAnClient(answer);
			this.monsterBewegung();
			sleep(600);

		}
	}
	/*Aus gegebenen Spiel
	 * 
	 */
	public Monster angriffsMonster(){
	for(int i = 0; i < this.monsterListe.size(); i++){
		Monster m = this.monsterListe.get(i);
		// Kann der Spieler angreifen?
		boolean kannAngreifen = false;
		if (m.getTyp() == 0) kannAngreifen = true; 
		if (m.getTyp() == 1) kannAngreifen = spieler.hatSchluessel();
		if (m.getTyp() == 2) kannAngreifen = true;
		if((Math.sqrt(Math.pow(spieler.getXPos() - m.getXPos(), 2)+ Math.pow(spieler.getYPos() - m.getYPos(), 2)) < 2)&&kannAngreifen){
			monsternr = i;
			return m;
			
		}
	}

	return null;
}
}
