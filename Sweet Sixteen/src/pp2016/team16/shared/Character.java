package pp2016.team16.shared;

public class Character {

	public final int ID;
	
	public int[][] level;
	public int life;	
	public int strength;
	public int attack;
	public int defence;
	public int[] position = new int[16];   // [x] = y;
	public int levelID;											
	public int experience;
	public int picID=-1;
	
	public Character(int id) {
		this.ID = id;
		this.life = 100;
	}
	//Setter
	public void setlevel(int[][] level){
		this.level=level;
	}
	
	public void setlife(int life){
		this.life=life;
	}
	
	public void setstrength(int strength){
		this.strength=strength;
	}

	public void setPosition(int[] position) {
		this.position = position;
	}
	
	public void setLevelID(int levelID){
		this.levelID = levelID;
	}
	
	//Getter
	public int[][] getlevel(){
		return level;
	}
	
	public int getlife(){
		return life;
	}
		
	public int getstrength(){
		return strength;
	}
	public int[] getPosition() {
		return position;
	}
	
	public int getlevelID(){
		return levelID;
	}
	
}
