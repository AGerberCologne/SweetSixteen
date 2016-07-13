package pp2016.team16.shared;

import java.io.Serializable;

/**
 * Message wird an Server gesendet, wenn man auf beenden geht
 * 
 * @author Gerber, Alina, 5961246
 *
 */

public class BeendeMessage extends MessageObject implements Serializable {

	/**
	 * zufällig generierte serialVersionUID
	 */
	private static final long serialVersionUID = -7186794407840374162L;

	public int level;

	/**
	 * Diese Nachricht wird initialisiert, wenn der Spieler das Spiel beenden
	 * will
	 * 
	 * @param level
	 *            übergibt das aktuelle Level auf dem sich der Spieler befindet
	 *            damit das Level noch gespeichert wird, bevor der Server
	 *            schliesst
	 * @author Gerber, Alina, 5961246
	 */
	public BeendeMessage(int level) {
		this.level = level;
	}

}
