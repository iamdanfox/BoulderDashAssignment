package controller;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class Selection implements Iterable<Point>{
	
	private Collection<Point> selection = new HashSet<Point>();
	private Collection<Listener> listeners = new HashSet<Listener>();
	/**
	 * Clear the selection and replace it with just those specified squares
	 * @param squares
	 */
	void update(Collection<Point> squares){
		selection.clear();
		selection.addAll(squares);
		fireChanged();
	}
	
	/**
	 * Shorthand. Clear the selection and replace it with all those squares within the rectangle
	 * @param rect
	 */
	void update(Rectangle rect){
		selection.clear();
		for (int y=rect.y;y<rect.height+rect.y;y++){
			for (int x=rect.x;x<rect.width+rect.x;x++){
				selection.add(new Point(x,y));
			}
		}
		fireChanged();
	}
	
	void add(Point sq){
		if (sq==null) throw new NullPointerException("nulls not allowed in selection");
		selection.add(sq);
		fireChanged();
	}
	
	void remove(Point sq){
		selection.remove(sq);
		fireChanged();
	}
	
	void clear(){
		selection.clear();
		fireChanged();
	}
	
	public boolean isEmpty(){
		return selection.isEmpty();
	}
	
	/** 
	 * Tests whether a cave square is in the selection
	 * @param p 
	 * @return true if the square is selected, false otherwise
	 */
	Boolean contains(Point sq){
		return selection.contains(sq);
	}
	
	public static interface Listener{
		public void selectionChanged();
	}
	
	public void addListener(Listener l){
		listeners.add(l);
	}
	
	public void removeListener(Listener l){
		listeners.remove(l);
	}
	
	public void fireChanged(){
		for(Listener l : listeners) l.selectionChanged();
	}
	
	public int size(){
		return selection.size();
	}

	@Override
	public Iterator<Point> iterator() {
		return selection.iterator();
	}
}