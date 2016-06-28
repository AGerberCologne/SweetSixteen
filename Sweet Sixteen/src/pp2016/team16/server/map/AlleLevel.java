package pp2016.team16.server.map;

/**
* @author <Noll , Markus , 5812500 > */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AlleLevel {
	public int hoehe, breite; // deklariere oeffentliche Variabeln hoehe und breite
	public int[][] level; // deklariere zweidimensionales Array level


/**
* <Zweidimensionales Array stellt die Grundlage des jeweiligen Levels dar >
* @author <Noll , Markus , 5812500 > */
	public AlleLevel() { // Konstruktor
		hoehe = 17; // initialisiere hoehe und breite
		breite = 17;
		level = new int[hoehe][breite]; // weise hoehe und breite dem Array zu
	}

/**
* <Block Methoden>* */
	
	/**
	* <Methode erzeugeLabyrinth() dient zur Erzeugung des Labyrinths. Zunaechst wird das gesamte Array mit dem Attribut Wand gefuellt.
	* Anschliessend wird eine zufaellige Startposition für die darauf folgende Methode gewaehlt. Wichtig dabei ist, dass sich
	* die Startposition in einer ungeraden Spalte, sowie ungeraden Zeile befindet. >
	* @author <Noll , Markus , 5812500 > */
	public int[][] erzeugeLabyrinth() { // Methode um Labyrinth zu erzeugen

		for (int i = 0; i < level.length; i++)
			for (int j = 0; j < level.length; j++)
				level[i][j] = Felder.WAND;

		Random zufall = new Random();
		// r für Reihe、c für Spalte
		// erzeuge Zufallsposition r
		int r = zufall.nextInt(hoehe - 2)+1; // initialisiert r mit einem
											// Bestimmten
											// Wert innerhalb meiner Hoehe (ohne Aussengrenze)
		while (r % 2 == 0) { // solange bis r eine ungerade Zahl ist
			r = zufall.nextInt(hoehe);
		}
		// erzeuge Zufallsposition c
		int c = zufall.nextInt(breite-2)+1; // initialisiert c mit einem bestimmten
										// Wert innerhalb meiner Breite (ohne Aussengrenzen)
		while (c % 2 == 0) { // solange bis c eine ungerade Zahl ist
			c = zufall.nextInt(breite);
		}
		// Startposition
		level[r][c] = Felder.BODEN;

		// 　Weise dem Level ein Labyrinth zu, mit der floodfillMethode
		floodfill(r, c);

		return level; // gebe das Labyrinth wieder
	}
	
	/**
	* <Methode erzeugeZufallsRichtung() erzeugt ein Array mit vier Zufallszahlen, die für die jeweiligen Richtungen stehen  >
	* @author <Noll , Markus , 5812500 > */
	public Integer[] erzeugeZufallsRichtung() {
		ArrayList<Integer> richtung = new ArrayList<Integer>();
		for (int i = 0; i < 4; i++)
			richtung.add(i + 1);
		Collections.shuffle(richtung);

		return richtung.toArray(new Integer[4]);
	}

	/**
	* <Methode floodFill() beinhaltet eine Form des FloodFill Algorithmus. Gestartet wird an der zuvor zufaellig bestimmten Stelle
	* (r,c). Zufaellig bewegt sich der Algorithmus in eine der vier moeglichen Richtungen. Dabei wird zunächst geprüft ob die uebernachste
	* Position in der jeweiligen Richtung noch im Bereich des zu befuellenden Arrays liegt. Dabei wird festgelegt, dass die aeusserste Zeile
	* und Spalte, nicht mehr zum "legalen" Bereich gehoert. Durch das zufaellige benutzen der cases wird ein Labyrinth erzeugt  >
	* @author <Noll , Markus , 5812500 > */
	public void floodfill(int r, int c) {
		// 4 random directions
		Integer[] zufallsRichtung = erzeugeZufallsRichtung();
		// Examine each direction
		for (int i = 0; i < zufallsRichtung.length; i++) {

			switch (zufallsRichtung[i]) {
			case 1: // Up
				// 　Whether 2 cells up is out or not
				if (r - 2 > 0 && level[r - 2][c] == Felder.WAND) { // prüfe ob
																	// nächste
																	// Position
																	// Wand
					// oder Weg, wenn Weg,
					level[r - 2][c] = Felder.BODEN; // setze die nächsten beiden
													// Zellen als
					// Weg
					level[r - 1][c] = Felder.BODEN;
					floodfill(r - 2, c); // starte die Methode an der neuen
					// Position
				}

			case 2: // Right
				// Whether 2 cells to the right is out or not

				if (c + 2 < breite - 1 && level[r][c + 2] == Felder.WAND) {
					level[r][c + 2] = Felder.BODEN;
					level[r][c + 1] = Felder.BODEN;
					floodfill(r, c + 2);
				}

			case 3: // Down
				// Whether 2 cells down is out or not
				if (r + 2 < hoehe - 1 && level[r + 2][c] == Felder.WAND) {
					level[r + 2][c] = Felder.BODEN;
					level[r + 1][c] = Felder.BODEN;
					floodfill(r + 2, c);
				}

			case 4: // Left
				// Whether 2 cells to the left is out or not

				if (c - 2 > 0 && level[r][c - 2] == Felder.WAND) {
					level[r][c - 2] = Felder.BODEN;
					level[r][c - 1] = Felder.BODEN;
					floodfill(r, c - 2);
