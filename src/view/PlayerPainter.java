package view;

import java.awt.Graphics2D;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Shape;

import javax.swing.*;

import model.*;


public class PlayerPainter implements CaveElementPainter {

	@Override
	public void paint(Graphics2D g2d, CaveElement e, int s) {
		Player p = (Player)e;
		Point loc = p.getLocation();
		
		Double theta=(double)0;
		switch (p.getFacing()){
		case UP:
			theta = -Math.PI/2;
			break;
		case DOWN:
			theta = Math.PI/2;
			break;
		case LEFT:
			theta= Math.PI;
			break;
		case RIGHT:
			theta=(double)0;
			break;
		}		
		
		// load image, check its valid
		ImageIcon imageIcon = new ImageIcon("images/player.png");
		assert (imageIcon.getImageLoadStatus() != MediaTracker.ERRORED) : "couldn't load image";
		assert(imageIcon.getIconHeight() <= s);
		assert(imageIcon.getIconWidth() <= s);
		
		//paint
		Shape oldClip = g2d.getClip();
		g2d.setClip(loc.x*s,loc.y*s,s,s);
		g2d.translate(loc.x*s,loc.y*s);
		g2d.rotate(theta, s/2, s/2);
		imageIcon.paintIcon(null, g2d, 0, 0);
		g2d.rotate(-theta,s/2,s/2);
		g2d.translate(-loc.x*s,-loc.y*s);
		g2d.setClip(oldClip);
	}

}
