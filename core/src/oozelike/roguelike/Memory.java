package oozelike.roguelike;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/******************************************
 * Represents a tile memory
 * Usually only held for the players
 * (perhaps complex AIs might use one?) 
 ******************************************/
public class Memory implements Serializable {
	private static final long serialVersionUID = -4965434499272692690L;
	private Actor holder; 
	private Map<Dungeon, boolean[][]> knownTiles;
	
	/********************
	 * Create a Memory
	 ********************/
	public Memory(){
		knownTiles = new HashMap<Dungeon, boolean[][]>();
	}
	
	/****************************
	 * Set the holder
	 * @param holder the holder
	 ****************************/
	public void setActor(Actor holder){
		this.holder = holder; 
	}
	
	/***************************************************************
	 * Get the holder's FOV 
	 * This is probably not the most appropriate place for the call
	 * but it saves a *significant* amount of complexity
	 * @return the list of points in the FOV
	 ****************************************************************/
	public List<int[]> getHoldersFOV(){
		return holder.getFOV(); 
	}
	
	/************************************************************************
	 * Method for learning a tile in the dungeon
	 * @param x the x position of the tile
	 * @param y the y position of the tile
	 ************************************************************************/
	public void learn(int x, int y){
		if (holder.getDungeon() == null) return; 
		if (!knownTiles.containsKey(holder.getDungeon())){
			boolean[][] known = new boolean[holder.getDungeon().getWidth()][holder.getDungeon().getHeight()]; 
			knownTiles.put(holder.getDungeon(), known);
		}
		knownTiles.get(holder.getDungeon())[x][y] = true; 
	}
	
	/*************************************************************************
	 * Method for checking if a tile is known in the current dungeon
	 * @param x the x position of the tile
	 * @param y the y position of the tile
	 * @return if it is known
	 *************************************************************************/
	public boolean knows(int x, int y){
		return knownTiles.containsKey(holder.getDungeon()) && knownTiles.get(holder.getDungeon())[x][y]; 
	}
	
}
