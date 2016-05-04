package oozelike.ai;

import oozelike.roguelike.AI;
import oozelike.roguelike.Actor;

public class AggressiveBeastAI extends AI {
		private static final long serialVersionUID = 7173507854541416629L;
		
		/**********************************
		 * Create a SimpleMemoryAI
		 * @param vision the vision radius
		 * @return a new SimpleMemoryAI
		 **********************************/
		public AggressiveBeastAI(int vision) {
			super(vision);
		}
		
		@Override
		/************************************************
		 * Called on turn.
		 * Updates all memories; does nothing
		 * @return whether the turn was successful
		 * If not, they will NOT move down in the queue! 
		 ************************************************/
		public boolean onTurn(){
			for (int[] point: holder.getFOV()){
				if (holder.getDungeon().getActor(point[0], point[1]) != null && holder.getDungeon().getActor(point[0], point[1]).isPlayer()){
					hunt(holder.getDungeon().getActor(point[0], point[1]));
					return true; 
				}
			}
			wander(); 
			return true; 
		}
		
		@Override
		/******************************************************
		 * Determine aggression. 
		 * SimpleMemoryAI is aggressive if the memory is < 0 
		 * @return if this is aggressive
		 ******************************************************/
		public boolean isAggressiveTo(Actor a){
			if (a.isPlayer()) return true;
			return false; 
		}
}
