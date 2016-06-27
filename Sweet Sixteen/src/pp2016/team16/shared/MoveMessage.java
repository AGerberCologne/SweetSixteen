package pp2016.team16.shared;

public class MoveMessage extends MessageObject {
	int[] posAlt;
	int[] posNeu = new int[16];
	boolean validerZug;
	
	void ueberschreibe(MoveMessage message){
		this.posAlt =  message.posAlt;
		this.posNeu = message.posNeu;
	}
	
	
	
}
