package oozelike.roguelike;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Body implements Serializable {
	private static final long serialVersionUID = -5339143189983027527L;
	private Map<BodyPart, Item[]> parts;
	
	/**********************************************************
	 * Create a new body
	 * @param plan a list of BodyParts in this creature's body
	 * @return a new body
	 **********************************************************/
	public Body(Map<BodyPart,Integer> plan){
		parts = new HashMap<BodyPart, Item[]>(); 
		for (BodyPart part: plan.keySet()){
			int number = plan.get(part);
			parts.put(part, new Item[number]);
		}
	}
	
	/*********************************************************
	 * Wear an item
	 * @param i the item to wear
	 * @return whether it was successfully worn
	 *********************************************************/
	public boolean addItem(Item i){
		List<BodyPart> partSet = i.getBodyParts();
		if (partSet.size() < 1) return false; 
	
		for (BodyPart p: partSet){
			if (!parts.containsKey(p)) return false; 
			boolean cont = false; 
			for (int x=0; x<parts.get(p).length; x++){
				if (parts.get(p)[x] == null) {
					cont = true; 
				if (parts.get(p)[x] == i) return false; 
				}
			}
			if (cont == false) return false;  
		}
		
		for (BodyPart p: partSet){
			for (int x=0; x<parts.get(p).length; x++){
				if (parts.get(p)[x] == null) {
					parts.get(p)[x] = i;
					break; 
				}
			}
		}
		return true; 
	}
	
	/*******************************************************
	 * Take off an item
	 * @param i the item to remove
	 * @return whether it was successfully removed
	 *******************************************************/
	public boolean removeItem(Item i){
		boolean rem = false; 
		for (BodyPart p: parts.keySet()){
			for (int x=0; x < parts.get(p).length; x++){
				if (parts.get(p)[x] == i) parts.get(p)[x] = null; 
				rem = true; 
			}
		}
		return rem; 
	}
}
