package pp2016.team16.shared;

/**
 * 
 * Antwort für den Client vom Server, wenn sich jemand neu anmelden/einloggen
 * wollte
 * 
 * @author Alina Gerber 5961246
 *
 */

public class LoginAnswerMessage extends MessageObject {
	/**
	 * zufällig generierte serialVersionUID
	 */
	private static final long serialVersionUID = -4890513527491085157L;
	/**
	 * leeres Level
	 */
	public int[][] map;
	/**
	 * Das Level des Spielers
	 */
	public int levelzaehler;
	/**
	 * gibt den Namen des Spielers zurück
	 */
	public String name;
	/**
	 * Gibt das passwort des Spielers zurück
	 */
	public String passwort;
	/**
	 * gibt true zurück, wenn anmelden oder einloggen funktioniert haben, wenn
	 * Name und/oder Passwort falsch waren beim einloggen oder der Name (und das
	 * Passwort) schon vergeben sind, ist der Wert false
	 */
	public boolean eingeloggt;
}
