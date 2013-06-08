package controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;

import model.Cave;

@SuppressWarnings("serial")
public class DeleteSelectionButton extends JButton implements ActionListener, Selection.Listener, ModeListener {
	private Cave c;
	private SelectingMode m;
	
	public DeleteSelectionButton(ApplicationState appState, SelectingMode m){
		super("Delete");
		this.c=appState.cave;
		this.m=m;

		appState.caveView.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) { 
                if (DeleteSelectionButton.this.isEnabled()
                        && (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e
                                .getKeyCode() == KeyEvent.VK_DELETE))
                    DeleteSelectionButton.this.actionPerformed(null);
            }
        });
		
		// initialise to not enabled
		this.setEnabled(false);
		this.setVisible(false);
		
		this.addActionListener(this);
		m.getSelection().addListener(this);
		m.registerModeListener(this);
	}
	
	@Override
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
