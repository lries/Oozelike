package oozelike.ai;

import java.util.HashMap;
import java.util.Map;

import oozelike.roguelike.AI;
import oozelike.roguelike.Actor;

/*******************************************************************************
 * An AI for passive creatures that become aggressive when attacked
 * If they are left unbothered for a long time they will become peaceful again. 
 * This AI is intended to be extended, NOT to be used on its own! 
 * It does nothing on its own.
 *******************************************************************************/
public class SimpleMemoryAI extends AI {
	private static final long serialVersionUID = 7173507854541416629L;
	protected Map<Actor, Integer> memories;
	
	/**********************************
	 * Create a SimpleMemoryAI
	 * @param vision the vision radius
	 * @return a new SimpleMemoryAI
	 **********************************/
	public SimpleMemoryAI(int vision) {
		super(vision);
		memories = new HashMap<Actor, Integer>(); 
	}

	@Override
	/*************************************************
	 * Called when attacked - updates memory.
	 * @param attacker the attacking target
	 * The memory will always be set to at *most* -50
	 *************************************************/
	public void onHit(Actor attacker){
		int currentValue = 0;
		if (memories.containsKey(attacker)) currentValue = memories.get(attacker);
		memories.put(attacker, Math.min(-50, currentValue-50));
	}
	
	@Override
	/************************************************
	 * Called on turn.
	 * Updates all memories; does nothing
	 * @return whether the turn was successful
	 * If not, they will NOT move down in the queue! 
	 ************************************************/
	public boolean onTurn(){
		for (Actor key: memories.keySet()){
			if (key.isDead()){
				memories.remove(key);
				continue; 
			}
			int value = memories.get(key);
			if (value < 0) value += 1; 
			else if (value > 0) value -= 1; 
			if (value != 0)	memories.put(key, value);
			else memories.remove(key);
		}
		return true;
	}
	
	@Override
	/******************************************************
	 * Determine aggression. 
	 * SimpleMemoryAI is aggressive if the memory is < 0 
	 * @return if this is aggressive
	 ******************************************************/
	public boolean isAggressiveTo(Actor a){
		if (a != null && memories.containsKey(a) && memories.get(a) < 0) return true;
		return false; 
	}
}
