package pp2016.team16.shared;

/**
 * Message, die versendet wird, wenn man einen Cheat machen will. Diese wird an
 * den Client zurück gesendet, wenn die durch die Messaeg ausgelöste Aktion
 * erfolgreich war im Server.
 * 
 * @author Alina Gerber 5961246
 *
 */
public class CheatMessage extends MessageObject {
	/**
	 * zufällig generierte serialVersionUID
	 */
	private static final long serialVersionUID = 7139911893023813591L;
	public int i;

	/**
	 * 
	 * @param i
	 *            Art des Cheats: 1 = Leben erhöhen, 2= zufälliges Monster
	 *            schwächen
	 */
	public CheatMessage(int i) {
		this.i = i;
	}
}
