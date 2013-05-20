package controller;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import view.CaveView;

public class SelectingMode extends InteractionMode implements Selection.Listener {
	
	protected Point sq1; // selection origin, can be null
	protected Point sq2; // current position of mouse.
	protected final Selection selection = new Selection();
	protected Boolean mouseIsSelecting = false;
	protected final int ss = CaveView.squareSize; // shorthand.
	
	
	@Override
	protected void construct() {
		selection.addListener(this);
	}
	
	@Override
    public void selectionChanged(){
		this.appState.caveView.repaint();
	}
	
	public Selection getSelection(){
		return this.selection;
	}

	@Override
	protected void makeActiveDelegate() {
		if (!this.appState.cave.isFrozen()) throw new RuntimeException("Cannot edit a non-frozen cave.");
	}

	@Override
	protected void makeNotActiveDelegate() {
		clearSelection();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		clearSelection();
		mouseIsSelecting=true;
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		mouseIsSelecting=false;
		if (sq1==null&&sq2==null){
			sq1 = new Point(arg0.getPoint().x/ss, arg0.getPoint().y/ss);
			sq2=sq1;
			selection.add(sq1);
			System.out.println("click");
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (mouseIsSelecting){
			Point oldSq1 = sq1;
			Point oldSq2 = sq2;
			if(sq1==null) sq1 = new Point(e.getPoint().x/ss, e.getPoint().y/ss);
			sq2 = new Point(e.getPoint().x/ss, e.getPoint().y/ss);

			if (!(sq1==oldSq1 && sq2==oldSq2)){
				Rectangle r = new Rectangle(Math.min(sq1.x, sq2.x), Math.min(sq1.y, sq2.y), 1+Math.abs(sq1.x-sq2.x), 1+Math.abs(sq1.y-sq2.y));
				selection.update(r.intersection(appState.cave.caveBounds));
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!mouseIsSelecting){
			sq2 = new Point(e.getPoint().x/ss, e.getPoint().y/ss);
			this.appState.caveView.repaint();
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		sq2=null;
		this.appState.caveView.repaint();
	}
	
	protected void clearSelection(){
		sq1=null;
		sq2=null;
		selection.clear();
	}

	@Override
	public void paintInterior(Graphics2D g2d) {
		// paint selection
		for (Point p : selection){
			int x = p.x*ss;
			int y = p.y*ss;

			g2d.setColor(new Color(0,0,0,70));
			g2d.fillRect(x, y, ss, ss);
			
			int o = 3;
			g2d.setColor(new Color(255,255,255,100));
			g2d.setStroke(new BasicStroke(2));
			g2d.drawRect(x+o, y+o, ss-2*o, ss-2*o);
		}

		// draw hover
		if (!mouseIsSelecting && sq2!=null){
			g2d.setColor(new Color(0,0,0,70));
			g2d.fillRect(sq2.x*ss, sq2.y*ss, ss, ss);
		}
	}

	@Override
	public void gridChanged() {
		this.appState.caveView.repaint();
	}


	@Override
	public void mouseClicked(MouseEvent arg0) { }

	@Override
	public void mouseEntered(MouseEvent arg0) { }

	@Override
	public void keyPressed(KeyEvent arg0) { }

	@Override
	public void keyReleased(KeyEvent arg0) { }

	@Override
	public void keyTyped(KeyEvent arg0) { }

	@Override
	public void wonStateChanged() { }

	@Override
	public void lostStateChanged() { }

	@Override
	public void diamondTargetChanged() { }

	@Override
	public void frozenStateChanged() { }

}
