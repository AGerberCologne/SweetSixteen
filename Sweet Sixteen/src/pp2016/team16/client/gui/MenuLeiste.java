package pp2016.team16.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import pp2016.team16.shared.Map;

/**
 * Klasse für die Menueleiste die oben angezeigt wird
 * @author Simon Nietz, Matr_Nr: 5823560
 *
 */

public class MenuLeiste extends JMenuBar implements ActionListener, WindowListener {

	private static final long serialVersionUID = 1L;
	public LoginDialog dialog;
	private MiniMap mini;
	public Map map;
	private boolean anzeigen=false;
	
	// die einzelnen Menuepunkte
    private JMenu spiel;
    private JMenu anzeige;
	private JMenu hilfe;
    private JMenu minimap;
    private JMenu chat;
	
    //die Unterpunkte der Menues
    private JMenuItem neuesSpiel;
    private JMenuItem highscore;
    private JMenuItem beenden;
    private JMenuItem karteaufdecken;
    private JMenuItem steuerung;
    private JMenuItem logout;
    private JMenuItem minimapzeigen;
    private JMenuItem minimapverstecken;
    private JMenuItem speicher;
    private JMenuItem cheats;
    private JMenuItem einloggen;
    private JMenuItem chat2;
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
		chat = new JMenu("Chat");
        
		// dieses sind die Unterpunkte
        neuesSpiel = new JMenuItem("Neues Spiel starten");
        highscore = new JMenuItem("Highscore anzeigen");
        beenden = new JMenuItem("Beenden");
        karteaufdecken = new JMenuItem("Karte aufdecken");
        steuerung = new JMenuItem("Steuerung");
        logout = new JMenuItem("Logout");
        minimapzeigen = new JMenuItem("Minimap zeigen");
        minimapverstecken = new JMenuItem("Minimap verstecken");
        speicher = new JMenuItem("Speichern");
        cheats = new JMenuItem("Cheats");
        einloggen = new JMenuItem("Login");
        chat2= new JMenuItem("Chat anzeigen");
        
        neuesSpiel.addActionListener(this);
        logout.addActionListener(this);
        highscore.addActionListener(this);
        beenden.addActionListener(this);
        karteaufdecken.addActionListener(this);
        steuerung.addActionListener(this);
        hilfe.addActionListener(this);
        minimapzeigen.addActionListener(this);
        minimapverstecken.addActionListener(this);
        speicher.addActionListener(this);
        cheats.addActionListener(this);
        einloggen.addActionListener(this);
        chat2.addActionListener(this);
        
        spiel.add(neuesSpiel);
        spiel.add(einloggen);
        spiel.add(logout);
        spiel.add(speicher);
        spiel.add(beenden);
        anzeige.add(highscore);
        anzeige.add(karteaufdecken);
        hilfe.add(steuerung);
        hilfe.add(cheats);
        minimap.add(minimapzeigen);
        minimap.add(minimapverstecken);
        chat.add(chat2);
        
        this.add(spiel);
        this.add(anzeige);
        this.add(hilfe);
        this.add(minimap);
        this.add(chat);
        // Aktionen die aufgerufen werden, falls draufgeklickt wird
	}
	public void windowClosing(WindowEvent e) {
		fenster.engine.beende(fenster.engine.map.levelzaehler);
	}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}
	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == neuesSpiel){
			// Popup schwierigkeit wird gelöscht Message
			//Methode von Client zum loeschen der Daten
			try {
				fenster.engine.changeLevel();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			fenster.startZeit=System.currentTimeMillis();
			//fenster.spielZuruecksetzen();
			//fenster.zeigeSpielfeld();
			
		}else if(e.getSource() == speicher){
			//System.out.println(map.levelzaehler);
			fenster.engine.speichereLevel(fenster.engine.map.levelzaehler);
			System.out.println("Methode funktioniert");
		
		}else if(e.getSource() == cheats){
		
			fenster.zeigeCheats();
		}else if(e.getSource() == chat2){
			System.out.println("Chat angekommen");
			fenster.chat();
			
		}else if(e.getSource() == logout){
		//	fenster.engine.logout(fenster.engine.map.levelzaehler);
			try {
				fenster.engine.logout(fenster.engine.map.levelzaehler);
				fenster.zeigeLogin();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if(LoginDialog.isSucceded()){
				try {
					fenster.engine.changeLevel();
					fenster.startZeit=System.currentTimeMillis();
				} catch (Exception e3) {
					e3.printStackTrace();
				}
			}
			
		}else if(e.getSource() == einloggen){
			if(LoginDialog.test2 == true){
				JOptionPane.showMessageDialog(null,
                        "Du bist schon eingeloggt",
                        "Anmeldung",
                        JOptionPane.INFORMATION_MESSAGE);
			}else {
				fenster.zeigeLogin();
			}
			
			
		}else if(e.getSource() == highscore){
			if(fenster.highscoreAngezeigt){
				fenster.zeigeSpielfeld();
				highscore.setText("Highscore anzeigen");
			}else{
				System.out.println("HighScore wird bei Client angefragt");
				fenster.engine.schickeHighScore();
				for(int i = 0; i <2; i++){
				for(int j = 0; j<5;j++){
					fenster.highscore.highScore.add(fenster.engine.highscore.get(j));
				}}
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
			
			fenster.engine.beende(fenster.engine.map.levelzaehler); 
			fenster.dispose();
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
