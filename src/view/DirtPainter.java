package view;

import java.awt.Graphics2D;
import java.awt.MediaTracker;
import java.awt.Point;

import javax.swing.*;

import model.*;


public class DirtPainter implements CaveElementPainter {

	@Override
	public void paint(Graphics2D g2d, CaveElement e, int s) {
		Point loc = e.getLocation();
		
		// load image, check its valid
		ImageIcon imageIcon = new ImageIcon("images/dirt.png");
		assert (imageIcon.getImageLoadStatus() != MediaTracker.ERRORED);
		assert(imageIcon.getIconHeight() <= s);
		assert(imageIcon.getIconWidth() <= s);
		
		// paint
		imageIcon.paintIcon(null, g2d, loc.x*s, loc.y*s);
	}

}

