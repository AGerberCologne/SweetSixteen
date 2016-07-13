package pp2016.team16.client.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import pp2016.team16.client.engine.ClientEngine;
import pp2016.team16.client.gui.HindiBones;

/**
 * <Klasse für den LoginDialog, es speichert bei einer Neuanmeldung, Name und Passwort in einer Liste.
 * Bei einem Einloggen, wird abgefragt ob die eingegebenen Daten zulaessig sind.>
 * 
 * @author Simon Nietz, Matr_Nr: 5823560
 *
 */

public class LoginDialog extends JDialog {
	public ClientEngine engine;

	private HindiBones fenster;
	private static final long serialVersionUID = 1L;
	private static JTextField tfUsername;
	private JPasswordField pfPassword;
	public JLabel lbUsername;
	private JLabel lbPassword;
	private JButton btnLogin;
	private JButton btnAnmelden;
	public static boolean succeeded;
	public static boolean loginerfolgreich;
	public static boolean zulaessigeDaten;
	public String passwortname;

	/**
	 * <Der Konstruktor LoginDialog erstellt ein neues JPanel. Dem JPanel werden JButtons und JTextField hinzugefeugt.>
	 * @param parent das Frame auf dem der Dialog ausgefuehrt wird
	 * @param fenster fenster von HindiBones 
	 * @author Simon Nietz, Matr_Nr: 5823560
	 */
	
	public LoginDialog(Frame parent, HindiBones fenster) {
		super(parent, "Login", true);
		this.fenster = fenster;

		// erzeuge ein neues JPanel
		// ordne die Buttons mithilfe GridBagLayout
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();

		cs.fill = GridBagConstraints.HORIZONTAL;

		// erstelle die Buttons Benutername, Passwort mit den zugehörigen
		// Textfeldern
		lbUsername = new JLabel("Benutzername: ");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		panel.add(lbUsername, cs);

		tfUsername = new JTextField(20);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 2;
		panel.add(tfUsername, cs);

		lbPassword = new JLabel("Passwort: ");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		panel.add(lbPassword, cs);

		pfPassword = new JPasswordField(20);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 2;
		panel.add(pfPassword, cs);
		panel.setBorder(new LineBorder(Color.GRAY));

		// Fuege den JButton Login und den ActionListener  hinzu
		btnLogin = new JButton("Login");
		
		btnLogin.addActionListener(new ActionListener() {

			/**
			 * <ActionListener fuer den JButton Login>
			 * @author Simon Nietz, Matr_Nr: 5823560
			 */
			public void actionPerformed(ActionEvent e) {
				if (getUsername().equals("") || getPassword().equals("")) {
					JOptionPane.showMessageDialog(LoginDialog.this,
							"Gib einen Benutzernamen und ein Passwort ein!",
							"Anmelden", JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						// Ueberpruefe, ob die Daten zulaessig sind
						zulaessigeDaten = fenster.engine.login(2, getUsername(),
								getPassword());
					} catch (InterruptedException e1) {
					}
					if (zulaessigeDaten == true) {
						// Falls die Daten zulaessig sind, wird der Benutzer eingeloggt
						JOptionPane.showMessageDialog(LoginDialog.this, "Hi "
								+ getUsername()
								+ "! Du hast dich erfolgreich eingeloggt.",
								"Login", JOptionPane.INFORMATION_MESSAGE);
						loginerfolgreich = true;

						dispose();
					} else {
						// Falls nicht, wird eine Error Messag angezeigt
						JOptionPane.showMessageDialog(LoginDialog.this,
								"Ungültiger Benutzername oder Passwort",
								"Login", JOptionPane.ERROR_MESSAGE);
						// Benutzername und Passwort zuruecksetzen
						tfUsername.setText("");
						pfPassword.setText("");
						loginerfolgreich = false;
					}
				}
			}
		});
		

		// neuen Benutzer erstellen
		btnAnmelden = new JButton("Neu Anmelden");
		btnAnmelden.addActionListener(new ActionListener() {
			/**
			 * <Fuege den ActionListener fuer den JButtoen Neu Anmelden hinzu>
			 * @author Simon Nietz, Matr_Nr: 5823560
			 */
			public void actionPerformed(ActionEvent e) {
				if (getUsername().equals("") || getPassword().equals("")) {
					JOptionPane.showMessageDialog(LoginDialog.this,
							"Gib einen Benutzernamen und ein Passwort ein!",
							"Anmelden", JOptionPane.ERROR_MESSAGE);
				} else {

					try {
						// Ueberpruefe erneut ob die Daten zulaessig sind
						zulaessigeDaten = fenster.engine.login(1, getUsername(),
								getPassword());
					} catch (InterruptedException e1) {
					}

					if (zulaessigeDaten == true) {
						// Falls die Daten zulaessig sind, wird ein neuer Benutzer angelegt
						JOptionPane.showMessageDialog(LoginDialog.this, "Hi "
								+ getUsername()
								+ "! Du hast dich erfolgreich neu angemeldet",
								"Anmeldung", JOptionPane.INFORMATION_MESSAGE);
						loginerfolgreich = false;
						tfUsername.setText("");
						pfPassword.setText("");
					} else {
						// Falls die Daten nicht zulaessig sind, wird eine Error Message angezeigt
						JOptionPane.showMessageDialog(LoginDialog.this,
								"Der Benutzername ist bereits vergeben!",
								"Login", JOptionPane.ERROR_MESSAGE);
						loginerfolgreich = false;
					}
				}
			}

		});
		
		// Fuege die Buttons und das Panel hinzu
		JPanel bp = new JPanel();
		bp.add(btnLogin);
		bp.add(btnAnmelden);
		
		// Setze die Position des Buttons und das Panel hinzu
		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(bp, BorderLayout.PAGE_END);

		pack();
		setResizable(false);
		setLocationRelativeTo(parent);
	}

	/**
	 * <Gibt den Text aus dem JTextField zurueck>
	 * @return gibt den Text im JTextField zurueck
	 * @author Simon Nietz, Matr_Nr: 5823560
	 */
	public static String getUsername() {
		return tfUsername.getText().trim();
	}

	/**
	 * <Gibt den String aus dem JPasswordField zurueck>
	 * @return das Passwort als String
	 * @author Simon Nietz, Matr_Nr: 5823560
	 */
	public String getPassword() {
		return new String(pfPassword.getPassword());

	}

	public static boolean isSucceded() {
		return loginerfolgreich;
	}

}