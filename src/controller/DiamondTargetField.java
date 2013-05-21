package controller;

//import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import model.Cave;
import model.CaveListener;


@SuppressWarnings("serial")
public class DiamondTargetField extends JTextField implements CaveListener, ActionListener, ModeListener,FocusListener {
	
	private final Cave cave;
	
	public DiamondTargetField(Cave cave, InteractionMode mode){
		this.cave = cave;
		this.setText(""+cave.getDiamondTarget());
		//this.setMaximumSize(new Dimension(50,200)); // TODO limit size of this field when using a large cave.
		// make cave react to field
		this.addActionListener(this);
		this.addFocusListener(this);
		
		// make field react to cave
		cave.addCaveListener(this);
		
		// only enable when in edit mode
		mode.registerModeListener(this);
	}

	// make field react to cave
	@Override
	public void diamondTargetChanged() {
		DiamondTargetField.this.setText(this.cave.getDiamondTarget()+"");
	}

	// make cave react to field
	@Override
	public void actionPerformed(ActionEvent arg0) {
		try{
			int x = Integer.parseInt(this.getText()); 
			if (x==0) throw new NumberFormatException(); // don't allow user to set game won at initial state.
			cave.setDiamondTarget(x);
		}catch(NumberFormatException e){
            errorMsg("Please enter a valid number greater than zero");
		}catch (Exception e){
			errorMsg(e.getMessage());
		}
	}
	
	private void errorMsg(String msg){ JOptionPane.showMessageDialog(null, msg, "Error Message", JOptionPane.ERROR_MESSAGE); }

	@Override
	public void madeActive(InteractionMode m) {
		if (cave.isFrozen()) this.setEnabled(true);
	}

	@Override
	public void madeNotActive(InteractionMode m) {
		this.setEnabled(false);
	}

	@Override
	public void focusGained(FocusEvent arg0) { }

	@Override
	public void focusLost(FocusEvent arg0) {
		actionPerformed(null); // equivalent of submitting the field (only with no ActionEvent).
	}

	@Override
	public void wonStateChanged() { }

	@Override
	public void lostStateChanged() { }

	@Override
	public void gridChanged() { }

	@Override
	public void frozenStateChanged() { }

	@Override
    public void dimensionsChanged() { } 

}
