package pp2016.team16.shared;

public class MStatusMessage extends MessageObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2675680805121417259L;
	
	public int monsternummer;
	public boolean tot;
	public boolean heilen;
	public MStatusMessage(int monsternummer, boolean tot, boolean heilen){
		this.monsternummer=monsternummer;
		this.tot=tot;
		this.heilen = heilen;
	}

}
