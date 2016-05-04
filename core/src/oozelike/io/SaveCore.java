package oozelike.io;

import java.io.Serializable;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import oozelike.generators.VisibleFactory;
import oozelike.roguelike.Core;
import oozelike.roguelike.Log;
import oozelike.roguelike.Memory;

/*********************************************
 * Saved values from a Core for a save file
 *********************************************/
public class SaveCore implements Serializable {
	private static final long serialVersionUID = 1973598463917631392L;
	
	private long seed;
	private Log log;
	private int tileSize;
	private KeyLog keyLog;
	private int focusX;
	private int focusY;
	private int viewLX;
	private int viewLY;
	private int viewHX;
	private int viewHY;
	private Memory playerMemory;

	/*******************************************
	 * Create a save core
	 * @param seed core's random seed
	 * @param log the current message log 
	 * @param tileSize the size of tiles
	 * @param keyLog the current key log
	 * @param focusX the focus x
	 * @param focusY the focus y
	 * @param viewLX the view window
	 * @param viewLY the view window
	 * @param viewHX the view window
	 * @param viewHY the view window
	 * @param playerMemory the player's memory
	 *******************************************/
	public SaveCore(long seed, Log log, int tileSize, KeyLog keyLog,
			int focusX, int focusY, int viewLX, int viewLY, int viewHX, int viewHY, Memory playerMemory) {
		this.seed = seed; 
		this.log = log; 
		this.tileSize = tileSize; 
		this.keyLog = keyLog; 
		this.focusX = focusX; 
		this.focusY = focusY; 
		this.viewLX = viewLX; 
		this.viewLY = viewLY; 
		this.viewHX = viewHX; 
		this.viewHY = viewHY; 
		this.playerMemory = playerMemory; 
	}
	
	/*************************************
	 * Load all saved values to the core
	 *************************************/
	public void load(){
		Core.seed = seed;
		Core.rand = new Random(seed);
		Core.log = log; 
		Core.batch = new SpriteBatch();
		Core.visFactory = new VisibleFactory();
		Core.tileSize = tileSize; 
		Core.keyLog = keyLog; 
		Core.focusX = focusX; 
		Core.focusY = focusY; 
		Core.viewLX = viewLX; 
		Core.viewLY = viewLY; 
		Core.viewHX = viewHX; 
		Core.viewHY = viewHY; 
		Core.playerMemory = playerMemory; 
	}
}
