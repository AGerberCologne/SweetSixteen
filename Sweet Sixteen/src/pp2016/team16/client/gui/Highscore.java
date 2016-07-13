package pp2016.team16.client.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import pp2016.team16.client.gui.LoginDialog;


/**
 * Klasse für das Panel um den Highscore anzuzeigen
 * @author Simon Nietz, Matr_Nr: 5823560
 *
 */

public class Highscore extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9029630360878826482L;
	//private static final long serialVersionUID = 1L;
	
	//private LinkedList<HighScoreElement> highScore;
	public LinkedList <String> highScore;
	
	
	
	public Highscore(){
		highScore = new LinkedList<String>();

		/*highScore = new LinkedList<String>();
		
		try {
			FileReader reader = new FileReader(new File("highscore.txt"));
			int c;
			String line = "";
			
			
			while((c = reader.read()) != -1){				
				
				if(c == '\n'){
					String[] temp = line.split("\t");
					highScore.add(new HighScoreElement(Integer.parseInt(temp[0].trim()), temp[1].trim()));					
					line = "";
				}else{
					line += (char) c;
				}
			}
			
			reader.close();
			
		} catch (IOException e) {
			System.out.println("Highscore konnte nicht gelesen werden");
		}
		while(highScore.size() < 10){
			highScore.add(new HighScoreElement(1000, "Anonym"));	
		}*/
	}
		
	/**
	 * Methode um einen Spieler in die Highscore Liste mit aufzunehmen
	 * @author Simon Nietz, Matr_Nr: 5823560
	 * @param zeit ist der Highscore
	 */
	
	/*public void addSpielerToHighScore(int zeit){
		String name;
		if(LoginDialog.succeeded==true ){
			 name = LoginDialog.getUsername();
		}else{
			 name = JOptionPane.showInputDialog("Bitte geben Sie Ihren Namen ein:");
		}
		for(int i = 0; i < highScore.size(); i++){
			if(highScore.get(i).zeit > zeit){
				highScore.add(i, new HighScoreElement(zeit, name));
				i = highScore.size();
			}
		}
		// der name und die zeit wird in die Liste Highscore.txt eingetragen
		try {
			FileWriter writer = new FileWriter(new File("highscore.txt"));
			for(int i = 0; i < 10; i++){
				writer.write(highScore.get(i).zeit + "\t" + highScore.get(i).name + "\n");
			}			

			writer.close();
			
		} catch (IOException e) {
			System.out.println("Highscore konnte nicht geschrieben werden");
		}
		
	}*/
	/**
	 * getter für den Highscore
	 * @author Simon Nietz, Matr_Nr: 5823560
	 * @return Die Highscore Liste
	 */
	/*
	public LinkedList<HighScoreElement> getHighScore(){
		return highScore;
	}
	*/
	/**
	 * paint methode um den Highscore anzuzeigen
	 * @author Simon Nietz, Matr_Nr: 5823560
	 */
	public void paint(Graphics g){
		
		Image img = null, boden = null;
		
		try{
			System.out.println("Versuch");
			img = ImageIO.read(new File("img//highscore.png"));
			boden = ImageIO.read(new File("img//status.png"));
		
		
		
		for(int i = 0; i < 17; i++){
			for(int j = 0; j < 18; j++){
				g.drawImage(boden, 32*i,32*j,null);
				System.out.println("Fenster wird gezeichnet");
			}
		}
		
		g.drawImage(img, 0, 0, null);
		System.out.println("Was auch immer das macht");
		g.setColor(Color.WHITE);
		
		for(int k = 1;k<6;k++){
		if(!highScore.isEmpty()){
			String m = highScore.removeFirst();
			System.out.println("Highscorename:"+m);
			
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
			g.drawString((k)+".: "+ m, 80, 150 + 30 * (k + 1));
			
			//g.drawString("" + zeit, 400, 150 + 30 * (i + 1));
			}}
		
		
	
		}catch(IOException e){ }
		
	}
	
}

/**
 * Klasse für HighScoreElement
 * @author Simon Nietz, Matr_Nr: 5823560
 *
 */

class HighScoreElement {
	
	String name;
	int zeit;
	
	/** Konstruktor für HighScoreElement
	 * @author Simon Nietz, Matr_Nr: 5823560
	 * @param punkte Die Punkte des Spielers
	 * @param name Der Name des Spielers 
	 */
	
	public HighScoreElement(int punkte, String name){
		this.name = name;
		this.zeit = punkte;
	}
	
}
