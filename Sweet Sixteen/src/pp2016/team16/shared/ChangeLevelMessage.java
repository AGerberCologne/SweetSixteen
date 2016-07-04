package pp2016.team16.shared;

public class ChangeLevelMessage extends MessageObject {
	public int[][] map;
	public int levelzaehler;
	public boolean login = false;
	
	public ChangeLevelMessage(){
		this.map = new int[21][21];
	}
	
	public ChangeLevelMessage(int i){
		this.login = true;
	}
	
	void  ueberschreibe(ChangeLevelMessage message){
		this.login = message.login;
		this.map = message.map;
		this.levelzaehler =message.levelzaehler;
	}

	

}
