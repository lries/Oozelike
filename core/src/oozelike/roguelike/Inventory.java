package oozelike.roguelike;
import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;

public class Inventory implements Serializable{
	private static final long serialVersionUID = 4490456991567597456L;
	private List<Item> items; 
	private int maxSize; 
	
	/****************************************************************************
	 * Create an inventory
	 * @param maxSize the current max inventory size (negative <-> infinite size
	 * @return a new inventory
	 ****************************************************************************/
	public Inventory(int maxSize){
		this.items = new ArrayList<Item>();
		this.maxSize = maxSize; 
	}
	
	
	/*****************************************
	 * Create an inventory of infinite size
	 * @return a new inventory
	 *****************************************/
	public Inventory(){
		this.items = new ArrayList<Item>();
		this.maxSize = -1; 
	}
	
	/************************************
	 * Add an item to the inventory
	 * @param i the item to add
	 * @return if adding was successful
	 ************************************/
	public boolean addItem(Item i){
		if ((maxSize >= 0 && items.size() >= maxSize) || items.contains(i)) return false; 
		return items.add(i);
	}
	
	/**************************************
	 * Remove an item from the inventory
	 * @param i an item to remove
	 * @return if removal was successful
	 **************************************/
	public boolean dropItem(Item i){
		if (items.contains(i)) return items.remove(i);
		return false; 
	}

	/***************************************
	 * Render the item pile
	 * @param x the position to render at
	 * @param y the position to render at
	 ***************************************/
	public void render(int x, int y) {
		if (items.isEmpty()) return; 
		items.get(0).render(x, y); 
	}

	/**************************************************
	 * Render the item pile
	 * @param x position
	 * @param y position
	 * @param ignoreShadows whether to ignore shadows
	 **************************************************/
	public void render(int x, int y, boolean ignoreShadows) {
		if (items.isEmpty()) return;
		items.get(0).render(x, y, ignoreShadows);
	}
}
