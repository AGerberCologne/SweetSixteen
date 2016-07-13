package pp2016.team16.shared;

/**
 * Diese Message wird an den Server gesendet, wenn man die Leertaste drückt.
 * Entweder wird dann ein Heiltrank aufgehoben, der Schlüssel aufgenommen, der
 * Schlüssel benutzt. War die daraus ausgelöste Aktion im Server erfolgreich,
 * wird eine Nahricht des gleichen Messagtypen wieder zurückgeschickt
 * 
 * @author Alina Gerber 5961246
 *
 */
public class LeertasteMessage extends MessageObject {

	/**
	 * zufällig generierte serialVersionUID
	 */
	private static final long serialVersionUID = 3212312797427051370L;

	/**
	 * art steht für das was passieren soll: 0=Heiltränke aufnehmen, 1 =
	 * Schlüssel aufnehmen, 2= Schlüssel wurde benutzt , 3= nichts passiert
	 */
	public int art;

}
