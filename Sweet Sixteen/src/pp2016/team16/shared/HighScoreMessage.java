package pp2016.team16.shared;

import java.io.Serializable;

public class HighScoreMessage extends MessageObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 883549027227066591L;
	
	public String zeile;
	
	public HighScoreMessage (String zeile){
		this.zeile=zeile;
	}
}
