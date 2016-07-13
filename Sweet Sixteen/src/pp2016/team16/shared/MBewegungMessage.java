package pp2016.team16.shared;
/**
 * Diese Nachricht wird vom Server verschickt, wenn das Monster sich beweget hat und diese Information an den Client uebertragen werden soll
 * @author Alina Gerber , 5961246
 *
 */
public class MBewegungMessage extends MessageObject{

	private static final long serialVersionUID = -8689100631004070639L;
	
	public int richtung;
	public int monsterNummer;

}
