package pp2016.team16.shared;

public class MAngriffMessage extends MessageObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8632275677807677032L;
	
	public boolean n;
	public int monsternummer;
	
	public MAngriffMessage( int monsternummer){
		this.monsternummer=monsternummer;
	}

}
