package pp2016.team16.shared;

import java.util.LinkedList;

public class ChangeLevelMessage extends MessageObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5031197884475560754L;
	public int[][] level;
	public int levelzaehler;
	public Spielelement [][] map;
	public Spieler spieler;
	public LinkedList<Monster> monsterListe;
	
	public ChangeLevelMessage(){
		this.level = new int[21][21];
	}
	public ChangeLevelMessage(Spieler spieler, LinkedList<Monster> monsterListe, Spielelement[][] map){
		this.spieler = spieler;
		this.monsterListe = monsterListe;
		this.map = map;
	}
	

}
