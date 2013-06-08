package controller;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import model.Cave;
import model.CaveElement;
import model.Wall;

@SuppressWarnings("serial")
public class ColorWallSelectionBox extends JComboBox implements ActionListener, Selection.Listener, ModeListener {
	private final Cave cave;
	private final SelectingMode mode;
	
	/**
	 * The ComboBox will only be visible while the mode is active.
	 * @param cave
	 * @param mode the mode to link this ComboBox to.  
	 */
	public ColorWallSelectionBox(Cave cave, SelectingMode mode){
		super(Wall.allowedColors.keySet().toArray(new String[0])); // fill with titles from allowedColors
		
		this.setPrototypeDisplayValue("Green");
		
		// initialise to not enabled.
		this.setSelectedIndex(-1);
		this.setEnabled(false);
		this.setVisible(false);
		
		this.cave=cave;
		this.mode=mode;
		
		this.addActionListener(this);
		mode.registerModeListener(this);
		mode.getSelection().addListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		// this seems to receiving loads of events, only trigger when user has actually just selected something.
		String colorName = (String) ColorWallSelectionBox.this.getSelectedItem();
		if (ColorWallSelectionBox.this.isEnabled() && colorName!=null){
			Color newColor = Wall.allowedColors.get(colorName);
			
			//if (newColor != null){
				for (Point p : mode.getSelection()){
					CaveElement elem = cave.getElementAt(p);
					if(elem!=null) ((Wall)elem).setColor(newColor);
				}
				System.out.println("Selection size "+mode.getSelection().size()+" colored "+colorName);
			//}
		}
	}

	@Override
	public void selectionChanged() {
		/* ...it should be disabled if the selection is empty or at least one object is selected that is not a wall. 
		 * When a non-empty selection consists only of wall objects, the combo box should be updated as follows:
		 * -if all selected walls are of the same colour, the combo box should show that colour.
		 * -otherwise,no colour should be selected in the combo box.
		 */
		if (mode.getSelection().size()!=0){
			
			Boolean allWallsOrBlanks = true;
			Boolean allBlank = true;
			Color singleColor = null;
			Boolean allSameColor = true;
			
			for (Point p : mode.getSelection()) {
				CaveElement elem = cave.getElementAt(p);		
				if (elem==null || elem instanceof Wall){
					if (elem !=null){
						Color thisColor = ((Wall)elem).getColor();
						if (singleColor==null){
							singleColor=thisColor;
						} else {
							allSameColor = allSameColor && (thisColor==singleColor);
						}
						allBlank = false;
					}
				} else {
					allWallsOrBlanks = false;
				}
			}
			
			this.setEnabled(allWallsOrBlanks && !allBlank);
			
			if(allSameColor && allWallsOrBlanks) {
				this.setSelectedItem(Wall.allowedColors.inverse().get(singleColor));
			} else {
				this.setSelectedIndex(-1);
			}
			
		} else {
			this.setSelectedIndex(-1);
			this.setEnabled(false);
		}
		
	}

	@Override
	public void madeActive(InteractionMode m) {
		this.setVisible(true);
	}

	@Override
	public void madeNotActive(InteractionMode m) {
		this.setVisible(false);
	}
	
}
