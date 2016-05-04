package oozelike.ai;

import oozelike.roguelike.AI;

/**************************************
 * PlantAI - an AI that does nothing
 * Appropriate for e.g. plants
 **************************************/
public class PlantAI extends AI {
	private static final long serialVersionUID = 6814340200220361297L;

	/***********************************
	 * Standard AI constructor
	 * @param vision the vision radius
	 ***********************************/
	public PlantAI(int vision) {
		super(vision);
	}
}
