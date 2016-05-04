package oozelike.ai;
import com.badlogic.gdx.Input.Keys;
import oozelike.io.KeyLogger;
import oozelike.roguelike.AI;
import oozelike.roguelike.Actor;
import oozelike.roguelike.Core;
import oozelike.roguelike.Item;
import oozelike.roguelike.Memory;
import oozelike.screens.DungeonOverviewScreen;

/**********************************************************************
 * PlayerAI - the standard player AI
 * The PlayerAI is controlled by a key logger
 * If multiple are created, the player will control each one at a time
 * (although death screens might not function correctly) 
 **********************************************************************/
public class PlayerAI extends AI {
	private static final long serialVersionUID = 6262115712594560664L;
	private int nutriment;
	private KeyLogger input;
	private Memory memory; 

	/*************************************************************************
	 * Create a PlayerAI
	 * @param vision the radius of vision
	 * @param memory the Memory pointer used for remembered tiles (for FOW) 
	 * @return a new PlayerAI
	 *************************************************************************/
	public PlayerAI(int vision, Memory memory) {
		super(vision);
		nutriment = 100; 
		input = new KeyLogger(); 
		this.memory = memory; 
	}

	/*****************************************
	 * Eat an item
	 * @param i the item to eat
	 * @return whether it was correctly eaten
	 *****************************************/
	public boolean eat(Item i){
		if (i.eat(holder)){
			nutriment += i.getNutriment();
			return true;
		}
		return false; 
	}
	
	/**********************************************************************************
	 * Change the holder of the AI. 
	 * This is generally called only once, after the AI's future holder is initialized.
	 * @param actor the new holder 
	 **********************************************************************************/
	public void setHolder(Actor actor) {
		holder = actor; 
		memory.setActor(actor);
	}
	
	/************************************************************************
	 * Method for learning a tile in the dungeon
	 * @param x the x position of the tile
	 * @param y the y position of the tile
	 ************************************************************************/
	public void learn(int x, int y){
		memory.learn(x, y);
	}
	
	@Override
	/***********************************
	 * Called when a tile is seen. 
	 * Used for memory reasons, mostly. 
	 ***********************************/
	protected void whenSee(int x, int y){
		learn(x, y);
	}
	
	@Override
	/*********************************************************
	 * Called on every turn
	 * @return true if something actually happened in a turn
	 * (if false, the player is NOT moved in the queue!)
	 *********************************************************/
	public boolean onTurn(){
		super.onTurn();
		Core.redraw = true; 
		Integer log = input.checkKeys();
		if (log == null) return false; 
		switch (log){
		case Keys.LEFT: 
			move(-1, 0);
			break; 
		case Keys.RIGHT:
			move(1, 0);
			break; 
		case Keys.UP:
			move(0, 1);
			break; 
		case Keys.DOWN:
			move(0, -1); 
			break; 
		case Keys.E:
			Core.game.setScreen(new DungeonOverviewScreen(holder.getDungeon().getWorld()));
		case Keys.X:
			holder.useStairs();
			holder.getDungeon().getWorld().changeFloors(holder.getDungeon());
			break; 
		default:
			return false; 
		}
		housekeep(); 
		return true; 
	}
	
	/**********************************************
	 * Run on a turn that does *something*
	 * (e.g. onTurn method for successful turns)
	 **********************************************/
	private void housekeep(){
		holder.makeFocus();
		nutriment--; 
		if (nutriment < 0) ;
	}
	
	/**********************************************
	 * Determines aggression
	 * @param a an actor to check
	 * @return whether this is aggressive to them 
	 **********************************************/
	public boolean isAggressiveTo(Actor a) {
		return a != null;
	}
	
	public boolean isPlayer() { 
		return true; 
	}
}