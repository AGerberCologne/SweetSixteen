package pp2016.team16.shared;

import pp2016.team16.client.engine.ClientEngine;
import pp2016.team16.client.gui.HindiBones;
import pp2016.team16.server.engine.Astern;
import pp2016.team16.server.engine.ServerEngine;
import pp2016.team16.server.engine.Wegpunkt;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

//import astern.Astern;
//import astern.Wegpunkt;
/* Team16: Sweet sixteen
 * Goekdag, Enes, 5615399
 * 
 * */
public class Monster extends Figur {

	public long lastAttack;
	public long lastStep;
	public int cooldownAttack;
	public int cooldownWalk;

	public int dir; // Laufrichtung: 0 Nord, 1 Ost, 2 Sued, 3 West
	private int typ; // Von Beginn an anwesend: 0, Erscheint sp�ter: 1
	public Astern astern;
	public boolean nachricht = true;
	public boolean angriff =false;
	
	
	/* Team16: Sweet sixteen
	 * Goekdag, Enes, 5615399
	 * 
	 * */
	private int zustand;// 1:ruhe,2:Spieler jagen;3:fl�chten                       // STATES

	

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
	
	
	
	
	

	

	

	public double cooldownProzent() {
		return 1.0 * (System.currentTimeMillis() - lastAttack) / cooldownAttack;
	}

	// Bewege das Monster
	public void move(boolean zulaessig) {
	
		boolean nextWalk = (System.currentTimeMillis() - lastStep) >= cooldownWalk;
		if (zulaessig) {
			if (nextWalk) {
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
			}
		} else {
			changeDir();
			System.out.println("Kein getesteter Schritt");
			nachricht = false;
		}
	}

	// Laufrichtung des Monsters aendern                                          // HERUMIRREN (NORMAL-ZUSTAND)
	public void changeDir() {
		Random random = new Random();
		dir = random.nextInt(4);
	}

	public int getTyp() {
		return typ;
	}


	public LinkedList<int[]> findeWeg(int[] startpunkt, int[] endpunkt) {
		LinkedList<int[]>besterWeg = new LinkedList<int[]>();

		return besterWeg;

	}
	/* Team16: Sweet sixteen
	 * Goekdag, Enes, 5615399
	 * 
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
	
	


