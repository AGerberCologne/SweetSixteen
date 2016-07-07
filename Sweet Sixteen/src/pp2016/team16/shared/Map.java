package pp2016.team16.shared;

public class Map {
	public int hoehe;
	public int breite;
	public Konstanten konstante = new Konstanten();
	public int[][] level = new int[konstante.HEIGHT][konstante.WIDTH];
	public int levelzaehler = 1;
	public Spielelement [][] karte = new Spielelement[konstante.HEIGHT][konstante.WIDTH];

}
