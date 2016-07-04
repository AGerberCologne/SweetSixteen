package pp2016.team16.shared;

public class LoginMessage extends MessageObject{
	public String name;
	public String passwort;
	public int artVonAnmeldung;
	
	public LoginMessage(int artVonAnmeldung,String name, String passwort){
		this.name = name;
		this.passwort = passwort;
		this.artVonAnmeldung=artVonAnmeldung;
		
	}

}
