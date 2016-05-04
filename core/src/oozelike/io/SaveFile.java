package oozelike.io;
import java.io.Serializable;
import oozelike.roguelike.World;

/***************************************************
 * A save file - generally saved via serialization 
 ***************************************************/
public class SaveFile implements Serializable {
	private static final long serialVersionUID = -4293840778542874731L;
	public World dungeon; 
	public SaveCore core; 
	
	/*************************************
	 * Create a save file
	 * @param w the game world
	 * @param asSaveCore the Core values
	 *************************************/
	public SaveFile(World w, SaveCore asSaveCore) {
		dungeon = w; 
		core = asSaveCore; 
	}

}
