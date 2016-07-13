package pp2016.team16.shared;
/**
 * Diese Message wird vom Server verschickt, wenn das Monster den Spieler angreift
 * @author Gerber, Alina, 5961246
 *
 */
public class MAngriffMessage extends MessageObject {

	private static final long serialVersionUID = 8632275677807677032L;
	
	public int monsternummer;
	/**
	 * In den Konstruktor wird die identifizierende Nummer des Monsters uebergeben
	 * @param monsternummer,um das angreifende Monster zu identifizieren
	 * @author Gerber, Alina, 5961246
	 */
	public MAngriffMessage( int monsternummer){
		this.monsternummer=monsternummer;
	}

}
