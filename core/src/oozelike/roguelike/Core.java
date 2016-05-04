package oozelike.roguelike;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import oozelike.game.OozelikeGame;
import oozelike.generators.VisibleFactory;
import oozelike.io.KeyLog;
import oozelike.io.SaveCore;

/*********************************************************************************
 * Static class for maintaining key game state information and shared resources. 
 * The Core's status can be saved through the SaveCore class. 
 *********************************************************************************/
public class Core {
	public static long seed = -8458565866829686375L;
	public static Random rand = new Random(-8458565866829686375L);
	public static Log log = new Log(); 
	public static VisibleFactory visFactory = new VisibleFactory();
	public static SpriteBatch batch = new SpriteBatch();
	public static int tileSize = 32;
	public static KeyLog keyLog = new KeyLog();
	public static int focusX = 0;
	public static int focusY = 0;
	public static int viewLX = 0; 
	public static int viewLY = 0;
	public static int viewHX = 100;
	public static int viewHY = 100;
	public static BitmapFont font = new BitmapFont(); 
	public static boolean redraw = true;
	public static Memory playerMemory = new Memory();
	public static OozelikeGame game;
	
	/************************************************************************************************
	 * Compute a new seed based on the new Random() method 
	 * This gives a seed "very likely to be distinct from any other invocation of this constructor"
	 ************************************************************************************************/
	public static void computeNewSeed(){
		seed = new Random().nextLong();
		rand = new Random(seed);
	}
	
	/***********************************************************
	 * Determine if a change in x and y requires a redraw.
	 * If so, raise the redraw flag.
	 * @param x the x position. 
	 * @param y the y position. 
	 ***********************************************************/
	public static void determineRedraw(int x, int y){
		if (x >= viewLX && x <= viewHX && y >= viewLY && y <= viewHY) redraw = true; 
	}
	
	/*******************************************************
	 * Save this core to a SaveCore
	 * @return a SaveCore with state corresponding to this
	 *******************************************************/
	public static SaveCore asSaveCore(){
		SaveCore c = new SaveCore(seed, log, tileSize, keyLog, focusX, focusY, viewLX, viewLY, viewHX, viewHY, playerMemory);
		return c; 
	}
	
}
