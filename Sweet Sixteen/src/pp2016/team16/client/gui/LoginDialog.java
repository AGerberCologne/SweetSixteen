package pp2016.team16.client.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
 
public class LoginDialog extends JDialog {
 
	public static ArrayList<String> User = new ArrayList<String>();
	public static ArrayList<String> Passwort = new ArrayList<String>();
  
	private static final long serialVersionUID = 1L;
	private static JTextField tfUsername;
    private JPasswordField pfPassword;
    public JLabel lbUsername;
    private JLabel lbPassword;
    private JButton btnLogin;
    private JButton btnAnmelden;
    private JButton btnCancel1;
    public static boolean succeeded;
    public static boolean test;

    public LoginDialog(Frame parent) {
        super(parent, "Login", true);
        //
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
        
        //erstelle die Buttons Benutername, Passwort mit den zugehörigen Textfeldern 
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
 
        btnLogin = new JButton("Login");
 
        btnLogin.addActionListener(new ActionListener() {
 
        	// abfragen, ob der eingegeben Benutzername existiert und das Passwort stimmt
            public void actionPerformed(ActionEvent e) {
                if (Loginn.authenticate(getUsername(), getPassword())) {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Hi " + getUsername() + "! Du hast dich erfolgreich eingeloggt.",
                            "Login",
                            JOptionPane.INFORMATION_MESSAGE);
                    succeeded = true;
                    test= true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Ungültiger Benutzername oder Passwort",
                            "Login",
                            JOptionPane.ERROR_MESSAGE);
                    // Benutzername und Passwort zurücksetzen
                    tfUsername.setText("");
                    pfPassword.setText("");
                    succeeded = false;
 
                }
            }
        });
        //möglichkeit ohne sich einzuloggen zu spielen
        btnCancel1 = new JButton("Spiele als Gast");
        btnCancel1.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                dispose();
                test=true;
            }
        });
        
        btnAnmelden = new JButton("Neu Anmelden");
        btnAnmelden.addActionListener(new ActionListener(){
        	
        	public void actionPerformed(ActionEvent e) {   
        		if(User.contains(getUsername())){
        			JOptionPane.showMessageDialog(LoginDialog.this,
                            "Der Benutzername ist bereits vergeben!",
                            "Login",
                            JOptionPane.ERROR_MESSAGE);
        			succeeded = false;
        		}else if(getUsername().equals("") || getPassword().equals("")){
        			JOptionPane.showMessageDialog(LoginDialog.this,
                            "Bitte gib ein Namen und ein Passwort ein",
                            "Login",
                            JOptionPane.ERROR_MESSAGE);
        			succeeded = false;
        			
        		}else {
        		User.add(getUsername());
        		Passwort.add(getPassword());
        		JOptionPane.showMessageDialog(LoginDialog.this,
                        "Hi " + getUsername() + "! Du hast dich erfolgreich neu angemeldet",
                        "Anmeldung",
                        JOptionPane.INFORMATION_MESSAGE);
                succeeded = false;
                tfUsername.setText("");
                pfPassword.setText("");
                test= true;
        		}
        		
        	}
        });
        
        JPanel bp = new JPanel();
        bp.add(btnLogin);
        bp.add(btnCancel1);
        bp.add(btnAnmelden);
        
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
 
    public static String getUsername() {
        return tfUsername.getText().trim();
    }
 
    public String getPassword() {
        return new String(pfPassword.getPassword());
    }
 
    public boolean isSucceeded() {
        return succeeded;
    }
    
    public static class Loginn {
    	static int a;
		static int b;
		static boolean c;
		static boolean d;
        public static boolean authenticate(String username, String password) {
        
        	//Verlgeiche ob das Passwort bei gegebenen Benutzernamen stimmt und ob sie in der gleichen Stelle gespeichert wurden.
        	for(String u : LoginDialog.User){
        		if(u.equals(username)){
        			  c = true;
        			  
        			   a = User.indexOf(username);
        			
        		}
        	}
        	for(String p : LoginDialog.Passwort){
            	if(p.equals(password)){
            		d = true; 
            		
            		  
            		b = User.indexOf(password);
            		
            	}
        	}
            	if(c==true && d==true && c==d){
            		return true;}
            	else
            		return false;
            	
    }
    }
}