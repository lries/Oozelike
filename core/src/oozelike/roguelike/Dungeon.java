package oozelike.roguelike;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/******************************************************************************
 * A class representing a dungeon.
 * Handles actors, items, and tiles, as well as connections to other dungeons
 * Manages the queue of when actors are to get their turns 
 * Manages interactions between actors and the world 
 ******************************************************************************/
public class Dungeon implements Serializable {
	private static final long serialVersionUID = 6135461677758966532L;
	private Tile[][] map; 
	private Map<Actor,Integer> actors; 
	private Inventory[][] items;
	private World world;
	private StairData[][] stairs; 

	/*****************************************************
	 * Constructor for a dungeon. 
	 * @param map The Tile[][] of the map.
	 * For sample Tile[][] generators see DungeonFactory.
	 *****************************************************/
	public Dungeon(Tile[][] map){
		this.map = map; 
		this.actors = new HashMap<Actor, Integer>(); 
		this.items = new Inventory[map.length][map[0].length]; 
		this.stairs = new StairData[map.length][map[0].length];
		for (int x=0; x<getWidth(); x++){
			for (int y=0; y<getHeight(); y++){
				items[x][y] = new Inventory(); 
			}
		}
	}

	/**********************
	 * Assign to a world
	 * @param w the world
	 **********************/
	public void setWorld(World w) { 
		world = w; 
	}

	/**********************************
	 * Add an item at location (x, y)
	 * @param i the item 
	 * @param x the location 
	 * @param y the location
	 **********************************/
	public void addItem(Item i, int x, int y) {
		items[x][y].addItem(i);
		Core.determineRedraw(x,y); 
	}

	/************************************************
	 * Change the wait-until-turn time for an actor
	 * @param a the actor to change it for
	 * @param change the amount to change it by
	 ************************************************/
	public void changeActorTime(Actor a, int change){
		if (!actors.keySet().contains(a)) return; 
		int newValue = actors.get(a)+change; 
		if (newValue < 0) newValue = 0; 
		actors.put(a, newValue);
	}

	/*********************************************
	 * Get the actor at a specified location. 
	 * @param x the location
	 * @param y the location
	 * @return the actor at [x,y] (or null)
	 *********************************************/
	public Actor getActor(int x, int y){
		for (Actor a:actors.keySet()){
			if (a.getX() == x && a.getY() == y) return a; 
		}
		return null; 
	}

