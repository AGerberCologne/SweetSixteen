package pp2016.team16.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MenuLeiste extends JMenuBar implements ActionListener {

	private static final long serialVersionUID = 1L;

	private Minimap mini;
	private boolean anzeigen=false;
	
    private JMenu spiel;
    private JMenu anzeige;
	private JMenu hilfe;
    private JMenu minimap;
	
    private JMenuItem neuesSpiel;
    private JMenuItem einloggen;
    private JMenuItem highscore;
    private JMenuItem beenden;
    private JMenuItem karteaufdecken;
    private JMenuItem steuerung;
    private JMenuItem ausloggen;
    private JMenuItem minimapzeigen;
    private JMenuItem minimapverstecken;
    
    
    
    private HindiBones fenster;
    
	public MenuLeiste(HindiBones fenster){
		this.fenster = fenster;
		
		spiel = new JMenu("Spiel");
		anzeige = new JMenu("Anzeige");
		hilfe = new JMenu("Hilfe");
		minimap = new JMenu("Minimap");
        
        neuesSpiel = new JMenuItem("Neues Spiel starten");
        einloggen = new JMenuItem("Einloggen");
        highscore = new JMenuItem("Highscore anzeigen");
        beenden = new JMenuItem("Beenden");
        karteaufdecken = new JMenuItem("Karte aufdecken");
        steuerung = new JMenuItem("Steuerung");
        ausloggen = new JMenuItem("Ausloggen");
        minimapzeigen = new JMenuItem("Minimap zeigen");
        minimapverstecken = new JMenuItem("Minimap verstecken");
        
        neuesSpiel.addActionListener(this);
        einloggen.addActionListener(this);
        ausloggen.addActionListener(this);
        highscore.addActionListener(this);
        beenden.addActionListener(this);
        karteaufdecken.addActionListener(this);
        steuerung.addActionListener(this);
        hilfe.addActionListener(this);
        minimapzeigen.addActionListener(this);
        minimapverstecken.addActionListener(this);
        
        spiel.add(neuesSpiel);
        spiel.add(einloggen);
        spiel.add(ausloggen);
        spiel.add(beenden);
        anzeige.add(highscore);
        anzeige.add(karteaufdecken);
        hilfe.add(steuerung);
        minimap.add(minimapzeigen);
        minimap.add(minimapverstecken);
        
        this.add(spiel);
        this.add(anzeige);
        this.add(hilfe);
        this.add(minimap);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == neuesSpiel){
			fenster.spielZuruecksetzen();
			fenster.zeigeSpielfeld();
		}else if(e.getSource() == einloggen){
			if(LoginDialog.succeeded==true){
				JOptionPane.showMessageDialog(null,
						"Du bist schon eingeloggt");
			}else{
			fenster.zeigeLogin();
			}
		}else if(e.getSource() == ausloggen){
			if(LoginDialog.succeeded==true){
				LoginDialog.succeeded = false;
				fenster.spielZuruecksetzen();
				fenster.spieler.setName("Hindi");
				JOptionPane.showMessageDialog(null,							
                        "Du wurdest ausgeloggt"
                        );
				
				}else{ 
					JOptionPane.showMessageDialog(null,							
	                        "Du musst eingeloggt sein"
	                        );
				}
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
			mini=new Minimap(fenster);
				
			}else if(e.getSource() == minimapverstecken && anzeigen==true){
			anzeigen=false;
			mini.jFrame.dispose();
		}
	}
	
	
	public JMenuItem getHighscore(){
		return highscore;
	}
	
}
