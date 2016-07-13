package pp2016.team16.server.engine;

import java.util.LinkedList;

import pp2016.team16.client.gui.HindiBones;
import pp2016.team16.shared.*;


/** Team16: Sweet sixteen
 * 
 * Astern berechnet den kuerzesten Weg.
 * Beim Monster wird der Astern im State JAGEN aufgerufen, um den kuerzesten Weg zum Spieler zu finden.
 * Beim Spieler wird der Astern aufgerufen um den kuerzesten Weg zum gewaehlten (MAUSKLICK) Ziel zu finden.
 * 
 * @author Goekdag, Enes, 5615399
 * 
 * */
public class Astern extends Thread {
	 // ServerEngine sengine;
	  private Spielelement [][] karte = new Spielelement[21][21];
	  private int x;
	  private int y;
	  private boolean gefunden;
	  public LinkedList<Integer> positionX = new LinkedList<Integer>();
	  public LinkedList<Integer> positionY = new LinkedList<Integer>();
	  
	  private boolean[][] field;
	  private final int ZELLEN = 21;
	  private int monsterX;
	  private int monsterY;
	  private int zielX;
	  private int zielY;
	  private boolean suche = true;
	  
	  LinkedList<Wegpunkt> openList;
	  LinkedList<Wegpunkt> closedList;
	  private Wegpunkt finish;
	  
	  
	  /** Team16: Sweet sixteen
	   *
	   * Konstruktor fuer den Astern.
	   * Initialisierung aller wichtigen Werte. 
	   * 
	   * @author Goekdag, Enes, 5615399
	   * 
	   * */
	  public Astern(int monsterY,int monsterX,int zielX,int zielY, Spielelement[][] karte){
		  this.karte =karte;
		  this.monsterX=monsterX;
		  this.monsterY=monsterY;
		  this.zielX=zielX;
		  this.zielY=zielY;
		  this.gefunden=false;
		  
		  //initialisiere die listen
		    openList = new LinkedList<Wegpunkt>();
		    openList.add(new Wegpunkt(this.monsterX, this.monsterY, 1, this.abstandSchaetzen(this.monsterX, this.monsterY), null));
		    closedList = new LinkedList<Wegpunkt>();

		    

		    
		    //Hallo welt
		    //erzeuge ein zufaelliges feld
		    this.field = new boolean[ZELLEN][ZELLEN];
		    
		    for (int i = 0; i < ZELLEN; i++) {
		      for (int j = 0; j < ZELLEN; j++) {
		       if (karte[i][j] instanceof Boden || karte[i][j] instanceof Schluessel || karte[i][j] instanceof Heiltrank) {
				field[i][j]=true;
			}
		      }
		    }
		    
		    //r�ume start und endposition frei von steinen
		    this.field[this.zielX][this.zielY] = true;
		    this.field[this.monsterX][this.monsterY] = true;
		    
		  
	  }
	  
	  
	  /**
	   * Hauptfunktion des Astern-Algorithmus. Es wird iterativ der naechstbeste Wegpunkt berechnet,
	   * bis das gesuchte Ziel erreicht wurde.
	   * 
	   * @author Goekdag, Enes, 5615399 
	   * @return Wegpunkt der bisher berechnete letzte Wegpunkt des berechneten Weges
	   */
	  public Wegpunkt aStern() {
	        
	    Wegpunkt bestWegpunkt;
	    
	      //untersuche die vier nachbarn wenn m�glich
	      //also nicht wenn dort ein stein liegt
	      //und nicht wenn der punkt bereits in der closed list ist
	      bestWegpunkt = openList.get(this.getFirstBestListEntry(openList));
	      closedList.add(openList.remove(this.getFirstBestListEntry(openList)));
	      //System.out.println("untersuche: x:" + bestWegpunkt.x + " y:" + bestWegpunkt.y + " DeltaZiel: " + this.heuristik(bestWegpunkt.x, bestWegpunkt.y));
	      
	      //wenn der beste punkt das ziel ist, ist der beste weg gefunden
	      if (bestWegpunkt.x == this.zielX && bestWegpunkt.y == this.zielY) {
	        this.print(bestWegpunkt);
	        this.finish = bestWegpunkt;
	        gefunden=true;
	        return bestWegpunkt;
	      }

	      //oberhalb
	      this.openListAddHelper(bestWegpunkt.x, bestWegpunkt.y - 1, openList, closedList, bestWegpunkt);
	      
	      //rechts
	      this.openListAddHelper(bestWegpunkt.x + 1, bestWegpunkt.y, openList, closedList, bestWegpunkt);
	      
	      //unterhalb
	      this.openListAddHelper(bestWegpunkt.x, bestWegpunkt.y + 1, openList, closedList, bestWegpunkt);
	      
	      //links
	      this.openListAddHelper(bestWegpunkt.x - 1, bestWegpunkt.y, openList, closedList, bestWegpunkt);


	    return bestWegpunkt;
	  }
	  
