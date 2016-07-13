package pp2016.team16.client.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
/**
 * <Klasse um die Cheats nachher anzeigen zu können.>
 * @author Simon Nietz, Matr_Nr: 5823560
 *
 */
public class Cheats extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * <In der Paint Methode wird die Position des Bildes der Cheats festgelegt und dieses hinzugefuegt. >
	 * @author Simon Nietz, Matr_Nr: 5823560
	 */
	
	public void paint(Graphics g){
		
		Image img = null, boden = null;
			// Versuche die Bilder die benutzt werden zu laden
		try{
			img = ImageIO.read(new File("img//cheats.png"));
			boden = ImageIO.read(new File("img//status.png"));
		}catch(IOException e){ }
		
		// Die gesamte Spielflaeche wird mit dem Bild Boden uebermalt
		for(int i = 0; i < 16; i++){
			for(int j = 0; j < 17; j++){
				g.drawImage(boden, 32*i,32*j,null);
			}
		}
		//Fuege nun das Bild der Steuerung hinzu.
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		
	}
	
}
