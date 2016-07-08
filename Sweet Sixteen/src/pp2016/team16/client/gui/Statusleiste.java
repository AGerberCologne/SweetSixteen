package pp2016.team16.client.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import pp2016.team16.shared.Heiltrank;
import pp2016.team16.shared.Konstanten;
import pp2016.team16.shared.Schluessel;
import pp2016.team16.shared.Spielelement;
import pp2016.team16.shared.Tuer;

/**
 * Klasse für die Statusleiste die unten zu sehen ist
 * @author Simon Nietz, Matr_Nr: 5823560
 *
 */

public class Statusleiste extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Image hintergrund, schluessel, heiltrank;
	
	private HindiBones fenster;
	
	/**
	 * @author Simon Nietz, Matr_Nr: 5823560
	 * @param fenster fenster der Anwendung
	 */
	
	public Statusleiste(HindiBones fenster){
		this.fenster = fenster;
		
		// lade die Bilder die anzeigt werden sollen
		try {
			hintergrund = ImageIO.read(new File("img//status.png"));
			schluessel = ImageIO.read(new File("img//schluessel.png"));
			heiltrank = ImageIO.read(new File("img//heiltrank.png"));
		} catch (IOException e) {
			System.err.println("Hintergrundbild konnte nicht geladen werden.");
		}
	}
	

	
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		
		// Lege die Groeße der Statusleiste fest
		for(int i = 0; i < Konstanten.BREITE; i++){
			g.drawImage(hintergrund, i*fenster.BOX, 0, null);
		}		
		// male ein Bild des Spieler auf die Statusleiste
		g.drawImage(fenster.engine.spieler.getImage(), 4, 4, fenster.BOX - 8, fenster.BOX - 8, null);
		// male ein Bild des Schluessels
		if(fenster.engine.spieler.hatSchluessel()){
			g.drawImage(schluessel, fenster.BOX * (Konstanten.BREITE - 1), 0, null);
		}
		
		// die Zeite wird mitlaufen angezeigt, sowie das Level
		g.setColor(Color.WHITE);	
		g.drawString(fenster.engine.spieler.getName(), fenster.BOX + 5, 20);
		g.drawString("Zeit: " + (System.currentTimeMillis() - fenster.startZeit)/1000, fenster.BOX * (Konstanten.BREITE - 6), 20);
		g.drawString("Level " + fenster.engine.map.levelzaehler + "/" + fenster.MAXLEVEL, fenster.BOX * (Konstanten.BREITE - 4)-5, 20);
		
		// Heiltrankanzeige
		int anzahlHeiltraenke = fenster.engine.spieler.getAnzahlHeiltraenke();
		String s;
		if (anzahlHeiltraenke < 10) s = "  "+anzahlHeiltraenke;
		else s = String.valueOf(anzahlHeiltraenke);
		g.drawString(s, fenster.BOX*(Konstanten.BREITE-2)-8, 20);
		g.drawImage(heiltrank,fenster.BOX * (Konstanten.BREITE-2),0,null);

		Spielelement feld = fenster.engine.map.karte[fenster.engine.spieler.getXPos()][fenster.engine.spieler.getYPos()];
		// falls ein Schluessel aufgehoben wird, wird dieser auch in die Statusleiste gezeichnet
		if(feld instanceof Schluessel){
			g.drawString("Leertaste zum Aufnehmen", fenster.BOX * (Konstanten.BREITE - 11) - 5, 20);
		}else if(feld instanceof Tuer){
			if(!((Tuer) feld).istOffen()){
				if(fenster.engine.spieler.hatSchluessel())
					g.drawString("Leertaste zum \u00d6ffnen", fenster.BOX * (Konstanten.BREITE - 11) - 5, 20);
				else
					g.drawString("T\u00fcr verschlossen!", fenster.BOX * (Konstanten.BREITE - 11) - 5, 20);
			}			
		}else if(feld instanceof Heiltrank){
			g.drawString("Leertaste zum Aufnehmen", fenster.BOX * (Konstanten.BREITE - 11) - 5, 20);
		}
		
		// zeige die Lebensanzeige des Spielers an
		g.setColor(Color.RED);
		g.fillRect((Konstanten.BREITE / 2) * fenster.BOX - 80, getHeight() - 8, fenster.engine.spieler.getMaxHealth(), 5);
		g.setColor(Color.GREEN);
		g.fillRect((Konstanten.BREITE / 2) * fenster.BOX - 80, getHeight() - 8, fenster.engine.spieler.getHealth(), 5);
	
	}
	
}
