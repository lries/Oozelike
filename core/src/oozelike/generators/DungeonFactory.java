package oozelike.generators;

import java.util.ArrayList;
import java.util.List;

import oozelike.roguelike.Core;
import oozelike.roguelike.Dungeon;
import oozelike.roguelike.Tile;
import oozelike.roguelike.WalkType;

public class DungeonFactory {
	/****************************************************************
	 * Generate a plain map with walls at the edge
	 * @param width the width of the map
	 * @param height the height of the map
	 * @return a new dungeon
	 ****************************************************************/
	public static Dungeon generateBoxMap(int width, int height){
		Tile[][] map = new Tile[width][height]; 
		for (int x=0; x<map.length; x++){
			for (int y=0; y<map[0].length; y++){
				map[x][y] = TileFactory.getCaveWallTile();
			}
		}
		setEdgesToWall(map); 
		return new Dungeon(map);
	}

	/*********************************************************************
	 * Apply the "automata property" to a cell
	 * @param read the Tile[][] to read from
	 * @param write the Tile[][] to write changes to
	 * @param coerceWalls the number of walls needed to coerce a square
	 * @param x the position
	 * @param y the position
	 *********************************************************************/
	private static void applyAutomataProperty(Tile[][] read, Tile[][] write, int coerceWalls, int x, int y){
		int nwalls = 0; 
		for (int ix = Math.max(0, x-1); ix < Math.min(write.length, x+2); ix++){
			for (int iy = Math.max(0, y-1); iy < Math.min(write[0].length, y+2); iy++){
				if (!read[ix][iy].canWalk(WalkType.MAGMA_FLY_SWIM)) {
					nwalls++;
				}
			}
		}
		if (nwalls >= coerceWalls){
			write[x][y] = TileFactory.getCaveWallTile();
		}
		else {
			write[x][y] = TileFactory.getCaveFloorTile(); 
		}
	}

	/***************************************
	 * Set the edges of the map to a wall
	 * @param map the map to set on 
	 ***************************************/
	private static void setEdgesToWall(Tile[][] map){
		for (int in=0; in<map.length; in++){
			map[in][0] = TileFactory.getCaveWallTile();
			map[in][map[0].length-1] = TileFactory.getCaveWallTile();
		}
		for (int in=0; in<map[0].length; in++){
			map[0][in] = TileFactory.getCaveWallTile();
			map[map.length-1][in] = TileFactory.getCaveWallTile();
		}
	}

	/******************************************************************
	 * Helper method for flood filling walkable terrain
	 * @param map the map to work on 
	 * @param sx starting x
	 * @param sy starting y
	 * @return a boolean[][] with tiles to fill set to true
	 ******************************************************************/
	private static boolean[][] floodFill(Tile[][] map, int sx, int sy){
		int width = map.length;
		int height = map[0].length; 
		List<int[]> frontier = new ArrayList<int[]>();
		boolean[][] visited = new boolean[width][height];
		visited[sx][sy] = true; 
		Tile type = map[sx][sy]; 
		for (int nx = Math.max(0, sx-1); nx<=Math.min(sx+1, width); nx++){
			for (int ny = Math.max(0, sy-1); ny<=Math.min(sy+1, height); ny++){
				if ((nx == sx && ny == sy) || !map[nx][ny].equals(type)) continue;
				int[] point = {nx,ny};
				frontier.add(point);
			}
		}

		while (!frontier.isEmpty()){
			int[] current = frontier.remove(0); 
			for (int nx = Math.max(current[0]-1, 0); nx<=Math.min(current[0]+1,width); nx++){
				for (int ny = Math.max(current[1]-1, 0); ny<=Math.min(current[1]+1, height); ny++){
					if (visited[nx][ny] || !map[nx][ny].equals(type)){
						continue;
					}
					visited[nx][ny] = true;
					int[] np = {nx, ny};
					frontier.add(np);
				}
			}
		}

		return visited; 
	}

	/**************************************
	 * Get a map consisting of only walls
	 * @param width the width
	 * @param height  the height
	 * @return a map of walls
	 **************************************/
	private static Tile[][] getWallMap(int width, int height){
		Tile[][] ret = new Tile[width][height]; 
		for (int x=0; x<width; x++){
			for (int y=0; y<width; y++){
				ret[x][y] = TileFactory.getCaveWallTile(); 
			}
		}
		return ret; 
	}

