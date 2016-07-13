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

/**
 * <Klasse um ein JFrame zu erzeugen fuer das Chat Fenster.>
 * @author Simon Nietz, Matr_Nr: 5823560
 * 
 */

public class Chat extends JFrame   {

	private static final long serialVersionUID = -9142707690379587523L;

	JFrame jFrame = new JFrame("Chatfenster");

	public HindiBones fenster;
	public JTextField in = new JTextField(3); // Erzeugt ein neues JTextField in dem die Nachricht eingegeben wird
	public JButton senden = new JButton("Senden"); // Erzeugt einen neuen JButton
	public JTextArea out = new JTextArea(12,1); // Erzeugt eine neue JTextArea, auf der die Nachrichten ausgegeben werden
	private final static String newline = "\n"; // Erzeugt eine neue Zeile
	public JScrollPane scrollPane; 

	/**
	 * <Konstruktor der ein neues ChatFenster erzeugt.>
	 * @param fenster Objekt von HindiBones 
	 * @author Simon Nietz, Matr_Nr: 5823560
	 */


	public Chat(HindiBones fenster){
		this.fenster=fenster;
		JPanel jPanel = new JPanel(new BorderLayout());
		JPanel panel = new JPanel(new BorderLayout());

		//Die JTextArea soll von der Groeße her nicht veraendert werden koennen
		out.setEditable(false);

		//Ein vertikales scrollen wird eingebaut.
		scrollPane = new JScrollPane(out);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		//Die Groeße des Frames wird festgelegt
		jFrame.setSize(258,288);
		// Groeße soll nicht veraendert werden koennen
		jFrame.setResizable(false); // soll konstante Größe haben
		final Dimension d = jFrame.getToolkit().getScreenSize();
		// Definiere die Position
		jFrame.setLocation((int) ((d.getWidth() - jFrame.getWidth()) / 2)+((32*16)/2)+162,
				(int) ((d.getHeight() - jFrame
						.getHeight()) / 2)-((32*16)/2)+430);
		// Die Moeglichkeit zu scrollen wird zu dem Panel hinzugefügt
		panel.add(scrollPane);

		// Füge dem Panel jPanel den Button und das Textfield hinzu
		jPanel.add(in, BorderLayout.NORTH);
		jPanel.add(senden, BorderLayout.SOUTH);

		// Füge dem JFrame jFrame die beiden Panels hinzu.
		jFrame.add(jPanel, BorderLayout.SOUTH);
		jFrame.add(panel, BorderLayout.NORTH);

		jFrame.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenster.toFront(); // das Spiel sollte direkt weiterspielbar sein

		//Der Button senden kriegt einen ActionListener,
		//Auf Aktion wird der Text aus dem Textfield in der Textarea ausgegeben
		senden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){

				String text = in.getText();
				out.append(text + newline);
				in.setText("");

			}
		});
	}



}




