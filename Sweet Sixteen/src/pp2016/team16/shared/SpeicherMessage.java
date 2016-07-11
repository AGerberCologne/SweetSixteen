package pp2016.team16.shared;

import java.io.Serializable;

public class SpeicherMessage extends MessageObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7942172586756718808L;
	
	public int level;
	
	public SpeicherMessage (int level){
		this.level=level;
	}

}
