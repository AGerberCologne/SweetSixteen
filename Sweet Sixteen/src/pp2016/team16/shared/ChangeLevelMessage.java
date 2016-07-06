package pp2016.team16.shared;

public class ChangeLevelMessage extends MessageObject {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7268833034090983192L;
	public int[][] level;
	public int levelzaehler = 0;
	//public Spielelement [][] map;
	//public Spieler spieler;
	//public LinkedList<Monster> monsterListe;
	
	public ChangeLevelMessage(){
		this.level = new int[21][21];
	}
	

}
