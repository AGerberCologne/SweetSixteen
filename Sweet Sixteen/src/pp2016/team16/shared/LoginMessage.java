package pp2016.team16.shared;

public class LoginMessage extends MessageObject{
	String name;
	String passwort;
	int artVonAnmeldung;
	public LoginMessage(String name, String passwort, int artVonAnmeldung){
		this.name = name;
		this.passwort = passwort;
		this.artVonAnmeldung=artVonAnmeldung;
		
	}

}
