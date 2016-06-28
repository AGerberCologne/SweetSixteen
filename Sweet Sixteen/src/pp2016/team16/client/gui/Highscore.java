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
import gui.LoginDialog;
public class Highscore extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private LinkedList<HighScoreElement> highScore;
	
	
	
	public Highscore(){

		highScore = new LinkedList<HighScoreElement>();
		
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
		}
	}
		
	public void addSpielerToHighScore(int zeit){
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
		
		try {
			FileWriter writer = new FileWriter(new File("highscore.txt"));
			for(int i = 0; i < 10; i++){
				writer.write(highScore.get(i).zeit + "\t" + highScore.get(i).name + "\n");
			}			

			writer.close();
			
		} catch (IOException e) {
			System.out.println("Highscore konnte nicht geschrieben werden");
		}
		
	}
	
	public LinkedList<HighScoreElement> getHighScore(){
		return highScore;
	}
	
	public void paint(Graphics g){
		Image img = null, boden = null;
		
		try{
			img = ImageIO.read(new File("img//highscore.png"));
			boden = ImageIO.read(new File("img//status.png"));
		}catch(IOException e){ }
		
		
		for(int i = 0; i < 16; i++){
			for(int j = 0; j < 17; j++){
				g.drawImage(boden, 32*i,32*j,null);
			}
		}
		g.drawImage(img, 0, 0, null);
		g.setColor(Color.WHITE);
		
		for (int i = 0; i < 10; i++) {
			String name = highScore.get(i).name;
			int zeit = highScore.get(i).zeit;

			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
			g.drawString((i + 1) + ".  " + name, 80, 150 + 30 * (i + 1));
			g.drawString("" + zeit, 400, 150 + 30 * (i + 1));
		}
	}
}

class HighScoreElement {
	
	String name;
	int zeit;
	
	public HighScoreElement(int punkte, String name){
		this.name = name;
		this.zeit = punkte;
	}
	
}
