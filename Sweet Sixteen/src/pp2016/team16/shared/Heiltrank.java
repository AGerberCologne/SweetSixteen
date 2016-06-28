package pp2016.team16.shared;

public class Heiltrank extends Spielelement {
	private int wirkung;
	
	public Heiltrank(int wirkung){
		this.wirkung = wirkung;
	}
	
	public int getWirkung(){
		return wirkung;
	}
}