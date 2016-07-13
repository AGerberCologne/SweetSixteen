package pp2016.team16.server.map;

/**
 * <Die Klasse "AlleLevel" beinhaltet mehrere Methoden zur Erstellung eines Levels bzw. des Labyrinths. 
 * Die Methode erzeugeLabyrinth ist eine rekursive Methode, die auf die Methode floodfill(r, c) zugreift. Diese Methode
 * erzeugt mit HIlfe des FloodFill Allgorithmus in ein zweidimensionales Array ein Labyrinth. Anschliessend setzen einzelne Methoden
 * die Startposition des Spielers, die Zielposition der Ausgangstuer, Startpositionen fuer unterschiedliche Monstertpen, sowie 
 * Platierungspositionen fuer Heiltranke in das zuvor erzeugte Labyrinth. 
 * Eine letzte Methode fasst alle zuvor erlaeuterten Methoden in eine zusammen. >
 * @author <Noll, Markus, 5812500 > */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import pp2016.team16.shared.Spielelement;


public class AlleLevel {
	/**
	 * <Block Attribute>
	 * 4 Attribute
	 * 
	 * @author <Noll , Markus , 5812500 >
	 */
	public Spielelement[][] level2;
	public int hoehe, breite; // deklariere oeffentliche Variabeln hoehe und
								// breite
	public int[][] level; // deklariere zweidimensionales Array level

	/**
	 * < Block Konstruktor > <Zweidimensionales Array stellt die Grundlage des
	 * jeweiligen Levels dar >	 
	 * @param hoehe gibt die Hoehe des Levles an
	 * @param breite gibt die Breite des Levels an
	 * * @author <Noll , Markus , 5812500 >
	 */
	public AlleLevel(int hoehe, int breite) { // Konstruktor
		this.hoehe = hoehe; // initialisiere hoehe und breite
		this.breite = breite;
		level = new int[hoehe][breite]; // weise hoehe und breite dem Array zu
	}

	/**
	 * <Block Methoden>*
	 */

	/**
	 * <Methode erzeugeLabyrinth() dient zur Erzeugung des Labyrinths. Zunaechst
	 * wird das gesamte Array mit dem Attribut Wand gefuellt. Anschliessend wird
	 * eine zufaellige Startposition für die darauf folgende Methode gewaehlt.
	 * Wichtig dabei ist, dass sich die Startposition in einer ungeraden Spalte,
	 * sowie ungeraden Zeile befindet. >
	 * @return das bisher erzeugte Labyrinth, bestehend aus Weg und Wand, wird zurueckgegeben
	 * @author <Noll , Markus , 5812500 >
	 */
	public int[][] erzeugeLabyrinth() { // Methode um Labyrinth zu erzeugen

		for (int i = 0; i < level.length; i++)
			for (int j = 0; j < level.length; j++)
				level[i][j] = Felder.WAND;

		Random zufall = new Random();
		// r für Reihe、c für Spalte
		// erzeuge Zufallsposition r
		int r = zufall.nextInt(hoehe - 2) + 1; // initialisiert r mit einem
												// bestimmten
												// Wert innerhalb meiner Hoehe
												// (ohne Aussengrenze)
		while (r % 2 == 0) { // solange bis r eine ungerade Zahl ist
			r = zufall.nextInt(hoehe);
		}
		// erzeuge Zufallsposition c
		int c = zufall.nextInt(breite - 2) + 1; // initialisiert c mit einem
												// bestimmten
		// Wert innerhalb meiner Breite (ohne Aussengrenzen)
		while (c % 2 == 0) { // solange bis c eine ungerade Zahl ist
			c = zufall.nextInt(breite);
		}
		// Startposition
		level[r][c] = Felder.BODEN;

		// 　Weise dem Level ein Labyrinth zu, mit der Methode floodfill(int,
		// int)
		floodfill(r, c);

		return level; // gebe das Labyrinth wieder
	}

	/**
	 * <Methode erzeugeZufallsRichtung() erzeugt ein Array mit vier
	 * Zufallszahlen, die für die jeweiligen Richtungen stehen > 
	 * @return Interger Array wird zureuck gegeben
	 * @author <Noll , Markus , 5812500 >
	 */
	public Integer[] erzeugeZufallsRichtung() {
		ArrayList<Integer> richtung = new ArrayList<Integer>(); // ArrayListe
																// mit INteger
																// Werten
		for (int i = 0; i < 4; i++)
			richtung.add(i + 1);
		Collections.shuffle(richtung);

		return richtung.toArray(new Integer[4]);
	}

