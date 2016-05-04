package oozelike.generators;

import java.util.ArrayList;
import java.util.List;

import oozelike.roguelike.Dungeon;
import oozelike.roguelike.StairData;
import oozelike.roguelike.World;

public class WorldFactory {

	/******************************************************
	 * Generate a map of random automata maps
	 * @param width floor width
	 * @param height floor height
	 * @param minFillPerc min fill percent of each floor
	 * @param maxFillPerc max fill percent of each floor
	 * @param numIter number of smoothing iterations
	 * @param unsafeMode whether to allow unsafe floors
	 * @param numfloors the number of floors
	 * @return a new World as specified
	 ******************************************************/
	public static World generateRandomAutomataWorld(int width, int height, float minFillPerc, float maxFillPerc, int numIter, boolean unsafeMode, int numfloors){
		System.out.println("Generating world");
		List<Dungeon> dungeons = new ArrayList<Dungeon>(); 
		Dungeon last = DungeonFactory.generateRandomAutomataMap(width, height, minFillPerc, maxFillPerc, numIter, unsafeMode);
		Dungeon first = last; 
		for (int x=0; x<numfloors-1; x++){
			Dungeon next = DungeonFactory.generateRandomAutomataMap(width, height, minFillPerc, maxFillPerc, numIter, unsafeMode);
			for (int y=0; y<3; y++){
				StairData stairsDown = new StairData(false, last, next, null); 
				StairData stairsUp = new StairData(true, next, last, stairsDown);
				stairsDown.setPartner(stairsUp);
				last.addStairs(stairsDown, TileFactory.getStairsDown());
				next.addStairs(stairsUp, TileFactory.getStairsUp());
			}
			dungeons.add(last);
			last = next; 
		}
		dungeons.add(last); 
		return new World(first, dungeons);
	}
}
