package pp2016.team16.shared;

/**
 * Diese Nachricht wird erstellt, wenn ein Level geladen werden soll, sei es
 * durch neues Spiel starten, anmelden oder passieren der Tuer. Diese
 * Nachrichtenart bekommt der Client dann auch vom Server zurück, nur mit
 * aktualisierten Daten.
 * 
 * 
 * @author Gerber, Alina, 5961246
 *
 */
public class ChangeLevelMessage extends MessageObject {

	/**
	 * zufaellig generiete serialVersionUID
	 */
	private static final long serialVersionUID = 7268833034090983192L;
	/**
	 * level wird als String uebergeben
	 */
	public int[][] level = new int[21][21];
	/**
	 * das level wird erstmal auf null gesetzt, im Server aber auf da Level des
	 * Spielers gesetzt
	 */
	public int levelzaehler = 0;

}
