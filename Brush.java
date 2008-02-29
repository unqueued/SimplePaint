import java.awt.*;

public class Brush {
	
	public final int SQUARE = 2;
	public final int OVAL = 1;
	public final int FILL = 3;
	
	private int tool = 0;
	
	private Color color = Color.black;
	private BasicStroke basicStroke;
	private boolean enabled = true;
	
	public Brush() {
		color = Color.black;
		basicStroke = new BasicStroke(2);
	}
	
	public Brush(Brush b) {
		basicStroke = new BasicStroke(b.getBasicStroke().getLineWidth());
		color = b.getColor();
		tool = b.getToolType();
		enabled = b.isEnabled();
	}
	
	public Brush(Color c, BasicStroke bs) {
		color = c;
		basicStroke = bs;
	}
	
	public BasicStroke getBasicStroke() {
		return basicStroke;
	}
	
	public int getToolType() {
		return tool;
	}
	
	public void setToolType(int tool) {
		this.tool = tool;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void disable() {
		enabled = false;
	}
	
	public void enable() {
		enabled = true;
	}
	
	public void setBasicStroke(BasicStroke b) {
		basicStroke = b;
	}

	public Color getColor() {
		return color;
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	public Brush getCopy() {
		return new Brush();
	}
	
	public void drawBrush(Graphics g, Component caller) {
		Point center = new Point(caller.getWidth() / 2, caller.getHeight() / 2);
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(basicStroke);
		g2.setColor(color);
		g2.drawLine(center.x, center.y, center.x, center.y);
	}
	
	public String toString() {
		return "Brushes color: " + color;
	}
}