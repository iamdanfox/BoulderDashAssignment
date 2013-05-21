

package model;

import java.awt.Color;


public class Wall extends CaveElement {
	
	public static final BiMap<String, Color> allowedColors = new BiMap<String, Color>(){{
		put("Red",Color.red);
		put("Green",Color.green);
		put("Blue",Color.blue);
	}};
	
	protected Color color;

	/**
	 * @param parentCave
	 */
	public Wall(Cave parentCave) {
		super(parentCave);
		this.color = Color.red;
	}
	
	public Wall(Cave parentCave, Color color){
		super(parentCave);
        assert(allowedColors.containsValue(color));
		this.color = color;
	}

	public Color getColor(){
		return this.color;
	}
	
	public void setColor(Color c){
		assert(allowedColors.containsValue(c));
		this.color = c;
		this.parentCave.fireGridChanged();
	}
	
	@Override
	public Boolean acceptPlayer(Direction d) {
		return false;
	}

	@Override
	public Wall cloneToCave(Cave parentCave2) {
		Wall newWall = new Wall(parentCave2);
		newWall.setColor(color);
		return newWall;
	}

	@Override
    public String toString() {
        return "Wall["+allowedColors.backwards.get(color)+"]";
    }

}
