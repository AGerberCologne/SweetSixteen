package pp2016.team16.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.JFrame;

import pp2016.team16.client.gui.Leser;
import pp2016.team16.client.engine.ClientEngine;
import pp2016.team16.server.map.AlleLevel;
import pp2016.team16.shared.Boden;
import pp2016.team16.shared.Heiltrank;
import pp2016.team16.shared.Monster;
import pp2016.team16.shared.Schluessel;
import pp2016.team16.shared.Spielelement;
import pp2016.team16.shared.Spieler;
import pp2016.team16.shared.Tuer;
import pp2016.team16.shared.Wand;

/**
 * Klasse für das gesamte Fenster des Spiels
 * Mit KeyListener und MouseListener
 * @author Simon Nietz, Matr_Nr: 5823560
 *
 */

public class HindiBones extends JFrame implements KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;


	private Spielflaeche spielflaeche;
	private Statusleiste statusleiste;
	private Highscore highscore;
	private MenuLeiste menuLeiste;
	private Steuerung steuerung;
	//	public boolean test;
	


	//public AlleLevel level2 = new AlleLevel();

	public ClientEngine engine = new ClientEngine();
	public boolean spielende = false;
	public boolean verloren = false;
	public long startZeit;
	public int benoetigteZeit;
	public boolean nebelAn = true;
	public static JFrame frame;
	private boolean spielerInHighscore = false;
	public boolean highscoreAngezeigt = false;

	public final int MAXLEVEL = 5;
	public final int WIDTH = 21;
	public final int HEIGHT = 21;
	public final int BOX = 32;
	public final int Width = 17;
	public final int Height = 17;

	/**
	 * @author Simon Nietz, Matr_Nr: 5823560
	 * @param width Breite des Fensters
	 * @param height Hoehe des Fensters
	 * @param title Titel des Fensters
	 */

	public HindiBones(int width, int height, String title) {
		//zeigeLogin();
		//if(LoginDialog.isSucceded()){
		//	System.out.println(LoginDialog.isSucceded());
		//	initialisiereJFrame(width, height, title);
		//	starteNeuesSpiel();
		
		try {
			engine.changeLevel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//}
		initialisiereJFrame(width, height, title);
		starteNeuesSpiel();
		
	}	

	/**
	 * 
	 * @param width Breite des erzeugten JFrame
	 * @param height Heohe des erzeugten JFrame
	 * @param title Titel des erzeugten JFrame
	 */

	public void initialisiereJFrame(int width, int height, String title) {
		// Layout fuer unser Fenster
		this.setLayout(new BorderLayout());
		// Erzeuge Objekte der Panels
		this.spielflaeche = new Spielflaeche(this);
		this.statusleiste = new Statusleiste(this);
		this.steuerung = new Steuerung();
		this.highscore = new Highscore();
		// Erzeuge Menuleiste
		this.menuLeiste = new MenuLeiste(this);
		// Es wird die gewuenschte Groesse angegeben
		spielflaeche.setPreferredSize(new Dimension(width, height));
		statusleiste.setPreferredSize(new Dimension(width, BOX));
		steuerung.setPreferredSize(new Dimension(width, height + BOX));
		highscore.setPreferredSize(new Dimension(width, height + BOX));

		// Erstelle das Spielfeld
		zeigeSpielfeld();
		// Zentriere das Fenster auf dem Bildschirm
		final Dimension d = this.getToolkit().getScreenSize();
		this.setLocation((int) ((d.getWidth() - this.getWidth()) / 2),
				(int) ((d.getHeight() - this.getHeight()) / 2));
		// Standardsetup
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.setResizable(false);
		this.setTitle(title);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void zeigeSpielfeld() {
		// entferne alles
		highscoreAngezeigt = false;
		this.remove(highscore);
		this.remove(steuerung);
		// erstelle das Spielfeld
		this.add(spielflaeche, BorderLayout.CENTER);
		this.add(statusleiste, BorderLayout.SOUTH);
		this.add(menuLeiste, BorderLayout.NORTH);
		// aktiviere das fertige Spielfeld
		this.requestFocus();
		this.pack();
	}

	public void zeigeLogin() {
		// erstelle das Fenster für den Login     	
		LoginDialog loginDlg = new LoginDialog(frame);
		loginDlg.setVisible(true);
		
			//     engine.spieler.setName(LoginDialog.getUsername());

		

	}




	public void zeigeHighscore() {
		// entferne alles
		highscoreAngezeigt = true;
		this.remove(spielflaeche);
		this.remove(statusleiste);
		this.remove(steuerung);
		// erstelle die Highscoreanzeige
		this.add(highscore, BorderLayout.CENTER);
		// aktiviere die Highscoreanzeige
		this.requestFocus();
		this.pack();
		highscore.repaint();
	}

	public void zeigeSteuerung() {
		// entferne alles
		highscoreAngezeigt = false;
		this.remove(spielflaeche);
		this.remove(statusleiste);
		this.remove(highscore);
		// erstelle die Steuerungsanzeige
		this.add(steuerung, BorderLayout.CENTER);
		// aktiviere die Steuerungsanzeige
		this.requestFocus();
		this.pack();
		steuerung.repaint();
	}

	// Getter fuer die Spielflaeche bzw. Statusleiste
	public Spielflaeche getSpielflaeche() {return spielflaeche;}
	public Statusleiste getStatusleiste() {return statusleiste;}
	public Highscore getHighscore() {return highscore;}


	// Methoden der Schnittstelle KeyListener

	public void keyPressed(KeyEvent e) {
		// Aktuelle Position des Spielers
		int xPos = engine.spieler.getXPos();
		int yPos = engine.spieler.getYPos();


		// Frage Tastatureingaben auf den Pfeiltasten ab.
		// Es wird geprueft, ob der naechste Schritt zulaessig ist.
		// Bleibt die Figur innerhalb der Grenzen des Arrays?
		// Wenn ja, ist das naechste Feld begehbar?
		// Falls beides "wahr" ist, dann gehe den naechsten Schritt
		if (!spielende) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				if (yPos > 0 && !(engine.map.karte[xPos][yPos - 1] instanceof Wand))
					engine.spieler.hoch();
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				if (yPos < HEIGHT + 4 && !(engine.map.karte[xPos][yPos + 1] instanceof Wand))
					engine.spieler.runter();
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (xPos > 0 && !(engine.map.karte[xPos - 1][yPos] instanceof Wand))
					engine.spieler.links();
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (xPos < WIDTH + 4 && !(engine.map.karte[xPos + 1][yPos] instanceof Wand))
					engine.spieler.rechts();
			} else if (e.getKeyCode() == KeyEvent.VK_Q) {
				System.out.println("Angreifen");
			//	Monster	m = engine.spieler.angriffsMonster();
			//	Monster m = engine.monsterListe.get(i);
			Monster m = engine.spieler.angriffsMonster();
				if (m != null)
					m.changeHealth(-BOX / 4);
				// B für 'Heiltrank benutzen'
				
			} else if (e.getKeyCode() == KeyEvent.VK_B){
				if(engine.spieler.anzahlHeiltraenke>0){
					int change = engine.spieler.benutzeHeiltrank();
					// Heilungseffekt wird verbessert, falls neue Monster durch das Aufheben des Schlüssels ausgelöst wurden
					if (engine.spieler.hatSchluessel())
						engine.spieler.changeHealth((int)(change*1.5));
					else
						engine.spieler.changeHealth((int)(change*0.5));
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			// Schluessel aufnehmen
			int a = engine.benutzeItem();				
			// Heiltrank aufnehmen

			// Schluessel benutzen
			if(a == 2){

				nextLevel();
			}else if(a == 3) {
				spielende = true;
			}
		}
	}




	// int aktuelleXPos = fenster.engine.spieler.getX();
	// int aktuelleYPos = fenster.engine.spieler.getY();	

	public void mouseClicked(MouseEvent e) {
		int	zielX = e.getX() / 32; // Koordinaten des Klicks ...
		int	zielY = e.getY() / 32; // auslesen und als Ziel setzen
		if (e.getButton() == MouseEvent.BUTTON1) {
			// Es war die linke Maustaste
			if (!spielende) {
			System.out.println(zielX+" "+zielY);
		//	engine.wegAnfragen(zielX, zielY);
			
			try {
				engine.wegAnfragen(zielX, zielY);
			} catch (InterruptedException e1) {
				System.out.println("Es ist was schief gelaufen");
				e1.printStackTrace();
			}
			
			}

		}
	}

	/* laufweg = engine.Astern(int zielX, zielY, startX, StartY, this)   | was für ein Typ ist laufweg?






	 */
	/* die uebrigen Methoden des MouseListener muessen auch implementiert werden 
	 auch wenn diese nicht genutzt werden
	 */
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}




	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

	public void spielZuruecksetzen() {

		// das Spiel wird neu initialisiert
/*		engine.spieler = new Spieler("img//spieler.png", this);
		monsterListe = new LinkedList<Monster>();
		level = new Spielelement[WIDTH][HEIGHT];
*/

//		currentLevel = 0;
		spielende = false;
		verloren = false;
		nebelAn = true;
		//nextLevel();
		spielerInHighscore = false;
		startZeit = System.currentTimeMillis();
	}

	// Spielschleife
	public void starteNeuesSpiel() {
		spielZuruecksetzen();

		do {

			if (!spielende) {
				// Hier wird alle 50ms neu gezeichnet
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {}

				getSpielflaeche().repaint();
				getStatusleiste().repaint();

				if (engine.spieler.getHealth() <= 0) {
					spielende = true;
					verloren = true;
				}
			} else {
				benoetigteZeit = (int) ((System.currentTimeMillis() - startZeit) / 1000);

				if (!verloren && !spielerInHighscore) {
					getHighscore().addSpielerToHighScore(benoetigteZeit);
					getHighscore().repaint();
					spielerInHighscore = true;
				} else {
					getSpielflaeche().repaint();
				}
			}

		} while (true);

	}

	public void nextLevel()  {
		// das nächste Level wird geladen
	//	currentLevel++;

		//	laby = level2.setzeInhalt(currentLevel);		
		try {
			engine.map.karte = engine.changeLevel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//	Leser leser = new Leser(laby, this);
		//	level = leser.getLevel();
	}

	public ClientEngine getEngine() {
		return engine;
	}



}

