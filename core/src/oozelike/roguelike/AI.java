package oozelike.roguelike;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*********************************************************************************************
 * Class managing AI behavior for enemies. 
 * Individual AIs extending this can be found in the AI package. 
 * It provides a variety of useful methods and the general functions by which it is accessed.
 *********************************************************************************************/
public class AI implements Serializable{
	private static final long serialVersionUID = 407320253869319383L;
	protected Actor holder; 
	protected int vision; 
	public AI(int vision){
		this.vision = vision; 
	}

	/**********************************************************************************
	 * Change the holder of the AI. 
	 * This is generally called only once, after the AI's future holder is initialized.
	 * @param actor the new holder 
	 **********************************************************************************/
	public void setHolder(Actor actor) {
		holder = actor; 
	}

	/************************************************************************
	 * Execute the turn. 
	 * Note: this function does nothing; it is overrode by AI's descendants 
	 * @return whether anything happened 
	 ************************************************************************/
	public boolean onTurn() {
		return true; 
	}

	/************************************************************************
	 * Eat an item. This checks whether the holder is able to eat i as well.
	 * @param i the item to eat
	 * @return whether or not the item was successfully eaten 
	 ************************************************************************/
	public boolean eat(Item i) {
		return false; 
	}

	/************************************************************************
	 * Utility method for checking if the holder is alive
	 * @return whether the holder is alive
	 ************************************************************************/
	public boolean isAlive(){
		return !holder.getCombatManger().isDead();
	}

	/************************************************************************
	 * Method for checking if a particular square can be seen
	 * @param x square to see
	 * @param y square to see
	 * @return whether it can be seen
	 ***********************************************************************/
	public boolean canSee(int x, int y){
		int hx = holder.getX();
		int hy = holder.getY(); 
		if (Math.sqrt((x-hx)*(x-hx)+(y-hy)*(y-hy)) > vision+0.5) return false; 
		List<int[]> line = Shapes.getLine(hx, hy, x, y);
		for (int pos=0; pos<line.size()-1; pos++){
			if (!holder.getDungeon().getTile(line.get(pos)[0], line.get(pos)[1]).canWalk(WalkType.LIGHT)) return false; 
		}
		whenSee(x, y);
		return true;
	}

	/************************************************************************
	 * Called when a square is seen for the purpose of certain AIs 
	 * @param x the x position
	 * @param y the y position
	 ************************************************************************/
	protected void whenSee(int x, int y) {

	}

	/**************************************
	 * Get all tiles in the field of view
	 * @return the field of view
	 **************************************/
	public List<int[]> tilesInSight(){
		List<int[]> ret = new ArrayList<int[]>();
		for (int x=Math.max(0, holder.getX()-vision); x<Math.min(holder.getDungeon().getWidth(), holder.getX()+vision+1); x++) {
			for (int y=Math.max(0, holder.getY()-vision); y<Math.min(holder.getDungeon().getHeight(), holder.getY()+vision+1); y++) {
				if (!canSee(x,y)) continue; 
				int[] pos = {x, y};
				ret.add(pos);
			}			
		}
		return ret; 

	}

	/******************************************
	 * Get all creatures in the field of view
	 * @return a list of visible creatures
	 ******************************************/
	public List<Actor> creaturesInSight(){
		List<Actor> ret = new ArrayList<Actor>();
		for (int[] pos: tilesInSight()){
			Actor atPos = holder.getDungeon().getActor(pos[0], pos[1]);
			if (atPos != null) ret.add(atPos);
		}
		return ret; 
	}

	/*******************************************
	 * Method for moving randomly
	 * @return whether moved successfully
	 *******************************************/
	protected boolean wander(){
		int dx = Core.rand.nextInt(3)-1;
		int dy = Core.rand.nextInt(3)-1; 
		return move(dx, dy);
	}

	/*******************************************
	 * Method for moving
	 * @param dx change in x position
	 * @param dy change in y position
	 * @return whether an action occurred
	 *******************************************/
	public boolean move(int dx, int dy){
		if (Math.abs(dx) > 1 || Math.abs(dy) > 1) return false; 
		if (dx+holder.getX() >= holder.getDungeon().getWidth() || dx+holder.getX() < 0) return false; 
		if (dy+holder.getY() >= holder.getDungeon().getHeight() || dy+holder.getY() < 0) return false; 
		else if (holder.getDungeon().addActor(holder.getX()+dx, holder.getY()+dy, holder, false)) return true;
		if (isAggressiveTo(holder.getDungeon().getActor(holder.getX()+dx, holder.getY()+dy))){
			holder.getCombatManger().makeBasicAttack(holder.getDungeon().getActor(holder.getX()+dx, holder.getY()+dy).getCombatManger());
			return true; 
		}
		return false; 
	}

	/*******************************************
	 * Method for moving without attacking
	 * @param dx change in x position
	 * @param dy change in y position
	 * @return whether movement occured
	 *******************************************/
	public boolean justMove(int dx, int dy){
		if (Math.abs(dx) > 1 || Math.abs(dy) > 1) return false; 
		return holder.getDungeon().addActor(holder.getX()+dx, holder.getY()+dy, holder, false);
	}

	/*******************
	 * Called when hit
	 *******************/
	public void onHit(Actor attacker) {

	}

	/**********************************************
	 * Determines aggression
	 * @param a an actor to check
	 * @return whether this is aggressive to them 
	 **********************************************/
	public boolean isAggressiveTo(Actor a) {
		return false;
	}

	/**********************************************
	 * Determines learning abilities
	 * @param prop a property to possibly learn
	 * @return whether this AI can learn it
	 ***********************************************/
	public static boolean canLearnProperty(Property prop) {
		return false;
	}

	/***********************************************
	 * Determines learning abilities
	 * @param skill a skill to possibly learn
	 * @return whether this AI can learn it
	 ***********************************************/
	public static boolean canLearnSkill(CombatSkill skill) {
		return false;
	}

	/***********************************************
	 * Called on death.
	 * Of importance to certain AIs (e.g. player)
	 ***********************************************/
	public void die() {

	}

	/***********************************************
	 * Move in the direction of a point
	 * @param nx the point's position by x
	 * @param ny the point's position by y
	 * @return whether movement was successful
	 ***********************************************/
	public boolean goTo(int nx, int ny){
		int dx = nx - holder.getX(); 
		if (dx > 1) dx = 1; 
		if (dx < -1) dx = -1; 
		int dy = ny - holder.getY(); 
		if (dy > 1) dy = 1; 
		if (dy < -1) dy = -1; 
		System.out.println(dx+" "+dy);
		return move(dx, dy);
	}
	
	/********************************************************
	 * Hunt - hunt a target
	 * @param target the target to hunt
	 * @return whether hunting was successful
	 * Currently doesn't consider other actors due to bugs. 
	 ********************************************************/
	public boolean hunt(Actor target){
		List<int[]> noActorsPath = Pathfinder.findPathAStar(holder.getDungeon(), holder.getX(), holder.getY(), target.getX(), target.getY(), holder.getWalkType(), false, true);
		if (noActorsPath == null || noActorsPath.isEmpty()) return false; 
		goTo(noActorsPath.get(0)[0], noActorsPath.get(0)[1]);
		return true; 
	}
}
