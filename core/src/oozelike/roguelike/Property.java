package oozelike.roguelike;

import java.io.Serializable;
/*************************************************
 * Represents a property an actor has
 * It might have an effect on attack or defend 
 * or one every turn, or one when gained or lost
 *************************************************/
public abstract class Property implements Serializable {
	private static final long serialVersionUID = -9104027931995716785L;

	/************************************
	 * Called on attack
	 * @param holder the holder
	 * @param target the attack victim
	 ************************************/
	public abstract void onAttack(Actor holder, Actor target);
	
	/*************************************
	 * Called on defend
	 * @param holder the holder of this
	 * @param attacker the attacker
	 *************************************/
	public abstract void onDefend(Actor holder, Actor attacker);
	
	/*************************************
	 * Called every turn
	 * @param holder the holder of this
	 *************************************/
	public abstract void onTurn(Actor holder);
	
	/**************************************
	 * Called when this is learned
	 * @param holder the holder of this
	 **************************************/
	public abstract void onLearn(Actor holder);
	
	/**************************************
	 * Called when this is forgotten
	 * @param holder the holder of this
	 **************************************/
	public abstract void onForget(Actor holder);
}
