/**
 * 
 */
package controller;

import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.util.*;

import javax.swing.event.MouseInputListener;


/**
 * An InteractionMode is a class that enables the user to interact with the
 * underlying cave model rather than just observing it. It is primarily part of
 * the 'controller' but does allow painting over the view.
 * 
 */
public abstract class InteractionMode implements KeyListener, MouseInputListener {
	
	private final Collection<ModeListener> modeListeners = new HashSet<ModeListener>();
	protected ApplicationState appState;
	
	public void registerTo(ApplicationState appState){
		this.appState = appState;
		construct();
	}
	
	/**
	 * This method is executed when the interaction mode has been supplied with a cave and caveView by ApplicationState. 
	 * Performs set up of the object.  InteractionMode should be constructed in the Not Active state.
	 */
	protected abstract void construct();
	
	/** 
	 * Make this mode no longer active, stop receiving events from the model, keyboard and mouse.  Notify any ModeListeners.
	 */
	public final void makeNotActive(){
		makeNotActiveDelegate();
		// only stop listening after mode has done its own set down
		//appState.cave.removeCaveListener(this);
		appState.caveView.removeKeyListener(this);
		appState.caveView.removeMouseListener(this);
		appState.caveView.removeMouseMotionListener(this);
		for(ModeListener l : modeListeners) l.madeNotActive(this);
	}
	
	/**
	 * Executed when this mode is made not active.
	 */
	protected abstract void makeNotActiveDelegate();
	
	/**
	 * Make this mode the active mode, start listening to cave model and events from view (keyboard & mouse). Notify ModeListeners.
	 */
	public final void makeActive(){
		//appState.cave.addCaveListener(this);
		appState.caveView.addMouseListener(this);
		appState.caveView.addMouseMotionListener(this);
		appState.caveView.addKeyListener(this);

		// ensure keyboard activity gets through!
		appState.caveView.requestFocusInWindow();
		makeActiveDelegate();
		for(ModeListener l : modeListeners) l.madeActive(this);
	}
	
	/**
	 * Executed when this mode is made active.
	 */
	protected abstract void makeActiveDelegate();
	
	/**
	 * Paint over the interior of the CaveView.  
	 * @param g2d Graphics object to be used.  Clip and origin restrict this method to the space within the borders.
	 */
	public void paintInterior(Graphics2D g2d) {	}	

	// ModeListeners ===================
	public final void registerModeListener(ModeListener l){
		modeListeners.add(l);
	}
	
	public final void removeModeListener(ModeListener l){
		modeListeners.remove(l);
	}

}
