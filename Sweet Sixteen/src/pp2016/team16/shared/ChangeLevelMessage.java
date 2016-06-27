package pp2016.team16.shared;

public class ChangeLevelMessage extends MessageObject {
	int[][] map;
	boolean login;
	
	public ChangeLevelMessage(){
		this.map = new int[16][16];
		this.login = false;
	}
	
	public ChangeLevelMessage(int i){
		this.login = true;
	}
	
	void  ueberschreibe(ChangeLevelMessage message){
		this.login = message.login;
	}

	

}
