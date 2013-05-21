package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import model.Cave;
import model.CaveListener;
import model.Direction;

public class PlayMode extends InteractionMode implements CaveListener {
	
	private JComponent bigMessage;
	private JLabel bigMessageLabel;
	private Cave startingCave;

	@Override
	protected void construct() {
		// make big message
		bigMessageLabel = new JLabel();		
		bigMessageLabel.setFont(new Font("Sans-Serif", Font.BOLD, 65));
		bigMessageLabel.setForeground(Color.WHITE);
		bigMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// semi-transparent background
		bigMessageLabel.setBackground(new Color(0,0,0,127));
		bigMessageLabel.setOpaque(true);

		// set border
		bigMessageLabel.setBorder(new CompoundBorder(new EmptyBorder(5,5,5,5), new LineBorder(Color.WHITE, 3)));

		// spacing from edge of cave
		bigMessage = new JPanel(new BorderLayout());
		bigMessage.setVisible(false);
		bigMessage.setBackground(new Color(0,0,0,0));
		bigMessage.setBorder(new EmptyBorder(50,50,50,50));

		// add to caveView
		bigMessage.add(BorderLayout.CENTER, bigMessageLabel);
		this.appState.caveView.add(BorderLayout.CENTER, bigMessage);
	}

	@Override
	protected void makeNotActiveDelegate() {
		// suspend motion while not playing
		this.appState.cave.setFrozen(true);
		
		// reset cave (undo all user actions)
		this.appState.cave.copyStateFrom(startingCave);

        // stop listening to cave
        this.appState.cave.removeCaveListener(this);
	}

	@Override
	protected void makeActiveDelegate() {
        // listen to cave
        this.appState.cave.addCaveListener(this);
        
		// save pristine state
		this.startingCave = this.appState.cave.clone();
		
		// let there be gravity
		if(!this.appState.cave.hasWon() || this.appState.cave.hasLost()) this.appState.cave.setFrozen(false);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(!this.appState.cave.isFrozen()){
			System.out.println(e);
			switch (e.getKeyCode()){
			case 37:
				this.appState.cave.getPlayer().attemptMove(Direction.LEFT);
				break;
			case 38:
				this.appState.cave.getPlayer().attemptMove(Direction.UP);
				break;
			case 39:
				this.appState.cave.getPlayer().attemptMove(Direction.RIGHT);
				break;
			case 40:
				this.appState.cave.getPlayer().attemptMove(Direction.DOWN);
				break;
			}
		}
	}

	@Override
	public void wonStateChanged() {
		//System.out.println("won state changed to "+this.appState.cave.hasWon());
		bigMessageLabel.setText("Game won!");
		bigMessage.setVisible(this.appState.cave.hasWon());
	}

	@Override
	public void lostStateChanged() {
		bigMessageLabel.setText("Game lost!");
		bigMessage.setVisible(this.appState.cave.hasLost());
	}

	@Override
	public void gridChanged() { }

	@Override
	public void mouseClicked(MouseEvent arg0) {
		this.appState.caveView.requestFocusInWindow();
	}

	@Override
	public void diamondTargetChanged() { }

	@Override
	public void frozenStateChanged() { }

    @Override
    public void dimensionsChanged() { }

	@Override
	public void mouseEntered(MouseEvent arg0) { }

	@Override
	public void mouseExited(MouseEvent arg0) { }

	@Override
	public void mousePressed(MouseEvent arg0) { }

	@Override
	public void mouseReleased(MouseEvent arg0) { }

	@Override
	public void keyReleased(KeyEvent arg0) { }

	@Override
	public void keyTyped(KeyEvent arg0) { }

	@Override
	public void mouseDragged(MouseEvent e) { }

	@Override
	public void mouseMoved(MouseEvent e) { }

}
