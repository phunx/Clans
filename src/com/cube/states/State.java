package com.cube.states;

import com.cube.core.Entity;

public abstract class State {
	
	// Executes when a state is entered
	abstract public void enter(Entity e);
	
	// Used to update the current state
	abstract public void execute(Entity e);
	
	// Executes when a state is exited
	abstract public void exit(Entity e);
	
	// Ensure access to constructor is blocked.
	protected State() {}
}
