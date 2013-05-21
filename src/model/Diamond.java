/**
 * 
 */
package model;

public class Diamond extends SlipperyGravityElement {

	/**
	 * @param parentCave
	 */
	public Diamond(Cave parentCave) {
		super(parentCave);
	}

	/* (non-Javadoc)
	 * @see bd.model.CaveElement#acceptPlayer(bd.model.Direction)
	 */
	@Override
	public Boolean acceptPlayer(Direction directionOfMotion) {
		return true;
	}
	
	@Override
    public void afterSuccessfulMove(){
		parentCave.setDiamondTarget(parentCave.getDiamondTarget() - 1);
	}

	@Override
	public Diamond cloneToCave(Cave parentCave2) {
		return new Diamond(parentCave2);
	}

	@Override
    public String toString() {
        return "Diamond";
    }
}
