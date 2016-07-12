package pp2016.team16.client.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;


import javax.swing.JFrame;

import javax.swing.JTextArea;


public class Chat extends JFrame {

    private static final long serialVersionUID = -9142707690379587523L;
    
    public HindiBones fenster;
 //   JFrame jFrame = new JFrame();
 //	JTextArea jTextMessage = new JTextArea();
    // private JTextArea user = new JTextArea(15, 5);

    public Chat(HindiBones fenster){
    	this.fenster=fenster;
    	JFrame jFrame = new JFrame();
    	JTextArea jTextMessage = new JTextArea();
    	jFrame.setSize(258,288);
		jFrame.setResizable(false); // soll konstante Größe haben
		
		jFrame.setVisible(true);
		final Dimension d = jFrame.getToolkit().getScreenSize();
		jFrame.setLocation((int) ((d.getWidth() - jFrame.getWidth()) / 2)+((32*16)/2)+162,
				(int) ((d.getHeight() - jFrame
						.getHeight()) / 2)-((32*16)/2)+430);
		Container c = jFrame.getContentPane();
		c.add(jTextMessage);
		
		//jFrame.add
		this.setBackground(Color.BLACK);
		
		//showMessages();
		
		fenster.toFront(); // das Spiel sollte direkt weiterspielbar sein
	}

	
	}

    


