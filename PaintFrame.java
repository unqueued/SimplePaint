import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class PaintFrame extends JFrame implements ActionListener {

	private JMenuItem menuNew, menuOpen, menuExit,
		menuSave, menuSaveAs, menuPrint, menuAbout;
	private JMenu menuFile, menuHelp;
	private JMenuBar menuBar;
	
	private ContentPanel contentPanel;
	
	public PaintFrame(String title) {
		super(title);
		
		contentPanel = new ContentPanel(this);
		
		menuNew = new JMenuItem("New", new ImageIcon("images/new.png"));
		menuNew.addActionListener(this);
		menuOpen = new JMenuItem("Open", new ImageIcon("images/open.png"));
		menuOpen.addActionListener(this);
		menuSave = new JMenuItem("Save", new ImageIcon("images/save.png"));
		menuSave.addActionListener(this);
		menuSaveAs = new JMenuItem("Save As", new ImageIcon("images/saveas.png"));
		menuSaveAs.addActionListener(this);
		menuPrint = new JMenuItem("Print", new ImageIcon("images/print.png"));
		menuPrint.addActionListener(this);
		menuExit = new JMenuItem("Exit", new ImageIcon("images/exit.png"));
		menuExit.addActionListener(this);
		menuAbout = new JMenuItem("About", new ImageIcon("images/help.png"));
		menuAbout.addActionListener(this);
				
		menuFile = new JMenu("File");
		menuFile.add(menuNew);
		menuFile.add(menuOpen);
		menuFile.add(menuSave);
		menuFile.add(menuSaveAs);
		menuFile.add(menuPrint);
		menuFile.add(menuExit);

		menuHelp = new JMenu("Help");
		menuHelp.add(menuAbout);
		
		menuBar = new JMenuBar();
		menuBar.add(menuFile);
		menuBar.add(menuHelp);
		
		setJMenuBar(menuBar);
		
		
		//And launch our content panel
		getContentPane().add(contentPanel);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == menuNew)
			contentPanel.newFile();
		else if(e.getSource() == menuOpen)
			contentPanel.open();
		else if(e.getSource() == menuSave)
			contentPanel.save();
		else if(e.getSource() == menuSaveAs)
			contentPanel.saveAs();
		else if(e.getSource() == menuPrint)
			System.out.println("Print");
		else if(e.getSource() == menuAbout)
			new AboutFrame();
		else if(e.getSource() == menuExit)
			die();
	}
	
	private void die() {
		//inform contentpanel that we are exiting, confirm save
		System.exit(0);
	}
}