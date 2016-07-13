package pp2016.team16.shared;

/**
 * Diese Nachricht wird an Server verschickt, wenn der Spieler einer
 * Bewegungsanfrage verschickt und wird an den Client zurueck geschickt mit der
 * naechsten neuen Position
 * 
 * @author Alina Gerber, 5961246
 *
 */
public class SBewegungMessage extends MessageObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5289293065587642357L;
	public int altX;
	public int altY;
	public int neuX;
	public int neuY;
	public int richtung;
}
