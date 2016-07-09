package pp2016.team16.client.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import pp2016.team16.shared.Boden;
import pp2016.team16.shared.Heiltrank;
import pp2016.team16.shared.Konstanten;
import pp2016.team16.shared.Monster;
import pp2016.team16.shared.Schluessel;
import pp2016.team16.shared.Spieler;
import pp2016.team16.shared.Tuer;
import pp2016.team16.shared.Wand;

/**
 * Klasse f�r das JPanel auf dem die Spielflaeche gezeichnet wird
 * @author Simon Nietz, Matr_Nr: 5823560
 *
 */


	//Panel f�r die Spielflache
public class Spielflaeche extends JPanel {

	private static final long serialVersionUID = 1L;

	private Image boden, wand, tuerOffen, tuerZu, schluessel, heiltrank, feuerball;
	public HindiBones fenster;
	
	/**
	 * @author Simon Nietz, Matr_Nr: 5823560
	 * @param fenster das Spielfenster 
	 */
	
	public Spielflaeche(HindiBones fenster) {
		this.fenster = fenster;
		// Lade die Bilder
		try {
			boden = ImageIO.read(new File("img//boden.png"));
			wand = ImageIO.read(new File("img//wand.png"));
			tuerZu = ImageIO.read(new File("img//tuer.png"));
			tuerOffen = ImageIO.read(new File("img//tueroffen.png"));
			schluessel = ImageIO.read(new File("img//schluessel.png"));
			heiltrank = ImageIO.read(new File("img//heiltrank.png"));
			feuerball = ImageIO.read(new File("img//feuerball.png"));
		} catch (IOException e) {
			System.err.println("Ein Bild konnte nicht geladen werden.");
		}
	}
	
	/**
	 * @author Simon Nietz, Matr_Nr: 5823560
	 * Die paint Methode zeichnet die Spieler, Monster, Feuerbaelle, Spielflaeche, etc.
	 */

	public void paint(Graphics g) {
		
		// Beim neuzeichnen wird zunaechst alles uebermalt
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		int zaehler1=0;
		int zaehler2=0;
		// Ueberpruefe an welcher Positition der Spieler ist
		if(fenster.engine.spieler.getYPos()>(fenster.HEIGHT+1)/2+3)
			zaehler2 = 4;
		else if(fenster.engine.spieler.getYPos()>(fenster.HEIGHT+1)/2+2)
			zaehler2 = 3;
		else if(fenster.engine.spieler.getYPos()>(fenster.HEIGHT+1)/2+1)
			zaehler2 = 2;
		else if(fenster.engine.spieler.getYPos()>(fenster.HEIGHT+1)/2)
			zaehler2 = 1;
		
		if(fenster.engine.spieler.getXPos()>(fenster.HEIGHT+1)/2+3)
			zaehler1 = 4;
		else if(fenster.engine.spieler.getXPos()>(fenster.HEIGHT+1)/2+2)
			zaehler1 = 3;
		else if(fenster.engine.spieler.getXPos()>(fenster.HEIGHT+1)/2+1)
			zaehler1 = 2;
		else if(fenster.engine.spieler.getXPos()>(fenster.HEIGHT+1)/2)
			zaehler1 = 1;
		
		// Male die einzelnen Felder
		// falls der Spieler eine gewisse Position ueberschreitet wird mitgescrollt
		for (int i = 0; i < Konstanten.BREITE; i++) {
			for (int j = 0; j < Konstanten.HOEHE; j++) {
				if (inRange(i+zaehler1,j+zaehler2)) {

					if (fenster.engine.map.karte[i+zaehler1][j+zaehler2] instanceof Wand) {
						// Hier kommt eine Wand hin
						g.drawImage(wand, i * fenster.BOX, j * fenster.BOX,
								null);
					} else if (fenster.engine.map.karte[i+zaehler1][j+zaehler2] instanceof Boden) {
						// Dieses Feld ist begehbar
						g.drawImage(boden, i * fenster.BOX,
								j * fenster.BOX, null);
					} else if (fenster.engine.map.karte[i+zaehler1][j+zaehler2] instanceof Schluessel) {
						// Hier liegt ein Schluessel
						g.drawImage(boden, i * fenster.BOX,
								j * fenster.BOX, null);
						g.drawImage(schluessel, i * fenster.BOX, j
								* fenster.BOX, null);
					} else if (fenster.engine.map.karte[i+zaehler1][j+zaehler2] instanceof Tuer){
						// Hier ist die Tuer
						g.drawImage(boden, i * fenster.BOX,
								j * fenster.BOX, null);
						if (((Tuer) fenster.engine.map.karte[i+zaehler1][j+zaehler2]).istOffen())
							g.drawImage(tuerOffen, i * fenster.BOX, j
									* fenster.BOX, null);
						else
							g.drawImage(tuerZu, i * fenster.BOX, j
									* fenster.BOX, null);
					} else if (fenster.engine.map.karte[i+zaehler1][j+zaehler2] instanceof Heiltrank) {
						// Hier ist die Tuer
						g.drawImage(boden, i * fenster.BOX,
								j * fenster.BOX, null);
						// Hier steht ein Heiltrank
						g.drawImage(boden, i * fenster.BOX,
								j * fenster.BOX, null);
						g.drawImage(heiltrank, i * fenster.BOX, j
								* fenster.BOX, null);
					}
				}
			}
		}

		// Male die Monster an ihrer Position
		/*for (int i = 0; i < fenster.engine.monsterListe.size(); i++) {
			Monster m = fenster.engine.monsterListe.get(i);
			boolean event = fenster.engine.spieler.hatSchluessel();
			// Da hier alle Monster aufgerufen werden, wird an dieser
			// Stelle auch ein Angriffsbefehl fuer die Monster
			// abgegeben, falls der Spieler in der Naehe ist.
			// Ansonsten soll das Monster laufen
			if(!m.attackiereSpieler(event)){
				m.move();
			}else{
				int box = fenster.BOX;
				Spieler s = fenster.engine.spieler;
				
				double p = m.cooldownProzent();
				g.setColor(Color.RED);
				g.drawImage(feuerball, (int)(((1-p) * m.getXPos() + (p) * s.getXPos()-zaehler1)*box) + box/2, 
						   (int)(((1-p) * m.getYPos() + (p) * s.getYPos()-zaehler2)*box) + box/2, 8, 8, null);
			}	

			// Male das Monster, falls es von anfang an anwesend ist
			if (m.getTyp() == 0) drawMonster(g,m);
			else if (m.getTyp() == 2) drawMonster(g,m);
			// Male das Monster, falls es erst durch das Event 'Schluessel aufheben' erscheint
			else if (event && m.getTyp() == 1) drawMonster(g,m);
			
		}*/
		
		// Male den Spieler an seiner Position
	//	g.drawImage(fenster.spieler.getImage(), fenster.spieler.getXPos()
	//			* fenster.BOX, fenster.spieler.getYPos() * fenster.BOX,
	//			null);
		
		if(zaehler1>0 && zaehler2>0){
			g.drawImage(fenster.engine.spieler.getImage(), (fenster.engine.spieler.getXPos()-zaehler1)
					* fenster.BOX, (fenster.engine.spieler.getYPos()-zaehler2) * fenster.BOX,
					null);
		}else if(zaehler2>0){
			g.drawImage(fenster.engine.spieler.getImage(), fenster.engine.spieler.getXPos()
					* fenster.BOX, (fenster.engine.spieler.getYPos()-zaehler2) * fenster.BOX,
					null);
		}else if(zaehler1>0){
			g.drawImage(fenster.engine.spieler.getImage(), (fenster.engine.spieler.getXPos()-zaehler1)
					* fenster.BOX, (fenster.engine.spieler.getYPos()) * fenster.BOX,
					null);
		}else{	g.drawImage(fenster.engine.spieler.getImage(), fenster.engine.spieler.getXPos()
					* fenster.BOX, fenster.engine.spieler.getYPos() * fenster.BOX,
					null);
		}
		
		if(fenster.verloren){
			g.setColor(Color.WHITE);
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
			g.drawString("GAME OVER", getWidth()/2 - 120, getHeight()/2);
		}else{
			if(fenster.spielende){
				g.setColor(Color.WHITE);
				g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
				g.drawString("GEWONNEN", getWidth()/2 - 120, getHeight()/2);
			}
		}
	}

