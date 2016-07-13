package pp2016.team16.shared;

/**
 * Diese Message wird an den Server gesendet, wenn man die Leertaste dr�ckt.
 * Entweder wird dann ein Heiltrank aufgehoben, der Schl�ssel aufgenommen, der
 * Schl�ssel benutzt. War die daraus ausgel�ste Aktion im Server erfolgreich,
 * wird eine Nahricht des gleichen Messagtypen wieder zur�ckgeschickt
 * 
 * @author Alina Gerber 5961246
 *
 */
public class LeertasteMessage extends MessageObject {

	/**
	 * zuf�llig generierte serialVersionUID
	 */
	private static final long serialVersionUID = 3212312797427051370L;

	/**
	 * art steht f�r das was passieren soll: 0=Heiltr�nke aufnehmen, 1 =
	 * Schl�ssel aufnehmen, 2= Schl�ssel wurde benutzt , 3= nichts passiert
	 */
	public int art;

}
