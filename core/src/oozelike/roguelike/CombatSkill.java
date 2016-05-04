package oozelike.roguelike;
import java.io.Serializable;
import java.util.List;

/*********************************************
 * Class representing a skill used in combat.
 * Potentially other activated abilities too.
 *********************************************/
public abstract class CombatSkill implements Serializable {
	private static final long serialVersionUID = -9022117895286763364L;

	/*************************************
	 * Get the range of a skill
	 * @param user the skill's user
	 * @param d the dungeon to use it in 
	 * @param x the x position targeted
	 * @param y the y position targeted
	 * @return all points in the range. 
	 *************************************/
	public abstract List<int[]> getRange(Actor user, Dungeon d, int x, int y);
	
	/**********************************************
	 * Use the skill. By default, it forms a beam. 
	 * @param user the user
	 * @param d the dungeon to use in
	 * @param x the x position targeted
	 * @param y the y position targeted. 
	 **********************************************/
	public void use(Actor user, Dungeon d, int x, int y){
		List<int[]> range = getRange(user,d,x,y);
		for (int[] position: range){
			onTile(user, d, position[0], position[1]);
			Actor targ = d.getActor(x, y);
			if (targ == user){
				onSelf(user); 
			}
			else if (targ != null){
				onEnemy(user, targ);
			}
		}
	}
	
	/******************************************
	 * Effect of using the skill on yourself. 
	 * @param user the user who used it. 
	 ******************************************/
	public abstract void onSelf(Actor user);
	
	/******************************************
	 * Effect of using the skill on an ally. 
	 * @param user the user
	 * @param target the ally
	 ******************************************/
	public abstract void onAlly(Actor user, Actor target);
	
	/**********************************************
	 * Effect of using the skill on a neutral mob. 
	 * @param user the user
	 * @param target the mob
	 **********************************************/
	public abstract void onNeutral(Actor user, Actor target);
	
	/********************************************
	 * Effect of using the skill on an enemy
	 * @param user the user
	 * @param target the enemy
	 ********************************************/
	public abstract void onEnemy(Actor user, Actor target);
	
	/*********************************************
	 * Effect of using the skill on a tile 
	 * @param user the user
	 * @param d the dungeon the tile is in
	 * @param x its x position
	 * @param y its y position
	 *********************************************/
	public abstract void onTile(Actor user, Dungeon d, int x, int y);
}
