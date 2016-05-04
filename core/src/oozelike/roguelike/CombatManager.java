package oozelike.roguelike;
import java.io.Serializable;
import java.util.List;

/*********************************************************
 * A class for managing creature combat behavior.
 * Handles stats, attacking, etc. etc. 
 * May at times need to access things through its holder.
 *********************************************************/
public class CombatManager implements Serializable {
	private static final long serialVersionUID = 8603651278344894960L;
	private Actor holder; 
	private boolean dead = false; 
	private int HP; 
	private int maxHP; 
	private int baseAtk; 
	private int baseDef; 
	private int baseInt;
	private int baseDex; 
	private int basePH; 
	private List<CombatSkill> skills; 
	
	/******************************************************************************
	 * CombatManager constructor
	 * @param actor the holder (it is assumed this CM is already assigned to them)
	 * @return this combatManager
	 ******************************************************************************/
	public CombatManager(Actor actor, int maxHP, int baseAtk, int baseDef, int baseInt, int baseDex, int basePH) {
		this.maxHP = maxHP;
		this.HP = this.maxHP; 
		this.baseAtk = baseAtk; 
		this.baseDef = baseDef;
		this.baseInt = baseInt; 
		this.baseDex = baseDex; 
		this.basePH = basePH; 
		holder = actor;
	}

	public void changeHP(int i) {
		HP += i; 
		if (HP < 0){
			holder.die(); 
			dead = true; 
		}
		if (HP > maxHP) HP = maxHP; 
	}
	
	//Getters
	public boolean isDead() { return dead; } 
	public int getMaxHP() { return maxHP; }
	public int getHP() { return HP; }
	public int getBaseAtk() { return baseAtk; }
	public int getBaseDef() { return baseDef; } 
	public int getBaseInt() { return baseInt; }
	public int getBaseDex() { return baseDex; }
	public int getBasePH() { return basePH; } 
	public Actor getHolder() { return holder; }
	//setter
	public void setHolder(Actor holder) { this.holder = holder; } 
	
	/****************************************************************
	 * Calculates functional attack, considering things like buffs.
	 * (Currently, that's just attack, but it'll change!)
	 * @return the functional attack value
	 ****************************************************************/
	public int getFunctionalAtk(){
		int fAtk = baseAtk; 
		return fAtk; 
	}

	/*********************************************************************
	 * Calculates functional intelligence, considering things like buffs.
	 * (Currently, that's just intelligence, but it'll change!)
	 * @return the functional intelligence value
	 *********************************************************************/
	public int getFunctionalInt(){
		int fInt = baseInt; 
		return fInt; 
	}
	
	/****************************************************************
	 * Calculates functional defense, considering things like buffs.
	 * (Currently, that's just defense, but it'll change!)
	 * @return the functional defense value
	 ****************************************************************/
	public int getFunctionalDef(){
		int fDef = baseDef; 
		return fDef; 
	}
	
	/****************************************************************
	 * Calculates functional dexterity, considering things like buffs.
	 * (Currently, that's just dexterity, but it'll change!)
	 * @return the functional dexterity value
	 ****************************************************************/
	public int getFunctionalDex(){
		int fDex = baseDex; 
		return fDex; 
	}
	
	/******************************************************************************
	 * Calculates functional PH, considering things like buffs.
	 * (Currently, that's just PH, but it'll change!)
	 * @return the functional PH value
	 * PH is sort of a weird stat, but because *many* enemies in the game (and the 
	 * player!) are highly basic or acidic, it isn't as useless as it seems! 
	 ******************************************************************************/
	public int getFunctionalPH(){
		int fPH = basePH; 
		return fPH; 
	}
	
	/*********************************************************************************
	 * Make a basic attack against a target. This is a special action, not a skill! 
	 * Because of that it may occasionally bypass skill boosts and opponent's skills. 
	 * Nothing that takes damage is fully immune to a basic attack. 
	 * @param target the creature to attack
	 *********************************************************************************/
	public void makeBasicAttack(CombatManager target){
		int atk = getFunctionalAtk(); 
		int def = target.getFunctionalDef(); 
		float randomFactor = (Core.rand.nextFloat())*0.4f + 0.2f; //in range 0.8-1.2
		int damage = Math.max(1, (int) (randomFactor*(atk/(0.5*def)-0.5*def))); 
		target.onHit(damage, holder);
		if (target.isDead()){
			//Future code for leveling up and stuff
		}
	}

	/*********************************************
	 * Ability triggered on a hit taking damage.
	 * @param damage the amount of damage taken
	 * @param attacker the attacker's identity
	 *********************************************/
	private void onHit(int damage, Actor attacker) {
		holder.onHit(attacker);
		changeHP(-1*damage);
	}

	/*************************************************
	 * Learn a skill. The CombatManager tracks them.
	 * @param skill the skill to learn
	 *************************************************/
	public void learnSkill(CombatSkill skill) {
		skills.add(skill); 
	}

	/*****************************************************************************************
	 * Modifiers for the different base stats.
	 * All of them change the stat by the provided amount, then clamp it to a legal value. 
	 *****************************************************************************************/
	public void modMHP(int mHPChange) {
		maxHP += mHPChange; 
		if (maxHP < 1) maxHP = 1; 
		if (maxHP > 999) maxHP = 999; 
	}
	public void modAtk(int AtkChange){
		baseAtk += AtkChange; 
		if (baseAtk < 1) baseAtk = 1; 
		if (baseAtk > 999) baseAtk = 999; 
	}
	public void modDef(int DefChange){
		baseDef += DefChange; 
		if (baseDef < 1) baseDef = 1; 
		if (baseDef > 999) baseDef = 999; 
	}
	public void modDex(int DexChange){
		baseDex += DexChange; 
		if (baseDex < 1) baseDex = 1; 
		if (baseDex > 999) baseDex = 999; 
	}
	public void modInt(int IntChange){
		baseInt += IntChange; 
		if (baseInt < 1) baseInt = 1; 
		if (baseInt > 999) baseInt = 999; 
	}
	public void modPH(int PHChange){
		basePH += PHChange; 
		if (basePH < 0) baseAtk = 0; 
		if (basePH > 14) baseAtk = 14; 
	}
}
