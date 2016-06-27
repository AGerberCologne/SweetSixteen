package pp2016.team16.shared;

public class LoginAnswerMessage extends MessageObject{
	int success; // 0 = Login war nicht erfolgreich, 1 = Login war erfolgreich
	int CharacterID;
	int[] startposition = new int[16] ; // Positionen wird durch [x] = y angegeben;
	public LoginAnswerMessage(){
	}
	public String toString(){
    if( this.success ==1){
    	return "Sie wurden erfolgreich eingeloggt \n ";
    }else return "Sie konnten nicht eingeloggt werden \n";
	}
}
