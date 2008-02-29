import java.awt.*;
import java.io.*;
import java.util.LinkedList;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.Image;
import java.util.Random;
import java.applet.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.*;

public class ContentPanel extends JPanel {

	private Brush currentBrush = new Brush();
	private JLabel currentColor;
	private DrawPane drawPane;
	private ButtonPanel buttonPanel;
	private Dimension toolButtonSize;
	private Dimension contentPanelSize;
	private Dimension colorButtonSize;
	private Dimension spacer;
	private StrokeBox strokeBox;
	private File file;
	private boolean dirty = false;
	private Component parentFrame;
	
	public final int SQUARE = 2;
	public final int OVAL = 1;
	public final int FILL = 3;

	public ContentPanel(Component p) {
	
		parentFrame = p;
		toolButtonSize = new Dimension(32, 32);
		contentPanelSize = new Dimension(800, 600);
		colorButtonSize = new Dimension(16, 16);
		spacer = new Dimension(10, 10);
		strokeBox = new StrokeBox();
		
		buttonPanel = new ButtonPanel();
		drawPane = new DrawPane();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		setPreferredSize(contentPanelSize);
		
		add(buttonPanel);
		add(drawPane);
	}
	
	public void newFile() {
		int confirm = 0;
	
		if(dirty)
			confirm = JOptionPane.showConfirmDialog (this, "Your work is not saved, would you like to save it?");
				if(confirm == 234)//JOptionPane.YES_OPTION)
					if(!save())
						return;
	
		strokeBox.eraseAll();
		drawPane.clearBackDropImage();
		dirty = false;
		file = null;
		drawPane.setUsableSize(new Dimension(drawPane.getWidth(),
															drawPane.getHeight()));
		repaint();
	}
	
	public boolean save() {
	
		Image image;
	
		if(file == null)
			saveAs();
		
		if(file == null)
			return false;
			
		image = drawPane.createImage(drawPane.getUsableWidth(), drawPane.getUsableHeight());
		Graphics g = image.getGraphics();
		drawPane.paint(g);
		
		try {
			ImageIO.write((RenderedImage)image, "png", file);
		}
		catch (IOException e) {}
		
		return true;
	}

