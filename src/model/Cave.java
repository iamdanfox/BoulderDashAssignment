package model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.*;

/**
 * A mutable representation of a Boulder Dash cave using CaveElements.
 *
 */
public class Cave {
	
	public final int width;
	public final int height;
	public final Rectangle caveBounds;
	private final BiMap<Point,CaveElement> grid;

	private int diamondTarget;
	private Boolean hasWon=false;
	private Boolean hasLost=false;
	private Boolean frozen=true;
	
	private final Timer stepTimer = new Timer();	
	private final int stepInterval = 100;
	
	private final Collection<CaveListener> listeners;

	
	/**
	 * Construct a new cave, initially frozen
	 * @param width number of squares horizontally.
	 * @param height number of squares vertically.
	 */
	public Cave(int width, int height) {
		this.width=width;
		this.height=height;
		caveBounds = new Rectangle(0,0,width,height); // (width-1, height-1) is the bottom right square allowed.
		
		this.grid = new BiMap<Point,CaveElement>();
		this.listeners = new HashSet<CaveListener>();
		
		stepTimer.scheduleAtFixedRate(new TimerTask(){
			public void run() { step();	}
		}, 0, stepInterval);
	}

	
	/**
	 * Gets the CaveElement at a particular square in the grid
	 * @param p the (x,y) coordinates from the top left corner of the square.
	 * @return the CaveElement at the location, or null if the square is blank.
	 */
	public CaveElement getElementAt(Point p){
		return grid.get(p);
	}
	
	/**
	 * @return the only player allowed on the grid
	 */
	public Player getPlayer(){
		return Player.getPlayerFor(this);
	}
	
