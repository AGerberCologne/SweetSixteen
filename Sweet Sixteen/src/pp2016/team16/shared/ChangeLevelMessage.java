package pp2016.team16.shared;

public class ChangeLevelMessage extends MessageObject {
	int[][] map;
	boolean login = false;
	
	public ChangeLevelMessage(){
		this.map = new int[16][16];
	}
	
	public ChangeLevelMessage(int i){
		this.login = true;
	}
	
	void  ueberschreibe(ChangeLevelMessage message){
		this.login = message.login;
	}

	

}