	public void saveAs() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new PNGFilter());
		
		int result = chooser.showSaveDialog(null);
		
		if(result != JFileChooser.APPROVE_OPTION)
			return;
		else
			file = chooser.getSelectedFile();
			
		save();
	}

	public void open() {
		Image image;
		ImageIcon icon;
		newFile();
		
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new PNGFilter());
		int result = chooser.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION) {
//			image = Toolkit.getDefaultToolkit().getImage(chooser.getSelectedFile().toString());
			icon = new ImageIcon(chooser.getSelectedFile().toString());
			drawPane.setBackDropImage(icon.getImage());
		}
	}
	
	public class PNGFilter extends
		javax.swing.filechooser.FileFilter {
	
		public boolean accept(File f) {

		if (f.isDirectory())
		 	return true;
  
		String extension = getExtension(f);
	
		if((extension.equals("png")))
			return true;

		return false;
		}
    
		public String getDescription() {
			return "PNG Files";
		}

		private String getExtension(File f) {
			String s = f.getName();
			int i = s.lastIndexOf('.');
			if (i > 0 &&  i < s.length() - 1)
				return s.substring(i+1).toLowerCase();
			return "";
		}
	}
	
	private class ButtonPanel extends JPanel {
	
		private ImageIcon
			pencilIcon = new ImageIcon("images/stock-tool-pencil-22.png"),
			eraserIcon = new ImageIcon("images/stock-tool-eraser-22.png"),
			fillIcon = new ImageIcon("images/stock-tool-bucket-fill-22.png"),
			undoIcon = new ImageIcon("images/stock-undo-history-24.png"),
			redoIcon = new ImageIcon("images/stock-redo-history-24.png"),
			brushIcon = new ImageIcon("images/stock-tool-paintbrush-22.png");
			
		private final Brush pencilBrush = new Brush(null , new BasicStroke(3));
		private final JButton pencilButton = new ToolButton(pencilIcon, pencilBrush);
		private final Brush eraserBrush = new Brush(Color.white, new BasicStroke(9));
		private final JButton eraserButton = new ToolButton(eraserIcon, eraserBrush);
		private final Brush brushBrush = new Brush(currentBrush.getColor(), new BasicStroke(6));
		private final JButton brushButton = new ToolButton(brushIcon, brushBrush);
		private final JButton fillButton = new ToolButton(fillIcon, new Brush());
		private final JButton undoButton = new ToolButton(undoIcon, new Brush());
		private final JButton redoButton = new ToolButton(redoIcon, new Brush());
			
		private Pallet pallet = new Pallet();
		private Brushes brushes = new Brushes();
		private CustomPallet customPallet = new CustomPallet();
		private PreviewPanel preview = new PreviewPanel();
		private MainToolPanel mainToolPanel = new MainToolPanel();
	
		private ButtonPanel() {
			
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			setPreferredSize(new Dimension(0, 100));
         setMaximumSize(new Dimension(800, 100));
			
			add(mainToolPanel);
			add(pallet);
			add(customPallet);
			add(preview);
			add(brushes);

		}
		
		private class MainToolPanel extends JPanel {
			private MainToolPanel() {
			
			setLayout(new GridLayout(2, 6));
			
         setMaximumSize(new Dimension(120, 70));
			
			add(pencilButton);
			add(eraserButton);
			add(brushButton);
			add(fillButton);
			add(undoButton);
			add(redoButton);
			}
		}
		
		private class PreviewPanel extends JPanel {		
			private PreviewPanel() {
				PreviewLabel previewLabel = new PreviewLabel();
				setPreferredSize(new Dimension(40, 40));
				setMaximumSize(new Dimension(90, 75));
				setBorder(BorderFactory.createTitledBorder("Current"));
				
				add(previewLabel);
			}

			public class PreviewLabel extends JLabel {
				public PreviewLabel() {
					setPreferredSize(new Dimension(15, 15));
				}
				
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					currentBrush.drawBrush(g, this);
				}
			}
		}
		
		private class Brushes extends JPanel {
			private Brushes() {
				setBorder(BorderFactory.createTitledBorder("Brushes"));
				setMaximumSize(new Dimension(120, 70));
				setPreferredSize(new Dimension(60, 70));
				setVisible(false);
				
				for(int i = 1; i < 16; i += 3)
					add(new BrushButton(i));

			}
			
			public void refresh() {
				preview.repaint();
			}
			
			private class BrushButton extends JButton implements ActionListener {
			
				private float size;
			
				private BrushButton(float size) {
					super();
					this.size = size;
					setPreferredSize(new Dimension(15, 15));
					setBorderPainted(false);
					addActionListener(this);
				}
				
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					new Brush(Color.black, new BasicStroke(size)).drawBrush(g, this);
				}
				
				public void actionPerformed(ActionEvent e) {
					currentBrush.setBasicStroke(new BasicStroke(size));
						
					brushes.refresh();
				}
			}
		}
		
		private class ToolButton extends JButton implements ActionListener {
			
			private Brush brush;
			
			private ToolButton() {
				addActionListener(this);
			}
			
			private ToolButton(ImageIcon i, Brush b) {
				super(i);
				setMaximumSize(new Dimension(32, 32));
				brush = b;
				addActionListener(this);
			}
			
			public void actionPerformed (ActionEvent e) {
			
				currentBrush.enable();

				currentBrush.setBasicStroke(brush.getBasicStroke());
				
				brushes.refresh();
				
				if(e.getSource() == pencilButton ||
					e.getSource() == fillButton)
					brushes.setVisible(false);
				else
					brushes.setVisible(true);
					
				if(e.getSource() == eraserButton)
					currentBrush.setColor(Color.white);


				if(e.getSource() == fillButton) {
					currentBrush.disable();
					currentBrush.setToolType(FILL);
				}
				
				if(e.getSource() == undoButton)
					strokeBox.undo();
					
				if(e.getSource() == redoButton)
					strokeBox.redo();
				
				
				}		
		}
		
		private class Pallet extends JPanel implements ActionListener {
			
			private JButton buttonColor[] = new JButton[8];
			
			private Pallet() {
				setBorder(BorderFactory.createTitledBorder("Color selecter"));
				setMaximumSize(new Dimension(100, 75));
				setPreferredSize(new Dimension(100, 75));
				
				for(int c = 0; c < buttonColor.length; c ++)
					buttonColor[c] = new JButton();
				
				buttonColor[0].setBackground(Color.red);
				buttonColor[0].setPreferredSize(colorButtonSize);
				buttonColor[0].addActionListener(this);
				buttonColor[1].setBackground(Color.green);
				buttonColor[1].setPreferredSize(colorButtonSize);
				buttonColor[1].addActionListener(this);
				buttonColor[2].setBackground(Color.blue);
				buttonColor[2].setPreferredSize(colorButtonSize);
				buttonColor[2].addActionListener(this);
				buttonColor[3].setBackground(Color.yellow);
				buttonColor[3].setPreferredSize(colorButtonSize);
				buttonColor[3].addActionListener(this);
				buttonColor[4].setBackground(Color.orange);
				buttonColor[4].setPreferredSize(colorButtonSize);
				buttonColor[4].addActionListener(this);
				buttonColor[5].setBackground(Color.white);
				buttonColor[5].setPreferredSize(colorButtonSize);
				buttonColor[5].addActionListener(this);
				buttonColor[6].setBackground(Color.black);
				buttonColor[6].setPreferredSize(colorButtonSize);
				buttonColor[6].addActionListener(this);
				buttonColor[7].setBackground(Color.magenta);
				buttonColor[7].setPreferredSize(colorButtonSize);
				buttonColor[7].addActionListener(this);
				
				
				for(int c = 0; c < buttonColor.length; c ++)
					add(buttonColor[c]);
			}
			
			public void actionPerformed (ActionEvent e) {
				currentBrush.setColor(((JButton)e.getSource()).getBackground());
				brushes.refresh();
			}
		}
			
		private class CustomPallet extends JPanel {
			
			Random rnd = new Random();
			
			private CustomPallet() {
				setBorder(BorderFactory.createTitledBorder("Custom"));
				setMaximumSize(new Dimension(90, 75));
				setPreferredSize(new Dimension(90, 75));
				
				for(int i = 0; i < 6; i ++)
					add(new CustomPalletButton(new Color(
																	(int)(java.lang.Math.random() * 255),
																	(int)(java.lang.Math.random() * 255),
																	(int)(java.lang.Math.random() * 255)
																	)));
				
			}
			
			private class CustomPalletButton extends JButton implements MouseListener {
			
				Color color;
			
				private CustomPalletButton(Color c) {
					color = c;
					setMaximumSize(colorButtonSize);
					setPreferredSize(colorButtonSize);
					setBackground(color);
					addMouseListener(this);
				}
				
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						color = JColorChooser.showDialog(null, "Select a custom Color", color);
						setBackground(color);
					}
					
					currentBrush.setColor(getBackground());
					
					brushes.refresh();
				}
				
				public void mouseReleased(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				
			}
		}		
	}
	
	private class DrawPane extends JPanel implements MouseMotionListener, MouseListener {
	
		private Dimension dimension = new Dimension(800, 600);
		private Image backDrop;
		private int oneIn;
	
		private DrawPane() {
			setBackground(Color.white);
			//setPreferredSize(new Dimension(400, 350));
			addMouseMotionListener(this);
			addMouseListener(this);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D)g;
			
			if(backDrop != null)
				g.drawImage(backDrop, 0, 0, this);
			
			strokeBox.drawStrokes(g);
			
			//Draw rectangle around drawable area
			g2.setStroke(new BasicStroke(1));
			g2.setColor(Color.black);
			g2.drawRect(0, 0, dimension.width, dimension.height);
		}
		
		public void setBackDropImage(Image i) {
			backDrop = i;
			setUsableSize(new Dimension(i.getWidth(null), i.getHeight(null)));
		}
		
		private boolean oneIn100() {
			if(oneIn > 100)
				oneIn = 0;
			
			if(++ oneIn == 100)
				return true;
			else 
				return false;
		}
		
		public void clearBackDropImage() {
			backDrop = null;
		}
		
		public void mouseDragged(MouseEvent e) {
			if(currentBrush.isEnabled()) {
				int x = e.getPoint().x;
				int y = e.getPoint().y;
				
				if(x > dimension.width)
					x = dimension.width;
				if(x < 0)
					x = 0;
				if(y > dimension.height)
					y = dimension.height;
				if(y < 0)
					y = 0;
				
				strokeBox.addPoint(new Point(x, y));
				repaint();
			}
		}
		
		private void setUsableSize(Dimension d) {
			dimension = new Dimension(d);
			//setPreferredSize(dimension);
			System.out.println(dimension);
		}
		
		private int getUsableWidth() {
			return dimension.width;
		}
		
		private int getUsableHeight() {
			return dimension.height;
		}
				
		public void mouseReleased(MouseEvent e) {
			
		}
		
		public void mousePressed(MouseEvent e) {
			if(e.getPoint().x < dimension.width ||
				e.getPoint().x > 0 ||
				e.getPoint().y < dimension.height ||
				e.getPoint().y > 0) {
				strokeBox.newStroke(e.getPoint(), currentBrush, this);
				dirty = true;
			}
		}
		
		public void mouseMoved(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {}		
	}
	
	private class StrokeBox {

		private LinkedList strokes = new LinkedList();
		private LinkedList undoStrokes = new LinkedList();
		private Component parent;
			
		public void drawStrokes(Graphics g) {
			for(int counter = 0; counter < strokes.size(); counter ++)
				((Stroke) strokes.get(counter)).draw(g);
		}
		
		public void eraseAll() {
			strokes.clear();
			undoStrokes.clear();
		}
			
		public void newStroke(Point p, Brush b, Component c) {
			parent = c;
			strokes.add(new Stroke(p, b, c));
		}
			
		public void addPoint(Point p) {
			((Stroke) strokes.getLast()).addPoint(p);
		}
			
		public void undo() {
			if(strokes.size() == 0)
				return;
				
			((Stroke)strokes.getLast()).revertFill();
			undoStrokes.addFirst(strokes.removeLast());
			parent.repaint();
		}
			
		public void redo() {
			if(undoStrokes.size() == 0)
				return;
				
			strokes.addLast(undoStrokes.removeFirst());
			((Stroke)strokes.getLast()).applyFill();
			parent.repaint();
		}
		
		private class Stroke {

			private LinkedList pointList = new LinkedList();
			private Brush brush;
			private Color previousColor;
			
			private Stroke(Point p, Brush b, Component c) {
				
				brush = new Brush(b);
				
				if(b.isEnabled()) {
					addPoint(p);
				} else if(brush.getToolType() == FILL) {
					previousColor = parent.getBackground();
					parent.setBackground(brush.getColor());
				}
			}
			
			public void revertFill() {
				if(previousColor != null)
					parent.setBackground(previousColor);
			}
			
			public void applyFill() {
				if(brush.getToolType() == FILL)
					parent.setBackground(brush.getColor());
			}
				
			public void addPoint(Point p) {
				pointList.add(p);
			}
				
			public void draw(Graphics g) {
				
				Graphics2D g2 = (Graphics2D)g;
					
				g2.setStroke(brush.getBasicStroke());
				g.setColor(brush.getColor());
				
				for(int counter = 1; counter < pointList.size(); counter ++)
					g2.drawLine(
						((Point) pointList.get(counter)).x,
						((Point) pointList.get(counter)).y,
						((Point) pointList.get(counter - 1)).x,
						((Point) pointList.get(counter - 1)).y
					);
			}
			
			public String toString() {
				return brush.toString();
			}
		}
	}
}