	/**
	 * Place a CaveElement in a specific square within the cave.  NB, if the element already has a location, it will be removed 
	 * from its old location and placed in its new one.
	 * @param p square to be filled
	 * @param e CaveElement to place in specified square.  This may overwrite the square's existing contents
	 * @return true if the operation was successful, false if the operation has failed (the square may have 
	 * been out of bounds, or the element refused to be placed).  If the operation fails, 
	 * the cave will be left in the state it was in before the operation.
	 */
	public Boolean setElementAt(Point p, CaveElement e){	
		if (caveBounds.contains(p)){
			// eliminate duplication
			CaveElement overWrittenElement = removeElementAt(p);
			Point overWrittenPoint = removeElement(e);			
			if(e.allowsPlacementAt(p)){
				// place in grid
				grid.put(p, e);
				fireGridChanged();
				return true;
			} else {
				if (overWrittenElement!=null) grid.put(p,overWrittenElement);
				if (overWrittenPoint!=null) grid.put(overWrittenPoint,e);
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Remove a CaveElement from the grid and notify listeners.
	 * @param p the point on the grid from which to delete an element
	 * @return the element that was removed or null if the square was empty.
	 */
	public CaveElement removeElementAt(Point p){
		CaveElement elem = grid.remove(p);
		if (elem!=null)	fireGridChanged();
		return elem;
	}
	
	/**
	 * Remove a CaveElement from the grid and notify listeners.
	 * @param e CaveElement to be removed
	 * @return the location from which it was removed (or null, if the CaveElement was not present on the grid).
	 */
	public Point removeElement(CaveElement e){
		Point point = grid.inverse().remove(e);
		if(point != null) fireGridChanged();
		return point;
	}

	/**
	 * @return The number of diamonds a player must collect in order to win.
	 */
	public int getDiamondTarget(){	return diamondTarget; }
	
	/**
	 * Set a new diamondTarget. If diamondTarget reaches zero (ie all diamonds have been collected) fireWon() is called.  
	 * Cave listeners are notified of the change in diamond target.
	 * @param diamondTarget number of diamonds the player must collect in order to win.
	 */
	public void setDiamondTarget(int diamondTarget){ 
		if (diamondTarget < 0){	throw new RuntimeException("Diamond target must be at least zero");	}
		this.diamondTarget = diamondTarget; 
		System.out.println("diamondTarget set to "+diamondTarget); // debug
		fireDiamondTargetChanged();
		if (diamondTarget==0) setWonState(true);
	}
	
	/**
	 * Lookup an element on the grid.
	 * @param e CaveElement to find
	 * @return Point at which the CaveElement is located, or null if the element is not in the grid.
	 */
	public Point getLocationOf(CaveElement e){
		return grid.inverse().get(e);
	}
	
	/**
	 * Iterate through all objects in cave, move them in accordance with the game rules.  
	 * This should be done by examining objects from bottom to top and from left to right.
	 */
	private void step(){
		if (!frozen) { // allow movement
			for (int y=this.height-1;y>=0;y--){
				for (int x=0;x<this.width;x++){
					CaveElement e = this.getElementAt(new Point(x,y));
					if (e!=null) e.step(x,y);
				}
			}
		}
	}
	
	public Boolean isFrozen(){
		return frozen;
	}
	
	/**
	 * Change frozen status and notify listeners.  You cannot unfreeze a game that has been won or lost.
	 * @param frozen true denotes that the cave motion is 'frozen' and thereby all movement attempts are refused.  Gravity is also paused.
	 */
	public void setFrozen(boolean frozen){
		if (frozen==false && (hasWon || hasLost)) throw new RuntimeException("You cannot unfreeze a finished game.");
		this.frozen = frozen;
		fireFrozenStateChanged();
	}
	
	public boolean hasWon(){
		return hasWon;
	}
	
	/**
	 * Change the won state and notify listeners.  (The won state is set to false when the grid is reset to the starting layout).
	 */
	public void setWonState(Boolean newState){
		if((newState && !this.hasWon) || (!newState && this.hasWon)){
			this.hasWon = newState;
			setFrozen(hasWon);
			fireWonStateChanged();
		}
	}
	
	public boolean hasLost(){
		return hasLost;
	}
	
	public void setLostState(Boolean newState){
		if((newState && !this.hasLost) || (!newState && this.hasLost)){
			this.hasLost = newState;
			setFrozen(hasLost);
			fireLostStateChanged();
		}
	}
	
	/**
	 * Replaces the current grid and accompanying state (hasWon, hasLost, diamondTarget and isFrozen)
	 * with a deep cloned copy from another cave.
	 * @param other the cave whose grid is to be cloned.
	 */
	public void copyStateFrom(Cave other){
		// wipe grid clean
		this.grid.clear();
		
		// populate with clone of other
		for(int j=0;j<height;j++){
			for(int i=0;i<width;i++){
				Point p = new Point(i,j);
				CaveElement elem = other.getElementAt(p);
				if(elem!=null) this.setElementAt(p, elem.cloneToCave(this));
			}
		}

		// set accompanying state.
		this.setWonState(other.hasWon());
		this.setLostState(other.hasLost());
		this.setDiamondTarget(other.getDiamondTarget());
		this.setFrozen(other.isFrozen());
	}
	
	/**
	 * Deep copy the cave grid and accompanying state.  Any changes made to the cloned cave 
	 * will not affect the original cave.
	 * @return a fresh cave, with an equal grid layout but with no registered listeners etc.
	 */
	public Cave clone(){
		Cave cave2 =new Cave(width, height);
		cave2.copyStateFrom(this);
		return cave2;
	}
	
	public void addCaveListener(CaveListener l){
		listeners.add(l);
	}
	
	public void removeCaveListener(CaveListener l){ 
		listeners.remove(l); 
	}
	
	protected void fireFrozenStateChanged(){
		for(CaveListener l : this.listeners) l.frozenStateChanged();
	}
	
	protected void fireGridChanged(){
		for(CaveListener l : this.listeners) l.gridChanged();
	}
	
	protected void fireWonStateChanged(){
		for(CaveListener l : this.listeners) l.wonStateChanged();
	}
	
	protected void fireLostStateChanged(){
		for(CaveListener l : this.listeners) l.lostStateChanged();
	}
	
	protected void fireDiamondTargetChanged(){
		for(CaveListener l : this.listeners) l.diamondTargetChanged();
	}
}
