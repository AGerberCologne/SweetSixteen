package pp2016.team16.shared;

/**
 * Message, die versendet wird, wenn man einen Cheat machen will. Diese wird an
 * den Client zur�ck gesendet, wenn die durch die Messaeg ausgel�ste Aktion
 * erfolgreich war im Server.
 * 
 * @author Alina Gerber 5961246
 *
 */
public class CheatMessage extends MessageObject {
	/**
	 * zuf�llig generierte serialVersionUID
	 */
	private static final long serialVersionUID = 7139911893023813591L;
	public int i;

	/**
	 * 
	 * @param i
	 *            Art des Cheats: 1 = Leben erh�hen, 2= zuf�lliges Monster
	 *            schw�chen
	 */
	public CheatMessage(int i) {
		this.i = i;
	}
}
