package oozelike.roguelike;
import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*********************************
 * Represents a Tile in the map
 *********************************/
public class Tile implements Serializable{
	private static final long serialVersionUID = 7948762592975779745L;
	private Visible visible; 
	private Set<WalkType> canWalk; 

	/*************************************
	 * Create an impassible tile
	 * @param v the visible of the tile
	 *************************************/
	public Tile(Visible v){
		canWalk = new HashSet<WalkType>();
		visible = v; 
	}

	/*****************************************
	 * Create a tile 
	 * @param v the visible
	 * @param r what walktypes can walk on it
	 *****************************************/
	public Tile(Visible v, List<WalkType> r){
		canWalk = new HashSet<WalkType>(); 
		canWalk.addAll(r); 
		visible = v; 
	}

	/*****************************************
	 * @param add the actor to check
	 * @return whether they can walk on this
	 *****************************************/
	public boolean canWalk(Actor add){
		return canWalk.contains(add.getWalkType());
	}

	/***************************************
	 * @param w a walktype to check
	 * @return whether it can walk on this
	 ***************************************/
	public boolean canWalk(WalkType w){
		return canWalk.contains(w);
	}

	/***********************
	 * Render this tile 
	 * @param x position
	 * @param y position
	 ***********************/
	public void render(int x, int y){
		List<int[]> FOV = Core.playerMemory.getHoldersFOV(); 
		for (int[] in: FOV){
			if (in[0]==x && in[1]==y){
				visible.render(x, y);
				return; 
			}
		}
		if (Core.playerMemory.knows(x, y)){
			visible.render(x, y);
			Core.visFactory.getDark().render(x, y);
		}
	}

	/*****************************************
	 * Allow all movement on this tile
	 * It uses an EnumSet; no need to update
	 *****************************************/
	public void allowAllMovement(){
		canWalk.addAll(EnumSet.allOf(WalkType.class));
	}

	/*****************************************
	 * Forbid all movement on this tile
	 *****************************************/
	public void allowNoMovement(){
		canWalk = new HashSet<WalkType>();
	}
	
	/********************************************
	 * Allow this type of movement on this tile
	 * @param w the walktype to allow
	 ********************************************/
	public void allowMovement(WalkType w) { 
		canWalk.add(w);
	}

	/***********************************************
	 * Disallow this type of movement on this tile
	 * @param w the walktype to disallow 
	 ***********************************************/
	public void disallowMovement(WalkType w){
		canWalk.remove(w);
	}

	/***************************************************
	 * Render this tile
	 * @param x position
	 * @param y position
	 * @param ignoreShadows whether to ignore lighting
	 ***************************************************/
	public void render(int x, int y, boolean ignoreShadows) {
		if (!ignoreShadows) render(x,y);
		else visible.render(x, y);
	}

	/******************************************************************
	 * Checks equality.
	 * This is based on VISIBILITY equality - not on memory equality
	 * @param t the tile to check equality on 
	 * @return whether they are equal 
	 ******************************************************************/
	public boolean equals(Tile t){
		return visible.equals(t.visible);
	}

	/*******************************************************************
	 * Checks equality
	 * This is based on movement equality - not on memory equality
	 * @param t the tile to check equality on
	 * @return whether they are equal 
	 *******************************************************************/
	public boolean equalsMovement(Tile t){ 
		return t.canWalk.containsAll(canWalk) && canWalk.containsAll(t.canWalk);
	}
}
