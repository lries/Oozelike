package oozelike.generators;

import java.util.HashMap;
import java.util.Map;

import oozelike.ai.OozeAI;
import oozelike.ai.PlayerAI;
import oozelike.roguelike.AI;
import oozelike.roguelike.Actor;
import oozelike.roguelike.Body;
import oozelike.roguelike.BodyPart;
import oozelike.roguelike.CombatManager;
import oozelike.roguelike.Core;
import oozelike.roguelike.Visible;
import oozelike.roguelike.WalkType;

public class ActorFactory {
	/*************************************************************
	 * Get a new player 
	 * @return a player with no dungeon at position (-1, -1)
	 *************************************************************/
	public static Actor getPlayer(){
		CombatManager cm = new CombatManager(null, 10, 5, 5, 5, 5, 7);
		AI ai = new PlayerAI(5, Core.playerMemory);
		Visible vis = Core.visFactory.getPlayer();
		Map<BodyPart, Integer> parts = new HashMap<BodyPart, Integer>(); 
		parts.put(BodyPart.JELLY, 1);
		Body body = new Body(parts); 
		Actor player = new Actor(null, ai, vis, body, 10, cm, WalkType.WALKER);
		return player; 
	}

	/*************************************************************
	 * Get a new ooze 
	 * @return a ooze with no dungeon at position (-1, -1)
	 *************************************************************/
	public static Actor getOoze(){
		CombatManager cm = new CombatManager(null, 8, 3, 2, 1, 2, 7);
		AI ai = new OozeAI(5);
		Visible vis = Core.visFactory.getGreenJelly();
		Map<BodyPart, Integer> parts = new HashMap<BodyPart, Integer>(); 
		parts.put(BodyPart.JELLY, 1);
		Body body = new Body(parts); 
		Actor player = new Actor(null, ai, vis, body, 10, cm, WalkType.WALKER);
		return player; 
	}
	
}
