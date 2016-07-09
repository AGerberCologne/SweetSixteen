package pp2016.team16.shared;

import pp2016.team16.client.engine.ClientEngine;
import pp2016.team16.client.gui.HindiBones;
import pp2016.team16.client.gui.LoginDialog;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import pp2016.team16.server.engine.*;

import javax.imageio.ImageIO;

//import astern.Astern;
//import astern.Wegpunkt;

public class Spieler extends Figur {

	
	private String name;
	private String passwort;
	private boolean hatSchluessel;
	public int anzahlHeiltraenke;
	private int heiltrankWirkung;
	
	public int zielX = 0;
	public int zielY = 0;
	public boolean hatSchrittgemacht =false;
	
	private ServerEngine sengine;
	private ClientEngine cengine;
	private Spielelement[][] karte ;
	private LinkedList<Monster> monsterListe;
	public Astern  astern;
	
	
	public Spieler(String imgDatei, ServerEngine sengine){
		this.sengine = sengine;
		this.karte = sengine.map.karte;
		this.monsterListe = sengine.monsterListe;
		
		setAnzahlHeiltraenke(0);
		setPos(0,0);		
		setHealth(100);
		setMaxHealth(getHealth());
		
		if(LoginDialog.succeeded){
			setName(LoginDialog.getUsername());
		}else{
		setName("Hindi");
		}
		
		// Bild fuer den Spieler laden
		try {
			setImage(ImageIO.read(new File(imgDatei)));
		} catch (IOException e) {
			System.err.print("Das Bild "+ imgDatei + " konnte nicht geladen werden.");
		}
	}
	
	public Spieler(String imgDatei, ClientEngine cengine){
		this.cengine = cengine;
		this.karte = cengine.map.karte;
		this.monsterListe = cengine.monsterListe;
		
		setAnzahlHeiltraenke(0);
		setPos(0,0);		
		setHealth(100);
		setMaxHealth(getHealth());
		
		if(LoginDialog.succeeded){
			setName(LoginDialog.getUsername());
		}else{
		setName("Hindi");
		}
		
		// Bild fuer den Spieler laden
		try {
			setImage(ImageIO.read(new File(imgDatei)));
		} catch (IOException e) {
			System.err.print("Das Bild "+ imgDatei + " konnte nicht geladen werden.");
		}
	}

	
	// Methode, um den Schluessel aufzuheben
	public void nimmSchluessel(){
		hatSchluessel = true;
	}
	
	// Methode, um den Schluessel zu entfernen
	public void entferneSchluessel(){
		hatSchluessel = false;
	}	
	
	public int benutzeHeiltrank(){
		setAnzahlHeiltraenke(anzahlHeiltraenke-1);
		return heiltrankWirkung;
	}
	
	public void nimmHeiltrank(Heiltrank t){
		anzahlHeiltraenke++;
		heiltrankWirkung = t.getWirkung();
			
		
	}
	
	public void setAnzahlHeiltraenke(int anzahl){
		if (anzahl >= 0) anzahlHeiltraenke = anzahl;
	}
	
	public int getAnzahlHeiltraenke(){
		return anzahlHeiltraenke;
	}
	
	// Hat der Spieler den Schluessel?
	public boolean hatSchluessel(){
		return hatSchluessel;
	}
		
	public String getName(){
	
		return name;
		
	}
	
	public void setName(String name){
		this.name = name ;
	}
	
	public String getPasswort(){
		
		return passwort;
		
	}
	
	public void setPasswort(String passwort){
		this.passwort = passwort ;
	}
	
	
	public Monster angriffsMonster(){
		for(int i = 0; i < this.monsterListe.size(); i++){
			Monster m = this.monsterListe.get(i);
			// Kann der Spieler angreifen?
			boolean kannAngreifen = false;
			if (m.getTyp() == 0) kannAngreifen = true; 
			if (m.getTyp() == 1) kannAngreifen = hatSchluessel;
			if (m.getTyp() == 2) kannAngreifen = true;
			if((Math.sqrt(Math.pow(getXPos() - m.getXPos(), 2)+ Math.pow(getYPos() - m.getYPos(), 2)) < 2)&&kannAngreifen){
				return m;
			}
		}
		
		return null;
	}
	
	public void geheZumZiel() {                                                                     // Spieler JAGEN (Angriffszustand)
	   astern= new Astern (getYPos(), getXPos(), this.zielX, this.zielY, this.karte);
		Wegpunkt test = astern.starten();
		System.out.println("Spieler:"+this.getXPos()+","+this.getYPos());
		System.out.println("SZiel:"+this.zielX+","+this.zielY);
	/*	if (test.x<getXPos()&&test.y==getYPos()) {
			links();
			return 3;
		}
    	if (test.x>getXPos()&&test.y==getYPos()) {
    		rechts();
    		return 1;
		}
		if (test.x==getXPos()&&test.y<getYPos()) {
			hoch();
			return 0;
		}
		if (test.x==getXPos()&&test.y>getYPos()) {
			runter();
			return 2;
		}
		return -1;*/
	}
	
	
	
}
