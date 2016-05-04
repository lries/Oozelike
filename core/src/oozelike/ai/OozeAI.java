package oozelike.ai;

import oozelike.roguelike.Actor;
import oozelike.roguelike.Item;

/***********************************************************
 * An AI class for oozes
 * Functions like a simple memory AI, but it can also eat
 * (For eating info, see the eating section) 
 ***********************************************************/
public class OozeAI extends SimpleMemoryAI {
	private static final long serialVersionUID = -496901794736740175L;
	int nutriment; 
	
	/***********************************
	 * Constructor
	 * @param vision the vision radius
	 ***********************************/
	public OozeAI(int vision) {
		super(vision);
		nutriment = 100;
	}

	@Override
	/*******************************************
	 * Standard onTurn method.
	 * Should probably be updated to seek foes. 
	 *******************************************/
	public boolean onTurn(){
		super.onTurn();
		for (Actor key: memories.keySet()){
			if (key != null && memories.get(key) < 0 && hunt(key)) return true; 
		}
		wander(); 
		nutriment--;
		return true; 
	}
	
	@Override
	/**************************************************************************
	 * Eat an item
	 * @param i the item to eat
	 * @return whether the item was eaten successfully
	 * Does *NOT* delete the eaten item - that's the caller's responsibility! 
	 **************************************************************************/
	public boolean eat(Item i){
		if (i.eat(holder)){
			nutriment += i.getNutriment();
			return true;
		}
		return false; 
	}
}
