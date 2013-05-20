package controller;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import model.CaveElement;

public class CreatorMode extends SelectingMode {
	
	private final CaveElement c;
	
	public CreatorMode(CaveElement c){
		this.c = c;
	}
	
	protected CreatorMode() {c=null;} // allows subclassing

	/**
	 * Iterate through selection, replacing every element with a new instance of c.
	 */
	@Override
    public void mouseReleased(MouseEvent arg0){
		super.mouseReleased(arg0);
		for (Point p : selection)	{
			CaveElement e = c.cloneToCave(this.appState.cave);
			if (!this.appState.cave.setElementAt(p, c.getClass().cast(e))){
				JOptionPane.showMessageDialog(null, "Couldn't create element", "Error Message", JOptionPane.ERROR_MESSAGE);
			}
		}
		clearSelection();
		System.out.println(selection.size() + " "+ c.getClass() + " elements created");
	}
}
