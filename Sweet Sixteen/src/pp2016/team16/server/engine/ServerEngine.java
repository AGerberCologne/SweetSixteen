package pp2016.team16.server.engine;

import java.io.*;
import java.util.*;

import pp2016.team16.shared.*;
import pp2016.team16.shared.Map;
import pp2016.team16.server.comm.ServerComm;
import pp2016.team16.server.map.AlleLevel;

/**
 * 
 * @author Alina Gerber 5961246
 * @author Enes Gödak 5615399
 * @author Ann-Catherine Hartmann 6038514
 *
 */

public class ServerEngine extends Thread {

	public ServerComm server;
	public Map map = new Map();
	public Spieler spieler = new Spieler("img//spieler.png");
	public LinkedList<Monster> monsterListe = new LinkedList<Monster>();
	public Konstanten konstante = new Konstanten();

	/**
	 * enthält den Namen des aktuell eingeloggten Spielers
	 */

	public String spielername;
	/**
	 * enthält das Passwort des aktuell eingeloggten Spielers
	 */
	public String spielerpasswort;

	public int monsternr;

	/**
	 * @param
	 */
	public boolean eingeloggt;
	/**
	 * @param enthaelt
	 *            die Listen mit x-Positionen und y-Positionen des Spielers, die
	 *            berechnet wurden
	 */
	public Astern astern;
	/**
	 * @param gibt
	 *            an ob das Monster gestorben ist
	 */
	public boolean monstertot;
	/**
	 * @param gibt
	 *            an wie oft ein Cheat in diesem Level veraendert wurde
	 */
	public int cheatZaehler;

	/**
	 * Der Konstruktor initialisiert die serverseitige Kommunikation und startet
	 * den Thread. Die Serverengine enthält die "Spielmatrix".
	 * 
	 * @author Alina Gerber, 5961246
	 * 
	 */
	public ServerEngine() {
		server = new ServerComm();
		this.start();
		System.out.println("Starte Server");

	}

