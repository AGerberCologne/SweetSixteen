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
	public ServerComm server;
	public Konstanten konstante =new Konstanten();
	public Map map = new Map();
	public Spieler spieler = new Spieler("img//spieler.png",this);
	public LinkedList<Monster> monsterListe;
	public boolean eingeloggt;
	

	public ServerEngine() {
		this.start();
		System.out.println("Starte Server");
		
	}
	public ServerEngine(String n){
		
	}
	public void run(){
		server = new ServerComm();
		while(server.serverOpen){
			MessageObject m = server.gebeWeiterAnServer();
			if(m == null){
				System.out.println("Test1");
				try {
					sleep(600);
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
		}/* else if (eingehendeNachricht instanceof LogoutMessage)
		{
			System.out.println("Server Shutdown");
			break; // Aufforderung zum runterfahren
			
		} else */if (eingehendeNachricht instanceof ChangeLevelMessage) {
			System.out.println("Server hat Level-Anfrage erhalten");
			map.levelzaehler = ((ChangeLevelMessage) eingehendeNachricht).levelzaehler;
			map.breite = konstante.WIDTH;
			map.hoehe = konstante.HEIGHT;
			AlleLevel levelObject = new AlleLevel(map.hoehe, map.breite);
			map.level =levelObject.setzeInhalt(map.levelzaehler);
			Leser l = new Leser(map.level, this);
			this.spieler = l.sengine.spieler;
			this.monsterListe = l.sengine.monsterListe;
			map.karte = l.getLevel();
			ChangeLevelMessage answer = new ChangeLevelMessage();
			answer.level = map.level;
			answer.levelzaehler = map.levelzaehler;
			server.gebeWeiterAnClient(answer);
			
		} else if (eingehendeNachricht instanceof SBewegungMessage) {
			System.out.println("Der Spieler möchte sich bewegen");
			SBewegungMessage m = (SBewegungMessage) eingehendeNachricht;
			spieler.setPos(m.altX, m.altY);
			spieler.zielX = m.neuX;
			spieler.zielY = m.neuY;
			int richtung = spieler.geheZumZiel();
			SBewegungMessage answer = new SBewegungMessage();
			answer.richtung = richtung;
			server.gebeWeiterAnClient(answer);
			
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
			
		}/*else if (eingehendeNachricht instanceof CheatMessage) {
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
 * 
 //Die Methode sollte im boolean eingeloggt true speichern, falls man erfolgreich 
  * eine anmeldung oder einen login durchgeführt hat;
  * außerdem sollte wenn es eine neue anmeldung ist 1 bei this.map.levelzaehler gespeichert werden
  * wenn man sich einlogt, dann soll nicht nur geprüft werden ob name und passwort zusammen passen sondern 
  * auch noch die levelnr ausgelesen werden ( die speichern wir mit), und an this.map.levelzaehler übergeben werden*/
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
			bw.close();//Schließt die Datei
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
public void abmelden(String name, String passwort, int levelNr){
	this.speichern(name,passwort,levelNr);
	
}
//Ann-Catherine Hartmann,37658
 /*es wird immer name, passwort und levelnr gespeichert
  wichtig ist, dass die bereits bestehenden eintragungen entweder überschrieben oder gelöscht werden*/
public void speichern( String name, String passwort, int levelNr){
	String abgleich = name + " "+ passwort;
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
				bw.newLine();
				bw.write(neuesLevel);
				bw.newLine();
				br.readLine();
				zeile=br.readLine();
			}
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

void bewegung(){
	for (int i = 0; i < this.monsterListe.size(); i++) {
		Monster m = this.monsterListe.get(i);
		boolean event = this.spieler.hatSchluessel();
		// Da hier alle Monster aufgerufen werden, wird an dieser
		// Stelle auch ein Angriffsbefehl fuer die Monster
		// abgegeben, falls der Spieler in der Naehe ist.
		// Ansonsten soll das Monster laufen
		if(m.aktuellenZustandBestimmen()==1){
			m.ruhe();
			
		}else if(m.aktuellenZustandBestimmen()==3){
			m.fluechten();
		}
		else {
			m.jagen();
			m.attackiereSpieler(event);
			int box = this.konstante.BOX;
			Spieler s = this.spieler;

		/*	double p = m.cooldownProzent();
			g.setColor(Color.RED);
			g.drawImage(feuerball, (int)(((1-p) * m.getXPos() + (p) * s.getXPos())*box) + box/2,
					(int)(((1-p) * m.getYPos() + (p) * s.getYPos())*box) + box/2, 8, 8, null);
		*/}
	}
}
}
