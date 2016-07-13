package pp2016.team16.shared;

/**
 * Message wird gesendet, wenn sich der Spieler ummelden will. Dabei wird das
 * Level zum abspeichern mitgeschickt
 * 
 * @author Alina Gerber 5961246
 *
 */
public class LogoutMessage extends MessageObject {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3152951812997307951L;
	int messageID = 1;
	public int level;

	/**
	 * Message wird gesendet, wenn man sich ummelden will
	 * 
	 * @param level
	 *            übergibt das aktuelle Level, damit dieses im Server in der
	 *            Datei Spielerdaten gespeichert werden kann
	 * @author Alina Gerber 5961246
	 */
	public LogoutMessage(int level) {
		this.level = level;
	}
}
