package pp2016.team16.client.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import pp2016.team16.shared.Heiltrank;
import pp2016.team16.shared.Schluessel;
import pp2016.team16.shared.Spielelement;
import pp2016.team16.shared.Tuer;

public class Statusleiste extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Image hintergrund, schluessel, heiltrank;
	
	private HindiBones fenster;
	
	public Statusleiste(HindiBones fenster){
		this.fenster = fenster;
		
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
		
		for(int i = 0; i < fenster.WIDTH; i++){
			g.drawImage(hintergrund, i*fenster.BOX, 0, null);
		}		

		g.drawImage(fenster.spieler.getImage(), 4, 4, fenster.BOX - 8, fenster.BOX - 8, null);
		
		if(fenster.spieler.hatSchluessel()){
			g.drawImage(schluessel, fenster.BOX * (fenster.WIDTH - 1), 0, null);
		}
		
		g.setColor(Color.WHITE);	
		g.drawString(fenster.spieler.getName(), fenster.BOX + 5, 20);
		g.drawString("Zeit: " + (System.currentTimeMillis() - fenster.startZeit)/1000, fenster.BOX * (fenster.WIDTH - 6), 20);
		g.drawString("Level " + fenster.currentLevel + "/" + fenster.MAXLEVEL, fenster.BOX * (fenster.WIDTH - 4)-5, 20);
		
		// Heiltrankanzeige
		int anzahlHeiltraenke = fenster.spieler.getAnzahlHeiltraenke();
		String s;
		if (anzahlHeiltraenke < 10) s = "  "+anzahlHeiltraenke;
		else s = String.valueOf(anzahlHeiltraenke);
		g.drawString(s, fenster.BOX*(fenster.WIDTH-2)-8, 20);
		g.drawImage(heiltrank,fenster.BOX * (fenster.WIDTH-2),0,null);

		Spielelement feld = fenster.level[fenster.spieler.getXPos()][fenster.spieler.getYPos()];
		
		if(feld instanceof Schluessel){
			g.drawString("Leertaste zum Aufnehmen", fenster.BOX * (fenster.WIDTH - 11) - 5, 20);
		}else if(feld instanceof Tuer){
			if(!((Tuer) feld).istOffen()){
				if(fenster.spieler.hatSchluessel())
					g.drawString("Leertaste zum \u00d6ffnen", fenster.BOX * (fenster.WIDTH - 11) - 5, 20);
				else
					g.drawString("T\u00fcr verschlossen!", fenster.BOX * (fenster.WIDTH - 11) - 5, 20);
			}			
		}else if(feld instanceof Heiltrank){
			g.drawString("Leertaste zum Aufnehmen", fenster.BOX * (fenster.WIDTH - 11) - 5, 20);
		}
		
		g.setColor(Color.RED);
		g.fillRect((fenster.WIDTH / 2) * fenster.BOX - 80, getHeight() - 8, fenster.spieler.getMaxHealth(), 5);
		g.setColor(Color.GREEN);
		g.fillRect((fenster.WIDTH / 2) * fenster.BOX - 80, getHeight() - 8, fenster.spieler.getHealth(), 5);
	
	}
	
}
