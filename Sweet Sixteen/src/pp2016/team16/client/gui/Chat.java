package pp2016.team16.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


public class Chat extends JFrame   {

	private static final long serialVersionUID = -9142707690379587523L;

	JFrame jFrame = new JFrame("Chatfenster");

	public HindiBones fenster;
	public JTextField in = new JTextField(3);
	public JButton senden = new JButton("Senden");
	public JTextArea out = new JTextArea(12,1);
	private final static String newline = "\n";
	public JScrollPane scrollPane;


	public Chat(HindiBones fenster){
		this.fenster=fenster;
		JPanel jPanel = new JPanel(new BorderLayout());
		JPanel panel = new JPanel(new BorderLayout());

		out.setEditable(false);

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

		jFrame.add(jPanel, BorderLayout.SOUTH);
		jFrame.add(panel, BorderLayout.NORTH);

		jFrame.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenster.toFront(); // das Spiel sollte direkt weiterspielbar sein

		senden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){

				String text = in.getText();
				out.append(text + newline);
				in.setText("");

			}
		});
	}



}




