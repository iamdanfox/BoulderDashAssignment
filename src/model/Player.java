package model;

import java.awt.Point;

public class Player extends CaveElement {
	
	// BiMap maintaining the invariant that there is only one player per cave.
	private static BiMap<Cave,Player> playerForCave = new BiMap<Cave,Player>();
	
	private Direction facing; // the player is displayed differently depending on which direction it is facing

	public Player(Cave parentCave) {
		super(parentCave);
		facing = Direction.RIGHT; // the line shows the direction of the last move, initially to the left.
	}
	
	public Direction getFacing(){
		return facing;
	}

	@Override
    public Boolean acceptPlayer(Direction d) {
		throw new UnsupportedOperationException();
	}
	
	@Override
    public Boolean allowsPlacementAt(Point point){
		Player c = getPlayerFor(parentCave);
		if (this==c || c==null || c.getLocation()==null){
			setCurrent(this);
			return true;
		} else {
			System.out.println("placement failed; current="+c+"("+c.getLocation()+") this="+this);
			return false;
		}
	}
	
	private void setCurrent(Player pl){
		playerForCave.remove(parentCave);
		playerForCave.put(parentCave, pl);
	}
	
	public static Player getPlayerFor(Cave c){
		return playerForCave.get(c);
	}
	
	public Boolean attemptMove(Direction direction){
		System.out.println("Player attemptMove "+direction);
		Point destPoint = direction.applyMoveTo(this.getLocation());
		
		CaveElement destElement = parentCave.getElementAt(destPoint); 
		facing = direction;

		if (!parentCave.isFrozen()){
			if (destElement == null){
				return parentCave.setElementAt(destPoint, this);
			} else if (destElement.acceptPlayer(direction)){
				if (parentCave.setElementAt(destPoint, this)){
					destElement.afterSuccessfulMove();
					return true; 
				} else {
					destElement.afterUnsuccessfulMove();
					return false; // grid disallowed move (out of bounds)
				}
			} else {
				return false; // destElement rejected player
			}
		} else {
			return false; // cave is frozen
		}
	}

	@Override
	public Player cloneToCave(Cave parentCave2) {
		Player newPlayer = new Player(parentCave2);
		newPlayer.facing = this.getFacing();
		return newPlayer;
	}

}
