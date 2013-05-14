package view;

import java.awt.Graphics2D;

import model.CaveElement;

public interface CaveElementPainter {
	/**
	 * 
	 * @param g2d graphics object used to paint the cave element
	 * @param e element to be painted
	 * @param caveElementSize size in pixels of each square in the grid
	 */
	public void paint(Graphics2D g2d, CaveElement e, int caveElementSize);
}
