package controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import model.Cave;

@SuppressWarnings("serial")
public class DeleteSelectionButton extends JButton implements ActionListener, Selection.Listener, ModeListener {
	private Cave c;
	private SelectingMode m;
	
	public DeleteSelectionButton(Cave c, SelectingMode m){
		super("Delete");
		this.c=c;
		this.m=m;

		// initialise to not enabled
		this.setEnabled(false);
		this.setVisible(false);
		
		this.addActionListener(this);
		m.getSelection().addListener(this);
		m.registerModeListener(this);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		for (Point p : m.getSelection()) c.removeElementAt(p);
		m.getSelection().clear();
	}
	
	@Override
	public void selectionChanged() {
		this.setEnabled(m.getSelection().size()>0);
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
