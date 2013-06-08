package controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;

import view.CaveView;

import model.Cave;
import model.CaveElement;

public class EditMode extends SelectingMode {
	
	protected Boolean mouseIsDragging=false;
	protected Point dragOriginSq;
	private Selection suspendedSelection = new Selection();
	
	protected Cave flyingCave;
	
	@Override
    protected void makeNotActiveDelegate(){
		//save selection for later.
		suspendedSelection.clear();
		for (Point p : selection) suspendedSelection.add((Point)p.clone());
		System.out.println("Saved selection size: "+suspendedSelection.size());
		super.makeNotActiveDelegate();
	}
	
	@Override
    protected void makeActiveDelegate(){
		super.makeActiveDelegate();
		
		// restore last selection, filter out any dead elements
		selection.clear();
		for (Point p : suspendedSelection) if (appState.cave.getElementAt(p) != null) selection.add(p);
		System.out.println("Restored last selection size: "+suspendedSelection.size());
	}
	
	@Override
	public void paintInterior(Graphics2D g2d) {
		
		int offsetX=0;
		int offsetY=0;
		if (mouseIsDragging){
			offsetX=(sq2.x-dragOriginSq.x)*ss;
			offsetY=(sq2.y-dragOriginSq.y)*ss;
		}

		// paint flyingCave, offset by appropriate amount
		if (mouseIsDragging){
			g2d.translate(offsetX, offsetY);
			Cave c = flyingCave;
			for(int y=0;y<c.getHeight();y++){
				for(int x=0;x<c.getWidth();x++){
					CaveElement e = c.getElementAt(new Point(x,y));
					if (e != null)	CaveView.painters.get(e.getClass()).paint(g2d,e, CaveView.squareSize); 
				}
			}
			g2d.translate(-offsetX, -offsetY);
		}
		
		// paint selection
		g2d.translate(offsetX, offsetY);
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
		g2d.translate(-offsetX, -offsetY);
		
		// draw hover square
		if (!mouseIsDragging && !mouseIsSelecting && sq2!=null){
			g2d.setColor(new Color(0,0,0,70));
			g2d.fillRect(sq2.x*ss, sq2.y*ss, ss, ss);
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		Cave cave = appState.cave;
		Point mouse = arg0.getPoint();
		Point mouseSq = new Point(mouse.x/ss, mouse.y/ss);
		
		if (!mouseIsSelecting && !mouseIsDragging){
			// change something
			if (selection.size()>0 && selection.contains(mouseSq)){
				// begin drag
				System.out.println("Begin dragging");
				dragOriginSq = mouseSq;
				sq2 = mouseSq;
				mouseIsDragging=true;		

				// copy all selected elements to flying cave, removing them from main cave.
				flyingCave = new Cave(cave.getWidth(),cave.getHeight());
				for (Point p : selection){
					flyingCave.setElementAt(p, cave.getElementAt(p).cloneToCave(flyingCave));
					cave.removeElementAt(p);
				}
			} else {
				// start a new selection
				System.out.println("Begin selection");
				clearSelection();
				selection.add(mouseSq);
				mouseIsSelecting=true;
				sq1 = dragOriginSq;
			}
			
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// mouse was selecting, but has now been released
		if (mouseIsSelecting){
			mouseIsSelecting=false;
			// filter to real elements
			Set<Point> realElements = new HashSet<Point>();
			for(Point p : selection){
				if (this.appState.cave.getElementAt(p) != null) realElements.add(p);
			}
			selection.update(realElements);
			System.out.println("Finished selecting ("+selection.size()+" elements)");
		}
		// mouse was dragging, has now been released
		if (mouseIsDragging){
			// if they all fit, move to new positions otherwise return to start.
			int sqOffsetX = sq2.x-dragOriginSq.x;
			int sqOffsetY = sq2.y-dragOriginSq.y;
			
			// check all elements fit.
			Cave cave = appState.cave;
			Boolean allFit = true;
			for (Point p : selection) allFit = allFit && cave.getCaveBounds().contains(new Point(p.x+sqOffsetX,p.y+sqOffsetY));
			
			// place elements
			for (Point p1 : selection){
				Point p2 = allFit ? new Point(p1.x+sqOffsetX,p1.y+sqOffsetY) : p1;
				CaveElement elem = flyingCave.getElementAt(p1);
				if(elem!=null) cave.setElementAt(p2, elem.cloneToCave(cave));
			}
			
			// reset state ready for new drag
			mouseIsDragging=false;
			System.out.println("finished dragging");
			dragOriginSq=null;
			if(allFit) clearSelection();
			flyingCave=null;
			appState.caveView.repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// handle selecting part:
		super.mouseDragged(e); 
		// handle dragging part:
		if (mouseIsDragging){
			sq2 = new Point(e.getPoint().x/ss, e.getPoint().y/ss);
			appState.caveView.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		sq2 = new Point(e.getPoint().x/ss, e.getPoint().y/ss);
		if (!mouseIsDragging && !mouseIsSelecting){
			this.appState.caveView.repaint(); // draw little hover square
		}
	}
	

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println(e);
        switch (e.getKeyCode()){
        case 27: // Esc
            clearSelection();
            break;
        }
    }
}
