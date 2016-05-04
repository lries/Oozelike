package oozelike.skills;
import java.util.List;

import oozelike.roguelike.Actor;
import oozelike.roguelike.CombatSkill;
import oozelike.roguelike.Dungeon;
import oozelike.roguelike.Shapes;

/*******************************************************************
 * Class representing a projectile skill
 * Still abstract; expresses projectile behavior but not damage
 *******************************************************************/
public abstract class ProjectileSkill extends CombatSkill{
	private static final long serialVersionUID = -5253711652217662784L;

	@Override
	/*************************************************************************************
	 * Get the range of the projectile. Note that this ignores how actors will block it! 
	 * That would be fairly easy to add if needed, but in the case of a failed behavior
	 * one might want to continue. 
	 *************************************************************************************/
	public List<int[]> getRange(Actor user, Dungeon d, int x, int y) {
		List<int[]> line = Shapes.getLine(user.getX(), user.getY(), x, y);
		line.remove(0);
		return line; 
	}
	
	@Override
	/****************************************
	 * Use the skill (fire the projectile)
	 ****************************************/
	public void use(Actor user, Dungeon d, int x, int y){
		List<int[]> fireLine = getRange(user, d, x, y); 
		for (int[] pos: fireLine){
			Actor target = d.getActor(pos[0], pos[1]);
			if (target != null){
				onEnemy(user, target);
				return; 
			}
		}
	}

	@Override
	/****************************************************************************************
	 * Use on self. Same as on enemy - a gun doesn't stop working because someone is nice! 
	 ****************************************************************************************/
	public void onSelf(Actor user) {
		onEnemy(user, user); 
	}

	@Override
	public void onAlly(Actor user, Actor target) {
		onEnemy(user, target); 
		
	}

	@Override
	public void onNeutral(Actor user, Actor target) {
		onEnemy(user, target);
		
	}

	@Override
	public void onTile(Actor user, Dungeon d, int x, int y) {
		
	} 
	
	
}