	  public void print(Wegpunkt punkt) {
		  Wegpunkt vorgaenger = punkt.vorgaenger;
			if (vorgaenger != null) {
				print(vorgaenger);
			}
			this.speichereWeg(punkt.x, punkt.y);
			System.out.println("x:" + punkt.x + " y:" + punkt.y);
		}
	  public void speichereWeg(int x, int y){
		  System.out.println("Es wurde etwas gespeichert");
			positionX.addLast(x);
			positionY.addLast(y);
		}
	  /**
	   * Hilfsfunktion um einen neuen Knoten in die openList hinzuzufuegen.
	   * Es wird ueberprueft, dass der Knoten nicht bereits in der openList oder der closedList ist bzw.
	   * das der neue Knoten ueberhaupt noch auf der Map ist sowie das er begehbar ist. In diesem Fall
	   * wird er der openList hinzugefuegt, ansonsten nicht.
	   * 
	   * @author Goekdag, Enes, 5615399
	   * @param x x-Koordinate des neuen Knoten
	   * @param y y-Koordinate des neuen Knoten
	   * @param openList
	   * @param closedList
	   * @param vorher Vorgaenger-Knoten zum Verweisen im neuem Knoten
	   */
	  private void openListAddHelper(int x, int y, LinkedList<Wegpunkt> openList, LinkedList<Wegpunkt> closedList, Wegpunkt vorher){
		    if (x < 0 || y < 0 || x >= this.ZELLEN || y >= this.ZELLEN) {
		      //nichts
		    } else {
		      if (this.isPointInList(x, y, closedList) == false 
		          && this.field[x][y] == true
		          && this.isPointInList(x, y, openList) == false) {
		        openList.add(new Wegpunkt(x, y, 1, this.abstandSchaetzen(x, y), vorher));
		      }
		    }
		  }

	  
	  
	  /**
	   * Hilfsfunktion, die den Abstand eines bestimmten Knoten anhand seiner Koordinaten
	   * zum Zielknoten schaetzt.
	   * 
	   * @author Goekdag, Enes, 5615399
	   * @param x x-Koordinate des Knoten dessen Entfernung geschaetzt werden soll
	   * @param y y-Koordinate des Knoten dessen Entfernung geschaetzt werden soll
	   * @return Die geschaetze Entfernung
	   */
	  private int abstandSchaetzen(int x, int y) {                                      //SCHAETZFUNKTION des A*-Algo
		    int dx = x - this.zielX;
		    if (dx < 0) {
		      dx = -dx;
		    }
		    
		    int dy = y - this.zielY;
		    if (dy < 0) {
		      dy = -dy;
		    }
		    
		    return dx + dy;
		  }

	  /**
	   * Hilfsfunktion, die nachschaut welcher Knoten in der openList den (geschaetzt) geringsten Abstand
	   * zum Zielknoten hat und die Position des jeweiligen Knoten in der Liste zurueckgibt.
	   * 
	   * @author Goekdag, Enes, 5615399
	   * @param list Die closedList
	   * @return Die Position des besten Eintrags in der closedList
	   */
	  private int getFirstBestListEntry(LinkedList<Wegpunkt> list) {
		    int best = list.get(0).getGesamtkosten();
		    
		    for (int i = 1; i < list.size(); i++) {
		      if (list.get(i).getGesamtkosten() < best) {
		        best = list.get(i).getGesamtkosten();
		      }
		    }
		    
		    for (int i = 0; i < list.size(); i++) {
		      if (list.get(i).getGesamtkosten() == best) {
		        return i;
		      }
		    }
		    System.out.println("da ist etwas schiefgelaufen in 'getFirstBestListEntry'");
		    return 0;
		  }
	  
	  /**
	   * Hilfsfunktion die nachschaut ob ein bestimmter Wegpunkt in der Liste ist.
	   * 
	   * @author Goekdag, Enes, 5615399
	   * @param x x-Koordinate des Wegpunktes
	   * @param y y-Koordinate des Wegpunktes
	   * @param list Liste in der der Wegpunkt gesucht werden soll
	   * @return true falls der Wegpunkt in der Liste ist, sonst false
	   */
	  private boolean isPointInList(int x, int y, LinkedList<Wegpunkt> list) {
		    for (int i = 0; i < list.size(); i++) {
		      if (list.get(i).x == x && list.get(i).y == y) {
		        return true;
		      }
		    }
		    
		    return false;
		  }
	  
	  /**
	   * Setzt alles zurueck auf Anfang.
	   * 
	   * @author Goekdag, Enes, 5615399
	   */
	  public void reset() {
	    openList = new LinkedList<Wegpunkt>();
	    openList.add(new Wegpunkt(this.monsterX, this.monsterY, 1, this.abstandSchaetzen(this.monsterX, this.monsterY), null));
	    closedList = new LinkedList<Wegpunkt>();
	    this.finish = null;
	  }
	  
	  /**
	   * Funktion, die die iterative Berechnung des Weges mit dem Astern startet und am Ende den berechneten
	   * ersten Schritt fuer die Figur (entlang des berechneten Weges) zurueck gibt.
	   * 
	   * @return Wegpunkt des berechneten Weges, auf den die Figur als naechstes gehen soll.
	   */
	  public Wegpunkt starten() {
		  Wegpunkt wegpunkt=null ;
		  while (this.suche) {
              wegpunkt = null;
		    if (this.finish != null) {
		      	wegpunkt = this.finish;
		      	// Haben den kompletten Weg berechnet
		      	// Gehen den Weg nun zurueck zum Spieler/Monster bis zum ersten Feld auf das die Figur gehen muss.
		      	if(wegpunkt.vorgaenger!=null) {
		      		while(wegpunkt.vorgaenger.vorgaenger != null) {
		      			wegpunkt = wegpunkt.vorgaenger;
		      		}
		      	}
		      	this.suche = false;
		    } else if (this.openList.isEmpty() == false && this.suche == true){
		      wegpunkt = this.aStern();
		    }
		}
		  return wegpunkt;
	  
	  }
}
