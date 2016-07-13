package pp2016.team16.client.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;




import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


public class Chat extends JFrame   {

    private static final long serialVersionUID = -9142707690379587523L;
    
    public HindiBones fenster;
    public JTextField in = new JTextField(3);
    public JButton senden = new JButton("Senden");
    public JTextArea out = new JTextArea(12,1);
    private final static String newline = "\n";
    public JScrollPane scrollPane;
    
    
    public Chat(HindiBones fenster){
    	this.fenster=fenster;
    	JFrame jFrame = new JFrame("Chatfenster");
    	JPanel jPanel = new JPanel(new BorderLayout());
    	JPanel panel = new JPanel(new BorderLayout());
  
	//    out.setPreferredSize(new Dimension());
	    out.setEditable(false);
	   // out.setVisible(true);
	    
	    scrollPane = new JScrollPane(out);
	    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    
    	jFrame.setSize(258,288);
    	panel.add(scrollPane);
        jFrame.setResizable(false); // soll konstante Größe haben
		final Dimension d = jFrame.getToolkit().getScreenSize();
		jFrame.setLocation((int) ((d.getWidth() - jFrame.getWidth()) / 2)+((32*16)/2)+162,
				(int) ((d.getHeight() - jFrame
						.getHeight()) / 2)-((32*16)/2)+430);
	    
		
		
	    jPanel.add(in, BorderLayout.NORTH);
	    jPanel.add(senden, BorderLayout.SOUTH);
	    
	  //  panel.add(out, BorderLayout.CENTER);
	    jFrame.add(jPanel, BorderLayout.SOUTH);
	    jFrame.add(panel, BorderLayout.NORTH);

//	    jFrame.add(out, BorderLayout.NORTH);
	    jFrame.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenster.toFront(); // das Spiel sollte direkt weiterspielbar sein

        senden.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e){
        
        	       //this will give you myText's contents. Do what you will with them.
        	       String text = in.getText();
        	       out.append(text + newline);
        	     //   in.selectAll();
        	       in.setText("");
        	       
        	    }
        	});
        }
			
    public void zuChat(String tex){
   // 	this.text = text;
    	out.append(tex + newline);
    }
  
    
	
	}

    


