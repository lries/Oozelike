package oozelike.generators;

import oozelike.roguelike.Core;
import oozelike.roguelike.Tile;
import oozelike.roguelike.WalkType;

public class TileFactory {

	/********************************
	 * Get a stairs up tile
	 * @return a new stairs up tile
	 ********************************/
	public static Tile getStairsUp(){
		Tile ret = new Tile(Core.visFactory.getStairsUp());
		ret.allowAllMovement();
		return ret; 
	}

	/********************************
	 * Get a stairs down tile
	 * @return a new stairs down tile
	 ********************************/
	public static Tile getStairsDown(){
		Tile ret = new Tile(Core.visFactory.getStairsDown());
		ret.allowAllMovement();
		return ret; 
	}

	/********************************
	 * Get a cave floor tile
	 * @return a new cave floor tile
	 ********************************/
	public static Tile getCaveFloorTile() {
		Tile ret = new Tile(Core.visFactory.getCaveFloor());
		ret.allowAllMovement();
		return ret;
	}

	/********************************
	 * Get a cave wall tile
	 * @return a new cave wall tile
	 ********************************/
	public static Tile getCaveWallTile(){
		Tile ret = new Tile(Core.visFactory.getCaveWall());
		ret.allowMovement(WalkType.DEITY);
		return ret;
	}
	
}
