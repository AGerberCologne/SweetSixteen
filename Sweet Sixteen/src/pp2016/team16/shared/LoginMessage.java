package pp2016.team16.shared;

/**
 * Diese Message wird gesendet an den Server, wenn man seine Daten im
 * Loginfenster eingegeben und abgesendet hat
 * 
 * @author Alina Gerber 5961246
 *
 */

public class LoginMessage extends MessageObject {
	/**
	 * zufällig generierte serialVersionUID
	 */
	private static final long serialVersionUID = -8224722058770817482L;
	public String name;
	public String passwort;
	public int artVonAnmeldung;

	/**
	 * Diese Message sendet der Client an den Server, wenn sich jemand einloggen
	 * will
	 * 
	 * @param artVonAnmeldung
	 *            1 = neu anmelden 2 = einloggen
	 * @param name
	 *            Eingabe des Spielers in Feld Name
	 * @param passwort
	 *            Eingabe des Spielers in Feld Passwort
	 * @author Alina Gerber 5961246
	 */
	public LoginMessage(int artVonAnmeldung, String name, String passwort) {
		this.name = name;
		this.passwort = passwort;
		this.artVonAnmeldung = artVonAnmeldung;

	}

}
