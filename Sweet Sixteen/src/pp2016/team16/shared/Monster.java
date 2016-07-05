package pp2016.team16.shared;

import pp2016.team16.client.gui.HindiBones;

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

	private long lastAttack;
	private long lastStep;
	private int cooldownAttack;
	private int cooldownWalk;

	private int dir; // Laufrichtung: 0 Nord, 1 Ost, 2 Sued, 3 West
	private int typ; // Von Beginn an anwesend: 0, Erscheint später: 1
	
	
	/* Team16: Sweet sixteen
	 * Goekdag, Enes, 5615399
	 * 
	 * */
	private int zustand;// 1:ruhe,2:Spieler jagen;3:flüchten                       // STATES

	private HindiBones fenster;
	private Spieler spieler;

	public Monster(int x, int y, HindiBones fenster, int typ) {
		this.fenster = fenster;
		this.spieler = fenster.spieler;
		this.typ = typ;
		setPos(x, y);
		setHealth(32);
		setMaxHealth(getHealth());
		lastAttack = System.currentTimeMillis();
		lastStep = System.currentTimeMillis();
		cooldownAttack = 500 - 10 * fenster.currentLevel; // ms
		cooldownWalk = 1000;

		zustand = 1;

		setSchaden(5 + fenster.currentLevel * 2);
		Random r = new Random();
		changeDir();

		// Bild fuer das Monster laden
		int i = r.nextInt(3) + 1;

		try {
			setImage(ImageIO.read(new File("img//drache" + i + ".png")));
		} catch (IOException e) {
			System.err.print("Das Bild drache.png konnte nicht geladen werden.");
		}
	}
	
	public Monster(int x, int y, int typ) {
		this.typ = typ;
		setPos(x, y);
		setHealth(32);
		setMaxHealth(getHealth());
		lastAttack = System.currentTimeMillis();
		lastStep = System.currentTimeMillis();
	//	cooldownAttack = 500 - 10 * fenster.currentLevel; // ms
		cooldownWalk = 1000;

		zustand = 1;

	//	setSchaden(5 + fenster.currentLevel * 2);
		Random r = new Random();
		changeDir();

	}

	public boolean attackiereSpieler(boolean hatSchluessel) {
		// Ist der Spieler im Radius des Monsters?
		boolean spielerImRadius = (Math
				.sqrt(Math.pow(spieler.getXPos() - getXPos(), 2) + Math.pow(spieler.getYPos() - getYPos(), 2)) < 2);

		// Kann das Monster angreifen?
		boolean kannAngreifen = false;
		if (typ == 0)
			kannAngreifen = ((System.currentTimeMillis() - lastAttack) >= cooldownAttack);
		if (typ == 1)
			kannAngreifen = (hatSchluessel && ((System.currentTimeMillis() - lastAttack) >= cooldownAttack));
		if (typ == 2)
			kannAngreifen = ((System.currentTimeMillis() - lastAttack) >= cooldownAttack);
		if (spielerImRadius && kannAngreifen) {
			lastAttack = System.currentTimeMillis();
			spieler.changeHealth(-getSchaden());
		}
		return spielerImRadius;
	}

	public void changeHealth(int change) {
		super.changeHealth(change);
		if (getHealth() <= 0) {
			if(typ==0 || typ==1){
			fenster.level[getXPos()][getYPos()] = new Heiltrank(30);
			fenster.monsterListe.remove(this);
			}
			if(typ == 2){
			fenster.level[getXPos()][getYPos()] = new Schluessel();
			fenster.monsterListe.remove(this);
			}
			
		}
	}

	public double cooldownProzent() {
		return 1.0 * (System.currentTimeMillis() - lastAttack) / cooldownAttack;
	}

	// Bewege das Monster
	public void move() {
	
		boolean nextWalk = (System.currentTimeMillis() - lastStep) >= cooldownWalk;
		if (zulaessig()) {
			if (nextWalk) {
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
				lastStep = System.currentTimeMillis();
			}
		} else {
			changeDir();
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

	// Pruefe, ob naechster Schritt zulaessig ist
	private boolean zulaessig() {
		if (dir == -1)
			return true;

		if (dir == 0 && getYPos() - 1 > 0) {
			return !(fenster.level[getXPos()][getYPos() - 1] instanceof Wand)
					&& !(fenster.level[getXPos()][getYPos() - 1] instanceof Tuer)
					&& !(fenster.level[getXPos()][getYPos() - 1] instanceof Schluessel);
		} else if (dir == 1 && getXPos() + 1 < fenster.WIDTH) {
			return !(fenster.level[getXPos() + 1][getYPos()] instanceof Wand)
					&& !(fenster.level[getXPos() + 1][getYPos()] instanceof Tuer)
					&& !(fenster.level[getXPos() + 1][getYPos()] instanceof Schluessel);
		} else if (dir == 2 && getYPos() + 1 < fenster.HEIGHT) {
			return !(fenster.level[getXPos()][getYPos() + 1] instanceof Wand)
					&& !(fenster.level[getXPos()][getYPos() + 1] instanceof Tuer)
					&& !(fenster.level[getXPos()][getYPos() + 1] instanceof Schluessel);
		} else if (dir == 3 && getXPos() > 0) {
			return !(fenster.level[getXPos() - 1][getYPos()] instanceof Wand)
					&& !(fenster.level[getXPos() - 1][getYPos()] instanceof Tuer)
					&& !(fenster.level[getXPos() - 1][getYPos()] instanceof Schluessel);
		} else
			return false;
	}

	public LinkedList<int[]> findeWeg(int[] startpunkt, int[] endpunkt) {
		LinkedList<int[]>besterWeg = new LinkedList<int[]>();

		return besterWeg;

	}
	/* Team16: Sweet sixteen
	 * Goekdag, Enes, 5615399
	 * 
	 * */
	public int aktuellenZustandBestimmen() {
		// Ist der Spieler im Radius des Monsters?                                             // endlicher AUTOMAT- 3 STATES
		boolean spielerImRadius = (Math.sqrt(Math.pow(spieler.getXPos() - getXPos(), 2)
				+ Math.pow(spieler.getYPos() - getYPos(), 2)) < 2);
        if (!spielerImRadius) {
			zustand= 1;
		}
        if (spielerImRadius&&getHealth()>8) {
			zustand= 2;
		}
        if (spielerImRadius&&getHealth()<=8) {
			zustand=3;
		}
		return zustand;

	}
	/* Team16: Sweet sixteen
	 * Goekdag, Enes, 5615399
	 * 
	 * */
/*	public void jagen() {                                                                      // Spieler JAGEN (Angriffszustand)
		Astern  astern= new Astern(getYPos(), getXPos(), spieler.getXPos(),spieler.getYPos() , fenster);
		Wegpunkt test = astern.starten();
		System.out.println("Monster:"+this.getXPos()+","+this.getYPos());
		System.out.println("Spieler:"+spieler.getXPos()+","+spieler.getYPos());
		if (test.x<getXPos()&&test.y==getYPos()) {
			dir=3;
			move();
		}
    	if (test.x>getXPos()&&test.y==getYPos()) {
    		dir=1;
			move();
		}
		if (test.x==getXPos()&&test.y<getYPos()) {
			dir=0;
			move();
		}
		if (test.x==getXPos()&&test.y>getYPos()) {
			dir=02;
			move();
		}
	
	
		    
		
		
	}*/
	/* Team16: Sweet sixteen
	 * Goekdag, Enes, 5615399
	 * 
	 * */
	public void fluechten() {                                                                 // von Spieler FLUECHTEN (Defensivzustand)
		
		System.out.println("Monster:"+this.getXPos()+","+this.getYPos());
		System.out.println("Spieler:"+spieler.getXPos()+","+spieler.getYPos());

		// Daten speichern zum einfacheren Zugriff
		int spielerX = spieler.getXPos();
		int spielerY = spieler.getYPos();
		int monsterX = this.getXPos();
		int monsterY = this.getYPos();
		// Monster nimmt erstbesten Fluchtweg (Es ist ja auch in Panik)
		if(fenster.level[monsterX][monsterY+1] instanceof Boden && spielerY<=monsterY) {
			dir = 2; 
			move();
		}
		if(fenster.level[monsterX+1][monsterY] instanceof Boden && spielerX<=monsterX) {
			dir = 1; 
			move();
		}
		if(fenster.level[monsterX-1][monsterY] instanceof Boden && spielerX>=monsterX) {
			dir = 3; 
			move();
		}
		if(fenster.level[monsterX][monsterY-1] instanceof Boden && spielerY>=monsterY) {
			dir = 0; 
			move();
		}
		
	}
	public void ruhe(){                                                             
		//Heilt im "Ruhe" Zustand
				if (getHealth()<getMaxHealth()) {
				setHealth(getHealth()+1);	
				}
				move();
	}
	}
	
	


