package oozelike.roguelike;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import oozelike.ui.RenderingUtilities;

/***********************************************************************************
 * This class represents an actor (e.g. a creature who gets turns) in the dungeon
 * Actors include enemies, NPCs, the player, plants, etc. 
 * The behavior choices of an actor are mostly relegated to the AI 
 * The combat behavior of an actor is mostly relegated to the CombatManager
 * The actor class itself handles rendering, location, item ownership, and abilities
 * It also ties the other parts of an actor together.
 ************************************************************************************/
public class Actor implements Serializable {
	private static final long serialVersionUID = -6042835994892666669L;
	//Private member variables
	//Location
	private int x; 
	private int y; 
	private Dungeon dungeon;

	//Behavior
	private AI ai; 
	private CombatManager combatManager;
	private WalkType walktype; 
	private List<Property> properties; 

	//Rendering
	private Visible visible;

	//Inventory
	private Inventory inventory; 
	private Body body; 

	/*************************************************************************************
	 * Actor constructor. 
	 * @param dungeon the dungeon this actor starts in
	 * @param ai the initial AI of the actor
	 * @param visible the initial Visible for this actor
	 * @param body this actor's body
	 * @param inventorySize initial max inventory size
	 * @param cm the combat manager this actor will use
	 * @param walktype this actor's movement type
	 * @return a new actor with position -1,-1.
	 *************************************************************************************/
	public Actor(Dungeon dungeon, AI ai, Visible visible, Body body, int inventorySize, CombatManager cm, WalkType walktype){
		this.x = -1;
		this.y = -1; 
		this.dungeon = dungeon; 

		this.ai = ai; 
		this.ai.setHolder(this); 
		this.walktype = walktype; 
		this.combatManager = cm;
		this.combatManager.setHolder(this); 
		this.properties = new ArrayList<Property>(); 

		this.visible = visible;

		this.inventory = new Inventory(inventorySize); 
		this.body = body;
	}

	/*****************************************************************************
	 * Execute this Actor's turn when called
	 * @return whether anything happens
	 * Note that if this returns false, the actor immediately gets the next turn! 
	 * If the actor is resting, this still needs to return true! 
	 *****************************************************************************/
	public boolean onTurn() {
		return ai.onTurn(); 
	}

	/************************************************
	 * Eat an item. Eating is AI-dependent behavior. 
	 * @param i the item to be eaten
	 * @return whether eating was a success
	 ************************************************/
	public boolean eat(Item i){
		return ai.eat(i); 
	}

	/***************************************************
	 * Obtain an item
	 * @param i the item to be obtained
	 * @return whether the item was added to inventory
	 ***************************************************/
	public boolean obtain(Item i){
		return inventory.addItem(i); 
	}

	/****************************************************
	 * Drop an item
	 * @param i the item to be dropped
	 * @return whether the item was dropped 
	 ****************************************************/
	public boolean drop(Item i){
		if (!inventory.dropItem(i)) return false; 
		dungeon.addItem(i, x, y);
		return true; 
	}

	/****************************************************
	 * Wear an item
	 * @param i the item to wear
	 * @return whether it was successfully worn 
	 ****************************************************/
	public boolean wear(Item i){
		return body.addItem(i);
	}

	/****************************************************
	 * Unwear an item
	 * @param i the item to unwear
	 * @return whether it was successfully unworn 
	 ***************************************************/
	public boolean unwear(Item i){
		return body.removeItem(i);
	}

	/***********************************************
	 * Render the object.
	 * This renders both monster and health bar.
	 ***********************************************/
	public void render() {
		List<int[]> FOV = Core.playerMemory.getHoldersFOV(); 
		for (int[] in: FOV){
			if (in[0]==x && in[1]==y){
				visible.render(x, y);
				RenderingUtilities.drawHealthBar(this);
				return; 
			}
		}
	}

	//Getters
	public int getX() { return x; }
	public int getY() { return y; }
	public Dungeon getDungeon() { return dungeon; } 
	public CombatManager getCombatManger() { return combatManager; } 
	public WalkType getWalkType() { return walktype; } 

	/*******************************************
	 * Sets X and triggers a redraw if needed
	 * @param x2 the x location
	 * Future edits may move this functionality.
	 *******************************************/
	public void setX(int x2) {
		x = x2; 
		Core.determineRedraw(x,y); 
	}
	
	/*******************************************
	 * Sets Y and triggers a redraw if needed. 
	 * @param y2 the y location
	 * Future edits may move this functionality.
	 *******************************************/
	public void setY(int y2) { 
		y = y2; 
		Core.determineRedraw(x,y); 
	}
	public void setDungeon(Dungeon dungeon2) { dungeon = dungeon2; }

	/*************************************************
	 * Called when attacked. Really just accesses AI.
	 * @param attacker the attacker
	 *************************************************/
	public void onHit(Actor attacker) {
		ai.onHit(attacker);
	}

	/*************************************************
	 * Called when a property is learned.
	 * @param prop the property to potentially learn.
	 *************************************************/
	public void learnProperty(Property prop) {
		if (AI.canLearnProperty(prop)) properties.add(prop);

	}

	/*************************************************
	 * Called when a skill is learned
	 * @param skill the skill to potentially learn.
	 *************************************************/
	public void learnSkill(CombatSkill skill) {
		if (AI.canLearnSkill(skill)) combatManager.learnSkill(skill); 
	}

	/**********************************
	 * Make this actor the focus. 
	 **********************************/
	public void makeFocus() {
		Core.focusX = x;
		Core.focusY = y;
	}
	
	/****************************************
	 * Get the field of view of this actor.
	 * @return a list of tiles in sight
	 ****************************************/
	public List<int[]> getFOV() {
		return ai.tilesInSight(); 
	}

	/****************************************************
	 * Render this actor, potentially ignoring shadows.
	 * @param ignoreShadows whether to ignore visibility
	 ****************************************************/
	public void render(boolean ignoreShadows) {
		if (ignoreShadows) {
			visible.render(x, y);
			RenderingUtilities.drawHealthBar(this);
		}
		else render();
	}
	
	/*******************
	 * Use the stairs.
	 *******************/
	public void useStairs() {
		dungeon.useStairs(x,y);
	}

	/*******************************************
	 * Die. Also checks AI for death behavior.
	 *******************************************/
	public void die() {
		dungeon.removeActor(this);
		ai.die(); 
	}

	/*************************************************************************************************************
	 * Check if this is dead.
	 * Generally dead actors will be garbage collected, but this is used by MemoryAIs, etc. to forget dead people
	 * @return whether this is dead.
	 *************************************************************************************************************/
	public boolean isDead() {
		return combatManager.isDead();
	}
}