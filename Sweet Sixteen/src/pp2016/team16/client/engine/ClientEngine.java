package pp2016.team16.client.engine;

import java.util.*;

import pp2016.team16.shared.*;
import pp2016.team16.shared.Map;
import pp2016.team16.client.comm.ClientComm;
import pp2016.team16.shared.Heiltrank;

/**
 * In der Klasse werden die "Spielmatrix" verwaltet:
 * 
 * @author Gerber, Alina, 5961246
 */
public class ClientEngine extends Thread {
	public ClientComm client;
	public Konstanten konstante = new Konstanten();
	public Map spielfeld = new Map();
	public Spieler spieler = new Spieler("img//spieler.png");
	public LinkedList<Monster> monsterListe = new LinkedList<Monster>();
	public LinkedList<String> highscore = new LinkedList<String>();

	/**
	 * @param Gibt
	 *            an ob der Spieler eingeloggt wurde
	 */
	public boolean eingeloggt = false;
	/**
	 * @param Parameter
	 *            , der angibt ob es eine Login-Antwort vom Server gekommen ist
	 */
	public boolean login = false;
	/**
	 * @param Parameter
	 *            , der angibt ob eine Level-Nachricht vom Server gekommen ist
	 */
	public boolean neuesLevel = false;
	/**
	 * @param Wird
	 *            gebraucht fuer das versenden von Leertasten-Messages 0 oder 1
	 *            = wenn man den Gegenstand aufgehoben hat oder auf einem Feld
	 *            ohne Item steht, 2 = ein neues Level muss geladen, 3 = das
	 *            Spiel wird beende 4 = warte auf Server-Antwort
	 */
	public int itemBenutzen = 4;
	public boolean gespeichert = false;

	/**
	 * Der Konstruktor initialisiert die clientseitige Kommunikation und startet
	 * den Thread. ClientEngine enthält die "Spielmatrix".
	 * 
	 * @author Gerber,Alina, 5961246
	 */
	public ClientEngine() {
		System.out.println("Starte Client");
		client = new ClientComm();
		this.start();

	}

