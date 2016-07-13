package pp2016.team16.shared;

/**
 * Diese Nachricht wird erstellt, wenn ein Level geladen werden soll, sei es
 * durch neues Spiel starten, anmelden oder passieren der Tür. Diese
 * Nachrichtenart bekommt der Client dann auch vom Server zurück, nur mit
 * aktualisierten Daten.
 * 
 * 
 * @author Alina Gerber
 *
 */
public class ChangeLevelMessage extends MessageObject {

	/**
	 * zufällig generiete serialVersionUID
	 */
	private static final long serialVersionUID = 7268833034090983192L;
	/**
	 * level wird als String übergeben
	 */
	public int[][] level = new int[21][21];
	/**
	 * das level wird erstmal auf null gesetzt, im Server aber auf da Level des
	 * Spielers gesetzt
	 */
	public int levelzaehler = 0;

}
