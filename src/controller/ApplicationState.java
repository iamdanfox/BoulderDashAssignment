package controller;

import java.util.Collection;
import java.util.HashSet;

import model.Cave;

import view.CaveView;


/**
 * Keeps track of the application state.  This includes the cave model, the main cave view and the current controller mode.
 *
 */
public class ApplicationState {
	
	public final Cave cave;
	public final CaveView caveView;
	
	private final Collection<InteractionMode> modes = new HashSet<InteractionMode>();
	private InteractionMode activeMode;
	
	public ApplicationState(Cave cave){
		this.cave = cave;
		this.caveView = new CaveView(this);
	}

	/**
	 * Adds a mode to the pool of available modes.  It also notifies the InteractionMode of its parent 
	 * application state (this) and enables the mode to perform any necessary 'construction'. 
	 * @param m
	 */
	public void registerMode(InteractionMode m){
		// tell interaction mode who owns it
		m.registerTo(this);
		modes.add(m);
	}
	
	public InteractionMode getActiveMode(){
		return activeMode;
	}
	
	public void setActiveMode(InteractionMode m){
		// Invariant: only one mode is active at once.
		if (!modes.contains(m)) throw new RuntimeException("Cannot make an unregistered mode active");
		
		// deactivate old mode, make it stop listening to events 
		InteractionMode oldMode = activeMode;
		if (oldMode!=null) {
			oldMode.makeNotActive();
			System.out.println(oldMode.getClass() + " made not active");		
		}
		
		// activate new mode, ensure it is wired up to receive events in the cave, keyboard and mouse activity.
		System.out.println("make "+ m.getClass() + " active");
		activeMode = m;
		m.makeActive();
	}
}
