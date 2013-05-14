/**
 * 
 */
package model;

public class Dirt extends CaveElement {

	/**
	 * @param parentCave
	 */
	public Dirt(Cave parentCave) {
		super(parentCave);
	}

	@Override
	public Boolean acceptPlayer(Direction d) {
		/** The player can move to an adjacent square containing dirt.
		 *  In this case, the computer removes the dirt from the square before the player's move.
		 */
		 // this.parentCave.removeElement(this); // unnecessary because player simply overwrites the location in the BiMap
		
		return true;
	}

	@Override
	public Dirt cloneToCave(Cave parentCave2) {
		return new Dirt(parentCave2);
	}
}
