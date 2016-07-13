package pp2016.team16.shared;

import java.io.Serializable;

/**
 * Diese Message wird von der Serverengine geschickt, nachdem einer der ersten 5
 * Plaetze im Highscore als String zeile gespeichert wurden. Diese werden
 * absteigend gelesen im Server.
 * 
 * @author Ann-Catherine Hartmann 6038514
 *
 */

public class HighScoreMessage extends MessageObject implements Serializable {

	/**
	 * zufaellig generierte serialVersionUID
	 */
	private static final long serialVersionUID = 883549027227066591L;

	public String zeile;

	/**
	 * Wird gesendet an den Client, wenn dieser den HighScore anzeigen will.
	 * 
	 * @param zeile
	 *            uebergibt einen String bestehend aus Namen und Zeit eines der
	 *            besten 5 Spieler, die 5 Plaetze werden nacheineander geschickt
	 */
	public HighScoreMessage(String zeile) {
		this.zeile = zeile;
	}
}