	/**
	 * Ueberschreiben der run-Methode des Threads; diese laeuft solange wie
	 * client nicht beendet ist. Die Methode horcht nach neuen Nachrichten vom
	 * Server und versucht diese zu bearbeiten.
	 * 
	 * @author Gerber, Alina, 5961246
	 */
	public void run() {
		while (client.clientOpen) {
			MessageObject m = client.gebeWeiterAnClient();
			if (m == null) {
				System.out.println("Test 2");
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					this.nachrichtVerarbeiten(m);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		this.interrupt();
	}

	// Nachrichtenverarbeiten
	/**
	 * Die Nachricht verarbeitet die an den Client gesendeten Nachrichten.
	 * 
	 * @param eingehendeNachricht
	 *            eingehende Nachricht
	 * @throws Exception
	 * 
	 * @author Gerber, Alina, 5961246
	 */
	void nachrichtVerarbeiten(MessageObject eingehendeNachricht)
			throws Exception {
		// Fall: Login
		if (eingehendeNachricht instanceof LoginAnswerMessage) {
			LoginAnswerMessage daten = (LoginAnswerMessage) eingehendeNachricht;
			spielfeld.level = daten.map;
			spielfeld.levelzaehler = daten.levelzaehler;
			spieler.setName(daten.name);
			spieler.setPasswort(daten.passwort);
			this.eingeloggt = daten.eingeloggt;
			System.out.println("Login wurde empfangen");
			this.login = true;

			// Fall : Wechseln des Levels/Neues Spielfeld
		} else if (eingehendeNachricht instanceof ChangeLevelMessage) {
			ChangeLevelMessage daten = (ChangeLevelMessage) eingehendeNachricht;
			spielfeld.breite = konstante.WIDTH;
			spielfeld.hoehe = konstante.HEIGHT;
			while (!monsterListe.isEmpty()) {
				monsterListe.remove();
			}
			// wurde mehr oder weniger aus dem alten Spiel ubernommen
			for (int i = 0; i < daten.level.length; i++) {
				for (int j = 0; j < daten.level.length; j++) {
					int Variable = daten.level[i][j];
					switch (Variable) {
					case 0:
						spielfeld.karte[i][j] = new Wand();
						break;
					case 1:
						spielfeld.karte[i][j] = new Boden();
						break;
					case 5:
						spielfeld.karte[i][j] = new Heiltrank(20);
						break;
					case 6:
						spielfeld.karte[i][j] = new Tuer(false);
						break;
					case 4:
						spielfeld.karte[i][j] = new Tuer(true);
						this.spieler.setPos(i, j);
						break;
					case 2:
						spielfeld.karte[i][j] = new Boden();
						this.monsterListe.add(new Monster(i, j, 0));
						break;
					// Monster, welche erst nach dem Aufheben des Schluessels
					// erscheinen
					case 3:
						spielfeld.karte[i][j] = new Boden();
						this.monsterListe.add(new Monster(i, j, 2));
						break;
					case 8:
						spielfeld.karte[i][j] = new Boden();
						this.monsterListe.add(new Monster(i, j, 1));
						break;
					}

				}

			}

			spielfeld.levelzaehler = daten.levelzaehler;
			this.neuesLevel = true;
			System.out.println("Neues Level gespeichert");

			// Fall: Spieler-Bewegung
		} else if (eingehendeNachricht instanceof SBewegungMessage) {
			System.out.println("Neue Position");
			SBewegungMessage daten = (SBewegungMessage) eingehendeNachricht;
			this.spieler.setPos(daten.neuX, daten.neuY);

			// Fall: Monster-Bewegung
		} else if (eingehendeNachricht instanceof MBewegungMessage) {
			try {
				System.out.println("Monster-Bewegung");

				int richtung = ((MBewegungMessage) eingehendeNachricht).richtung;
				Monster m = monsterListe
						.get(((MBewegungMessage) eingehendeNachricht).monsterNummer);
				System.out
						.println(((MBewegungMessage) eingehendeNachricht).monsterNummer
								+ "  " + richtung);
				switch (richtung) {
				case 0:
					m.hoch();
					break;
				case 1:
					m.rechts();
					break;
				case 2:
					m.runter();
					break;
				case 3:
					m.links();
					break;
				}
			} catch (Exception e) {

			}

			// Fall : Antwort auf das Druecken der LeerTaste
		} else if (eingehendeNachricht instanceof LeertasteMessage) {
			// Schluessel aufnehmen
			if (((LeertasteMessage) eingehendeNachricht).art == 1) {
				spieler.nimmSchluessel();
				spielfeld.karte[spieler.getXPos()][spieler.getYPos()] = new Boden();
				this.itemBenutzen = 1;

				spieler.nimmSchluessel();
			}
			// HeilTrank aufnehmen
			else if (((LeertasteMessage) eingehendeNachricht).art == 0) {
				spieler.nimmHeiltrank((Heiltrank) spielfeld.karte[spieler.getXPos()][spieler
						.getYPos()]);
				spielfeld.karte[spieler.getXPos()][spieler.getYPos()] = new Boden();
				this.itemBenutzen = 1;
			}
			// Schluessel benutzen
			else if (((LeertasteMessage) eingehendeNachricht).art == 2) {
				if (!((Tuer) spielfeld.karte[spieler.getXPos()][spieler.getYPos()])
						.istOffen() && spieler.hatSchluessel()) {
					((Tuer) spielfeld.karte[spieler.getXPos()][spieler.getYPos()])
							.setOffen();
					// Nach dem Oeffnen der Tuer ist der Schluessel wieder weg
					spieler.entferneSchluessel();
					if (spielfeld.levelzaehler <= konstante.MAXLEVEL) {
						this.itemBenutzen = 2;
					} else
						this.itemBenutzen = 3;
				}
			} else if (((LeertasteMessage) eingehendeNachricht).art == 3) {
				this.itemBenutzen = 1;
			}
			// Fall: Antwort auf das druecken der B-Taste
		} else if (eingehendeNachricht instanceof BTasteMessage) {
			if (spieler.anzahlHeiltraenke > 0) {
				int change = spieler.benutzeHeiltrank();
				// Heilungseffekt wird verbessert, falls neue Monster durch das
				// Aufheben des Schlüssels ausgelöst wurden
				if (spieler.hatSchluessel())
					spieler.changeHealth((int) (change * 1.5));
				else
					spieler.changeHealth((int) (change * 0.5));
			}

			// Fall: Monster greift an
		} else if (eingehendeNachricht instanceof MAngriffMessage) {
			Monster m = monsterListe
					.get(((MAngriffMessage) eingehendeNachricht).monsternummer);
			spieler.changeHealth(-konstante.BOX / 8);
			m.angriff = true;

			// Fall: Monster-Leben haben scih veraendert
		} else if (eingehendeNachricht instanceof MStatusMessage) {
			MStatusMessage daten = (MStatusMessage) eingehendeNachricht;
			System.out.println("Statusmessage kommt an");
			int j = daten.monsternummer;
			boolean mtot = daten.tot;
			boolean heilen = daten.heilen;
			Monster m = monsterListe.get(j);
			if (mtot == true) {
				if (m.getTyp() == 2) {
					this.spielfeld.karte[m.getXPos()][m.getYPos()] = new Schluessel();
				} else {
					this.spielfeld.karte[m.getXPos()][m.getYPos()] = new Heiltrank(30);
				}
				monsterListe.remove(j);
			} else if (mtot == false && heilen == false)
				m.changeHealth(-(konstante.BOX / 4));
			else if (mtot == false && heilen == true)
				m.changeHealth(konstante.BOX / 8);

			// Fall : Speichern des Highscores in Highscore in einer liked List,
			// auf die HindiBones zugreifen kann
		} else if (eingehendeNachricht instanceof HighScoreMessage) {
			HighScoreMessage daten = (HighScoreMessage) eingehendeNachricht;
			String n = daten.zeile;
			highscore.add(n);
			System.out.println("HighScore wird gelesen in Liste:" + n);

		}

		// Fall : Cheat- Antowrt
		else if (eingehendeNachricht instanceof CheatMessage) {
			int i = ((CheatMessage) eingehendeNachricht).i;
			switch (i) {
			case 1:
				spieler.changeHealth(konstante.BOX / 4);
				break;
			case 2:
				int zufallszahl;
				zufallszahl = (int) (Math.random() * monsterListe.size());
				Monster m = monsterListe.get(zufallszahl);
				m.changeHealth(-konstante.BOX / 2);
				break;
			}
		}
	}

	// Methoden fuer GUI
	/**
	 * Die Methode verschickt eine Login Anfrage an den Server
	 * 
	 * @param gibt
	 *            die Art der Anmeldung an : 1 = neu anmelden; 2 = login
	 * @param n
	 *            entspricht dem Namen
	 * @param p
	 *            entspricht dem Passwort
	 * @return gibt zurueck ob der login erfolgreich war
	 * @throws InterruptedException
	 * 
	 * @author Gerber, Alina, 5961246
	 */
	public boolean login(int i, String n, String p) throws InterruptedException {
		LoginMessage anfrage = new LoginMessage(i, n, p);
		client.bekommeVonClient(anfrage);
		while (login == false) {
			System.out.println("Warte auf Login Antwort");
			sleep(600);
		}
		this.login = false;
		return eingeloggt;
	}

	/**
	 * Die Methode schickt eine Benutzer Wechsel Nachricht an den Server
	 * 
	 * @param level
	 *            gibt an in welchem Level sich der Spieler gerade befindet
	 * @throws Exception
	 * @author Gerber, Alina, 5961246
	 */
	public void logout(int level) throws Exception {
		LogoutMessage anfrage = new LogoutMessage(level);
		client.bekommeVonClient(anfrage);
	}

	/**
	 * Die Methode verschickt eine Beende-Nachricht
	 * 
	 * @param level
	 *            gibt an in welchem Level sich der Spieler gerade befindet
	 * @author Gerber, Alina, 5961246
	 */
	public void beende(int level) {
		BeendeMessage bm = new BeendeMessage(level);
		client.bekommeVonClient(bm);

	}

	/**
	 * Die Methode verschickt eine Bewegungsanfrage des Spielers. Sollte der
	 * Spieler auf eine Wand geklickt haben wird dies hier direkt abgefangen.
	 * 
	 * @param x
	 *            ist die x-Koordinate der ZielPosition
	 * @param y
	 *            ist die y-Koordinate der ZielPosition
	 * @throws InterruptedException
	 * @author Gerber, Alina, 5961246
	 */
	public void wegAnfragen(int x, int y) throws InterruptedException {
		System.out.println("Der Spieler moechte sich bewegen");
		if (!(spielfeld.karte[x][y] instanceof Wand)) {
			spieler.zielX = x;
			spieler.zielY = y;
			SBewegungMessage anfrage = new SBewegungMessage();
			anfrage.altX = spieler.getXPos();
			anfrage.altY = spieler.getYPos();
			anfrage.neuX = spieler.zielX;
			anfrage.neuY = spieler.zielY;
			client.bekommeVonClient(anfrage);
			sleep(600);
		} else
			System.out.println("Du versuchst auf eine Wand zu gehen");
	}

	/**
	 * Die Methode verschickt eine Level-Anfrage an den Server , wartet auf eine
	 * Antwort und kann das erhaltenen Spielelement zurueck
	 * 
	 * @return gibt das erhaltene Level in Form von Spielelement[][] zurueck
	 * @throws Exception
	 * 
	 * @author Gerber, Alina, 5961246
	 */
	public Spielelement[][] changeLevel() throws Exception {
		System.out.println("Der Client fragt ein neues Level an");
		ChangeLevelMessage anfrage = new ChangeLevelMessage();
		anfrage.levelzaehler = this.spielfeld.levelzaehler;
		client.bekommeVonClient(anfrage);
		while (this.neuesLevel == false) {
			System.out.println("Neues Level wurde noch nicht geladen");
			sleep(600);
		}
		this.neuesLevel = false;
		System.out.println("Endlich geschafft");
		return spielfeld.karte;
	}

	/**
	 * Die Methode verschickt eine Leertasten Message und wartet bis es eine
	 * Antwort vom Server gibt
	 * 
	 * @return gibt 0 oder 1 = wenn man den Gegenstand aufgehoben hat oder auf
	 *         einem Feld ohne Item steht, 2 = ein neues Level muss geladen, 3 =
	 *         das Spiel wird beendet
	 * @throws InterruptedException
	 * 
	 * @author Gerber, Alina, 5961246
	 */
	public int benutzeItem() throws InterruptedException {
		itemBenutzen = 4;
		LeertasteMessage anfrage = new LeertasteMessage();
		client.bekommeVonClient(anfrage);
		while (itemBenutzen == 4) {
			sleep(600);
		}
		return itemBenutzen;
	}

	/**
	 * Die Methode wird aufgerufen, wenn ein Trank benutzt werden soll
	 * 
	 * @author Gerber, Alina, 5961246
	 */
	public void trankBenutzen() {
		BTasteMessage anfrage = new BTasteMessage();
		client.bekommeVonClient(anfrage);
	}

	/**
	 * Die Methode verschickt eine Angriff- Nachricht des Spielers
	 * 
	 * @author Gerber, Alina, 5961246
	 */
	public void angriffSpieler() {
		SAngriffMessage anfrage = new SAngriffMessage();
		client.bekommeVonClient(anfrage);
	}

	
	/**
	 * Die Methode verschickt eine Cheat-Anfrage an den Server
	 * 
	 * @param i
	 *            = 1 = leben erhoehen, 2 = bei einem zufaelligen Monster wird die
	 *            hälfte der Leben weg genommen
	 * @throws Exception
	 * @author Gerber, Alina, 5961246
	 */
	public void cheatBenutzen(int i) throws Exception {
		CheatMessage anfrage = new CheatMessage(i);
		client.bekommeVonClient(anfrage);
	}

	/**
	 * Die Methode verschickt eine Speicher-Anfrage an den Server
	 * 
	 * @param levelhoehe
	 *            die gespeichert werden soll
	 * @author Gerber, Alina, 5961246
	 */
	public void speichereLevel(int level) {
		SpeicherMessage anfrage = new SpeicherMessage(level);
		System.out.println("Versuche zu speichern");
		client.bekommeVonClient(anfrage);
	}

	/**
	 * Die Methode verschickt eine Highscore-Anfrage an den Server
	 * 
	 * @author Hartmann, Ann-Catherine, 6038514
	 */
	public void schickeHighScore() {
		HighScoreAnfrageMessage anfrage = new HighScoreAnfrageMessage();
		System.out.println("HighScore wird bei server angefragt");
		client.bekommeVonClient(anfrage);
		try {
			sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param name
	 *            gibt den Namen des Spielers an
	 * @param zeit
	 *            gibt die benoetigte Zeit an
	 * @author Hartmann, Ann-Catherine, 6038514
	 */
	public void fuegeZuHighScorehinzu(String name, int zeit) {
		SetzeHighScoreMessage anfrage = new SetzeHighScoreMessage(name, zeit);
		client.bekommeVonClient(anfrage);
	}
}