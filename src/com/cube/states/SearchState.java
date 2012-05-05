package com.cube.states;

import javax.vecmath.Vector2d;

import com.cube.core.Clan;
import com.cube.core.Entity;
import com.cube.core.Physics;
import com.cube.core.Resources;
import com.cube.util.Utilities;

public class SearchState extends State {

	Vector2d tempVect;
	
	/*
	 * Entering the Hunt state means entering the 'search' or probably more accurately, 'wander,' movement
	 * mechanic. The hunting entity is given a randomly generated destination to begin walking towards, and
	 * is pushed in that direction.
	 */
	@Override
	public void enter(Entity e) {
		
		if (debugMessages) System.out.println("Entity " + e + " is now hunting!");
		
		Physics.updateDestination(e, 25);
		e.force.set(e.destination.x * Physics.SPEED.SLOW.value(), e.destination.z * Physics.SPEED.SLOW.value());
	}

	/*
	 * The execution for the hunter primarily means continuing his search through the world for food. If the 
	 * hunter gets close enough to his destination, it should reset to continue his search (this is in order
	 * to avoid the effect of circling a position tighter and tighter until the hunter steps on the destination
	 * point. Getting within a close enough position means his search looks much more realistic).
	 */
	@Override
	public void execute(Entity e) {
		boolean print = false;
		if (e.types.contains(Clan.HUNTER)) print = true;
		// Look for resources
		for (Entity x : Resources.entities) {
			if (Physics.distSquared(x.position, e.position) < 400) {		//Entity spotted a resource

				if (Utilities.containsAny(e.targets, x.types)) {			//Entity can interact with resource

					if (print) System.out.println("Type: " + Utilities.printArrayList(x.types));

					e.focusEntity = x;										
					e.setDestination(e.focusEntity.position);
					
					if (x.types.contains(Entity.EDIBLE))					//Resource type is immediately edible, travel to it
						e.changeState( TravelState.getState() );
					else {													//Resource must be mined/chopped/attacked
						e.changeState( AttackState.getState() );
						x.focusEntity = e;

						if (e.focusEntity.types.contains(Entity.PASSIVE))	// If the focus is passive, it should flee
							e.focusEntity.changeState( FleeState.getState() );
						else												// If the focus is not passive, it counter attacks
							e.focusEntity.changeState( AttackState.getState() );
					}
					return;
				}
			}
		}
		
		// Didn't see anything and arrived at destination, set a new destination
		if (Physics.distSquared(e.position, e.destination) < 10) {
			Physics.updateDestination(e, 25);
			e.force.set(e.destination.x * Physics.SPEED.SLOW.value(), 
					    e.destination.z * Physics.SPEED.SLOW.value());
		}
		
		// Didn't see anything and has not arrived at destination, keep moving
		tempVect = Physics.seekDestination(e);
		e.force.set(tempVect.x * Physics.SPEED.SLOW.value(), 
				    tempVect.y * Physics.SPEED.SLOW.value());

	}

	/*
	 * Exiting the hunt means cleaning up any junk we set up in order to start the hunt! 
	 */
	@Override
	public void exit(Entity e) {
		if (debugMessages) System.out.println("Entity " + e + " is no longer hunting!");
	}
	
	//-------------------------------------------------------------------------//
	//------------------------ Singleton Necessities --------------------------//
	//-------------------------------------------------------------------------//
	
	/*
	 * The following is required for every singleton. Characteristics of java and object orientation
	 * means that this needs to be copied (and slightly modified) for each new state :(. This will 
	 * probably be modified in the future to be a bit cleaner.
	 */
	
	// Self reference
	protected static State ref;
	
	// Return the instance of this singleton state
	public static synchronized State getState() {
		if (ref == null) {
			ref = new SearchState();
		}
		return ref;
	}
}