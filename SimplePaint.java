import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class SimplePaint {
	public static void main(String[] args) {	
	
		PaintFrame frame = new PaintFrame("Simple Paint");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.pack();
		frame.show();
	}
}