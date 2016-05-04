package oozelike.roguelike;

import java.io.Serializable;
import java.util.List;

/*******************************************************
 * Represents an entire game world, including dungeons
 *******************************************************/
public class World implements Serializable {	
	private static final long serialVersionUID = -151793290237521361L;
	private List<Dungeon> dungeons; 
	private Dungeon activeDungeon; 
	
	/*********************************************
	 * Create a world
	 * @param dungeon the initial active dungeon
	 * @param all the list of all dungeons
	 *********************************************/
	public World(Dungeon dungeon, List<Dungeon> all){
		activeDungeon = dungeon;
		dungeons = all; 
		for (Dungeon d : all){
			d.setWorld(this);
		}
	}
	
	/*********************************************
	 * Set the active dungeon (and learn it)
	 * @param the new active dungeon
	 *********************************************/
	public void setActiveDungeon(Dungeon dungeon){
		if (!dungeons.contains(dungeon)) dungeons.add(dungeon);
		activeDungeon = dungeon;
	}
	
	/*********************************************
	 * Get the list of all dungeons
	 * @return the list of all dungeons
	 *********************************************/
	public List<Dungeon> getDungeons(){
		return dungeons; 
	}
	
	/*********************************************
	 * Change floors 
	 * @param s the stair data who changes floor
	 *********************************************/
	public void changeFloors(StairData s){
		if (s.getContainer() == activeDungeon){
			activeDungeon = s.getPortalTo();
		}
	}
	
	/********************************************
	 * Render the active dungeon
	 ********************************************/
	public void render(){
		activeDungeon.render();
	}
	
	/*********************************************
	 * Render the active dungeon
	 * @param b whether to consider lighting/FOV
	 *********************************************/
	public void render(Boolean b){
		activeDungeon.render(b);
	}
	
	/*********************************
	 * Render the active dungeon
	 * @param minX min X to render
	 * @param minY min Y to render 
	 * @param maxX max X to render
	 * @param maxY max Y to render
	 *********************************/
	public void render(int minX, int minY, int maxX, int maxY){
		activeDungeon.render(minX, minY, maxX, maxY);
	}
	
	/********************************************
	 * Render the active dungeon
	 * @param minX min X to render
	 * @param minY min Y to render 
	 * @param maxX max X to render
	 * @param maxY max Y to render
	 * @param ignoreShadows ignore FOV lighting
	 ********************************************/
	public void render(int minX, int minY, int maxX, int maxY, boolean b){
		activeDungeon.render(minX, minY, maxX, maxY, b);
	}

	/***************
	 * Run the turn
	 ***************/
	public void runTurn() {
		activeDungeon.runTurn();
	}

	public Dungeon activeDungeon() { return activeDungeon; }

	/************************************
	 * Change floors 
	 * @param dungeon new active dungeon
	 ************************************/
	public void changeFloors(Dungeon dungeon) {
		activeDungeon = dungeon; 
		if (!dungeons.contains(dungeon)) dungeons.add(dungeon);
	}
}
