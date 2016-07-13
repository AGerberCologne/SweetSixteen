package pp2016.team16.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import pp2016.team16.client.engine.ClientEngine;

/**
 * <Klasse fuer das gesamte Fenster des Spiels Implementiert wird ein
 * KeyListener und ein MouseListener Der KeyListener wird für die Bewegung
 * benoetigt, der MouseListener wird Aktionen>
 * 
 * @author Simon Nietz, Matr_Nr: 5823560
 *
 */

public class HindiBones extends JFrame implements KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private Spielflaeche spielflaeche;
	private Statusleiste statusleiste;
	private MenuLeiste menuLeiste;
	private Steuerung steuerung;

	public Highscore highscore;
	public Cheats cheats;
	public ClientEngine engine = new ClientEngine();
	public static JFrame frame;

	public boolean spielerInHighscore = false;
	public boolean highscoreAngezeigt = false;
	public boolean nebelAn = true;
	public boolean spielende = false;
	public boolean verloren = false;

	public final int MAXLEVEL = 5;
	public final int WIDTH = 21;
	public final int HEIGHT = 21;
	public final int BOX = 32;
	public static int itemtyp;
	public long startZeit;
	public int benoetigteZeit;

	/**
	 * <Der Konstruktor startet zunächst die Loginabfrage Wird diese erfuellt,
	 * so wird ein neues JFrame initialisiert und ein neues Spiel gestartet.>
	 * 
	 * @author Simon Nietz, Matr_Nr: 5823560
	 * 
	 * @param width
	 *            die Breite des erzeugten JFrame fuer das Spiel
	 * @param height
	 *            die Hoehe des erzeugten JFrame fuer das Spiel
	 * @param title
	 *            der Titel des JFrame
	 * 
	 */

	public HindiBones(int width, int height, String title) {

		// starte zunaechst den LoginDialog und ueberpruefe, ob der Login
		// erfolgreich ist
		zeigeLogin();
		if (LoginDialog.isSucceded()) {
			try {
				engine.changeLevel();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// bei erfolgreichem Login, wird ein neues Spiel gestartet
			initialisiereJFrame(width, height, title);
			starteNeuesSpiel();
		}

	}

	/**
	 * 
	 * @author Simon Nietz, Matr_Nr:5823560
	 * @param width
	 *            Breite des erzeugten JFrame
	 * @param height
	 *            Heohe des erzeugten JFrame
	 * @param title
	 *            Titel des erzeugten JFrame
	 */

	public void initialisiereJFrame(int width, int height, String title) {
		// Layout fuer unser Fenster
		this.setLayout(new BorderLayout());
		// Erzeuge Objekte der Panels
		this.spielflaeche = new Spielflaeche(this);
		this.statusleiste = new Statusleiste(this);
		this.steuerung = new Steuerung();
		this.highscore = new Highscore();
		this.cheats = new Cheats();
		// Erzeuge Menuleiste
		this.menuLeiste = new MenuLeiste(this);
		// Es wird die gewuenschte Groesse angegeben
		spielflaeche.setPreferredSize(new Dimension(width, height));
		statusleiste.setPreferredSize(new Dimension(width, BOX));
		steuerung.setPreferredSize(new Dimension(width, height + BOX));
		cheats.setPreferredSize(new Dimension(width, height + BOX));
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

	/**
	 * @author Die Methode wurde aus dem alten Spiel uebernommen
	 */
	public void zeigeSpielfeld() {
		// entferne alles
		highscoreAngezeigt = false;
		this.remove(highscore);
		this.remove(steuerung);
		this.remove(cheats);
		// erstelle das Spielfeld
		this.add(spielflaeche, BorderLayout.CENTER);
		this.add(statusleiste, BorderLayout.SOUTH);
		this.add(menuLeiste, BorderLayout.NORTH);
		// aktiviere das fertige Spielfeld
		this.requestFocus();
		this.pack();
	}

	/**
	 * @author Diese Methode wurde aus dem alten Spiel uebernommen
	 */
	public void zeigeHighscore() {
		// entferne alles
		highscoreAngezeigt = true;
		this.remove(spielflaeche);
		this.remove(statusleiste);
		this.remove(steuerung);
		this.remove(cheats);
		// erstelle die Highscoreanzeige
		this.add(highscore, BorderLayout.CENTER);
		// aktiviere die Highscoreanzeige
		this.requestFocus();
		this.pack();
		highscore.repaint();
	}

	/**
	 * @author Diese Methode wurde aus dem alten Spiel uebernommen
	 */
	public void zeigeSteuerung() {
		// entferne alles
		highscoreAngezeigt = false;
		this.remove(spielflaeche);
		this.remove(statusleiste);
		this.remove(highscore);
		this.remove(cheats);
		// erstelle die Steuerungsanzeige
		this.add(steuerung, BorderLayout.CENTER);
		// aktiviere die Steuerungsanzeige
		this.requestFocus();
		this.pack();
		steuerung.repaint();
	}

	/**
	 * <Die Methode zeigt das Bild der Cheats an>
	 * 
	 * @author Simon Nietz, Matr_Nr: 5823560
	 */
	public void zeigeCheats() {
		highscoreAngezeigt = false;
		// Entferne zunaechst alles, was angezeigt wird
		this.remove(spielflaeche);
		this.remove(statusleiste);
		this.remove(highscore);
		this.remove(steuerung);
		// erstelle die Steuerungsanzeige
		this.add(cheats, BorderLayout.CENTER);
		// aktiviere die Steuerungsanzeige
		this.requestFocus();
		this.pack();
		cheats.repaint();
	}

	/**
	 * Die Methode initialisiert ein neues Objekt des Datentype LoginDialog>
	 * 
	 * @author Simon Nietz, Matr_Nr: 5823560
	 */
	public void zeigeLogin() {
		// erstelle das Fenster für den Login
		LoginDialog loginDlg = new LoginDialog(frame, this);
		loginDlg.setVisible(true);
	}

	/**
	 * @author Diese Methode wurde aus dem alten Spiel uebernommen
	 */
	// Getter fuer die Spielflaeche bzw. Statusleiste
	public Spielflaeche getSpielflaeche() {
		return spielflaeche;
	}
	/**
	 * @author Diese Methode wurde aus dem alten Spiel uebernommen
	 */
	public Statusleiste getStatusleiste() {
		return statusleiste;
	}
	/**
	 * @author Diese Methode wurde aus dem alten Spiel uebernommen
	 */
	public Highscore getHighscore() {
		return highscore;
	}

	
	/**
	 * <Die Klasse implementiert den KeyListener>
	 * @author Simon Nietz, Matr_Nr: 5823560
	 */
	public void keyPressed(KeyEvent e) {
		// Frage ab, welche Tasten gedrueckt werden
		// Rufe bei den Tasten die jeweiligen Methoden aus der ClientEngine auf
		if (!spielende) {
			if (e.getKeyCode() == KeyEvent.VK_B) {
				engine.trankBenutzen();
				//
			} else if (e.getKeyCode() == KeyEvent.VK_L) {
				try {
					engine.cheatBenutzen(1);
				} catch (Exception e1) {
				}
			} else if (e.getKeyCode() == KeyEvent.VK_D) {
				try {
					engine.cheatBenutzen(2);
				} catch (Exception e1) {
				}
			}

			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				// Item aufnehmen
				try {
					itemtyp = engine.benutzeItem();
				} catch (InterruptedException e1) {

				}
				if (itemtyp == 2) {

					nextLevel();
				} else if (itemtyp == 3) {
					spielende = true;
				}
			}
		}

	}

	// Die restlichen Methoden des KeyListener muessen implementiert werden
	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	/**
	 * <Der MouseListener wird implementiert.>
	 * @author Simon Nietz
	 */
	public void mouseClicked(MouseEvent e) {
		int zielX = e.getX() / 32 + spielflaeche.zaehler1; // Koordinaten des
															// Klicks ...
		int zielY = (e.getY() / 32) - 1 + spielflaeche.zaehler2; // auslesen und
																	// als Ziel
																	// setzen
		if (e.getButton() == MouseEvent.BUTTON1) {
			// Es war die linke Maustaste
			if (!spielende) {
				System.out.println(zielX + " " + zielY);

				// Gebe die Koordinaten an die Methode wegAnfragen der
				// ClientEngine weiter
				try {
					engine.wegAnfragen(zielX, zielY);
					System.out.println("Anfrage wurde verschickt");
				} catch (InterruptedException e1) {
				}

			}
			// Bei Rechter Maustaste wird die Methode angriffSpieler der
			// ClientEngine weitergegeben
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			engine.angriffSpieler();

		}
	}

	// Die restlichen Methoden des Mouselistener müssen implementiert werden
	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	/**
	 * @author Diese Methode wurde aus dem alten Spiel uebernommen
	 */
	public void spielZuruecksetzen() {
		spielende = false;
		verloren = false;
		nebelAn = true;
		spielerInHighscore = false;
		startZeit = System.currentTimeMillis();
	}

	/**
	 * @author Diese Methode wurde aus dem alten Spiel uebernommen
	 */
	// Spielschleife
	public void starteNeuesSpiel() {
		spielZuruecksetzen();

		do {

			if (!spielende) {
				// Hier wird alle 50ms neu gezeichnet
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}

				getSpielflaeche().repaint();
				getStatusleiste().repaint();

				if (engine.spieler.getHealth() <= 0) {
					spielende = true;
					verloren = true;
				}
			} else {
				benoetigteZeit = (int) ((System.currentTimeMillis() - startZeit) / 1000);

				if (!verloren && !spielerInHighscore) {
					engine.fuegeZuHighScorehinzu(LoginDialog.getUsername(),
							benoetigteZeit);
				} else {
					getSpielflaeche().repaint();
				}
			}

		} while (true);

	}
	
	/**
	 * <Die Methode lädt das Level aus der ClientEngine>
	 * @author Simon Nietz, Matr_Nr: 5823560
	 */
	public void nextLevel() {
		try {
			engine.spielfeld.karte = engine.changeLevel();
		} catch (Exception e) {
		}
	}
	


}