	/**
	 * Ueberschreiben der run-Methode des Threads; diese läuft solange wie
	 * server nicht beendet ist. Die Methode horcht nach neuen Nachrichten vom
	 * Client und versucht diese zu bearbeiten. Außerdem sendet sie periodisch
	 * die Monsterbwegungen.
	 * 
	 * @author Alina Gerber, 5961246
	 */
	public void run() {
		while (server.serverOpen) {
			MessageObject n = server.gebeWeiterAnServer();
			if (n == null) {
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					this.nachrichtenVerarbeiten(n);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				this.monsterBewegung();
				this.spielermacheSchritt();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Diese Methode beinhaltet das Verarbeiten von Nachrichten vom Client.
	 * 
	 * @author Alina Gerber, 5961246
	 *
	 */
	void nachrichtenVerarbeiten(MessageObject eingehendeNachricht)
			throws InterruptedException {

		// Fall : Spieler möchte sich einloggen
		if (eingehendeNachricht instanceof LoginMessage) {
			System.out.println("Login - Anfrage");
			LoginMessage daten = (LoginMessage) eingehendeNachricht;
			this.logIn(daten.artVonAnmeldung, daten.name, daten.passwort);
			// Antwort-Nachricht verschicken
			LoginAnswerMessage anfrage = new LoginAnswerMessage();
			if (this.eingeloggt) {
				spieler.setName(daten.name);
				spieler.setPasswort(daten.passwort);
				anfrage.eingeloggt = eingeloggt;
				anfrage.name = daten.name;
				anfrage.passwort = daten.passwort;
				anfrage.levelzaehler = map.levelzaehler;
				map.levelzaehler--;
			}
			server.gebeWeiterAnClient(anfrage);

			// Fall : Benutzer wechseln
		} else if (eingehendeNachricht instanceof LogoutMessage) {
			System.out.println("Benutzer Wechsel - Anfrage");
			// Antwort-Nachricht verschicken
			LogoutMessage daten = (LogoutMessage) eingehendeNachricht;
			speichern(daten.level);
			eingeloggt = false;

			// Fall : Level wechseln oder neuladen
		} else if (eingehendeNachricht instanceof ChangeLevelMessage) {
			System.out.println("Level - Anfrage");
			this.cheatZaehler = 0; // Zuruecksetzen
			map.levelzaehler++;
			map.breite = konstante.WIDTH;
			map.hoehe = konstante.HEIGHT;
			// Berechnen des neuen Level
			AlleLevel levelObject = new AlleLevel(map.hoehe, map.breite);
			map.level = levelObject.setzeInhalt(map.levelzaehler);
			// Zuruecksetzen
			while (!monsterListe.isEmpty()) {
				monsterListe.remove();
			}
			// Umwandeln von Integer - Array zu Spielelement-Array
			for (int i = 0; i < map.level.length; i++) {
				for (int j = 0; j < map.level.length; j++) {
					int Variable = map.level[i][j];
					switch (Variable) {
					case 0:
						map.karte[i][j] = new Wand();
						break;
					case 1:
						map.karte[i][j] = new Boden();
						break;
					case 5:
						map.karte[i][j] = new Heiltrank(20);
						break;
					case 6:
						map.karte[i][j] = new Tuer(false);
						break;
					case 4:
						map.karte[i][j] = new Tuer(true);
						this.spieler.setPos(i, j);
						this.spieler.zielX = i;
						this.spieler.zielY = j;
						break;
					case 2:
						map.karte[i][j] = new Boden();
						this.monsterListe.add(new Monster(i, j, 0));
						break;
					case 3:
						map.karte[i][j] = new Boden();
						this.monsterListe.add(new Monster(i, j, 2));
						break;
					// Monster, welche erst nach dem Aufheben des Schluessels
					// erscheinen
					case 8:
						map.karte[i][j] = new Boden();
						this.monsterListe.add(new Monster(i, j, 1));
						break;
					}
				}
			}
			this.speichern(map.levelzaehler);
			// Antwort-Nachricht verschicken
			ChangeLevelMessage answer = new ChangeLevelMessage();
			answer.level = map.level;
			answer.levelzaehler = map.levelzaehler;
			server.gebeWeiterAnClient(answer);

			// Fall : Spieler möchte sich bewegen
		} else if (eingehendeNachricht instanceof SBewegungMessage) {
			System.out.println("Spieler-Bewegungs - Anfrage");
			SBewegungMessage daten = (SBewegungMessage) eingehendeNachricht;
			spieler.setPos(daten.altX, daten.altY);
			spieler.zielX = daten.neuX;
			spieler.zielY = daten.neuY;
			// Berechnen des Weges, abspeichern in astern.YPosition bzw.
			// astern.XPostition
			this.berechneWeg();
			// Auslesen der Positionsliste und bewegen der Spielfigur
			try {
				this.spielermacheSchritt();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Fall: Leertaste wurde gedrueckt
		} else if (eingehendeNachricht instanceof LeertasteMessage) {
			System.out.println("LeerTaste wurde gedrueckt");
			LeertasteMessage anfrage = new LeertasteMessage();
			// Schluessel aufnehmen
			if (map.karte[spieler.getXPos()][spieler.getYPos()] instanceof Schluessel) {
				spieler.nimmSchluessel();
				map.karte[spieler.getXPos()][spieler.getYPos()] = new Boden();
				anfrage.art = 1;

			}
			// Heiltrank aufnehmen
			else if (map.karte[spieler.getXPos()][spieler.getYPos()] instanceof Heiltrank) {
				spieler.nimmHeiltrank((Heiltrank) map.karte[spieler.getXPos()][spieler
						.getYPos()]);
				map.karte[spieler.getXPos()][spieler.getYPos()] = new Boden();
				anfrage.art = 0;
			}
			// Schluessel benutzen
			else if (map.karte[spieler.getXPos()][spieler.getYPos()] instanceof Tuer) {
				if (!((Tuer) map.karte[spieler.getXPos()][spieler.getYPos()])
						.istOffen() && spieler.hatSchluessel()) {
					((Tuer) map.karte[spieler.getXPos()][spieler.getYPos()])
							.setOffen();
					// Nach dem Oeffnen der Tuer ist der Schluessel wieder weg
					spieler.entferneSchluessel();
					anfrage.art = 2;
				} else
					anfrage.art = 3;
			} else
				anfrage.art = 3;
			// Antwort-Nachricht verschicken
			server.gebeWeiterAnClient(anfrage);

			// Fall : B-Taste wurde gedrueckt
		} else if (eingehendeNachricht instanceof BTasteMessage) {
			System.out.println("B-Taste wurde gedrueckt");
			// Spieler hat Heiltrank
			if (spieler.anzahlHeiltraenke > 0) {
				int change = spieler.benutzeHeiltrank();
				// Heilungseffekt wird verbessert, falls neue Monster durch das
				// Aufheben des Schlüssels ausgelöst wurden
				if (spieler.hatSchluessel())
					spieler.changeHealth((int) (change * 1.5));
				else
					spieler.changeHealth((int) (change * 0.5));
				// Antwort-Nachricht verschicken
				BTasteMessage anfrage = new BTasteMessage();
				server.gebeWeiterAnClient(anfrage);
			}

			// Fall: Speichern der Levelhoehe
		} else if (eingehendeNachricht instanceof SpeicherMessage) {
			System.out.println("Speicher - Anfrage");
			int daten = ((SpeicherMessage) eingehendeNachricht).level;
			this.speichern(daten);

			// Fall: Spieler greift an
		} else if (eingehendeNachricht instanceof SAngriffMessage) {
			System.out.println("Spieler-Angriff");
			Monster m = angriffsMonster();
			if (m != null) {
				this.monsterChangeHealth(m, -(konstante.BOX / 4));
				MStatusMessage anfrage = new MStatusMessage(monsternr,
						monstertot, false);
				// Antwort-Nachricht verschicken
				server.gebeWeiterAnClient(anfrage);
			}
			// Fall: Monster-Bewegung
		} else if (eingehendeNachricht instanceof MBewegungMessage) {
			monsterBewegung();

			// Fall: Spiel Schließen
		} else if (eingehendeNachricht instanceof BeendeMessage) {
			System.out.println("Beende-Anfrage");
			// Antwort-Nachricht verschicken
			BeendeMessage daten = (BeendeMessage) eingehendeNachricht;
			speichern(daten.level);
			server.serverOpen = false;

			// Fall: Highscore Setzen
		} else if (eingehendeNachricht instanceof SetzeHighScoreMessage) {
			System.out.println("HighScore wird gespeichert");
			SetzeHighScoreMessage daten = (SetzeHighScoreMessage) eingehendeNachricht;
			this.setHighScore(daten.zeit, daten.name);

			// Fall: Highscore Anfrage
		} else if (eingehendeNachricht instanceof HighScoreAnfrageMessage) {
			System.out.println("HighScore wird angefragt");
			this.leseHighScore();
		}

		// Fall: Cheat - Anfrage
		else if (eingehendeNachricht instanceof CheatMessage) {
			this.cheatZaehler++;
			if (this.cheatZaehler <= 2) {
				System.out.println("Es wurde ein Cheat benutzt");
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
				CheatMessage anfrage = new CheatMessage(i);
				server.gebeWeiterAnClient(anfrage);
			}

			// Fall: unbekannte Nachricht
		} else {
			new Exception(
					"Server hat eine Nachricht erhalten, die nicht verarbeitet werden kann");
		}
	}

	// Monster Methoden
	/**
	 * Die Methode fragt den aktuellen Zustand des Monsters an und bewegt ihn
	 * entweder anhand der Methode ruhe(), fluechten() oder jagen(). Die
	 * Zulässigkeit wird mit der Methode zulaessig() abgefragt. Die Nachricht
	 * wird nur verschcikt, wenn m.nachricht = true ist, also der Schritt im
	 * Server auch wirklich gemacht wurde.
	 * 
	 * @author Alina Gerber, 5961246
	 * @throws InterruptedException
	 */
	public void monsterBewegung() throws InterruptedException {
		for (int i = 0; i < monsterListe.size(); i++) {
			Monster m = monsterListe.get(i);
			boolean event = spieler.hatSchluessel();
			// Zustand: Ruhe
			if (m.aktuellenZustandBestimmen(spieler.getXPos(),
					spieler.getYPos()) == 1) {
				this.ruhe(m);
				if (m.nachricht) {
					MBewegungMessage anfrage = new MBewegungMessage();
					anfrage.richtung = m.dir;
					anfrage.monsterNummer = i;
					server.gebeWeiterAnClient(anfrage);
					sleep(100);
				}
				// Zustand: Fluechten
			} else if (m.aktuellenZustandBestimmen(spieler.getXPos(),
					spieler.getYPos()) == 3) {
				this.fluechten(m);
				if (m.nachricht) {
					MBewegungMessage anfrage = new MBewegungMessage();
					anfrage.richtung = m.dir;
					anfrage.monsterNummer = i;
					server.gebeWeiterAnClient(anfrage);
					m.changeHealth(konstante.BOX / 8);
					MStatusMessage statusAnfrage = new MStatusMessage(i, false,
							true);
					server.gebeWeiterAnClient(statusAnfrage);
					sleep(100);
				}
				// Zustand: Jagen
			} else {
				this.jagen(m);
				sleep(100);
				if (m.nachricht) {
					MBewegungMessage anfrage = new MBewegungMessage();
					anfrage.richtung = m.dir;
					anfrage.monsterNummer = i;
					server.gebeWeiterAnClient(anfrage);
					sleep(100);
				}
				if (this.attackiereSpieler(event, m)) {
					MAngriffMessage angriffAnfrage = new MAngriffMessage(i);
					server.gebeWeiterAnClient(angriffAnfrage);
					spieler.changeHealth(-konstante.BOX / 8);
				}

			}
		}
	}

	/**
	 * Funktion wurde weitestgehen vom alten HindiBones Spiel uebernommen. Ein
	 * bestimmtes Monster versucht den Spieler anzugreifen. Sind die
	 * Vorraussetzungen fuer einen Angriff gegeben, greift das Monster an den
	 * Spieler an ansonsten nicht.
	 * 
	 * @author Goekdag, Enes, 5615399
	 * @param hatSchluessel
	 *            Ob der Spieler bereits den Schluessel hat
	 * @param m
	 *            Das Monster welches den Spieler versuchen soll anzugreifen
	 * @return true, falls der Angriff erfolgt ist, false sonst
	 */
	public boolean attackiereSpieler(boolean hatSchluessel, Monster m) {
		// Ist der Spieler im Radius des Monsters?
		boolean spielerImRadius = (Math.sqrt(Math.pow(
				spieler.getXPos() - m.getXPos(), 2)
				+ Math.pow(spieler.getYPos() - m.getYPos(), 2)) < 2);

		// Kann das Monster angreifen?
		boolean kannAngreifen = false;
		if (m.getTyp() == 0)
			kannAngreifen = ((System.currentTimeMillis() - m.lastAttack) >= m.cooldownAttack);
		if (m.getTyp() == 1)
			kannAngreifen = (hatSchluessel && ((System.currentTimeMillis() - m.lastAttack) >= m.cooldownAttack));
		if (m.getTyp() == 2)
			kannAngreifen = ((System.currentTimeMillis() - m.lastAttack) >= m.cooldownAttack);
		if (spielerImRadius && kannAngreifen) {
			m.lastAttack = System.currentTimeMillis();
			// spieler.changeHealth(-m.getSchaden());
		}
		return (spielerImRadius && kannAngreifen);
	}

	/**
	 * Wurde weitestgehen vom alten HindiBones Spiel uebernommen. Veraendert die
	 * Gesundheit eines bestimmten Monsters und setzt im Falle des Todes
	 * entsprechende Objekte in die Spielwelt an deren Stelle.
	 * 
	 * @author Goekdag, Enes, 5615399
	 * @param m
	 *            Monster, wessen Gesundheit veraendert werden soll
	 * @param change
	 *            Die Veraenderung der Gesundheit
	 */
	public void monsterChangeHealth(Monster m, int change) {
		m.changeHealth(change);
		if (m.getHealth() <= 0) {
			if (m.getTyp() == 0 || m.getTyp() == 1) {
				map.karte[m.getXPos()][m.getYPos()] = new Heiltrank(30);
				monsterListe.remove(m);
				monstertot = true;
			}
			if (m.getTyp() == 2) {
				map.karte[m.getXPos()][m.getYPos()] = new Schluessel();
				monsterListe.remove(m);
				monstertot = true;
			}

		} else
			monstertot = false;
	}

	/**
	 * Wurde weitestgehen vom alten HindiBones Spiel uebernommen.
	 * 
	 * @author Goekdag, Enes, 5615399
	 * @param m
	 *            Monster dessen naechster Schritt ueberprueft werden soll
	 * @return Ob die Bewegung zulaessig ist
	 */
	boolean zulaessig(Monster m) {
		if (m.dir == 0 && m.getYPos() - 1 > 0) {
			return !(map.karte[m.getXPos()][m.getYPos() - 1] instanceof Wand)
					&& !(map.karte[m.getXPos()][m.getYPos() - 1] instanceof Tuer)
					&& !(map.karte[m.getXPos()][m.getYPos() - 1] instanceof Schluessel);
		} else if (m.dir == 1 && m.getXPos() + 1 < map.karte.length) {
			return !(map.karte[m.getXPos() + 1][m.getYPos()] instanceof Wand)
					&& !(map.karte[m.getXPos() + 1][m.getYPos()] instanceof Tuer)
					&& !(map.karte[m.getXPos() + 1][m.getYPos()] instanceof Schluessel);
		} else if (m.dir == 2 && m.getYPos() + 1 < map.karte.length) {
			return !(map.karte[m.getXPos()][m.getYPos() + 1] instanceof Wand)
					&& !(map.karte[m.getXPos()][m.getYPos() + 1] instanceof Tuer)
					&& !(map.karte[m.getXPos()][m.getYPos() + 1] instanceof Schluessel);
		} else if (m.dir == 3 && m.getXPos() - 1 > 0) {
			return !(map.karte[m.getXPos() - 1][m.getYPos()] instanceof Wand)
					&& !(map.karte[m.getXPos() - 1][m.getYPos()] instanceof Tuer)
					&& !(map.karte[m.getXPos() - 1][m.getYPos()] instanceof Schluessel);
		} else
			return false;
	}

	/**
	 * Team16: Sweet sixteen Funktion, welches ein bestimmtes Monster den
	 * Spieler jagen laesst.
	 * 
	 * @author Goekdag, Enes, 5615399
	 * @param m
	 *            Monster welches sich im Jagen-Modus befindet
	 */
	public void jagen(Monster m) { // Spieler JAGEN (Angriffszustand)
		m.astern = new Astern(m.getYPos(), m.getXPos(), spieler.getXPos(),
				spieler.getYPos(), map.karte);
		Wegpunkt test = m.astern.starten();
		System.out.println("Monster:" + m.getXPos() + "," + m.getYPos());
		System.out.println("Spieler:" + spieler.getXPos() + ","
				+ spieler.getYPos());
		if (test.x < m.getXPos() && test.y == m.getYPos()) {
			m.dir = 3;
			m.nachricht = true;
			System.out.println("m.nachricht wird gesetzt");
			m.move(zulaessig(m));
		} else if (test.x > m.getXPos() && test.y == m.getYPos()) {
			m.dir = 1;
			m.nachricht = true;
			System.out.println("m.nachricht wird gesetzt");
			m.move(zulaessig(m));
		} else if (test.x == m.getXPos() && test.y < m.getYPos()) {
			m.dir = 0;
			m.nachricht = true;
			System.out.println("m.nachricht wird gesetzt");
			m.move(zulaessig(m));
		} else if (test.x == m.getXPos() && test.y > m.getYPos()) {
			m.dir = 2;
			m.nachricht = true;
			System.out.println("m.nachricht wird gesetzt");
			m.move(zulaessig(m));
		} else {
			m.nachricht = false;
		}
	}

	/**
	 * Team16: Sweet sixteen Funktion, welches ein bestimmtes Monster vom
	 * Spieler weglaufen laesst.
	 * 
	 * @author Goekdag, Enes, 5615399
	 * @param m
	 *            Monster welches sich im Fluechten-Modus befindet
	 */
	public void fluechten(Monster m) { // von Spieler FLUECHTEN
										// (Defensivzustand)

		System.out.println("Monster:" + m.getXPos() + "," + m.getYPos());
		System.out.println("Spieler:" + spieler.getXPos() + ","
				+ spieler.getYPos());

		// Daten speichern zum einfacheren Zugriff
		int spielerX = spieler.getXPos();
		int spielerY = spieler.getYPos();
		int monsterX = m.getXPos();
		int monsterY = m.getYPos();
		// Monster nimmt erstbesten Fluchtweg (Es ist ja auch in Panik)
		if (map.karte[monsterX][monsterY + 1] instanceof Boden
				&& spielerY <= monsterY) {
			m.dir = 2;
			m.move(zulaessig(m));
		} else if (map.karte[monsterX + 1][monsterY] instanceof Boden
				&& spielerX <= monsterX) {
			m.dir = 1;
			m.move(zulaessig(m));
		} else if (map.karte[monsterX - 1][monsterY] instanceof Boden
				&& spielerX >= monsterX) {
			m.dir = 3;
			m.move(zulaessig(m));
		} else if (map.karte[monsterX][monsterY - 1] instanceof Boden
				&& spielerY >= monsterY) {
			m.dir = 0;
			m.move(zulaessig(m));
		}

	}

	/**
	 * Funktion, welches ein Monster sich zufaellig bewegen laesst bzw. sich
	 * heile laesst.
	 * 
	 * @author Goekdag, Enes, 5615399
	 * @param m
	 *            Monster im Ruhe-Modus
	 */
	public void ruhe(Monster m) {
		// Heilt im "Ruhe" Zustand
		if (m.getHealth() < m.getMaxHealth()) {
			m.setHealth(m.getHealth() + 1);
		}
		m.move(this.zulaessig(m));
	}

	// Spieler Methoden

	/**
	 * Funktion welche den Weg des Spielers zu seinem gewuenschten Ziel
	 * berechnet.
	 * 
	 * @author Goekdag, Enes, 5615399
	 */
	public void berechneWeg() { // Spieler JAGEN (Angriffszustand)
		astern = new Astern(spieler.getYPos(), spieler.getXPos(),
				spieler.zielX, spieler.zielY, map.karte);
		Wegpunkt test = astern.starten();
		System.out.println("Spieler:" + spieler.getXPos() + ","
				+ spieler.getYPos());
		System.out.println("SZiel:" + spieler.zielX + "," + spieler.zielY);

	}

	/**
	 * Die Methode geht die beiden Listen der x-Position und y-Position durch
	 * und übergibt diese an den Client. Außerdem wird nach jedem
	 * Positionswechsel des Spielers die MonsterBewegung einmal aufgerufen
	 * 
	 * @throws InterruptedException
	 * 
	 * @author Alina Gerber, 5961246
	 */
	public void spielermacheSchritt() throws InterruptedException {
		long jetzt = System.currentTimeMillis();
		long zeitvergangen = jetzt - spieler.letzterSchritt;
		if (zeitvergangen <= 300) {
			System.out.println("Noch nicht genug zeit vergangen: "
					+ zeitvergangen);
			return;
		}
		if (spieler.getYPos() != spieler.zielY
				|| spieler.getXPos() != spieler.zielX) {
			astern = new Astern(spieler.getYPos(), spieler.getXPos(),
					spieler.zielX, spieler.zielY, map.karte);
			Wegpunkt punkt = astern.starten();
			System.out.println("Neue Position wird verschickt");
			int x = punkt.x;
			int y = punkt.y;
			spieler.setPos(x, y);
			spieler.letzterSchritt = jetzt;
			SBewegungMessage answer = new SBewegungMessage();
			answer.neuX = x;
			answer.neuY = y;
			server.gebeWeiterAnClient(answer);
		}
	}

	/**
	 * 
	 * @return Das Monster, das von Spieler angegriffen werden kann
	 * @author Diese Methoden wurde aus dem alten Spiel uerbernommen
	 */
	public Monster angriffsMonster() {
		for (int i = 0; i < this.monsterListe.size(); i++) {
			Monster m = this.monsterListe.get(i);
			// Kann der Spieler angreifen?
			boolean kannAngreifen = false;
			if (m.getTyp() == 0)
				kannAngreifen = true;
			if (m.getTyp() == 1)
				kannAngreifen = spieler.hatSchluessel();
			if (m.getTyp() == 2)
				kannAngreifen = true;
			if ((Math.sqrt(Math.pow(spieler.getXPos() - m.getXPos(), 2)
					+ Math.pow(spieler.getYPos() - m.getYPos(), 2)) < 2)
					&& kannAngreifen) {
				monsternr = i;
				return m;

			}
		}

		return null;
	}

	/**
	 * übergibt die Anmeldeart, name und passwort und schaut je nach Anmeldeart,
	 * ob es den Namen (und das Passwort) schon gibt, wenn nicht, dann füge den
	 * Namen hinzu oder ob der Name und das Passwort in der Textdatei stehen und
	 * gibt an den Cliet zurück, dass die Anmeldung geklappt hat
	 * 
	 * @param i
	 *            steht für Anmeldeart 1= neu anmelden 2=einloggen
	 * @param name
	 *            , der eingegeben wurde ins Loginfenster
	 * @param passwort
	 *            , das eingegeben wurde ins Loginfenster
	 * 
	 * @author: Ann-Catherine Hartmann, Matrikelnr: 60038514/ Prüfungsnummer:
	 *          37658
	 **/
	public void logIn(int i, String name, String passwort) {
		String abgleich = name + " " + passwort; // Abzugleichender String
													// bestehend aus Name und
													// Passwort
		boolean namegibtesschon = false;// namegibtesschon ist ein boolean, der
										// auf true gesetzt wird, wenn es den
										// ausgesuchten Namen schon gibt
		if (i == 1) { // Neuanmeldung
			try {// Prüfung, ob es Name oder Name und Passwort schon gibt
				FileReader fr;
				fr = new FileReader("Spielerdaten");
				BufferedReader br = new BufferedReader(fr);
				String zeile = br.readLine();
				while ((zeile = br.readLine()) != null) {// während die Datei
															// noch einen Inhalt
															// hat
					if (zeile.equals(abgleich) || zeile.startsWith(name + "")) {// falls
																				// es
																				// den
																				// Namen
																				// und
																				// das
																				// Passwort
																				// schon
																				// gibt,
																				// dann
																				// ...
						eingeloggt = false;// setzte eingeloggt auf false
						namegibtesschon = true;// namegibtesschon auf true
						break;
					}
				}
				// Schließe den Buffered- und Filereader
				br.close();
				fr.close();

			} catch (IOException e) {
			}

			if (namegibtesschon == false) {// Wenn es den Namen noch nicht gibt
				String initiallevel = "Level 1";// setze das Level auf 1
				FileWriter fw;
				try {
					// hinzufügen des Spielers ans Ende der Spielerdatendatei
					fw = new FileWriter("Spielerdaten", true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.newLine();
					bw.write(abgleich);
					bw.newLine();
					bw.write(initiallevel);
					bw.newLine();
					bw.newLine();
					bw.newLine();
					bw.close();// Schliesst die Datei
					fw.close();
					eingeloggt = true;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (i == 2) {// wenn man sich einloggen will
			try {
				spielername = name;// speichert den Namen des Spielers
				spielerpasswort = passwort;// speichert das Passwort des
											// Spielers
				// Schaut ob Name und Passwort in der Datei Spielerdaten steht
				FileReader fr;
				fr = new FileReader("Spielerdaten");
				BufferedReader br = new BufferedReader(fr);
				String zeile;
				while ((zeile = br.readLine()) != null) {
					System.out.println("Zeile wird gelesen");
					if (zeile.equals(abgleich)) {// Wenn Name und Passwort
													// gefunden wurden in der
													// Datei
						eingeloggt = true;// setze eingeloggt gleich true
						String level = br.readLine();// lese das Level in der
														// darauffolgenden Zeile
						char c = level.charAt(6);// Levelnummer steht an Zeile 6
													// des Strings level
						this.map.levelzaehler = (int) c - 48;// level wird auf
																// den Wert aus
																// der Datei
																// gesetzt, -48,
																// da die Zahlen
																// im Asci-code
																// ab Stelle 49
																// anfangen
						break;
					}
				}
				br.close();
				fr.close();

			} catch (IOException e) {
			}

		}
	}

	/**
	 * Liest die ersten 5 Namen und Zeiten in der Highscoredatei aus und packt
	 * diesen in eine Nachricht und gibt diese über die Kommunikation an den
	 * Client weiter
	 * 
	 * @author: Ann-Catherine Hartmann, Matrikelnr: 6038514
	 * 
	 **/
	public void leseHighScore() {
		try {
			FileReader fr = new FileReader("HighScore"); // Initialisierung des
															// Filereaders
			BufferedReader br = new BufferedReader(fr);// Initialisierung des
														// Bufferedrreaders
			String zeile;// String zum Zwischenspeichern der gelesenen Zeile
			for (int i = 0; i < 5; i++) {// gehe 5 mal durch

				zeile = br.readLine();// lese eine zeile
				HighScoreMessage hm = new HighScoreMessage(zeile);// setze die
																	// Zeile als
																	// Parameter
																	// in die
																	// Nachricht
				server.gebeWeiterAnClient(hm);// Schicke die Nachricht an die
												// Kommunikation
				zeile = br.readLine();// Lese die leere Zeile zw. den
										// HighScorespielern
			}
			// Wenn man 5mal durchgegangen ist, schliesse den (Buffered-)
			// Filereader
			br.close();
			fr.close();

		} catch (IOException e) {
		}

	}

	/**
	 * Fügt das Ergebnis des Spielers und dessen Namen sortiert nach Zeit in die
	 * Highscoretextdatei ein,
	 * 
	 * @author: Ann-Catherine Hartmann 6038514
	 **/
	public void setHighScore(int zeit, String name) {
		String z = "Zeit: " + String.valueOf(zeit) + "   Name des Spielers: "
				+ name; // einzufügender String
		try {

			File original = new File("HighScore");// Originaldatei
			File kopie = new File("HighScore2");// Hilfsdatei
			FileReader fr = new FileReader(original);// Initialisierung des
														// Filereaders für die
														// Originaldatei
			// Initialisierung des BufferedReaders für die Originaldatei
			BufferedReader br = new BufferedReader(fr);
			// Initialisierung des Filewriters für die Hilsdatei
			FileWriter fw = new FileWriter(kopie);
			// Initialisierung des Bufferedwriters für die Hilsdatei
			BufferedWriter bw = new BufferedWriter(fw);
			// Zwischenspeicher für die aktuell gelesene Zeile
			String zeile;
			// wenn die aktuell gelesene Zeile ungleich Null ist, also man noch
			// nicht am Ende der Datei angekommen ist
			while ((zeile = br.readLine()) != null) {
				// wenn die zeile im Original nicht leer ist, dann...
				if (zeile.equals("") == false) {
					// Zähler gleich 6, da die Zahl für die Zeit an der 6.Stelle
					// der Zeile anfängt
					int i = 6;
					char k = zeile.charAt(i);// bekomme das Zeichen an der
												// Stelle i
					i++;// erhöhe i um 1
					String h = String.valueOf(k);//
					while (((int) (k = zeile.charAt(i))) != 32) {// Während das
																	// gelesene
																	// Zeichen
																	// an der
																	// Stelle i
																	// ungleich
																	// dem
																	// Leerzeichen
																	// ist (in
																	// ASCI-Code
																	// = 32),
																	// dann..
						h = h + k;// füge zu h den char k
						i++;// erhöhe i um 1
					}
					Integer l = Integer.valueOf(h);// Integerwert von dem String
													// h, nachdem alle Zahlen in
													// der Zeile gelesen wurden
					if (l > zeit) {// falls die aktuell gelesene Zeit größer ist
									// als die zeit unseres Spielers
						bw.write(z);// schreibe die Werte (Name und Zeit, die im
									// String s gespeichert sind) des aktuellen
									// Spielers in die Hilfsdatei
						bw.newLine();// gehe in die nächste Zeile in der
										// Hilfsdatei
						bw.newLine();// gehe in die nächste Zeile (damit wird
										// eine leere Zeile erzeugt) in der
										// Hilfsdatei
					}
				}
				bw.write(zeile);// Schreibe den aktuell ausgelesenen Wert in die
								// Hilfsdatei
				bw.newLine();// geh in die nächste Zeile in der Hilfsdatei
			}

			bw.flush();// flusht den BufferedWriter
			// Schliesst den (Buffered-)Filewriter und (Buffered-)FileReader
			fw.close();
			bw.close();
			br.close();
			fr.close();
			original.delete();// lösche die Originaldatei
			kopie.renameTo(original);// nenne die Hilfsdatei so wie die
										// Originaldatei

		} catch (IOException e) {

		}

	}

	/**
	 * speichert das aktuelle Level, wenn der Spieler auf Beenden drückt
	 * 
	 * @param levelNr
	 *            ist das aktualle Level, in dem sich der Spieler befindet
	 * @author: Ann-Catherine Hartmann, 6038514
	 **/
	public void abmelden(int levelNr) {
		this.speichern(levelNr);

	}

	/**
	 * Dieser Methode wird das aktuell vom eingeloggten Spieler übergeben und
	 * dann dieses Level neu abgespeichert anstatt dem, auf dem der Spieler
	 * davor war.
	 * 
	 * @author: Ann-Catherine Hartmann, Matrikelnr: 6038514
	 **/
	public void speichern(int levelNr) {
		// String zum abgleichen wird aus Name und Passwort erstellt
		String abgleich = spielername + " " + spielerpasswort;
		// Erstellung des Strings der neu abgespeichert werden soll
		String neuesLevel = "Level " + String.valueOf(levelNr);
		try {
			File original = new File("Spielerdaten");// Datei, in der die
														// Spielerdaten
														// abgespeichert sind
			File kopie = new File("Spielerdaten2");// Hilfsdatei zum
													// Zwischenspeichern der
													// Strings
			FileReader fr = new FileReader("Spielerdaten");// Initialisierung
															// des Filereaders
															// für das Original
			BufferedReader br = new BufferedReader(fr);// Initialisierung des
														// BufferedReaders für
														// das Original
			FileWriter fw = new FileWriter("Spielerdaten2");// Initialisierung
															// des Filewriters
															// für die
															// Zwischendatei
			BufferedWriter bw = new BufferedWriter(fw);// Initialisierung des
														// BufferedWriters
			String zeile;// Zwischenspeicher für die gelesenen Strings vom
							// BufferdReader
			while ((zeile = br.readLine()) != null) {// Während das Dokument
														// noch nicht ganz
														// durchgegangen ist,
														// tue..
				if (zeile.equals(abgleich)) {// teste, ob der name und das
												// passwort mit der gelesenen
												// Zeile übereinstimmen
					bw.write(zeile);// Wenn diese übereinstimmen, dann schreibe
									// die Zeile in die Hilfsdatei
					bw.newLine();// Gehe in die nächste Zeile in der Hilfsdatei
									// eine Zeile weiter
					bw.write(neuesLevel);// Schreibe das neue Level in diese
											// Zeile
					bw.newLine();// Gehe in die nächste Zeile in derHilfsdatei
					br.readLine();// Lese die nächste Zeile (=Zeile mit altem
									// Level)
					zeile = br.readLine();// Lese die nächste Zeile des
											// Originals
				}
				bw.write(zeile);// füge die zuletzt gelesene Zeile in die
								// Hilfsdatei
				bw.newLine();// gehe eine Zeile weiter in der Hilfsdatei
			}

			bw.flush();// flusht den Bufferedwriter
			// schließt alle (Buffered-)Filewriter und (Buffered-)Filereader
			fw.close();
			bw.close();
			br.close();
			fr.close();
			original.delete();// lösche das Original
			kopie.renameTo(original);// nenne die Hilfsdatei wie das Original

		} catch (IOException e) {
		}

	}
}
