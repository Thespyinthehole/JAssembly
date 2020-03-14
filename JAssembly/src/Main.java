import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Main {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Main window = new Main();
		window.frame.setVisible(true);
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new Screen();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		new Timer().scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				panel.repaint();
			}
		}, 0L, 1L);
	}

}