	/**
	 * <Methode floodFill() beinhaltet eine Form des FloodFill Algorithmus.
	 * Gestartet wird an der zuvor zufaellig bestimmten Stelle (r,c). Zufaellig
	 * bewegt sich der Algorithmus in eine der vier moeglichen Richtungen. Dabei
	 * wird zunaechst geprueft ob die uebernachste Position in der jeweiligen
	 * Richtung noch im Bereich des zu befuellenden Arrays liegt. Dabei wird
	 * festgelegt, dass die aeusserste Zeile und Spalte, nicht mehr zum
	 * "legalen" Bereich gehoert. Durch das zufaellige benutzen der cases wird
	 * ein Labyrinth erzeugt >
	 * @param r beschreibt die vorher per Zufall bestimmte Startposition des Allgorithmus
	 * @param c beschreibt die vorher per Zufall bestimmte Startposition des Allgorithmus
	 * @author <Noll , Markus , 5812500 >
	 */
	public void floodfill(int r, int c) {
		// Array mit den zufaelligen Richtungen
		Integer[] zufallsRichtung = erzeugeZufallsRichtung();
		// Examine each direction
		for (int i = 0; i < zufallsRichtung.length; i++) {

			switch (zufallsRichtung[i]) {
			case 1: // Hoch
				if (r - 2 > 0 && level[r - 2][c] == Felder.WAND) { // pruefe ob
																	// uebnaechste
																	// Wand oder
																	// Weg
					level[r - 2][c] = Felder.BODEN; // setze die naechsten
													// beiden Zellen als Weg
					level[r - 1][c] = Felder.BODEN;
					floodfill(r - 2, c); // starte die Methode an der neuen
											// Position
				}

			case 2: // Rechts

				if (c + 2 < breite - 1 && level[r][c + 2] == Felder.WAND) { // pruefe
																			// ob
																			// uebnaechste
																			// Wand
																			// oder
																			// Weg
					level[r][c + 2] = Felder.BODEN; // setze die naechsten
													// beiden Zellen als Weg
					level[r][c + 1] = Felder.BODEN;
					floodfill(r, c + 2); // starte die Methode an der neuen
											// Position
				}

			case 3: // Runter

				if (r + 2 < hoehe - 1 && level[r + 2][c] == Felder.WAND) { // pruefe
																			// ob
																			// uebnaechste
																			// Wand
																			// oder
																			// Weg
					level[r + 2][c] = Felder.BODEN; // setze die naechsten
													// beiden Zellen als Weg
					level[r + 1][c] = Felder.BODEN; // starte die Methode an der
													// neuen Position
					floodfill(r + 2, c);
				}

			case 4: // Links

				if (c - 2 > 0 && level[r][c - 2] == Felder.WAND) { // pruefe ob
																	// uebnaechste
																	// Wand oder
																	// Weg
					level[r][c - 2] = Felder.BODEN; // setze die naechsten
													// beiden Zellen als Weg
					level[r][c - 1] = Felder.BODEN; // starte die Methode an der
													// neuen Position
					floodfill(r, c - 2);
				}

			}
		}
	}

	/**
	 * <Methode zufallsPositionx() gibt eine zufaellige Zahl innerhalb der Hoehe
	 * des Arrays wieder. Die erste und letzte Zeile sind nicht inbegriffen. >
	 * @return gibt eine zufaellige Zahl innerhalb unseres Spielfelds wieder, diese Zahl steht fuer x-Koordinate 
	 * @author <Noll , Markus , 5812500 >
	 */
	public int zufallsPositionx() {
		Random roll = new Random();
		int number = Math.abs(roll.nextInt(hoehe - 2) + 1);
		return number;
	}

	/**
	 * <Methode zufallsPositiony() gibt eine zufaellige Zahl innerhalb der
	 * Breite des Arrays wieder. Die erste und letzte Spalte sind nicht
	 * inbegriffen. >
	 * @return gibt eine zufaellige Zahl innerhalb unseres Spielfelds wieder, diese Zahl steht fuer y-Koordinate
	 * @author <Noll , Markus , 5812500 >
	 */
	public int zufallsPositiony() {
		Random roll = new Random();
		int number = Math.abs(roll.nextInt(breite - 2) + 1);
		return number;
	}

	/**
	 * <Methode setzeTrank() setzt zufaellig vier Traenke in unser erzeugtes
	 * Labyrinth. Dabei wird zunaechst ueberprüft, ob die gewaehlte Position
	 * begehbar ist. >
	 * @return gibt das Level inklusive der gesetzten Traenke zurueck
	 * @author <Noll , Markus , 5812500 >
	 */
	public int[][] setzeTrank() {
		int maxTrank = 0;
		while (maxTrank < 4) {
			int x = zufallsPositionx();
			int y = zufallsPositiony();
			if (level[x][y] == Felder.BODEN) {
				level[x][y] = Felder.TRANK;
				maxTrank++;
			}
		}
		return level;

	}

	/**
	 * <Methode setzeStart() setzt zufaellig eine Startposition in unsere
	 * Aussenwand. >
	 * @return gibt das Level inklusive der gesetzten Startposition des Spielers zurueck.
	 * @author <Noll , Markus , 5812500 >
	 */
	public int[][] setzeStart() {

		int y = zufallsPositiony();
		int x = 0;
		if (level[x][y] == Felder.WAND && level[x + 1][y] == Felder.BODEN) {
			level[x][y] = Felder.START;
		} else {
			setzeStart();
		}

		return level;
	}

