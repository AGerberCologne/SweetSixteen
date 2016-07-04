package pp2016.team16.client.gui;


import java.util.LinkedList;

import pp2016.team16.shared.Boden;
import pp2016.team16.shared.Monster;
import pp2016.team16.shared.Spielelement;
import pp2016.team16.shared.Tuer;
import pp2016.team16.shared.Wand;

public class Leser {

	private int [][] dateiname;
//	private String dateiname;
	private Spielelement[][] karte;
	private HindiBones fenster;
	
	public Leser(int [][] a, HindiBones fenster){
		this.fenster = fenster;
		this.dateiname = a;
		
		readLevel();
	}
	
	private void readLevel(){

			
			karte = new Spielelement[fenster.WIDTH][fenster.HEIGHT];
			fenster.monsterListe = new LinkedList<Monster>();		
			
	
		
		for(int i=0;i<dateiname.length;i++){
			for(int j=0;j<dateiname.length;j++){
				int Variable = dateiname[i][j];
				switch(Variable){
			case 0: karte[i][j] = new Wand(); break;
			case 1: karte[i][j] = new Boden(); break;
//			case 3: karte[i][j] = new Schluessel(); break;
			case 6: karte[i][j] = new Tuer(false); break;
			case 4: karte[i][j] = new Tuer(true); fenster.spieler.setPos(i, j); break;
			case 2: karte[i][j] = new Boden(); fenster.monsterListe.add(new Monster(i,j, fenster, 0)); break;
			// Monster, welche erst nach dem Aufheben des Schluessels erscheinen
			case 3: karte[i][j] = new Boden(); fenster.monsterListe.add(new Monster(i,j, fenster, 2)); break;
			case 8: karte[i][j] = new Boden(); fenster.monsterListe.add(new Monster(i,j, fenster, 1)); break;
		}	
				
			}
		}
		}
	
	public Spielelement[][] getLevel(){
		return karte;
	}
}
