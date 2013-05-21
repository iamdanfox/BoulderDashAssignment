/**
 * 
 */
package model;

import java.awt.Point;

public abstract class SlipperyGravityElement extends CaveElement {
	
	public static final int stepsToFallOneSquare=3; // must be >=1.
	private int stepsToReachSquareBelow=0;	
	public Boolean isFalling;
	
	/**
	 * @param parentCave
	 */
	public SlipperyGravityElement(Cave parentCave) {
		super(parentCave);
		isFalling = false;
	}

	public int getStepsToReachSquareBelow() {
		return stepsToReachSquareBelow;
	}

	@Override
	public void step(int x,int y){
		Point squareBelow = new Point(x,y+1);
		CaveElement elementBelow = parentCave.getElementAt(squareBelow);
		Boolean silentChangeMade = false;
		
		if (!isFalling){
			if (elementBelow==null && parentCave.getCaveBounds().contains(squareBelow)){
				// if the square below a gravity-bound object becomes empty, the object starts falling at some constant rate
				isFalling = true;
				silentChangeMade=true;
			}
		}else{
			/* if the falling object hits the player, the player loses the game
			 * a falling object stops falling when it reaches a square above dirt, a wall, or the bottom of the cave
			 * !!!! I think a falling object should also stop when it reaches a square above a non falling object.
			 */
			if(getStepsToReachSquareBelow()!=0){
				// continue falling, don't need to make any decisions just yet
				stepsToReachSquareBelow--;
				silentChangeMade=true;
			} else {  
				// elem is exactly within a square.
				if (!parentCave.getCaveBounds().contains(squareBelow)){
					// hit bottom of cave
					isFalling=false;
					silentChangeMade=true;
				}else if (elementBelow!=null && elementBelow instanceof Player){ // I feel this instanceof is justified because 'Player' is a very unique class. 
					// element has struck top of player -> player is dead.
					parentCave.setLostState(true);
				} else if (elementBelow!=null){ 
					// landed on some object	
					if (elementBelow instanceof SlipperyGravityElement){
						/*
						 * 1. If the square to the right of the falling object is empty, and if the square to the lower 
						 * right of the falling object is empty, the falling object moves to the lower right, and it
						 * continues falling according to the rules.
						 * 2. If the previous condition is not satisfied, an analogous check is performed but with the 
						 * squares to the left and lower left of the falling object.
						 */
						Point r = new Point(x+1,y);
						Point br = new Point(x+1,y+1);
						Point l = new Point(x-1,y);
						Point bl = new Point(x-1,y+1);
						if (parentCave.getCaveBounds().contains(r) && 
								parentCave.getElementAt(r)==null && parentCave.getElementAt(br)==null){
							move(Direction.RIGHT);	
						} else if (parentCave.getCaveBounds().contains(l) && 
								parentCave.getElementAt(l)==null && parentCave.getElementAt(bl)==null){
							move(Direction.LEFT);	
						} else {
							/*
							 * 3. if none of the above two conditions is satisfied, the object stops falling.
							 */
							isFalling = false; // landed on a slippery object but is confined.
							silentChangeMade=true;
						}									
					} else {
						// landed on non slippery object
						isFalling=false;
						silentChangeMade=true;
					}
				} else {
					// elementBelow is null & within the cave -> continue falling, but inhabit one square down
					move(Direction.DOWN);
					stepsToReachSquareBelow = SlipperyGravityElement.stepsToFallOneSquare-1;
				}
			}
		}
		if (silentChangeMade) parentCave.fireGridChanged();
	}
	
	/**
	 * Attempts to move the current element one square in the direction provided.
	 * 
	 * @return true if the move was valid and successful, returns false for out of bounds moves or if destination square was not blank.
	 */
	public Boolean move(Direction direction){
		Point destination = direction.applyMoveTo(this.getLocation());
		if (parentCave.getElementAt(destination)==null){
			return parentCave.setElementAt(destination, this);
		} else {
			return false;
		}
	}
}
