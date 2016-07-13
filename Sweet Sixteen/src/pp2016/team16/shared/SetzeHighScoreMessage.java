package pp2016.team16.shared;

import java.io.Serializable;

/**
 * 
 * Diese Nachricht wird an den Server gesendet, falls der Spieler Level 5
 * erfolgreich gemeistert hat und im Server wird der Spieler dann in die
 * HighScore-Textdatei eingetragen mit seiner Zeit
 * 
 * @author Gerber, Alina, 5961246
 *
 */
public class SetzeHighScoreMessage extends MessageObject implements
		Serializable {

	/**
	 * zufällig generierte serialVersionUID
	 */
	private static final long serialVersionUID = -2803999198497154935L;

	public String name;
	public int zeit;

	/**
	 * Fordert beim Server an, dass die Zeit und der Name des Spielers in die
	 * HighScore-Datei eingefuegt werden
	 * 
	 * @param name
	 *            des Spielers
	 * @param zeit
	 *            des Spielers, die er brauchte für die 5 Level
	 *            
	 * @author Gerber, Alina, 5961246
	 */

	public SetzeHighScoreMessage(String name, int zeit) {
		this.name = name;
		this.zeit = zeit;
	}

}
