package pp2016.team16.shared;

import java.io.Serializable;

/**
 * Diese Message wird gesendet, wenn der Spieler auf speichern() gegangen ist
 * 
 * @author Alina Gerber 5961246
 *
 */
public class SpeicherMessage extends MessageObject implements Serializable {

	/**
	 * zufällig generierte serailVersionUID
	 */
	private static final long serialVersionUID = -7942172586756718808L;

	public int level;

	/**
	 * Wird gesendet, wenn Spieler sein Level speichern will
	 * 
	 * @param level
	 *            damit wird das aktuelle Level des Spielers an den Server
	 *            uebergeben
	 * @author Alina Gerber 5961246
	 */
	public SpeicherMessage(int level) {
		this.level = level;
	}

}
