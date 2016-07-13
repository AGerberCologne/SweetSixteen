package pp2016.team16.shared;

/**
 * Diese Message wird an den Server gesendet, wenn man die Leertaste drueckt.
 * Entweder wird dann ein Heiltrank aufgehoben, der Schluessel aufgenommen, der
 * Schluessel benutzt. War die daraus ausgeloeste Aktion im Server erfolgreich,
 * wird eine Nahricht des gleichen Messagtypen wieder zurueckgeschickt
 * 
 * @author Alina Gerber 5961246
 *
 */
public class LeertasteMessage extends MessageObject {

	/**
	 * zufaellig generierte serialVersionUID
	 */
	private static final long serialVersionUID = 3212312797427051370L;

	/**
	 * art steht für das was passieren soll: 0=Heiltraenke aufnehmen, 1 =
	 * Schluessel aufnehmen, 2= Schluessel wurde benutzt , 3= nichts passiert
	 */
	public int art;

}
