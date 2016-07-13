package pp2016.team16.shared;

import pp2016.team16.client.gui.LoginDialog;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Klasse mit den Eigenschaften des Spielers, die nicht direkt in der Engine
 * angegeben sind @ uebernommen aus dem alten Spiel
 *
 */
public class Spieler extends Figur {

	private String name;
	private String passwort;
	private boolean hatSchluessel;
	public int anzahlHeiltraenke;
	private int heiltrankWirkung;

	public int zielX = 0;
	public int zielY = 0;

	public long letzterSchritt = 0;

	public Spieler(String imgDatei) {

		setAnzahlHeiltraenke(0);
		setPos(0, 0);
		setHealth(100);
		setMaxHealth(getHealth());

		if (LoginDialog.succeeded) {
			setName(LoginDialog.getUsername());
		} else {
			setName("Hindi");
		}

		// Bild fuer den Spieler laden
		try {
			setImage(ImageIO.read(new File(imgDatei)));
		} catch (IOException e) {
			System.err.print("Das Bild " + imgDatei
					+ " konnte nicht geladen werden.");
		}
	}

	/**
	 * Methode, um den Schluessel aufzuheben
	 */
	public void nimmSchluessel() {
		hatSchluessel = true;
	}

	/**
	 * Methode, um den Schluessel zu entfernen
	 */
	public void entferneSchluessel() {
		hatSchluessel = false;
	}

	/**
	 * Methode, um den Heiltrank zu benutzen
	 * 
	 * @return heiltrankWirkung, also die hoehe der Wirkung
	 */
	public int benutzeHeiltrank() {
		setAnzahlHeiltraenke(anzahlHeiltraenke - 1);
		return heiltrankWirkung;
	}

	/**
	 * Methode um einen Heiltrank aufzuheben
	 * 
	 * @param Heiltrank
	 *            (ermittelt ueber Spielelement[][]
	 */
	public void nimmHeiltrank(Heiltrank t) {
		anzahlHeiltraenke++;
		heiltrankWirkung = t.getWirkung();

	}

	/**
	 * Methode um die Hoehe der Heiltraenke zu erhoehen
	 * 
	 * @param anzahl
	 *            der Heiltraenke ingesamt
	 */
	public void setAnzahlHeiltraenke(int anzahl) {
		if (anzahl >= 0)
			anzahlHeiltraenke = anzahl;
	}

	/**
	 * Methode die die Anzahl der Heiltraenke zurueck gibt
	 * 
	 * @return die Gesamtanzahl an Traenken
	 */
	public int getAnzahlHeiltraenke() {
		return anzahlHeiltraenke;
	}

	/**
	 * Methode um abzufargen ob der Spieler den Schluessel besitzt
	 * 
	 * @return gibt einen boolean zurueck
	 */
	public boolean hatSchluessel() {
		return hatSchluessel;
	}

	// Getter

	public String getName() {

		return name;

	}

	public String getPasswort() {

		return passwort;

	}

	// Setter
	public void setName(String name) {
		this.name = name;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

}
