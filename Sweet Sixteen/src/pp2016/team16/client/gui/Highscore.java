package pp2016.team16.client.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


/**
 * @author Diese Klasse wurde aus dem alten Spiel uebernommen.
 *
 */

public class Highscore extends JPanel {

	private static final long serialVersionUID = 9029630360878826482L;

	public LinkedList <String> highScore;


	/**
	 * @author Dieser Konstruktor wurde aus dem alten Spiel uebernommen.
	 */
	public Highscore(){
		highScore = new LinkedList<String>();
	}



	/**
	 * @author Diese Methode wurde aus dem alten Spiel uebernommen.
	 */
	public void paint(Graphics g){

		Image img = null, boden = null;

		try{
			img = ImageIO.read(new File("img//highscore.png"));
			boden = ImageIO.read(new File("img//status.png"));



			for(int i = 0; i < 17; i++){
				for(int j = 0; j < 18; j++){
					g.drawImage(boden, 32*i,32*j,null);
					System.out.println("Highscore wird gezeichnet");
				}
			}

			g.drawImage(img, 0, 0, null);
			g.setColor(Color.WHITE);

			for(int k = 1;k<6;k++){
				if(!highScore.isEmpty()){
					String m = highScore.removeFirst();
					System.out.println("Highscorename:"+m);

					g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
					g.drawString((k)+".: "+ m, 80, 150 + 30 * (k + 1));

				}}



		}catch(IOException e){ }

	}

}


