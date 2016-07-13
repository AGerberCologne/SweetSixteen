package pp2016.team16.shared;

import pp2016.team16.server.engine.Astern;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;


/** Team16: Sweet sixteen
 * Monster-Klasse fuer die Monster in HindiBones.
 * 
 * @author Goekdag, Enes, 5615399
 * 
 * */
public class Monster extends Figur {

	public long lastAttack;
	public long lastStep;
	public int cooldownAttack;
	public int cooldownWalk;

	public int dir; // Laufrichtung: 0 Nord, 1 Ost, 2 Sued, 3 West
	private int typ; // Von Beginn an anwesend: 0, Erscheint spaeter: 1, Hat den Schluessel: 2
	public Astern astern;
	public boolean nachricht = true;
	public boolean angriff =false;
	
	
	
	private int zustand;// 1:ruhe,2:Spieler jagen;3:fluechten                       // STATES

	
	/** Team16: Sweet sixteen
	 * Konstruktor der Monster Klasse. Es werden die x- und y-Koordinaten der Startposition des Monsters
	 * uebergeben sowie der Typ das Monsters.
	 * 
	 * @author Goekdag, Enes, 5615399
	 * 
	 * */
	public Monster(int x, int y, int typ) {
		this.typ = typ;
		setPos(x, y);
		setHealth(32);
		setMaxHealth(getHealth());
		lastAttack = System.currentTimeMillis();
		lastStep = System.currentTimeMillis();
		cooldownAttack = 500 - 10 * 1; // ms
		cooldownWalk = 0;

		zustand = 1;

		setSchaden(5 + 1 * 2);
		Random r = new Random();
		//changeDir();

		// Bild fuer das Monster laden
		int i = r.nextInt(3) + 1;

		try {
			setImage(ImageIO.read(new File("img//drache" + i + ".png")));
		} catch (IOException e) {
			System.err.print("Das Bild drache.png konnte nicht geladen werden.");
		}
	}
	
	
	/**
	 * 
	 * @return Bruchzahl, welche wiedergibt wie viel Prozent des Cooldowns vorbei ist.
	 * @author Diese Methoden wurde aus dem alten Spiel uerbernommen
	 */
	public double cooldownProzent() {
		return 1.0 * (System.currentTimeMillis() - lastAttack) / cooldownAttack;
	}

	/**
	 * Wurde weitestgehend von alten HindiBones Spiel uebernommen.
	 * Funktion welche das Monster entsprechend der vorgegebenen Richtung bewegt, sofern die Bewegung
	 * zulaessig ist und der Bewegungs-Cooldown vorbei ist.
	 * 
	 * @author Goekdag, Enes, 5615399
	 */
	public void move(boolean zulaessig) {
	
		boolean nextWalk = (System.currentTimeMillis() - lastStep) >= cooldownWalk;
		if (zulaessig && nextWalk) {
			System.out.println("Getesteter Schritt");
			switch (dir) {
			case 0:
				hoch();
				break;
			case 1:
				rechts();
				break;
			case 2:
				runter();
				break;
			case 3:
				links();
				break;
			}
			nachricht =true;
			lastStep = System.currentTimeMillis();
		} else {
			changeDir();
			System.out.println("Kein getesteter Schritt");
			nachricht = false;
		}
	}

	/**
	 * Setzt eine zufaellige neue Richtung fuer das Monster.
	 * 
	 * @author Diese Methoden wurde aus dem alten Spiel uerbernommen
	 */
	public void changeDir() {
		Random random = new Random();
		dir = random.nextInt(4);
	}

	public int getTyp() {
		return typ;
	}

	/** Team16: Sweet sixteen
	 * Bestimmt den aktuellen Zustand des Monsters und gibt ihn zurueck.
	 * 
	 * @author Goekdag, Enes, 5615399
	 * @return Der aktuelle Zustand des Monsters
	 * */
	public int aktuellenZustandBestimmen(int spielerX, int spielerY) {
		// Ist der Spieler im Radius des Monsters?                                             // endlicher AUTOMAT- 3 STATES
		boolean spielerImRadius = (Math.sqrt(Math.pow(spielerX - this.getXPos(), 2)
				+ Math.pow(spielerY - this.getYPos(), 2)) < 2);
        if (!spielerImRadius) {
			zustand= 1;
		}
        if (spielerImRadius&&this.getHealth()>8) {
			zustand= 2;
		}
        if (spielerImRadius&&this.getHealth()<=8) {
			zustand=3;
		}
		return zustand;

	}
}
	
	


