package oozelike.properties;

import oozelike.roguelike.Actor;
import oozelike.roguelike.CombatManager;
import oozelike.roguelike.Property;

public class CombatStatProperty extends Property {
	private static final long serialVersionUID = 6287710037833902185L;
	int mHPChange; 
	int atkChange;
	int defChange;
	int dexChange; 
	int intChange; 
	int phChange;

	/**************************************
	 * Create a new combat stat property
	 * @param hp the change in HP
	 * @param atk the change in ATK
	 * @param def the change in DEF
	 * @param dex the change in DEX
	 * @param in the change in INT
	 * @param ph the change in PH
	 **************************************/
	public CombatStatProperty(int hp, int atk, int def, int dex, int in, int ph){
		mHPChange = hp; 
		atkChange = atk;
		defChange = def;
		dexChange = dex; 
		intChange = in; 
		phChange = ph; 
	}

	@Override
	public void onAttack(Actor holder, Actor target) {
	}

	@Override
	public void onDefend(Actor holder, Actor attacker) {
	}

	@Override
	public void onTurn(Actor holder) {
	}

	@Override
	/*****************************************
	 * Called when the property is learned.
	 * Imparts the according stat changes.
	 *****************************************/
	public void onLearn(Actor holder) {
		CombatManager affected = holder.getCombatManger(); 
		affected.modMHP(mHPChange);
		affected.modAtk(atkChange);
		affected.modDef(defChange);
		affected.modInt(intChange);
		affected.modPH(phChange);
	}

	@Override
	/*****************************************
	 * Called when the property is forgotten.
	 * Removes the according stat changes.
	 *****************************************/
	public void onForget(Actor holder) {
		CombatManager affected = holder.getCombatManger(); 
		affected.modMHP(-mHPChange);
		affected.modAtk(-atkChange);
		affected.modDef(-defChange);
		affected.modInt(-intChange);
		affected.modPH(-phChange);
		
	}
	
	
	
}
