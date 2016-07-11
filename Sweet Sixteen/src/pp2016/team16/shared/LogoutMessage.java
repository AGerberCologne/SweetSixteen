package pp2016.team16.shared;

public class LogoutMessage extends MessageObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3152951812997307951L;
	int messageID = 1;
	public int level;
	
	public LogoutMessage(int level){
		this.level=level;
	}
}
