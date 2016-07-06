package pp2016.team16.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import pp2016.team16.shared.Boden;
import pp2016.team16.shared.Heiltrank;
import pp2016.team16.shared.Monster;
import pp2016.team16.shared.Schluessel;
import pp2016.team16.shared.Tuer;
import pp2016.team16.shared.Wand;

/**
 * Klasse für die erzeugung der Minimap
 * @author Simon Nietz, Matr_Nr: 5823560
 *
 */
public class MiniMap extends JPanel{	
	
	private HindiBones fenster;
	//erstelle JFrame für die Minimap	
	JFrame jFrame = new JFrame();
		
	private Image boden, wand, tuerOffen, tuerZu, schluessel, heiltrank;
	
	private Image scaledBoden, scaledWand, scaledTuerOffen, scaledTuerZu, scaledSchluessel, scaledHeiltrank;
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * @author Simon Nietz, Matr_Nr: 5823560
	 * @param fenster fenster der Anwendung
	 */
	
	public MiniMap(HindiBones fenster) {
		this.fenster = fenster;
		
		//setze die Groeße und Position fest.
	    jFrame.setSize(258,340);
		final Dimension d = jFrame.getToolkit().getScreenSize();
		jFrame.setLocation((int) ((d.getWidth() - jFrame.getWidth()) / 2)+((32*16)/2)+180,
				(int) ((d.getHeight() - jFrame
						.getHeight()) / 2)-((32*16)/2)+98);
		this.setBackground(Color.BLACK);
		jFrame.setResizable(false); // Die Groeße soll nicht veraendert werden
		jFrame.add(this);
		jFrame.setVisible(true); // sichtbar machen
		fenster.toFront();	// setze das Spielfenster in den Focus

		
		
	}
	
	/**
	 * Paint Methode zeichnet wie die Methode bei der Spielflaeche die Karte
	 * @author Simon Nietz, Matr_Nr: 5823560
	 */
	
	
	public void paint(Graphics g) {
		int size = 12;
		
		// benute Scaled Images um eine kleinere Version der Karten zu zeichnen
		
		try {
			boden = ImageIO.read(new File("img//boden.png")); 
			scaledBoden=boden.getScaledInstance(size, size,Image.SCALE_SMOOTH);
			wand = ImageIO.read(new File("img//wand.png"));
			scaledWand=wand.getScaledInstance(size, size,Image.SCALE_SMOOTH);
			tuerZu = ImageIO.read(new File("img//tuer.png"));
			scaledTuerZu = tuerZu.getScaledInstance(size, size,Image.SCALE_SMOOTH);
			tuerOffen = ImageIO.read(new File("img//tueroffen.png"));
			scaledTuerOffen=tuerOffen.getScaledInstance(size, size,Image.SCALE_SMOOTH);
			schluessel = ImageIO.read(new File("img//schluessel.png"));
			scaledSchluessel=schluessel.getScaledInstance(size, size,Image.SCALE_SMOOTH);
			heiltrank = ImageIO.read(new File("img//heiltrank.png"));
			scaledHeiltrank=heiltrank.getScaledInstance(size, size,Image.SCALE_SMOOTH);
		} catch (IOException e) {
			System.err.println("Error while loading one of the images.");
		}
		
		
		//super.paint(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		for (int i = 0; i < fenster.WIDTH; i++) {
			for (int j = 0; j < fenster.HEIGHT; j++) {
				if (fenster.engine.map.karte[i][j] instanceof Wand) {
					 // Hier kommt eine Wand hin
					g.drawImage(scaledWand, i*size, j*size,	null);
				} else if (fenster.engine.map.karte[i][j] instanceof Boden) {
					// Das Feld ist begehbar
					g.drawImage(scaledBoden, i*size,j*size, null);
				} else if (fenster.engine.map.karte[i][j] instanceof Schluessel) {
					// Hier liegt der Schluessel
					g.drawImage(scaledBoden, i*size,j*size , null);
					g.drawImage(scaledSchluessel, i*size,j*size, null);
				} else if (fenster.engine.map.karte[i][j] instanceof Tuer) {
					// Hier ist die Tür
					g.drawImage(scaledBoden, i*size,j*size, null);
					if (((Tuer) fenster.engine.map.karte[i][j]).istOffen())
						g.drawImage(scaledTuerOffen, i*size,j*size, null);
					else
						g.drawImage(scaledTuerZu, i*size,j*size, null);
				} else if (fenster.engine.map.karte[i][j] instanceof Heiltrank) {
					// Hier ist die Tür
					g.drawImage(scaledBoden, i*size,j*size, null);
					// Hier liegt ein Heiltrank
					g.drawImage(scaledBoden, i*size,j*size, null);
					g.drawImage(scaledHeiltrank, i*size,j*size, null);
				}
				
			}
		}
		
		// zeichne auch den Spieler aber als gruenen Punkt
		g.setColor(Color.GREEN);
		g.fillOval(fenster.engine.spieler.getXPos()* size+2, fenster.engine.spieler.getYPos() * size+
				2, 8,8);
		
		for (int i = 0; i < fenster.engine.monsterListe.size(); i++) {
			Monster m = fenster.engine.monsterListe.get(i);
			boolean event = fenster.engine.spieler.hatSchluessel();
		// zeichne die Monster als roten Punkt	
			if(!m.attackiereSpieler(event)){
				m.move();
			}if (m.getTyp() == 0) {
				g.setColor(Color.RED);
				g.fillOval(m.getXPos() * size+2, m.getYPos()* size+2,8,8);
			}if (m.getTyp() == 2){
				g.setColor(Color.RED);
				g.fillOval(m.getXPos() * size+2, m.getYPos()* size+2,8,8);
			}else if (event && m.getTyp() == 1) {
				g.setColor(Color.RED);
				g.fillOval(m.getXPos() * size+2, m.getYPos()* size+2,8,8);			
			}
			
		}
		//Legende für die MiniMap
		//Zunaechst werden die Bilder gezeichnet
		g.setColor(Color.GREEN);
		g.fillOval(10,270,8,8);
		g.setColor(Color.RED);
		g.fillOval(100,270,8,8);
		g.drawImage(scaledHeiltrank, 10, 290, null);			
		g.drawImage(scaledSchluessel, 100, 290, null);
		// Beschreibung der Bilder
		g.setColor(Color.WHITE);
		g.drawString("Spieler",30,278);
		g.drawString("Drache", 120, 278);
		g.drawString("Heiltrank", 30, 298);		
		g.drawString("Schluessel", 120, 298);	
		
		repaint();
		
	}			
	

}
