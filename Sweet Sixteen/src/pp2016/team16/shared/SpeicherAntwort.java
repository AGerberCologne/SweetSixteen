package pp2016.team16.shared;

import java.io.Serializable;

public class SpeicherAntwort extends MessageObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4945949626428426953L;
	
	public boolean hatGeklappt;
	
	public SpeicherAntwort (boolean hatGeklappt){
		this.hatGeklappt=hatGeklappt;
	}

}