	/*********************************************************************************
	 * Add an actor at x, y
	 * @param x the position 
	 * @param y the position
	 * @param add the actor
	 * @param breakMovementRules whether to put them down somewhere they can't walk
	 * @return whether placement was successful 
	 *********************************************************************************/
	public boolean addActor(int x, int y, Actor add, boolean breakMovementRules){
		if (getActor(x,y) != null){
			return false;
		}
		if (!breakMovementRules && !map[x][y].canWalk(add)) return false; 
		if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
			return false; 
		}
		add.setX(x);
		add.setY(y); 
		add.setDungeon(this);
		int turn = 1; 
		if (actors.containsKey(add)) turn = actors.get(add);
		actors.put(add, turn);
		Core.determineRedraw(x,y); 
		return true; 
	}

	/************************************************
	 * Randomly add an actor
	 * @param add the actor to add 
	 * @return whether they were added successfully
	 ************************************************/
	public boolean randomlyAddActor(Actor add){
		for (int tries = 0; tries < 2000; tries++){
			int x = Core.rand.nextInt(map.length);
			int y = Core.rand.nextInt(map[0].length);
			if (addActor(x, y, add, false)) {
				Core.determineRedraw(x,y); 
				return true; 
			}
		}
		return false; 
	}

	/*******************************************
	 * Remove an actor
	 * @param rem the actor to remove
	 * @return whether removal was successful. 
	 *******************************************/
	public boolean removeActor(Actor rem){
		return actors.remove(rem) != null;
	}

	public Tile getTile(int x, int y){
		return map[x][y]; 
	}

	public Inventory getInventory(int x, int y){
		return items[x][y]; 
	}

	/*******************************************
	 * Add an item and possibly redraw
	 * @param x the x to add at
	 * @param y the y to add at
	 * @param i the item to add
	 * @return whether adding was successful
	 ******************************************/
	public boolean addItem(int x, int y, Item i){
		Core.determineRedraw(x,y); 
		return items[x][y].addItem(i);
	}

	/*****************************************************
	 * Remove an item
	 * @param x the position to remove at
	 * @param y the position to remove at
	 * @param i the item to remove 
	 * @return whether the item was successfully removed
	 *****************************************************/
	public boolean removeItem(int x, int y, Item i){
		Core.determineRedraw(x,y); 
		return items[x][y].dropItem(i);
	}

	/********************************************************
	 * Run the turn
	 * Selects the next actor to get a move, and moves them
	 * Replaces them in the queue accordingly.
	 ********************************************************/
	public void runTurn(){
		Actor best = null;
		int bestScore = Integer.MAX_VALUE;

		//if (actors.keySet().isEmpty()) System.out.println("WARNING: no actors");
		
		for (Actor a:actors.keySet()){
			if (actors.get(a) <= bestScore){
				best = a; 
				bestScore = actors.get(a); 
			}
		}

		if (best == null) return;

		if (best.onTurn()){
			for (Actor a:actors.keySet()){
				actors.put(a, actors.get(a)-bestScore);
			}

			actors.put(best, Math.max(actors.get(best),1));
		}
	}

	public int getWidth() { return map.length; }
	public int getHeight() { return map[0].length; }

	/******************
	 * Render the map
	 ******************/
	public void render() {
		render(0, 0, getWidth(), getHeight(), false);
	} 
	
	/*************************************
	 * Render the map
	 * @param b whether to ignore shadows
	 *************************************/
	public void render(boolean b) {
		render(0, 0, getWidth(), getHeight(), b);
	} 
	
	/*************************************************
	 * Render the map
	 * @param minX minimum X to render
	 * @param minY minimum Y to render
	 * @param maxX max X to render
	 * @param maxY max Y to render
	 * @param ignoreShadows whether to ignore shadows
	 **************************************************/
	public void render(int minX, int minY, int maxX, int maxY, boolean ignoreShadows){
		for (int x=Math.max(0,minX); x<Math.min(maxX, getWidth()); x++){
			for (int y=Math.max(0,minY); y<Math.min(maxY, getHeight()); y++){
				map[x][y].render(x,y, ignoreShadows);
				items[x][y].render(x,y, ignoreShadows); 
			}
		}
		for (Actor a: actors.keySet()){
			if (a.getX() >= minX && a.getX() <= maxX){
				if (a.getY() >= minY && a.getY() <= maxY){
					a.render(ignoreShadows); 
				}
			}
		}
	}


	/*************************************************
	 * Render the map
	 * @param minX minimum X to render
	 * @param minY minimum Y to render
	 * @param maxX max X to render
	 * @param maxY max Y to render
	 ************************************************/
	public void render(int minX, int minY, int maxX, int maxY) {
		for (int x=Math.max(0,minX); x<Math.min(maxX, getWidth()); x++){
			for (int y=Math.max(0,minY); y<Math.min(maxY, getHeight()); y++){
				map[x][y].render(x,y);
				items[x][y].render(x,y); 
			}
		}
		for (Actor a: actors.keySet()){
			if (a.getX() >= minX && a.getX() <= maxX){
				if (a.getY() >= minY && a.getY() <= maxY){
					a.render(); 
				}
			}
		}

	}

	public World getWorld() {
		return world; 
	}

	/**************************************
	 * Add stairs to the map
	 * @param stairData the stairs's data 
	 * @param tile the tile for the stairs
	 **************************************/
	public void addStairs(StairData stairData, Tile tile){
		if (stairData == null || tile == null) {
			System.out.println("ERROR: null stairs");
		}
		for (int attempts = 0; attempts < 10000; attempts++){
			int sx = Core.rand.nextInt(getWidth());
			int sy = Core.rand.nextInt(getHeight());
			if (map[sx][sy].equalsMovement(tile)){
				stairs[sx][sy] = stairData;
				map[sx][sy] = tile; 
				return; 
			}
		}
	}

	/****************************************************
	 * Randomly set a tile of the same walktype to tile
	 * @param tile the tile to randomly set
	 ****************************************************/
	public void randomlySetTile(Tile tile) {
		for (int attempts = 0; attempts < 10000; attempts++){
			int sx = Core.rand.nextInt(getWidth());
			int sy = Core.rand.nextInt(getHeight());
			if (map[sx][sy].equalsMovement(tile) && !map[sx][sy].equals(tile)){
				map[sx][sy] = tile;
				return; 
			}
		}
	}

	/****************************************
	 * Use the stairs at the position x, y
	 * @param x the x position 
	 * @param y the y position
	 ****************************************/
	public void useStairs(int x, int y) {
		if (stairs[x][y] == null) return; 
		Actor a = getActor(x, y); 
		if (a == null) return; 
		a.setDungeon(stairs[x][y].getPortalTo());
		if (stairs[x][y].getPartner() == null) {
			stairs[x][y].getPortalTo().randomlyAddActor(a);
		}
		else { 
			for (int ix = 0; ix < stairs[x][y].getPortalTo().getWidth(); ix++){
				for (int iy = 0; iy < stairs[x][y].getPortalTo().getHeight(); iy++){
					if (stairs[x][y].getPortalTo().stairs[ix][iy] == null) continue; 
					if (stairs[x][y] == stairs[x][y].getPortalTo().stairs[ix][iy].getPartner()){
						Actor act = stairs[x][y].getPortalTo().getActor(ix, iy);
						if (act != null){
							stairs[x][y].getPortalTo().removeActor(act); 
							stairs[x][y].getPortalTo().addActor(ix, iy, a, true);
							stairs[x][y].getPortalTo().randomlyAddActor(act); 
							return; 
						}
						else {
							stairs[x][y].getPortalTo().addActor(ix, iy, a, true);
							return;
						}
					}
				}
			}
			stairs[x][y].getPortalTo().randomlyAddActor(a);
		}
	}

	/********************************
	 * Get the stairs at a position
	 * @param x the position
	 * @param y the position 
	 * @return the stairs
	 ********************************/
	public StairData getStairs(int x, int y) {
		return stairs[x][y]; 
	}
}

