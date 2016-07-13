package pp2016.team16.client.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * <Die Klasse zeigt das Bilder der Steuerung an>
 * @author Simon Nietz, Matr_Nr: 5823560
 *
 */

public class Steuerung extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * <Methode für die Festlegung und das anzeigen des Bildes>
	 * @author Simon Nietz, Matr_Nr: 5823560
	 */
	
	public void paint(Graphics g){
		Image img = null, boden = null;
				
		try{
			img = ImageIO.read(new File("img//Steuerung2.png"));
			boden = ImageIO.read(new File("img//status.png"));
		}catch(IOException e){ }
		
		for(int i = 0; i < 16; i++){
			for(int j = 0; j < 17; j++){
				g.drawImage(boden, 32*i,32*j,null);
			}
		}
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		
	}
	
}