	/************************************************************************
	 * Map noise as tiles. 
	 * @param noise a noise map 
	 * @param wallCutOff the point at which to draw walls instead of floors
	 * @return a Tile[][] with according noise 
	 ************************************************************************/
	private static Tile[][] mapNoiseAsTiles(float[][] noise, float wallCutOff){
		int width = noise.length;
		int height = noise[0].length; 
		Tile[][] ret = new Tile[width][height];
		for (int x=0; x<width; x++){
			for (int y=0; y<height; y++){
				if (noise[x][y] > wallCutOff) ret[x][y] = TileFactory.getCaveWallTile();
				else ret[x][y] = TileFactory.getCaveFloorTile();
			}
		}
		return ret; 
	}

	/***********************************************************************************************
	 * Generate a map using cellular automata
	 * Generally produces very open, sort of "natural" looking maps
	 * @param width the width of the map
	 * @param height the height of the map
	 * @param minFillPerc the min percent of the map that is walls
	 * @param maxFillPerc the max percent of the map that is walls 
	 * @param numIter the number of iterations to take
	 * @param unsafeMode whether to allow discontinuous maps; true also disables fill checking
	 * @return a new dungeon 
	 ***********************************************************************************************/
	public static Dungeon generateRandomAutomataMap(int width, int height, float minFillPerc, float maxFillPerc, int numIter, boolean unsafeMode){
		int gcount = 0;
		Tile[][] map = mapNoiseAsTiles(NoiseFactory.genWhiteNoise(width, height), 0.55f);
		for (int iter = 0; iter < numIter; iter++){
			Tile[][] newMap = new Tile[width][height];
			for (int x=1; x<width-1; x++){
				for (int y=1; y<height-1; y++){
					applyAutomataProperty(map, newMap, 5, x, y);	
				}
			}
			setEdgesToWall(newMap);
			map = newMap; 
		}
		if (unsafeMode) return new Dungeon(map); 
		int attempts = 0;
		while (attempts < 1000000){
			int sx = Core.rand.nextInt(width-2)+1; 
			int sy = Core.rand.nextInt(height-2)+1;
			if (map[sx][sy].canWalk(WalkType.MAGMA_FLY_SWIM)){			
				Tile[][] newMap = getWallMap(width, height); 
				boolean[][] fill = floodFill(map, sx, sy); 
				for (int x=0; x<width; x++)
					for (int y=0; y<height; y++)
						if (fill[x][y]) {
							gcount++;
							newMap[x][y] = TileFactory.getCaveFloorTile();
						}
				map = newMap; 
				if (!(((float) gcount/width/height) < minFillPerc || (float) gcount/width/height > maxFillPerc)){
					map = newMap;
					return new Dungeon(map); 
				}
			}
			attempts++;
		}
		return generateRandomAutomataMap(width, height, minFillPerc, maxFillPerc, numIter, false);
	}
	

	/***********************************************************************************************
	 * Generate a map using cellular automata
	 * Generally produces very open, sort of "natural" looking maps
	 * @param width the width of the map
	 * @param height the height of the map
	 * @param minFillPerc the min percent of the map that is walls
	 * @param maxFillPerc the max percent of the map that is walls 
	 * @param numIter the number of iterations to take
	 * @param unsafeMode whether to allow discontinuous maps; true also disables fill checking
	 * @return a new dungeon 
	 ***********************************************************************************************/
	public static Dungeon generateDoubleRandomAutomataMap(int width, int height, float minFillPerc, float maxFillPerc, long numEffect, boolean unsafeMode){
		int gcount = 0;
		Tile[][] map = mapNoiseAsTiles(NoiseFactory.genWhiteNoise(width, height), 0.55f);
		for (long iter = 0; iter < numEffect; iter++){
			applyAutomataProperty(map, map, 5, Core.rand.nextInt(width), Core.rand.nextInt(height));
			setEdgesToWall(map);
		}
		if (unsafeMode) return new Dungeon(map); 
		int attempts = 0;
		while (attempts < 1000000){
			int sx = Core.rand.nextInt(width-2)+1; 
			int sy = Core.rand.nextInt(height-2)+1;
			if (map[sx][sy].canWalk(WalkType.MAGMA_FLY_SWIM)){			
				Tile[][] newMap = getWallMap(width, height); 
				boolean[][] fill = floodFill(map, sx, sy); 
				for (int x=0; x<width; x++)
					for (int y=0; y<height; y++)
						if (fill[x][y]) {
							gcount++;
							newMap[x][y] = TileFactory.getCaveFloorTile();
						}
				map = newMap; 
				if (!(((float) gcount/width/height) < minFillPerc || (float) gcount/width/height > maxFillPerc)){
					map = newMap;
					return new Dungeon(map); 
				}
			}
			attempts++;
		}
		return generateDoubleRandomAutomataMap(width, height, minFillPerc, maxFillPerc, numEffect, false);
	}
}
