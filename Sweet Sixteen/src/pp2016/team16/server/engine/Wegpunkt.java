package pp2016.team16.server.engine;

/** Team16: Sweet sixteen
 * 
 * Klasse fuer den Astern. Hier werden alle wichtigen Daten eines Knoten gespeichert und verwaltet.
 * 
 * @author Goekdag, Enes, 5615399
 * 
 * */
public class Wegpunkt {
	public int x;
	public int y;

	public int kosten;
	public int heuristik;

	public Wegpunkt vorgaenger;

	/**
	 * Konstruktor fuer einen Wegpunkt. Es koennen alle wichtigen Werte direkt mit uebergeben werden.
	 * 
	 * @author Goekdag, Enes, 5615399
	 * @param x x-Koordinate des Wegpunkts
	 * @param y y-Koordinate des Wegpunkts
	 * @param kosten Die Kosten des Weges vom Vorgaenger zu diesem Knoten
	 * @param heuristik Die geschaetzte Entfernung zum Zielknoten
	 * @param vor Eventuell der Vorgaenger-Knoten entlang des Weges, ansonsten null
	 */
	public Wegpunkt(int x, int y, int kosten, int heuristik, Wegpunkt vor) {
		this.x = x;
		this.y = y;
		this.kosten = kosten;
		this.heuristik = heuristik;
		this.vorgaenger = vor;
	}

	/**
	 * Funktion, die die berechneten geschaetzten Kosten bis zum Zielpunkt zurueck gibt.
	 * 
	 * @author Goekdag, Enes, 5615399
	 * @return Die Gesamtkosten
	 */
	public int getGesamtkosten() {
		return this.getBisherigeWegkosten() + this.kosten + this.heuristik;
	}

	/**
	 * Hilfsfunktion, welche die exakten Kosten vom Startknoten ueber den berechneten Weg hinzu
	 * diesem Knoten berechnet. Die Funktion geht dabei rekursiv vor.
	 * 
	 * @author Goekdag, Enes, 5615399
	 * @return Die exakten Kosten vom Startknoten bis zum jetzigem Knoten
	 */
	public int getBisherigeWegkosten() {
		if (this.vorgaenger == null)
			return 0;
		return this.vorgaenger.getBisherigeWegkosten() + this.kosten;
	}

	/**
	 * @author Goekdag, Enes, 5615399
	 */
	public void setVorgaenger(Wegpunkt neuer) {
		this.vorgaenger = neuer;
	}

	/*public void print() {
		if (this.vorgaenger != null) {
			this.vorgaenger.print();
		}
		
		System.out.println("x:" + x + " y:" + y);
	}*/
	
	/**
	 * @author Goekdag, Enes, 5615399
	 */
	public Wegpunkt getLastWegpunkt() {
		Wegpunkt aktuellerPunkt = this;
		while (aktuellerPunkt.vorgaenger != null) {
			aktuellerPunkt = aktuellerPunkt.vorgaenger;
		}
		return aktuellerPunkt;
	}
	
	/**
	 * Funktion die die Anzahl der Vorgaenger berechnet und zurueck gibt.
	 * 
	 * @author Goekdag, Enes, 5615399
	 * @return Die Anzahl der Vorgaenger
	 */
	public int anzahlVorgaenger() {
		int anzahl=0;
		Wegpunkt aktuellerWegpunkt=this;
		while (aktuellerWegpunkt.vorgaenger!=null) {
			aktuellerWegpunkt=aktuellerWegpunkt.vorgaenger;
			anzahl++;
        }
		return anzahl;
	}

}
