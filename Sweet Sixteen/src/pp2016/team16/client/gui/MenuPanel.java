package pp2016.team16.client.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MenuPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	JButton start = new JButton("Start");
	JButton highscore = new JButton("Highscore");
	JButton beenden = new JButton("Beenden");
	JTextField name = new JTextField("Spieler Eins");
/*	JButton einloggen = new JButton("Login");
	JButton ausloggen = new JButton("Ausloggen");
	JButton minimapzeigen = new JButton("MiniMap zeigen");
*/
	HindiBones fenster;
	
	public MenuPanel(HindiBones f){
		this.fenster = f;
		
		setLayout(new GridLayout(5,1));
		
		
		JPanel p = new JPanel(){
			private static final long serialVersionUID = 1L;
			
			public void paint(Graphics g){
				Image img = null;
				
				try{
					img = ImageIO.read(new File("img//menu.png"));
				}catch(IOException e){ }
				
				g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
				g.setColor(Color.WHITE);
			}
			
		};
				
		add(p);
		
		// Spiel-starten-Button
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				fenster.zeigeSpielfeld();
				fenster.spieler.setName(name.getText());
				
			}	
		});
		//start.setPreferredSize(new Dimension(200,50));
		
		// Highscore-anzeigen-Button
		highscore.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				fenster.zeigeHighscore();
			}	
		});
		//highscore.setPreferredSize(new Dimension(200,50));
		
		// Beenden-Button
		beenden.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}	
		});
		//beenden.setPreferredSize(new Dimension(200,50));

		name.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				fenster.zeigeSpielfeld();
				fenster.spieler.setName(name.getText());
			}	
		});
		//name.setPreferredSize(new Dimension(200,50));
		
	
		
		
		add(name);
		add(start);
		add(highscore);
		add(beenden);
		p.repaint();

	}
	
}
