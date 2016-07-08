package pp2016.team16.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * Klasse für die Menueleiste die oben angezeigt wird
 * @author Simon Nietz, Matr_Nr: 5823560
 *
 */

public class MenuLeiste extends JMenuBar implements ActionListener {

	private static final long serialVersionUID = 1L;

	private MiniMap mini;
	private boolean anzeigen=false;
	
	// die einzelnen Menuepunkte
    private JMenu spiel;
    private JMenu anzeige;
	private JMenu hilfe;
    private JMenu minimap;
	
    //die Unterpunkte der Menues
    private JMenuItem neuesSpiel;
    private JMenuItem highscore;
    private JMenuItem beenden;
    private JMenuItem karteaufdecken;
    private JMenuItem steuerung;
    private JMenuItem wechsel;
    private JMenuItem minimapzeigen;
    private JMenuItem minimapverstecken;
    private JMenuItem speicher;
    private JMenuItem cheats;
    /**
     * 
     */
    
    private HindiBones fenster;
    
    /**
     * @author Simon Nietz, Matr_Nr: 5823560
     * @param fenster fenster von der Anwendung
     */
    
	public MenuLeiste(HindiBones fenster){
		this.fenster = fenster;
		
		// diese werden oben angezeigt
		spiel = new JMenu("Spiel");
		anzeige = new JMenu("Anzeige");
		hilfe = new JMenu("Hilfe");
		minimap = new JMenu("Minimap");
        
		// dieses sind die Unterpunkte
        neuesSpiel = new JMenuItem("Neues Spiel starten");
        highscore = new JMenuItem("Highscore anzeigen");
        beenden = new JMenuItem("Beenden");
        karteaufdecken = new JMenuItem("Karte aufdecken");
        steuerung = new JMenuItem("Steuerung");
        wechsel = new JMenuItem("Benutzer wechseln");
        minimapzeigen = new JMenuItem("Minimap zeigen");
        minimapverstecken = new JMenuItem("Minimap verstecken");
        speicher = new JMenuItem("Speichern");
        cheats = new JMenuItem("Cheats");
        
        neuesSpiel.addActionListener(this);
        wechsel.addActionListener(this);
        highscore.addActionListener(this);
        beenden.addActionListener(this);
        karteaufdecken.addActionListener(this);
        steuerung.addActionListener(this);
        hilfe.addActionListener(this);
        minimapzeigen.addActionListener(this);
        minimapverstecken.addActionListener(this);
        speicher.addActionListener(this);
        cheats.addActionListener(this);
        
        spiel.add(neuesSpiel);
        spiel.add(wechsel);
        spiel.add(beenden);
        spiel.add(speicher);
        anzeige.add(highscore);
        anzeige.add(karteaufdecken);
        hilfe.add(steuerung);
        hilfe.add(cheats);
        minimap.add(minimapzeigen);
        minimap.add(minimapverstecken);
        
        this.add(spiel);
        this.add(anzeige);
        this.add(hilfe);
        this.add(minimap);
        
        // Aktionen die aufgerufen werden, falls draufgeklickt wird
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == neuesSpiel){
			// Popup schwierigkeit wird gelöscht Message
			//Methode von Client zum loeschen der Daten
			//fenster.spielZuruecksetzen();
			//fenster.zeigeSpielfeld();
			
		}else if(e.getSource() == speicher){
			
		}else if(e.getSource() == cheats){
		
			fenster.zeigeCheats();
	/*	}else if(e.getSource() == einloggen){
			if(LoginDialog.test==true){
				JOptionPane.showMessageDialog(null,
						"Du bist schon eingeloggt");
			}else{
			fenster.zeigeLogin();
			}*/
			// else if Level in gleicher schwierigkeit
			//
			//
			
		}else if(e.getSource() == wechsel){
			fenster.zeigeLogin();
			
			/* Benutzer wechseln  dann Login Fenster
			if(LoginDialog.succeeded==true){
				LoginDialog.succeeded = false;
				fenster.spielZuruecksetzen();
				fenster.engine.spieler.setName("Hindi");
				JOptionPane.showMessageDialog(null,							
                        "Du wurdest ausgeloggt"
                        );
				
				}else{ 
					JOptionPane.showMessageDialog(null,							
	                        "Du musst eingeloggt sein"
	                        );
				}*/
		}else if(e.getSource() == highscore){
			if(fenster.highscoreAngezeigt){
				fenster.zeigeSpielfeld();
				highscore.setText("Highscore anzeigen");
			}else{
				fenster.zeigeHighscore();
				highscore.setText("Spielfeld anzeigen");
			}
			
		}else if(e.getSource() == karteaufdecken){
			if(fenster.nebelAn){
				fenster.nebelAn = false;
				karteaufdecken.setText("Karte verdecken");
			}else{
				fenster.nebelAn = true;
				karteaufdecken.setText("Karte aufdecken");
			}		
		}else if(e.getSource() == beenden){
			System.exit(0);
		}else if(e.getSource() == steuerung){
			fenster.zeigeSteuerung();
		}else if(e.getSource() == minimapzeigen && anzeigen==false){
			anzeigen=true;
			mini=new MiniMap(fenster);
				
			}else if(e.getSource() == minimapverstecken && anzeigen==true){
			anzeigen=false;
			mini.jFrame.dispose();
		}
	}
	
	/**
	 * @author Simon Nietz, Matr_Nr: 5823560
	 * @return der Highscore wird geladen
	 */
	public JMenuItem getHighscore(){
		return highscore;
	}
	
}
