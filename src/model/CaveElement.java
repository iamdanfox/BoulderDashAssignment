package model;

import java.awt.Point;

public abstract class CaveElement {
	
	public final Cave parentCave;
	
	/**
	 * Constructs a new CaveElement bound to a particular parentCave.
	 * @param parentCave
	 */
	public CaveElement(Cave parentCave) {
		this.parentCave = parentCave;
	}
	
	/** 
	 * Method called before the element is placed into that point on the grid. 
	 * @param point Empty point within the caveBounds into which the element will be placed.
	 * @return true if the operation should continue, false if it should roll-back.
	 */
	public Boolean allowsPlacementAt(Point point){ return true; }
	
	/**
	 * Called at regular intervals to allow animation independently of player movements.  
	 * It is important to call parentCave.fireGridChanged() if this method makes a change that 
	 * doesn't involve moving an element but should still be repainted in a view. 
	 * @param x x-coordinate of current square (more efficient than calling getLocation each time)
	 * @param y y-coordinate of current square
	 */
	public void step(int x,int y) { }

	/**
	 * Determines whether the player is allowed to move into this element's space.
	 * @param directionOfMotion the direction the player is moving in.
	 * @return True if the player if free to move in; false otherwise.
	 */
	public abstract Boolean acceptPlayer (Direction directionOfMotion);
	
	/**
	 * This method is called after a player successfully moves into this element's old square.
	 */
	public void afterSuccessfulMove(){ }

	/**
	 * This method is called to help restore the cave to its original state before acceptPlayer was called.
	 */
	public void afterUnsuccessfulMove(){ }

	/**
	 * Shorthand for looking up the current location.
	 * @return
	 */
	public Point getLocation(){
		return parentCave.getLocationOf(this);
	}
	
	/**
	 * 
	 * @param other a different cave to the one the current CaveElement inhabits
	 * @return an equal cave element, but bound to the new cave.
	 */
	public abstract CaveElement cloneToCave(Cave other);
	
	/**
	 * @return String representation of this CaveElement, should encapsulate all state.
	 */
	@Override
    public abstract String toString();
}
