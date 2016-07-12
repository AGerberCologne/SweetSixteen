package pp2016.team16.shared;

import java.io.Serializable;

public class SetzeHighScoreMessage extends MessageObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2803999198497154935L;
	
	public String name;
	public int zeit;
	
	public SetzeHighScoreMessage(String name, int zeit){
		this.name=name;
		this.zeit=zeit;
	}

}
