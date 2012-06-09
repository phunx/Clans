package com.cube.states;

import com.cube.core.Entity;
import com.cube.core.Resources;
import com.cube.gui.Menu;

public class DepositState extends State {
	
	@Override
	public void enter(Entity e) {
		if (debugMessages) System.out.println("Entity " + e + " is now depositing resources!");		
	}

	@Override
	public void execute(Entity e) {
		
		if (e.pause(10)) {

			if (e.inventory.contains("MEAT")) {
				e.inventory.removeItem(Resources.itemLibrary.get("MEAT"));
				e.clanRef.clanStockpile.addItem(Resources.itemLibrary.get("MEAT"));
			}
			else if (e.inventory.contains("BERRY")) {
				e.inventory.removeItem(Resources.itemLibrary.get("BERRY"));
				e.clanRef.clanStockpile.addItem(Resources.itemLibrary.get("BERRY"));
			}
			else if (e.inventory.contains("WOOD")) {
				e.inventory.removeItem(Resources.itemLibrary.get("WOOD"));
				e.clanRef.clanStockpile.addItem(Resources.itemLibrary.get("WOOD"));
			}
			else if (e.inventory.contains("LEATHER")) {
				e.inventory.removeItem(Resources.itemLibrary.get("LEATHER"));
				e.clanRef.clanStockpile.addItem(Resources.itemLibrary.get("LEATHER"));
			}
			
			if (e.inventory.isEmpty()) { 											//Done depositing
				if (e.focusEntity == null || e.focusEntity.inventory.isEmpty()) { 	//Last target is empty
					e.changeState( SearchState.getState() );
				}
				else { 																//Last target still has resources
					e.setDestination(e.focusEntity.position);
					e.changeState( TravelState.getState() );
				}
			}
		}
	}

	@Override
	public void exit(Entity e) {
		if (debugMessages) System.out.println("Entity " + e + " is no longer depositing resources.");
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
			ref = new DepositState();
		}
		return ref;
	}
}