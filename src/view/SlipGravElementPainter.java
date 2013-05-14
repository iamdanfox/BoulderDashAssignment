package view;

import java.awt.Graphics2D;
import java.awt.MediaTracker;
import java.awt.Point;

import javax.swing.ImageIcon;

import model.CaveElement;
import model.SlipperyGravityElement;


public class SlipGravElementPainter implements CaveElementPainter {
	
	private ImageIcon imageIcon;

	
	public SlipGravElementPainter(String pathToImage){
		imageIcon = new ImageIcon(pathToImage);
		assert (imageIcon.getImageLoadStatus() != MediaTracker.ERRORED) : "Error loading image";	
	}

	@Override
	public void paint(Graphics2D g2d, CaveElement elem, int s) {
		SlipperyGravityElement e = (SlipperyGravityElement) elem;
		
		Point loc = e.getLocation();
		
		assert(imageIcon.getIconHeight() <= s);
		assert(imageIcon.getIconWidth() <= s);
		
		int pxForOneStep = s/SlipperyGravityElement.stepsToFallOneSquare;
		int pxAboveSquareLine = e.getStepsToReachSquareBelow()*pxForOneStep;
		
		imageIcon.paintIcon(null, g2d, loc.x*s, loc.y*s - pxAboveSquareLine);
		
		// Falling debug
//		g2d.setColor(Color.pink);
//		g2d.drawRect(loc.x*s, loc.y*s-pxAboveSquareLine, 10, 10);
//		if (e.isFalling) {
//			g2d.fillRect(loc.x*s, loc.y*s-pxAboveSquareLine, 10, 10);
//			char[] data = (e.stepsToReachSquareBelow+"").toCharArray();
//			g2d.drawChars(data, 0, 1, loc.x*s, loc.y*s-pxAboveSquareLine);
//		}

	}

}
