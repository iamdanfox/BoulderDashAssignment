
package model;

public class Boulder extends SlipperyGravityElement {
	
	private Boolean boulderMoved;
	private Direction lastMove;

	/**
	 * @param parentCave
	 */
	public Boulder(Cave parentCave) {
		super(parentCave);
	}

	
	@Override
    public Boolean acceptPlayer(Direction directionOfMotion) {
		/*
		 * The player can push a boulder left or right, provided that the square that the boulder moves to is empty.  
		 * The player cannot push a boulder up or down.
		 */
		switch (directionOfMotion){
		case LEFT:
		case RIGHT:			
			boulderMoved = this.move(directionOfMotion);
			lastMove = directionOfMotion;
			return boulderMoved;
		default:
			return false;
		}
	}
	
	@Override
    public void afterUnsuccessfulMove(){ 
		/* this could technically be ignored, because it is unlikely that a player could successfully push a boulder 
		 * but be restrained from moving itself.  Nonetheless, this provides flexibility for adding further functionality 
		 * (like a limited number of moves) 
		 */
		if (boulderMoved) this.move(lastMove.opposite());
	}


	@Override
	public Boulder cloneToCave(Cave parentCave2) {
		return new Boulder(parentCave2);
	}


    @Override
    public String toString() {
        return "Boulder";
    }

}
