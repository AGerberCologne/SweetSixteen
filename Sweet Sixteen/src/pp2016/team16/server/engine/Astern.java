package pp2016.team16.server.engine;

import java.util.LinkedList;

import pp2016.team16.shared.*;


/* Team16: Sweet sixteen
 * Goekdag, Enes, 5615399
 * 
 * */
public class Astern {
	  HindiBones fenster;
	  private int x;
	  private int y;
	  private boolean gefunden;
	  
	  private boolean[][] field;
	  private final int ZELLEN = 16;
	  private int monsterX;
	  private int monsterY;
	  private int zielX;
	  private int zielY;
	  private boolean suche = true;
	  
	  LinkedList<Wegpunkt> openList;
	  LinkedList<Wegpunkt> closedList;
	  private Wegpunkt finish;
	  
	  public Astern(int monsterY,int monsterX,int zielX,int zielY,HindiBones fenster){
		  this.fenster=fenster;
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
		       if (fenster.level[i][j] instanceof Boden) {
				field[i][j]=true;
			}
		      }
		    }
		    
		    //r�ume start und endposition frei von steinen
		    this.field[this.zielX][this.zielY] = true;
		    this.field[this.monsterX][this.monsterY] = true;
		    
		  
	  }
	  
	  
	
   //aStern algorithmus
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
	        bestWegpunkt.print();
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
	  
	  // �berpr�ft ob das Feld schon untersucht wurde, 
	  //pr�ft auch ob da das Feld begehbar ist und obs schon in der openlist ist
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

	  
	  
	  //Sch�tzt den Abstand zum Ziel,wenn keine hindernisse im Weg w�ren
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
//sucht das f�r uns beste Element aus der Liste
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
	  
	  private boolean isPointInList(int x, int y, LinkedList<Wegpunkt> list) {
		    for (int i = 0; i < list.size(); i++) {
		      if (list.get(i).x == x && list.get(i).y == y) {
		        return true;
		      }
		    }
		    
		    return false;
		  }
	  
	  //resettet alles
	  public void reset() {
	    openList = new LinkedList<Wegpunkt>();
	    openList.add(new Wegpunkt(this.monsterX, this.monsterY, 1, this.abstandSchaetzen(this.monsterX, this.monsterY), null));
	    closedList = new LinkedList<Wegpunkt>();
	    this.finish = null;
	  }
	  public Wegpunkt starten() {
		  Wegpunkt wegpunkt=null ;
		  while (this.suche) {
              wegpunkt = null;
		    if (this.finish != null) {
		      	wegpunkt = this.finish;
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