	/**
	 * <Methode setzeZiel() setzt zufaellig eine Zielposition in unsere
	 * Aussenwand. >
	 * @return gibt das Level inklusive der gesetzten Zielpoistion zurueck.
	 * @author <Noll , Markus , 5812500 >
	 */
	public int[][] setzeZiel() {

		int y = zufallsPositiony();
		int x = hoehe - 1;
		if (level[x][y] == Felder.WAND && level[x - 1][y] == Felder.BODEN) {
			level[x][y] = Felder.ZIEL;
		} else {
			setzeZiel();
		}
		return level;
	}

	/**
	 * <Methode setzeMonsterTyp1() setzt zufaellig Monster des Typs 1 in das
	 * erzeugte Labyrinth. Auch hier wird zunaechst ueberprueft, ob die
	 * zufaellig gewaehlte Position begehbar ist. >
	 * @return gibt das Level inklusive der gesetzten Spornpositionen der Monster vom Typ1 zurueck.
	 * @author <Noll , Markus , 5812500 >
	 */
	public int[][] setzeMonsterTyp1(int maxMonster) {
		int zaehler = 0;
		while (zaehler < maxMonster + 1) {
			int x = zufallsPositionx();
			int y = zufallsPositiony();
			if (level[x][y] == Felder.BODEN) {
				level[x][y] = Felder.MONSTER1;
				zaehler++;
			}
		}
		return level;
	}

	/**
	 * <Methode setzeMonsterTyp2() setzt zufaellig Monster des Typs 1 in das
	 * erzeugte Labyrinth. Auch hier wird zunaechst ueberprueft, ob die
	 * zufaellig gewaehlte Position begehbar ist. >
	 * @return gibt das Level inklusive der gesetzten Spornpositionen der Monster vom Typ2 zurueck.
	 * @author <Noll , Markus , 5812500 >
	 */
	public int[][] setzeMonsterTyp2(int maxMonster) {
		int zaehler2 = 0;
		while (zaehler2 < maxMonster - 2) {
			int x = zufallsPositionx();
			int y = zufallsPositiony();
			if (level[x][y] == Felder.BODEN) {
				level[x][y] = Felder.MONSTER2;
				zaehler2++;
			}
		}
		return level;
	}

	/**
	 * <Methode setzeMonsterTyp12() setzt zufaellig Monster des Typs 1 in das
	 * erzeugte Labyrinth (Ab Level 3). Auch hier wird zunaechst ueberprueft, ob
	 * die zufaellig gewaehlte Position begehbar ist. >
	 * @return gibt das Level inklusive der gesetzten Spornpositionen der Monster vom Typ1 zurueck.
	 * @author <Noll , Markus , 5812500 >
	 */

	public int[][] setzeMonsterTyp12(int levelzaehler) {
		int zaehlerMonster = 0;
		while (zaehlerMonster < levelzaehler) {
			int x = zufallsPositionx();
			int y = zufallsPositiony();
			if (level[x][y] == Felder.BODEN) {
				level[x][y] = Felder.MONSTER1;
				zaehlerMonster++;
			}
		}
		return level;
	}

	/**
	 * <Methode setzeMonsterMitSchluessel() setzt zufaellig das Monster mit dem
	 * Schluessel in das erzeugte Labyrinth. Auch hier wird zunaechst
	 * ueberprueft, ob die zufaellig gewaehlte Position begehbar ist. >
	 ** @return gibt das Level inklusive der gesetzten Spornpositionen des Monsters mit dem Schluessel zurueck.
	 * @author <Noll , Markus , 5812500 >
	 */
	public int[][] setzeMonsterMitSchluessel() {
		int y = zufallsPositiony();
		int x = zufallsPositionx();
		if (level[x][y] == Felder.BODEN) {
			level[x][y] = Felder.MONSTERMITSCHLUESSEL;

		} else {
			setzeMonsterMitSchluessel();
		}
		return level;
	}

	/**
	 * <Methode zeichneLevel() zeigt den eigentlichen Test der Komponente als
	 * Code. Man sieht, dass das Labyrinth richtig mit FloodFill erzeugt und per
	 * Zufall mit Items und Monstern besetzt wird. >
	 * 
	 * @author <Noll , Markus , 5812500 >
	 */
	/**
	 * public void zeichneLevel(){ for (int x = 0; x < level.length; x++) { for
	 * (int y = 0; y < level[x].length; y++) { System.out.print(level[x][y]); }
	 * System.out.println(""); } }
	 */
	/**
	 * <Methode setzeInhalt() fasst alle zur Erzeugung des Levels notwendigen
	 * Methoden in einer Methode zusammen. >
	 * @return gibt das fertig generierte, mit allen Spielelementen gesetzen  Level zurueck.
	 * @author <Noll , Markus , 5812500 >
	 */
	public int[][] setzeInhalt(int levelzahl) {

		erzeugeLabyrinth();
		setzeStart();
		setzeZiel();
		setzeTrank();
		if (levelzahl < 3) {
			setzeMonsterTyp1(levelzahl);

			setzeMonsterTyp2(Math.max(3, levelzahl));
		} else {
			setzeMonsterTyp12(3);
			setzeMonsterTyp2(levelzahl);
		}
		setzeMonsterMitSchluessel();
		return level;

	}

	public int[][] getlevel() {
		// TODO Auto-generated method stub

		return level;
	}

}
