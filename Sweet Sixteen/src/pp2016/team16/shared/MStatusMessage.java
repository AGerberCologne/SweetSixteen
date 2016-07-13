package pp2016.team16.shared;
/**
 * Diese Nachricht wird vom Server verschickt, wenn sich die Lebenshoehe eines Monsters ändert(positiv oder negativ),
 * oder das Monster stirbt
 * @author Alina Gerber, 5960246
 *
 */
public class MStatusMessage extends MessageObject {


	private static final long serialVersionUID = 2675680805121417259L;
	
	public int monsternummer;
	public boolean tot;
	public boolean heilen;
	/**
	 * 
	 * @param monsternummer, um das Monster zu identifizieren
	 * @param tot boolean, um zu ueberpruefen ob das monster tot ist
	 * @param heilen, boolean , um zu ueberpruefen ob das monster geheilt werden soll
	 * @author Alina Gerber, 5961246
	 */
	public MStatusMessage(int monsternummer, boolean tot, boolean heilen){
		this.monsternummer=monsternummer;
		this.tot=tot;
		this.heilen = heilen;
	}

}
