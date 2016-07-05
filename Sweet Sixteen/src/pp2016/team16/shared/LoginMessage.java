package pp2016.team16.shared;

public class LoginMessage extends MessageObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8224722058770817482L;
	public String name;
	public String passwort;
	public int artVonAnmeldung;
	
	public LoginMessage(int artVonAnmeldung,String name, String passwort){
		this.name = name;
		this.passwort = passwort;
		this.artVonAnmeldung=artVonAnmeldung;
		
	}

}