	/**
	 * @author Simon Nietz, Matr_Nr: 5823560
	 * @param g
	 * @param m der Typ des Monsters
	 */
	private void drawMonster(Graphics g, Monster m){
		// Monster Health Points
		
		int a=0;
		int b=0;

		if(fenster.engine.spieler.getYPos()>(fenster.HEIGHT+1)/2+3)
			b = 4;
		else if(fenster.engine.spieler.getYPos()>(fenster.HEIGHT+1)/2+2)
			b = 3;
		else if(fenster.engine.spieler.getYPos()>(fenster.HEIGHT+1)/2+1)
			b = 2;
		else if(fenster.engine.spieler.getYPos()>(fenster.HEIGHT+1)/2)
			b = 1;
		
		if(fenster.engine.spieler.getXPos()>(fenster.HEIGHT+1)/2+3)
			a = 4;
		else if(fenster.engine.spieler.getXPos()>(fenster.HEIGHT+1)/2+2)
			a = 3;
		else if(fenster.engine.spieler.getXPos()>(fenster.HEIGHT+1)/2+1)
			a = 2;
		else if(fenster.engine.spieler.getXPos()>(fenster.HEIGHT+1)/2)
			a = 1;
		
		
		if(inRange(m.getXPos(), m.getYPos())){
			g.drawImage(m.getImage(), (m.getXPos()-a) * fenster.BOX, (m.getYPos()-b)
					* fenster.BOX, null);
			g.setColor(Color.GREEN);
			g.fillRect((m.getXPos()-a)*fenster.BOX, (m.getYPos()-b)
					* fenster.BOX - 2, m.getHealth(), 2);
			
			}
	}
	
	private boolean inRange(int i, int j) {
		return (Math.sqrt(Math.pow(fenster.engine.spieler.getXPos() - i, 2)
				+ Math.pow(fenster.engine.spieler.getYPos() - j, 2)) < 3 || !fenster.nebelAn);
	}

}
