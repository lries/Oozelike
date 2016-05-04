package oozelike.generators;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import oozelike.roguelike.Visible;

/************************************************************
 * Factory for loading Visibles.
 * Note that unlike other factories, it is not static! 
 * It needs to keep track of what images are already loaded. 
 ************************************************************/
public class VisibleFactory {
	
	private Map<String, Texture> loadedTextures = new HashMap<String, Texture>(); 
	
	/******************************************************************
	 * Get stairs up graphic - loads it if it hasn't been loaded yet
	 * @return a new stairs up graphic
	 ******************************************************************/
	public Visible getStairsUp() { 
		return new Visible(get("tiles/stairsUp.png"), "tiles/stairsUp.png");
	}

	/******************************************************************
	 * Get stairs down graphic - loads it if it hasn't been loaded yet
	 * @return a new stairs down graphic
	 ******************************************************************/
	public Visible getStairsDown() { 
		return new Visible(get("tiles/stairsDown.png"), "tiles/stairsDown.png");
	}

	/****************************************************************
	 * Get a shadows graphic - loads it if it hasn't been loaded yet
	 * @return a new shadows graphic
	 ****************************************************************/
	public Visible getDark() {
		return new Visible(get("tiles/gray70.png"), "tiles/gray70.png");
	}

	/****************************************************************
	 * Get the player graphic - loads it if it hasn't been loaded yet
	 * @return a new player graphic
	 ****************************************************************/
	public Visible getPlayer() { 
		return new Visible(get("actors/ooze.png"), "actors/ooze.png");
	}

	/****************************************************************
	 * Get cave floor graphic - loads it if it hasn't been loaded yet
	 * @return a new cave floor graphic
	 ****************************************************************/
	public Visible getCaveFloor() {
		return new Visible(get("tiles/light.png"), "tiles/light.png");
	}

	/****************************************************************
	 * Get cave wall graphic - loads it if it hasn't been loaded yet
	 * @return a new cave wall graphic
	 ****************************************************************/
	public Visible getCaveWall() {
		return new Visible(get("tiles/dark.png"), "tiles/dark.png");
	}

	/****************************************************************
	 * Get green jelly graphic - loads it if it hasn't been loaded yet
	 * @return a new green jelly graphic
	 ****************************************************************/
	public Visible getGreenJelly() {
		return new Visible(get("actors/green_ooze.png"), "actors/green_ooze.png");
	}

	/****************************************************
	 * Get a chompy graphic - loads if it's needed 
	 * @return a chompy graphic
	 ****************************************************/
	public Visible getChompy() {
		return new Visible(get("actors/chompy.png"), "actors/chompy.png");
	}

	/***************************************************
	 * Get a texture - loads it if it isn't yet loaded
	 * @param s the file location of the texture
	 * @return the requested texture
	 * WARNING: Illegal file location will crash! 
	 ***************************************************/
	public Texture get(String s){
		for (String key:loadedTextures.keySet()){
			if (key.equals(s)){
				return loadedTextures.get(key);
			}
		}
		loadedTextures.put(s, new Texture(Gdx.files.internal(s)));
		return loadedTextures.get(s); 
	}
}
