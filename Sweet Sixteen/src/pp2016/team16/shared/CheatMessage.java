package pp2016.team16.shared;

/**
 * Message, die versendet wird, wenn man einen Cheat machen will. Diese wird an
 * den Client zurueck gesendet, wenn die durch die Message ausgelueste Aktion
 * erfolgreich war im Server.
 * 
 * @author Gerber, Alina, 5961246
 *
 */
public class CheatMessage extends MessageObject {
	/**
	 * zufaellig generierte serialVersionUID
	 */
	private static final long serialVersionUID = 7139911893023813591L;
	public int i;

	/**
	 * 
	 * @param i
	 *            Art des Cheats: 1 = Leben erhoehen, 2= zufaelliges Monster
	 *            schwaechen
	 */
	public CheatMessage(int i) {
		this.i = i;
	}
}
