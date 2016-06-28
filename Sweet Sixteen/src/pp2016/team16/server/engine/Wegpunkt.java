package pp2016.team16.server.engine;

/* Team16: Sweet sixteen
 * Goekdag, Enes, 5615399
 * 
 * */
public class Wegpunkt {
	public int x;
	public int y;

	public int kosten;
	public int heuristik;

	public Wegpunkt vorgaenger;

	public Wegpunkt(int x, int y, int kosten, int heuristik, Wegpunkt vor) {
		this.x = x;
		this.y = y;
		this.kosten = kosten;
		this.heuristik = heuristik;
		this.vorgaenger = vor;
	}

	public int getGesamtkosten() {
		return this.getBisherigeWegkosten() + this.kosten + this.heuristik;
	}

	public int getBisherigeWegkosten() {
		if (this.vorgaenger == null)
			return 0;
		return this.vorgaenger.getBisherigeWegkosten() + this.kosten;
	}

	public void setVorgaenger(Wegpunkt neuer) {
		this.vorgaenger = neuer;
	}

	public void print() {
		if (this.vorgaenger != null) {
			this.vorgaenger.print();
		}

		System.out.println("x:" + x + " y:" + y);
	}

	public Wegpunkt getLastWegpunkt() {
		Wegpunkt aktuellerPunkt = this;
		while (aktuellerPunkt.vorgaenger != null) {
			aktuellerPunkt = aktuellerPunkt.vorgaenger;
		}
		return aktuellerPunkt;
	}
	public int anzahlVorgänger() {
		int anzahl=0;
		Wegpunkt aktuellerWegpunkt=this;
		while (aktuellerWegpunkt.vorgaenger!=null) {
			aktuellerWegpunkt=aktuellerWegpunkt.vorgaenger;
			anzahl++;
        }
		return anzahl;
	}

}
