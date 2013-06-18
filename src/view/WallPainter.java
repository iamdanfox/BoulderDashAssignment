package view;

import java.awt.Graphics2D;
import java.awt.Point;

import model.CaveElement;
import model.Wall;


public class WallPainter implements CaveElementPainter {

	@Override
	public void paint(Graphics2D g2d, CaveElement e, int s) {
		Wall w = (Wall)e;
		Point loc = w.getLocation();
	
		// paint square
		g2d.setColor(w.getColor());
		g2d.fillRect(loc.x*s, loc.y*s, s, s);
	}

}
