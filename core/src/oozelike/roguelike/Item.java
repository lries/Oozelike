package oozelike.roguelike;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Item implements Serializable {
	private static final long serialVersionUID = -6169896216757666092L;
	private List<BodyPart> wornOn; 
	private Map<CombatSkill, Float> skillImparts;
	private Map<Property, Float> propertyImparts;
	private Visible visible; 
	private int nutriment = 0; 
	public int getNutriment() { return nutriment; } 

	/***********************************************
	 * Get the body parts this can be worn on
	 * @return a list of body parts this takes up
	 ***********************************************/
	public List<BodyPart> getBodyParts(){
		return wornOn; 
	}

	/************************************************
	 * Eat this item
	 * @param eater the actor eating it
	 * @return whether it was eaten successfully 
	 ************************************************/
	public boolean eat(Actor eater){
		for (Property prop: propertyImparts.keySet()){
			if (Core.rand.nextFloat() < propertyImparts.get(prop)){
				eater.learnProperty(prop); 
			}
		}

		for (CombatSkill skill: skillImparts.keySet()){
			if (Core.rand.nextFloat() < skillImparts.get(skill)){
				eater.learnSkill(skill); 
			}
		}
		return true;
	}

	public void render(int x, int y) {
		List<int[]> FOV = Core.playerMemory.getHoldersFOV(); 
		for (int[] in: FOV){
			if (in[0]==x && in[1]==y){
				visible.render(x, y);
				return; 
			}
		}
	}

	public void render(int x, int y, boolean ignoreShadows) {
		if (ignoreShadows) visible.render(x, y); 
		else render(x, y);
		
	}
}
