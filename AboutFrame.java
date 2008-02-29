import java.awt.*;
import javax.swing.*;
import javax.swing.JFrame;

public class AboutFrame extends JFrame {
	public AboutFrame() {
		super();
		getContentPane().add(new AboutPane());
		pack();
		show();
	}
	
	private class AboutPane extends JPanel {
		private AboutPane() {
			add(new JLabel("Welcome to SimplePaint."));
			add(new JLabel("TODO: add about and simple help"));
		}
	}
}