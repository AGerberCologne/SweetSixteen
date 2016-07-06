package pp2016.team16.server.map;


import java.util.LinkedList;

import pp2016.team16.client.engine.ClientEngine;
import pp2016.team16.client.gui.HindiBones;
import pp2016.team16.server.engine.ServerEngine;
import pp2016.team16.shared.Boden;
import pp2016.team16.shared.Monster;
import pp2016.team16.shared.Spielelement;
import pp2016.team16.shared.Tuer;
import pp2016.team16.shared.Wand;

/**
 * Der Leser ließt ein array aus und speichert es in einem array des Spielelement
 * @author Simon Nietz, Matr_Nr: 5823560
 *
 */

public class Leser {

	private int [][] dateiname;
	public ServerEngine sengine;
	public ClientEngine cengine;
	public Spielelement[][] karte;
	
	
	/**
	 * @author Simon Nietz, Matr_Nr: 5823560
	 * @param a das übergebene zwei dimensionale Array
	 * @param fenster das HindiBones JFrame
	 */
	
	public Leser(int [][] a, ServerEngine sengine){
		this.sengine = sengine;
		this.dateiname = a;
		
		readLevelS();
	}
	public Leser(int [][] a,ClientEngine cengine){
		this.cengine = cengine;
		this.dateiname = a;
		
		readLevelC();
	}
	
	
	private void readLevelS(){

		// erzeuge  LinkedList
		    Spielelement[][] karte = sengine.map.karte;
			sengine.monsterListe = new LinkedList<Monster>();		
			
	
		// Lese das übergebene Array aus und Speicher es im Datentyp Spielelement
		for(int i=0;i<dateiname.length;i++){
			for(int j=0;j<dateiname.length;j++){
				int Variable = dateiname[i][j];
				switch(Variable){
			case 0: karte[i][j] = new Wand(); break;
			case 1: karte[i][j] = new Boden(); break;
//			case 3: karte[i][j] = new Schluessel(); break;
			case 6: karte[i][j] = new Tuer(false); break;
			case 4: karte[i][j] = new Tuer(true); sengine.spieler.setPos(i, j); break;
			case 2: karte[i][j] = new Boden(); sengine.monsterListe.add(new Monster(i,j, 0)); break;
			// Monster, welche erst nach dem Aufheben des Schluessels erscheinen
			case 3: karte[i][j] = new Boden(); sengine.monsterListe.add(new Monster(i,j, 2)); break;
			case 8: karte[i][j] = new Boden(); sengine.monsterListe.add(new Monster(i,j, 1)); break;
		}	
				
			}
			
		}
		}
	private void readLevelC(){

		// erzeuge zunächste ein Datentyp Spielelement
		// erzeuge  LinkedList
			Spielelement[][] karte  = cengine.map.karte;
			cengine.monsterListe = new LinkedList<Monster>();		
			
	
		// Lese das übergebene Array aus und Speicher es im Datentyp Spielelement
		for(int i=0;i<dateiname.length;i++){
			for(int j=0;j<dateiname.length;j++){
				int Variable = dateiname[i][j];
				switch(Variable){
			case 0: karte[i][j] = new Wand(); break;
			case 1: karte[i][j] = new Boden(); break;
//			case 3: karte[i][j] = new Schluessel(); break;
			case 6: karte[i][j] = new Tuer(false); break;
			case 4: karte[i][j] = new Tuer(true); cengine.spieler.setPos(i, j); break;
			case 2: karte[i][j] = new Boden(); cengine.monsterListe.add(new Monster(i,j, 0)); break;
			// Monster, welche erst nach dem Aufheben des Schluessels erscheinen
			case 3: karte[i][j] = new Boden(); cengine.monsterListe.add(new Monster(i,j, 2)); break;
			case 8: karte[i][j] = new Boden(); cengine.monsterListe.add(new Monster(i,j, 1)); break;
		}	
				
			}
			
		}
		}
	/**
	 * @author Simon Nietz, Matr_Nr: 5823560
	 * @return gebe das Spielelement zurück
	 */
	public Spielelement[][] getLevel(){
		return karte;
	}
}
